package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.v2.domain.update.Dimming;
import io.github.zeroone3010.yahueapi.v2.domain.update.On;

public class HueSetLightStateRun {

  public static final String ROOM_NAME = "My Room";
  public static final String LIGHT_NAME = "Hue white lamp 1";

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

    final Color sunflowerIsland = Color.of("#ffcc00");
    hue.getRoomByName(ROOM_NAME).get()
        .getLightByName(LIGHT_NAME).get()
        .setState(new UpdateState()
            .setColor(sunflowerIsland)
            .setDimming(new Dimming().setBrightness(100))
            .setOn(On.ON));

  }
}
