package com.github.zeroone3010.yahueapi;

public interface DimmerSwitch extends Sensor {
  /**
   * The latest button event of this switch.
   *
   * @return {@code true} if presence detected, {@code false} if not.
   */
  DimmerSwitchButtonEvent getLatestButtonEvent();
}
