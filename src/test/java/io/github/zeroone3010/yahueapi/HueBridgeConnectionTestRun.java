package io.github.zeroone3010.yahueapi;

import java.util.concurrent.CompletableFuture;

public class HueBridgeConnectionTestRun {
  /**
   * Starts to initialize a connection with the given bridge.
   *
   * @param args IP address of a bridge
   * @throws Exception
   */
  public static void main(final String... args) throws Exception {
    final CompletableFuture<String> bridgeConnectionTest = Hue.hueBridgeConnectionBuilder(args[0])
        .initializeApiConnection("testrun");
    bridgeConnectionTest.thenAccept(System.out::println).get();
  }
}
