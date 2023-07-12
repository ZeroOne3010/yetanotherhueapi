package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.time.Duration;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimedEffects {

  private static final long MAX_DURATION_MILLISECONDS = 21_600_000L;

  @JsonProperty("effect")
  private TimedEffectType effect;

  @JsonProperty("duration")
  private long duration;

  public TimedEffectType getEffect() {
    return effect;
  }

  /**
   * Sets a timed effect, or clears it with {@link TimedEffectType#NO_EFFECT}.
   *
   * @param effect A {@link TimedEffectType}.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public TimedEffects setEffect(TimedEffectType effect) {
    this.effect = effect;
    return this;
  }

  /**
   * Duration in milliseconds. Maximum of 21600000, i.e. six hours.
   *
   * @return Timed effect duration in milliseconds.
   */
  public long getDuration() {
    return duration;
  }

  /**
   * Duration of the effect.
   * The Bridge decreases the resolution for larger durations.
   * For example, effects with a duration of less than a minute will be rounded
   * to the nearest second, whereas durations larger than an hour
   * will be rounded to a resolution of 5 minutes. The maximum duration is 6 hours.
   *
   * @param duration Duration of the timed effect.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public TimedEffects setDuration(Duration duration) {
    this.duration = Math.min(
        Optional.ofNullable(duration).map(Duration::toMillis).orElse(0L),
        MAX_DURATION_MILLISECONDS
    );
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
