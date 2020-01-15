package io.github.zeroone3010.yahueapi.discovery;

import io.github.zeroone3010.yahueapi.HueBridge;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class NUPnPDiscovererTestRun {
  /**
   * Runs the NUPnPDiscoverer and prints the discovered bridges.
   *
   * @param args Not used.
   */
  public static void main(final String... args) throws ExecutionException, InterruptedException {
    final NUPnPDiscoverer discoverer = new NUPnPDiscoverer();
    final CompletableFuture<List<HueBridge>> bridges = discoverer.discoverBridges();
    System.out.println("Result: ");
    bridges.get().forEach(System.out::println);
    System.out.println("Done.");
  }
}
