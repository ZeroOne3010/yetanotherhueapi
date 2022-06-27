package io.github.zeroone3010.yahueapi.v2;

import java.util.Comparator;

public class HueTestRun {
  /**
   * Displays data of the given Bridge.
   *
   * @param args IP address of the Bridge, API key
   */
  public static void main(final String... args) {
    final String ip = args[0];
    final String apiKey = args[1];

    final Hue hue = new Hue(ip, apiKey);

    System.out.println("\nLights:");
    hue.getLights().values().stream()
        .sorted(Comparator.comparing(Light::getName))
        .forEach(value -> {
          System.out.println(value.getId() + " -> " + value.getName() + ": " + value.isOn());
        });

    System.out.println("Switches: ");
    hue.getSwitches().values().stream()
        .sorted(Comparator.comparing(Switch::getName))
        .forEach(value -> {
          System.out.println(value.getId() + " -> " + value.getName() + ": ");
          value.getButtons().forEach(button -> {
            System.out.println("\t" + button.getNumber() + ": " + button.getLatestEvent());
          });
        });
  }
}