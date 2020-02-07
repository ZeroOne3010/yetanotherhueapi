package io.github.zeroone3010.yahueapi.discovery;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

class MDNSDiscovererTestRun {
  private static final Logger logger = Logger.getLogger("MDNSDiscovererTestRun");

  /**
   * Runs the MDNSDiscoverer and prints the discovered bridges.
   *
   * @param args Not used.
   */
  public static void main(final String... args) throws InterruptedException, ExecutionException {
    final MDNSDiscoverer discoverer = new MDNSDiscoverer(bridge -> logger.info("Bridge found: " + bridge));
    System.out.println("Starting mDNS discoverer");
    discoverer.discoverBridges().get();
    System.out.println("Done.");
  }
}
