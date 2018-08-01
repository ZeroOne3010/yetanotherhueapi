package com.github.zeroone3010.yahueapi;

public class HueTestRun {
  /**
   * Copies state from a light in one room to another light in another (or the same) room.
   *
   * @param args IP address of the Bridge, API key, fromRoom, fromLight, toRoom, toLight
   */
  public static void main(final String... args) {
    final String ip = args[0];
    final String apiKey = args[1];
    final String fromRoom = args[2];
    final String fromLight = args[3];
    final String toRoom = args[4];
    final String toLight = args[5];

    final Hue hue = new Hue(ip, apiKey);
    hue.getRoomByName(fromRoom)
        .flatMap(r -> r.getLightByName(fromLight))
        .map(ILight::getState)
        .ifPresent(state ->
            hue.getRoomByName(toRoom)
                .flatMap(r -> r.getLightByName(toLight))
                .ifPresent(light -> light.setState(state))
        );
  }
}
