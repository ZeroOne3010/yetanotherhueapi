package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dimming {

  @JsonProperty("brightness")
  private int brightness;

  public int getBrightness() {
    return brightness;
  }

  /**
   * Brightness percentage. value cannot be 0, writing 0 changes it to the lowest possible brightness.
   * @param brightness A number greater than zero and maximum of 100.
   */
  public void setBrightness(int brightness) {
    this.brightness = brightness;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
