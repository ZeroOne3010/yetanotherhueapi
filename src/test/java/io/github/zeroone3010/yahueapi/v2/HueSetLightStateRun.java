package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.Color;

import java.util.ArrayList;
import java.util.List;

public class HueSetLightStateRun {

  public static final String ROOM_NAME = "My Room";
  public static final String LIGHT_NAME = "Lightstrip";

  /**
   * @param args IP address of the Bridge, API key
   */
  public static void main(final String... args) {
    if (args == null || args.length != 2) {
      System.err.println("You must give two arguments: 1) the IP of the Bridge and 2) the API key.");
      return;
    }
    final String ip = args[0];
    final String apiKey = args[1];

    final Hue hue = new Hue(ip, apiKey);
    hue.getRoomByName(ROOM_NAME).get().getLights()
        .forEach(light -> System.out.println(light.getName()));

    final List<Color> colors = new ArrayList<>();
    colors.add(Color.of(255, 0, 0));
    colors.add(Color.of(0, 255, 0));
    colors.add(Color.of(0, 0, 255));
    hue.getRoomByName(ROOM_NAME).get()
        .getLightByName(LIGHT_NAME).get()
        .setState(new UpdateState()
            .gradient(colors)
            .brightness(50)
            .on());

  }
}
