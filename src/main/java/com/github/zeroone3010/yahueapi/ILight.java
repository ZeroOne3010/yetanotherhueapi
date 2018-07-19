package com.github.zeroone3010.yahueapi;

public interface ILight {
  /**
   * Returns the name of the light, as set by the user.
   * @return Name of the light.
   */
  String getName();

  /**
   * Turns the light on.
   */
  void turnOn();

  /**
   * Turns the light off.
   */
  void turnOff();

  /**
   * Queries the light state.
   * @return True if the light is on, false if it is off.
   */
  boolean isOn();

  /**
   * Returns info on the type of the light.
   * @return True if it is a color light, false if it is not.
   */
  boolean isColor();

  /**
   * Returns info on the type of the light.
   * @return True if it is a color temperature light, false if it is not.
   */
  boolean isColorTemperature();
}
