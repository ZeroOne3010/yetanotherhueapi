package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.event.ButtonEvent;

import java.util.concurrent.TimeUnit;

/**
 * This class demonstrates a simple example of an application that listens to and logs
 * button events from the Bridge for ten minutes.
 */
public class HueEventsTestRun {
  /**
   * Displays a stream of events from the given Bridge for ten minutes.
   *
   * @param args IP address of the Bridge, API key
   */
  public static void main(final String... args) throws InterruptedException {
    final String ip = args[0];
    final String apiKey = args[1];

    if (args.length != 2) {
      System.out.println("This class requires two arguments: first the IP address of the Bridge, then the API key.");
      System.exit(1);
    }

    final Hue hue = new Hue(ip, apiKey);

    final HueEventSource hueEventSource = hue.subscribeToEvents(new HueEventListener() {
      @Override
      public void receiveButtonEvent(final ButtonEvent event) {
        System.out.println("Switch event received: " + event);
      }
    });
    System.out.println(hueEventSource.getState());
    TimeUnit.SECONDS.sleep(1);
    System.out.println(hueEventSource.getState());
    TimeUnit.MINUTES.sleep(10);
  }
}
