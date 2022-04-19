package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;
import io.github.zeroone3010.yahueapi.v2.domain.GradientPointGet;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Gradient {

  @JsonProperty("points")
  private List<GradientPoint> points;

  public List<GradientPoint> getPoints() {
    return points;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
