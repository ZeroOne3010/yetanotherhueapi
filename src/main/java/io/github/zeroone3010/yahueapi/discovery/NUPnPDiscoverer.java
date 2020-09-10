package io.github.zeroone3010.yahueapi.discovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.zeroone3010.yahueapi.HueBridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Discovers Hue Bridges using the N-UPnP protocol, i.e. by polling the Philips Hue portal
 * that stores the external and internal IP addresses of the Bridge.
 */
final class NUPnPDiscoverer implements HueBridgeDiscoverer {
  private static final Logger logger = Logger.getLogger("NUPnPDiscoverer");

  private static final String HUE_DISCOVERY_PORTAL = "https://discovery.meethue.com/";
  private static final String DEFAULT_TIMEOUT_MILLISECONDS = "4000";
  private static final String CONNECT_TIMEOUT_VARIABLE = "sun.net.client.defaultConnectTimeout";
  private static final String READ_TIMEOUT_VARIABLE = "sun.net.client.defaultReadTimeout";

  private final Consumer<HueBridge> discoverer;
  private final String discoveryPortalUrl;

  NUPnPDiscoverer(final Consumer<HueBridge> discoverer) {
    this(HUE_DISCOVERY_PORTAL, discoverer);
  }

  NUPnPDiscoverer(final String discoveryPortalUrl, final Consumer<HueBridge> discoverer) {
    this.discoveryPortalUrl = discoveryPortalUrl;
    this.discoverer = discoverer;

    if (toInt(System.getProperty(CONNECT_TIMEOUT_VARIABLE)) < 1) {
      System.setProperty(CONNECT_TIMEOUT_VARIABLE, DEFAULT_TIMEOUT_MILLISECONDS);
    }
    if (toInt(System.getProperty(READ_TIMEOUT_VARIABLE)) < 1) {
      System.setProperty(READ_TIMEOUT_VARIABLE, DEFAULT_TIMEOUT_MILLISECONDS);
    }
  }

  private static int toInt(final String string) {
    if (string == null) {
      return -1;
    }
    try {
      return Integer.valueOf(string);
    } catch (final NumberFormatException e) {
      logger.warning(String.format("'%s' is not a valid timeout value", string));
      return -1;
    }
  }

  @Override
  public CompletableFuture<Void> discoverBridges() {
    final String content;
    try (final BufferedReader reader = new BufferedReader(
        new InputStreamReader(new URL(discoveryPortalUrl).openStream(), StandardCharsets.UTF_8))) {
      content = reader.lines().collect(Collectors.joining("\n"));
    } catch (final MalformedURLException e) {
      throw new IllegalStateException(e);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    final Gson objectMapper = new GsonBuilder()
        .serializeNulls()
        .create();
    return CompletableFuture.supplyAsync(() -> {
      logger.fine("Discovering Bridges using the Philips Hue Portal.");
      final List<HueBridge> foundBridges = objectMapper.<ArrayList<HueBridge>>fromJson(content,
          new TypeToken<ArrayList<HueBridge>>() {
          }.getType());
      logger.info(String.format("%d Bridges found using the portal.", foundBridges.size()));
      foundBridges.forEach(discoverer);
      return null;
    });
  }
}
