package io.github.zeroone3010.yahueapi.v2.domain.event;

import io.github.zeroone3010.yahueapi.v2.Device;

import java.util.UUID;

public class MotionEvent {
  private final String eventTime;
  private final UUID eventGroupId;
  private Device owner;
  private final boolean motion;
  private final boolean motionValid;

  public MotionEvent(final String eventTime,
                     final UUID eventGroupId,
                     final Device owner,
                     final boolean motion,
                     final boolean motionValid) {
    this.eventTime = eventTime;
    this.eventGroupId = eventGroupId;
    this.owner = owner;
    this.motion = motion;
    this.motionValid = motionValid;
  }

  public String getEventTime() {
    return eventTime;
  }

  /**
   * The Bridge may emit several events simultaneously. In that case all of those events
   * will have the same {@code eventGroupId}.
   *
   * @return An {@link UUID}.
   */
  public UUID getEventGroupId() {
    return eventGroupId;
  }

  /**
   * Whether motion is sensed.
   *
   * @return {@code true} if motion is sensed, {@code false} if not.
   */
  public boolean isMotion() {
    return motion;
  }

  /**
   * Whether the value presented by {@link #isMotion()} is valid.
   * It is unclear why the Bridge would claim the value as invalid
   * -- possibly if it cannot reach the sensor.
   *
   * @return {@code true} if the motion value is valid, {@code false} if not.
   */
  public boolean isMotionValid() {
    return motionValid;
  }

  /**
   * The device that triggered this motion event.
   *
   * @return The device that triggered this motion event.
   */
  public Device getOwner() {
    return owner;
  }

  @Override
  public String toString() {
    return "MotionEvent{" +
        "eventTime='" + eventTime + '\'' +
        ", eventGroupId=" + eventGroupId +
        ", motion=" + motion +
        ", motionValid=" + motionValid +
        ", owner=" + owner +
        '}';
  }
}
