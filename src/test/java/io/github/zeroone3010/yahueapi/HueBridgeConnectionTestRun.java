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
    final Hue.HueBridgeConnectionBuilder connectionBuilder = Hue.hueBridgeConnectionBuilder(args[0]);
    System.out.println("Is there a Hue bridge at " + args[0] + "? "
        + (connectionBuilder.isHueBridgeEndpoint().get() ? "Yes." : "No."));
    final CompletableFuture<String> bridgeConnectionTest = connectionBuilder
        .initializeApiConnection("testrun");
    bridgeConnectionTest.thenAccept(System.out::println).get();
  }
}
