package io.github.zeroone3010.yahueapi;

import java.util.Collection;

class NUPnPDiscovererTestRun {
  /**
   * Runs the NUPnPDiscoverer and prints the discovered bridges.
   *
   * @param args Not used.
   */
  public static void main(final String... args) {
    final NUPnPDiscoverer discoverer = new NUPnPDiscoverer();
    final Collection<HueBridge> bridges = discoverer.discoverBridges();
    System.out.println("Result: ");
    bridges.forEach(System.out::println);
    System.out.println("Done.");
  }
}
