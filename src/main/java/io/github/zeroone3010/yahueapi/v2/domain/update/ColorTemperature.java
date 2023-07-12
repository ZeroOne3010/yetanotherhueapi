package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColorTemperature {

  @JsonProperty("mirek")
  private Integer mirek;

  public Integer getMirek() {
    return mirek;
  }

  /**
   * Color temperature in mirek, 153-500.
   *
   * @param mirek Color temperature in mirek, or null when the color is not in the color temperature spectrum.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public ColorTemperature setMirek(Integer mirek) {
    this.mirek = mirek;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
