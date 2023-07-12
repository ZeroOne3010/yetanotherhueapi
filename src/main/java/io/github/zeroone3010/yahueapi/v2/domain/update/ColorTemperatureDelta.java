package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColorTemperatureDelta {

  @JsonProperty("action")
  private DeltaAction action;

  @JsonProperty("brightness_delta")
  private int mirekDelta;

  public DeltaAction getAction() {
    return action;
  }

  /**
   * Sets the delta action for this object and returns self.
   *
   * @param action Delta action
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public ColorTemperatureDelta setAction(DeltaAction action) {
    this.action = action;
    return this;
  }

  public int getMirekDelta() {
    return mirekDelta;
  }

  /**
   * Mirek delta to current mirek. Clips at mirek_minimum and mirek_maximum of mirek_schema.
   *
   * @param mirekDelta A number, maximum value is 347.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public ColorTemperatureDelta setMirekDelta(int mirekDelta) {
    this.mirekDelta = mirekDelta;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
