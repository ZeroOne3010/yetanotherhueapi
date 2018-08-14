package com.github.zeroone3010.yahueapi;

public interface Light {
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
   * @return An enum value specifying the color and dimming capabilities of the light.
   */
  LightType getType();

  /**
   * Sets a state for the light.
   * @param state A state to be set for this light.
   */
  void setState(State state);

  /**
   * Gets the state of the light.
   * @return The current state of the light.
   */
  State getState();
}
