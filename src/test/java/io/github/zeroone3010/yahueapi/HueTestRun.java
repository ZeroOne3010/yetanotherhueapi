package io.github.zeroone3010.yahueapi;

import java.util.Comparator;

import static java.util.stream.Collectors.joining;

public class HueTestRun {
  /**
   * Copies state from a light in one room to another light in another (or the same) room.
   * If not enough parameters given, just displays data about the zones, rules, sensors and scenes of the given Bridge.
   *
   * @param args IP address of the Bridge, API key, fromRoom, fromLight, toRoom, toLight
   */
  public static void main(final String... args) {
    final String ip = args[0];
    final String apiKey = args[1];

    final Hue hue = new Hue(ip, apiKey);

    if (args.length > 4) {
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
    } else if (args.length > 2) {
      final String room = args[2];
      final String scene = args[3];
      hue.getRoomByName(room)
          .flatMap(r -> r.getSceneByName(scene))
          .ifPresent(Scene::activate);
      return;
    }

    hue.setCaching(true);

    System.out.println("\nRooms:");
    hue.getRooms().forEach(r -> System.out.println(String.format("\tRoom '%s' has these lights: %s",
        r.getName(), r.getLights())));

    System.out.println("\nZones:");
    hue.getZones().forEach(z -> System.out.println(String.format("\tZone '%s' has these lights: %s",
        z.getName(), z.getLights())));

    System.out.println("\nMotion sensors:");
    hue.getPresenceSensors().forEach(s -> System.out.println(String.format("\t%s (%s): Presence %s",
        s.getName(), s.getId(), s.isPresence())));

    System.out.println("\nTemperature sensors:");
    hue.getTemperatureSensors().forEach(s -> System.out.println(String.format("\t%s (%s): %s °C",
        s.getName(), s.getId(), s.getDegreesCelsius())));

    System.out.println("\nDaylight sensors:");
    hue.getDaylightSensors().forEach(s -> System.out.println(String.format("\t%s (%s): Is daylight: %s",
        s.getName(), s.getId(), s.isDaylightTime())));

    System.out.println("\nSwitches:");
    hue.getSwitches().stream().sorted(Comparator.comparing(s -> Integer.valueOf(s.getId())))
        .forEach(s -> System.out.println(
            String.format("\tSwitch %s: %s (last updated: %s; event: button #%s %s(%d))\n\t\tButtons: %d (events: %s)",
                s.getId(), s.getName(), s.getLastUpdated(),
                s.getLatestEvent().getButton().getNumber(),
                s.getLatestEvent().getAction().getEventType(),
                s.getLatestEvent().getAction().getEventCode(),
                s.getButtons().size(),
                s.getButtons().stream()
                    .map(b -> b.getPossibleEvents().stream()
                        .map(e -> e.getEventType() + "(" + e.getEventCode() + ")")
                        .collect(joining(", ", "[", "]"))
                    ).collect(joining("; ", "{", "}"))
            )));

    System.out.println("\nUnknown sensors:");
    hue.getUnknownSensors().stream().sorted(Comparator.comparing(s -> Integer.valueOf(s.getId())))
        .forEach(s -> System.out.println(
            String.format("\tSensor %s: %s (last updated: %s)", s.getId(), s.getName(), s.getLastUpdated())));

    System.out.println("\nRules:");
    hue.getRaw().getRules().forEach((id, rule) -> System.out.println(String.format("\tRule %s: %s", id, rule)));

    System.out.println("\nScenes:");
    hue.getRaw().getScenes().forEach((id, scene) -> System.out.println(String.format("\tScene %s: %s", id, scene)));

    System.out.println("\nLights:");
    hue.getRaw().getLights().forEach((id, light) -> System.out.println(String.format("\tLight %s: %s", id, light)));

    hue.getSwitches().forEach(s -> System.out.println(String.format("Switch: %s; last pressed button: #%d (%s) at %s",
        s.getName(),
        s.getLatestEvent().getButton().getNumber(),
        s.getLatestEvent().getAction().getEventType(),
        s.getLastUpdated())));
  }
}
