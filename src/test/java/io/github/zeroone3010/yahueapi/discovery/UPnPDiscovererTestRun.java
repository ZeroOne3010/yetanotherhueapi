package io.github.zeroone3010.yahueapi.discovery;

import java.util.concurrent.TimeUnit;

class UPnPDiscovererTestRun {
  /**
   * Runs the UPnPDiscoverer and prints the discovered bridges.
   *
   * @param args Not used.
   */
  public static void main(final String... args) throws InterruptedException {
    final UPnPDiscoverer discoverer = new UPnPDiscoverer() {
      @Override
      public void onBridgeDiscovered(final String ip) {
        System.out.println("Found a bridge: " + ip);
      }
    };
    final Thread discovererThread = new Thread(discoverer);
    System.out.println("Starting discoverer");
    discovererThread.start();
    System.out.println("Discoverer started");
    TimeUnit.SECONDS.sleep(5L);
    System.out.println("Stopping discoverer");
    discoverer.stop();
    System.out.println("Result: " + discoverer.getIps());
    System.out.println("Done.");
  }
}
