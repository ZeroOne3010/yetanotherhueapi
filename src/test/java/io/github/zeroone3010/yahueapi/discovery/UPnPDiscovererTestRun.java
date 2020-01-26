package io.github.zeroone3010.yahueapi.discovery;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

class UPnPDiscovererTestRun {
  private static final Logger logger = Logger.getLogger("UPnPDiscovererTestRun");

  /**
   * Runs the UPnPDiscoverer and prints the discovered bridges.
   *
   * @param args Not used.
   */
  public static void main(final String... args) throws InterruptedException, ExecutionException {
    final UPnPDiscoverer discoverer = new UPnPDiscoverer(bridge -> logger.info("Bridge found: " + bridge));
    System.out.println("Starting UPnP discoverer");
    discoverer.discoverBridges().get();
    System.out.println("Done.");
  }
}
