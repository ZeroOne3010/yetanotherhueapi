package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;
import io.github.zeroone3010.yahueapi.v2.domain.Xy;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Color {

  @JsonProperty("xy")
  private Xy xy;

  public Xy getXy() {
    return xy;
  }

  public void setXy(Xy xy) {
    this.xy = xy;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }

}
