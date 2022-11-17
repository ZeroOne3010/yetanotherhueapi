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

  public void setAction(DeltaAction action) {
    this.action = action;
  }

  public int getMirekDelta() {
    return mirekDelta;
  }

  /**
   * Mirekk delta to current mirek. Clips at mirek_minimum and mirek_maximum of mirek_schema.
   *
   * @param mirekDelta A number, maximum value is 347.
   */
  public void setMirekDelta(int mirekDelta) {
    this.mirekDelta = mirekDelta;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
