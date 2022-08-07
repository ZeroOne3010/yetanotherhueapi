package io.github.zeroone3010.yahueapi.discovery;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.HueBridge;
import io.github.zeroone3010.yahueapi.TrustEverythingManager;
import io.github.zeroone3010.yahueapi.domain.BridgeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * A service with which one can discover the available Hue Bridges.
 */
public final class HueBridgeDiscoveryService {
  private static final Logger logger = LoggerFactory.getLogger(HueBridgeDiscoveryService.class);

  /**
   * The different methods that one can use to discover the available bridges.
   * If one does not work, try the other. By default, if none of these options
   * are given to the {@link #discoverBridges} method, then all of the methods are used.
   */
  public enum DiscoveryMethod {
    /**
     * With the N-UPnP method the Philips Hue portal is polled over the internet
     * and the portal responds with local IP addresses if it knows about any Bridges that
     * exist in the network where it is polled from.
     */
    NUPNP(NUPnPDiscoverer::new),

    /**
     * With the multicast DNS method discovery queries are sent into the local network.
     * Any Bridges that are present in the network are expected to make themselves known by answering to these queries.
     */
    MDNS(MDNSDiscoverer::new);

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
   *                         try to use the NUPNP method in vain, but instead use just the MDNS method only.
   * @return A Future that is completed once the discovery processes have been finished.
   * As a result, the Future will hold a list of any and all Hue Bridges that were found,
   * ready to be given to the {@link io.github.zeroone3010.yahueapi.Hue#hueBridgeConnectionBuilder(String)}
   * method for establishing an authorized connection with the Bridge. If the {@code bridgeDiscoverer} Consumer is
   * implemented, this result can be safely ignored.
   */
  public Future<List<HueBridge>> discoverBridges(final Consumer<HueBridge> bridgeDiscoverer,
                                                 final DiscoveryMethod... discoveryMethods) {

    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    final Collection<HueBridge> bridges = new HashSet<>();
    final Collection<String> ips = new HashSet<>();
    final Consumer<HueBridge> commonConsumer = (discoveredBridge) -> {
      final String ip = discoveredBridge.getIp();
      boolean added;
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

  private BridgeConfig fetchBridgeConfiguration(final ObjectMapper objectMapper, final String ip) {
    try {
      TrustEverythingManager.trustAllSslConnectionsByDisablingCertificateVerification();
      return objectMapper.readValue(new URL("https://" + ip + "/api/config"), BridgeConfig.class);
    } catch (final IOException e) {
      logger.error("Unable to connect to a found Bridge at " + ip + ": " + e);
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
