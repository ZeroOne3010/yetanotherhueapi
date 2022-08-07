package io.github.zeroone3010.yahueapi;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class NewLightsSearchTestRun {
  /**
   * Orders the Bridge to search for new lights.
   *
   * @param args IP address of the Bridge, API key
   */
  public static void main(final String... args) throws ExecutionException, InterruptedException {
    final String ip = args[0];
    final String apiKey = args[1];

    final Hue hue = new Hue(ip, apiKey);

    System.out.println("Scanning: " + LocalDateTime.now());
    final Future<Collection<Light>> foundLights = hue.searchForNewLights();
    System.out.println("Lights found: " + foundLights.get());
    System.out.println("Scan finished: " + LocalDateTime.now());
  }
}
