package io.github.zeroone3010.yahueapi.discovery;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

class NUPnPDiscovererTestRun {
  private static final Logger logger = Logger.getLogger("NUPnPDiscovererTestRun");

  /**
   * Runs the NUPnPDiscoverer and prints the discovered bridges.
   *
   * @param args Not used.
   */
  public static void main(final String... args) throws ExecutionException, InterruptedException {
    final NUPnPDiscoverer discoverer = new NUPnPDiscoverer(bridge -> logger.info("Bridge found: " + bridge));
    System.out.println("Starting NUPnP discoverer");
    discoverer.discoverBridges().get();
    System.out.println("Done.");
  }
}
