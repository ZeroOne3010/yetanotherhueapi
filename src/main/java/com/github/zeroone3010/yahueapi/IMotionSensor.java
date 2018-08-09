package com.github.zeroone3010.yahueapi;

public interface IMotionSensor extends ISensor {
  /**
   * Whether presence has been detected.
   *
   * @return {@code true} if presence detected, {@code false} if not.
   */
  boolean isPresence();
}
