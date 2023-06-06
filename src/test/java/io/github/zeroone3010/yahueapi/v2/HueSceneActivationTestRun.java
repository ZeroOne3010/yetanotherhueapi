package io.github.zeroone3010.yahueapi.v2;

import java.util.List;
import java.util.Objects;

public class HueSceneActivationTestRun {

  public static final String ROOM_NAME = "Kirjastohuone";
  public static final String SCENE_NAME = "Crocus";

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
    final List<Scene> scenes = hue.getRoomByName(ROOM_NAME).get().getScenes();
    scenes.forEach(scene -> System.out.println(scene.getName()));

    scenes.stream().filter(scene -> Objects.equals(scene.getName(), SCENE_NAME))
        .findFirst()
        .get()
        .activate();
  }
}
