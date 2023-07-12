package io.github.zeroone3010.yahueapi.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

class MDNSDiscovererTestRun {
  private static final Logger logger = LoggerFactory.getLogger("MDNSDiscovererTestRun");

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
