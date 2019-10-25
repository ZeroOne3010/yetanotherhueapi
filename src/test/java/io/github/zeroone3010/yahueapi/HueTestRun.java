package io.github.zeroone3010.yahueapi;

import java.util.Comparator;

public class HueTestRun {
  /**
   * Copies state from a light in one room to another light in another (or the same) room.
   * If not enough parameters given, just displays data about the zones, rules and sensors of the given Bridge.
   *
   * @param args IP address of the Bridge, API key, fromRoom, fromLight, toRoom, toLight
   */
  public static void main(final String... args) {
    final String ip = args[0];
    final String apiKey = args[1];

    final Hue hue = new Hue(HueBridgeProtocol.UNVERIFIED_HTTPS, ip, apiKey);

    if (args.length > 2) {
      final String fromRoom = args[2];
      final String fromLight = args[3];
      final String toRoom = args[4];
      final String toLight = args[5];
      hue.getRoomByName(fromRoom)
          .flatMap(r -> r.getLightByName(fromLight))
          .map(Light::getState)
          .ifPresent(state ->
              hue.getRoomByName(toRoom)
                  .flatMap(r -> r.getLightByName(toLight))
                  .ifPresent(light -> light.setState(state))
          );
      return;
    }

    System.out.println("Zones:");
    hue.getZones().forEach(z -> System.out.println(String.format("\tZone '%s' has these lights: %s",
        z.getName(), z.getLights())));

    System.out.println("\nMotion sensors:");
    hue.getMotionSensors().forEach(s -> System.out.println(String.format("\t%s (%s): Presence %s",
        s.getName(), s.getId(), s.isPresence())));

    System.out.println("\nTemperature sensors:");
    hue.getTemperatureSensors().forEach(s -> System.out.println(String.format("\t%s (%s): %s °C",
        s.getName(), s.getId(), s.getDegreesCelsius())));

    System.out.println("\nDaylight sensors:");
    hue.getDaylightSensors().forEach(s -> System.out.println(String.format("\t%s (%s): Is daylight: %s",
        s.getName(), s.getId(), s.isDaylightTime())));

    System.out.println("\nUnknown sensors:");
    hue.getUnknownSensors().stream().sorted(Comparator.comparing(s -> Integer.valueOf(s.getId())))
        .forEach(s -> System.out.println(
            String.format("\tSensor %s: %s (last updated: %s)", s.getId(), s.getName(), s.getLastUpdated())));

    System.out.println("\nRules:");
    hue.getRaw().getRules().forEach((id, rule) -> System.out.println(String.format("\tRule %s: %s", id, rule)));
  }
}
