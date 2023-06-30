package io.github.zeroone3010.yahueapi.v2;

import java.time.ZonedDateTime;

/**
 *  A physical motion detector.
 */
public interface MotionSensor extends Device {
  /**
   * Whether motion has been detected.
   *
   * @return {@code true} if motion detected, {@code false} if not.
   */
  boolean isMotion();

  /**
   * The last time there was any change to the motion status.
    * @return Timestamp.
   */
  ZonedDateTime getLastChanged();
}
