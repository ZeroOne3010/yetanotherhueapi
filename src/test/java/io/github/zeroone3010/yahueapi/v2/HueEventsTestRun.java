package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.HueEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HueEventsTestRun {
  /**
   * Displays a stream of events from the given Bridge.
   *
   * @param args IP address of the Bridge, API key
   */
  public static void main(final String... args) throws InterruptedException {
    final String ip = args[0];
    final String apiKey = args[1];

    final Hue hue = new Hue(ip, apiKey);

    hue.subscribeToEvents(new HueEventListener() {
      @Override
      public void receive(final List<HueEvent> events) {
        System.out.println("Events received:");
        events.forEach(event -> System.out.println("\t" + event));
      }
    });
    TimeUnit.MINUTES.sleep(10);
  }
}
