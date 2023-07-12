package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dynamics {

  @JsonProperty("duration")
  private int duration;

  @JsonProperty("speed")
  private float speed;

  public int getDuration() {
    return duration;
  }

  /**
   * Sets the durationInMilliseconds of a light transition or timed effects in milliseconds.
   *
   * @param durationInMilliseconds Number of milliseconds.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public Dynamics setDuration(int durationInMilliseconds) {
    this.duration = durationInMilliseconds;
    return this;
  }

  public float getSpeed() {
    return speed;
  }

  /**
   * Speed of dynamic palette or effect.
   *
   * @param speed A value between 0 and 1.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public Dynamics setSpeed(float speed) {
    this.speed = speed;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
