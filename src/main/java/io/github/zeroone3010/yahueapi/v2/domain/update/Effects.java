package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Effects {
  @JsonProperty("effect")
  private String effect;

  public String getEffect() {
    return effect;
  }

  /**
   * One of fire, candle, no_effect
   *
   * @param effect "fire", "candle", or "no_effect"
   */
  public void setEffect(String effect) {
    this.effect = effect;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
