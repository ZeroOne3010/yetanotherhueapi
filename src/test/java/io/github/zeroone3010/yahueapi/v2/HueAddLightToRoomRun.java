package io.github.zeroone3010.yahueapi.v2;

public class HueAddLightToRoomRun {

  public static final String OLD_ROOM_NAME = "Bathroom";
  public static final String ROOM_NAME = "Living room";
  public static final String LIGHT_NAME = "The Lamp";

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

    System.out.println("Lights before: "+ hue.getRoomByName(ROOM_NAME).get());

    final Light lightToBeAdded = hue.getLights().values().stream()
        .filter(light -> light.getName().equals(LIGHT_NAME))
        .findFirst()
        .get();

    hue.getRoomByName(OLD_ROOM_NAME).get().removeLight(lightToBeAdded);

    hue.getRoomByName(ROOM_NAME).ifPresent(room -> room.addLight(lightToBeAdded));

    System.out.println("Lights after: "+ hue.getRoomByName(ROOM_NAME).get());
  }
}
