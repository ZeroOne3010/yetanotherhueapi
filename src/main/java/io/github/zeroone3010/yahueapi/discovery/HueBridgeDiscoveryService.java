package io.github.zeroone3010.yahueapi.discovery;

import com.google.gson.Gson;
import io.github.zeroone3010.yahueapi.HueBridge;
import io.github.zeroone3010.yahueapi.domain.BridgeConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public final class HueBridgeDiscoveryService {
  private static final Logger logger = Logger.getLogger("HueBridgeDiscoveryService");

  public enum DiscoveryMethod {
    /**
     * With the N-UPnP method the Philips Hue portal is polled over the internet
     * and the portal responds with local IP addresses if it knows about any Bridges that
     * exist in the network where it is polled from.
     */
    NUPNP(NUPnPDiscoverer::new),

    /**
     * With the UPnP method discovery queries are sent into the local network.
     * Any Bridges that are present in the network are expected to make themselves known by answering to these queries.
     */
    UPNP(UPnPDiscoverer::new);

    private final Function<Consumer<HueBridge>, HueBridgeDiscoverer> discovererCreator;

    DiscoveryMethod(final Function<Consumer<HueBridge>, HueBridgeDiscoverer> discovererCreator) {
      this.discovererCreator = discovererCreator;
    }

    Function<Consumer<HueBridge>, HueBridgeDiscoverer> getDiscovererCreator() {
      return discovererCreator;
    }
  }

  /**
   * <p>Discover the Hue Bridges in the current network, using the given discovery methods. Giving no discovery methods
   * means that all available methods will be used.</p>
   *
   * <p>As some discovery methods may take a while (let's say 5-10 seconds) to complete, this method returns a
   * {@link java.util.concurrent.Future} once the discovery process is completed and no Bridges can be found anymore.
   * As users may rightly get impatient while waiting, this method also takes a {@code Consumer<HueBridge>} as
   * an argument. This Consumer is called whenever a new Bridge is discovered at any time during the discovery process.
   * </p>
   *
   * <p>
   * The Consumer can be used, for example, to populate a list of Bridges for the end-user to choose from, while the
   * actual discovery process still continues in the background. If the Consumer is implemented properly, the result
   * of this method (List of HueBridge objects) can actually be discarded completely.
   * </p>
   *
   * @param bridgeDiscoverer A Consumer that is called whenever a new Bridge is encountered during
   *                         the discovery process. Even if multiple discovery methods are used the Consumer will only
   *                         be called once per Bridge, even if all the different methods would actually discover
   *                         the same Bridge independently.
   * @param discoveryMethods With this argument one can limit the methods being used. For example, if it is known
   *                         that this local network has no access to the outside internet, then one should not
   *                         try to use the NUPNP method in vain, but instead use just the UPNP method only.
   * @return A Future that is completed once the discovery processes have been finished.
   * As a result, the Future will hold a list of any and all Hue Bridges that were found,
   * ready to be given to the {@link io.github.zeroone3010.yahueapi.Hue#hueBridgeConnectionBuilder(String)}
   * method for establishing an authorized connection with the Bridge. If the {@code bridgeDiscoverer} Consumer is
   * implemented, this result can be safely ignored.
   */
  public Future<List<HueBridge>> discoverBridges(final Consumer<HueBridge> bridgeDiscoverer,
                                                 final DiscoveryMethod... discoveryMethods) {

    final Gson objectMapper = new Gson();

    final Collection<HueBridge> bridges = new HashSet<>();
    final Collection<String> ips = new HashSet<>();
    final Consumer<HueBridge> commonConsumer = (discoveredBridge) -> {
      final String ip = discoveredBridge.getIp();
      final boolean added;
      synchronized (ips) {
        added = ips.add(ip);
      }
      if (added) {
        final BridgeConfig config = fetchBridgeConfiguration(objectMapper, ip);
        if (config != null) {
          final HueBridge confirmedBridge = new HueBridge(ip, config.getName(), config.getMac());
          synchronized (bridges) {
            bridges.add(confirmedBridge);
          }
          bridgeDiscoverer.accept(confirmedBridge);
        }
      }
    };
    final List<DiscoveryMethod> methods = parseMethods(discoveryMethods);
    final CompletableFuture[] futures = methods.stream()
        .map(DiscoveryMethod::getDiscovererCreator)
        .map(creator -> creator.apply(commonConsumer))
        .map(HueBridgeDiscoverer::discoverBridges)
        .toArray(CompletableFuture[]::new);

    return CompletableFuture.allOf(futures).thenApply(allDone -> new ArrayList<>(bridges));
  }

  private BridgeConfig fetchBridgeConfiguration(final Gson objectMapper, final String ip) {
    try (final BufferedReader reader = new BufferedReader(
        new InputStreamReader(new URL("http://" + ip + "/api/config").openStream(), StandardCharsets.UTF_8))) {
      return objectMapper.fromJson(reader.lines().collect(Collectors.joining("\n")), BridgeConfig.class);
    } catch (final IOException e) {
      logger.severe("Unable to connect to a found Bridge at " + ip + ": " + e);
      return null;
    }
  }

  private List<DiscoveryMethod> parseMethods(final DiscoveryMethod[] discoveryMethods) {
    final List<DiscoveryMethod> methods = new ArrayList<>();
    if (discoveryMethods == null
        || discoveryMethods.length == 0
        || Stream.of(discoveryMethods).allMatch(Objects::isNull)) {

      methods.addAll(asList(DiscoveryMethod.values()));
    } else {
      Stream.of(discoveryMethods).filter(Objects::nonNull).forEach(methods::add);
    }
    return methods;
  }
}
