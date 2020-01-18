package io.github.zeroone3010.yahueapi.discovery;

import io.github.zeroone3010.yahueapi.HueBridge;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class UPnPDiscovererTestRun {
  /**
   * Runs the UPnPDiscoverer and prints the discovered bridges.
   *
   * @param args Not used.
   */
  public static void main(final String... args) throws InterruptedException, ExecutionException {
    final UPnPDiscoverer discoverer = new UPnPDiscoverer(bridge -> System.out.println("Found a bridge: " + bridge));
    System.out.println("Starting discoverer");
    CompletableFuture<List<HueBridge>> bridges = discoverer.discoverBridges();
    System.out.println("Discoverer started");
    System.out.println("Result: " + bridges.get());
    System.out.println("Done.");
  }
}
