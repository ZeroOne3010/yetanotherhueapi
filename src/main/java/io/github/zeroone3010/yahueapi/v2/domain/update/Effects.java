package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Effects {
  @JsonProperty("effect")
  private EffectType effect;

  public EffectType getEffect() {
    return effect;
  }

  /**
   * One of fire, candle, no_effect
   *
   * @param effect The desired effect.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public Effects setEffect(EffectType effect) {
    this.effect = effect;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
