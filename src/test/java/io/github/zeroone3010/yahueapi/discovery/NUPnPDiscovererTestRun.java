package io.github.zeroone3010.yahueapi.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

class NUPnPDiscovererTestRun {
  private static final Logger logger = LoggerFactory.getLogger("NUPnPDiscovererTestRun");

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
