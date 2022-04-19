package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColorTemperature {

  @JsonProperty("mirek")
  private int mirek;

  public int getMirek() {
    return mirek;
  }

  public void setMirek(int mirek) {
    this.mirek = mirek;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
