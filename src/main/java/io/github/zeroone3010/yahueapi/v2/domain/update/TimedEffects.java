package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.time.Duration;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimedEffects {

  @JsonProperty("effect")
  private String effect;

  @JsonProperty("duration")
  private long duration;

  public String getEffect() {
    return effect;
  }

  /**
   * One of sunrise, no_effect
   *
   * @param effect "sunrise" or "no_effect"
   */
  public void setEffect(String effect) {
    this.effect = effect;
  }

  public long getDuration() {
    return duration;
  }

  /**
   * Milliseconds until the timed effect is to take place.
   * The Bridge decreases the resolution for larger durations.
   * For example, effects with a duration of less than a minute will be rounded
   * to the nearest second, whereas durations larger than an hour
   * will be rounded to a resolution of 5 minutes.
   *
   * @param duration Duration until the timed effect should be applied.
   */
  public void setDuration(Duration duration) {
    this.duration = Optional.ofNullable(duration).map(Duration::toMillis).orElse(0L);
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
