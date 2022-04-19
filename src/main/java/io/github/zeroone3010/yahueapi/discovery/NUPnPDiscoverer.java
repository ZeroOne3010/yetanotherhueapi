package io.github.zeroone3010.yahueapi.discovery;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.zeroone3010.yahueapi.HueBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Discovers Hue Bridges using the N-UPnP protocol, i.e. by polling the Philips Hue portal
 * that stores the external and internal IP addresses of the Bridge.
 */
final class NUPnPDiscoverer implements HueBridgeDiscoverer {
  private static final Logger logger = LoggerFactory.getLogger(NUPnPDiscoverer.class);

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
      logger.warn("'{}' is not a valid timeout value", string);
      return -1;
    }
  }

  @Override
  public CompletableFuture<Void> discoverBridges() {
    final URL url;
    try {
      url = new URL(discoveryPortalUrl);
    } catch (final MalformedURLException e) {
      throw new IllegalStateException(e);
    }
    final ObjectMapper objectMapper = new ObjectMapper();
    final SimpleModule module = new SimpleModule();
    module.addDeserializer(HueBridge.class, new NUPnPDeserializer());
    objectMapper.registerModule(module);
    return CompletableFuture.supplyAsync(() -> {
      try {
        logger.debug("Discovering Bridges using the Philips Hue Portal.");
        final List<HueBridge> foundBridges = objectMapper.<ArrayList<HueBridge>>readValue(url,
            new TypeReference<ArrayList<HueBridge>>() {
            });
        logger.info("{} Bridges found using the portal.", foundBridges.size());
        foundBridges.forEach(discoverer);
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
      return null;
    });
  }


  /**
   * <p>Deserializes a JSON object that has an <code>internalipaddress</code> field.</p>
   */
  static class NUPnPDeserializer extends StdDeserializer<HueBridge> {
    NUPnPDeserializer() {
      super(HueBridge.class);
    }

    @Override
    public HueBridge deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
      final JsonNode node = parser.getCodec().readTree(parser);
      final String ip = node.get("internalipaddress").textValue();
      return new HueBridge(ip);
    }
  }
}
