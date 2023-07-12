package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class ColorTemperature {

  @JsonProperty("mirek")
  private int mirek;

  @JsonProperty("mirek_valid")
  private boolean mirekValid;

  @JsonProperty("mirek_schema")
  private MirekSchema mirekSchema;

  public int getMirek() {
    return mirek;
  }

  public boolean isMirekValid() {
    return mirekValid;
  }

  public MirekSchema getMirekSchema() {
    return mirekSchema;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
