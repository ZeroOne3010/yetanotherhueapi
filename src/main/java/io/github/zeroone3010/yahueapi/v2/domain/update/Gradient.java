package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Gradient {

  @JsonProperty("points")
  private List<GradientPoint> points;

  public List<GradientPoint> getPoints() {
    return points;
  }

  /**
   * @param points Minimum of 2, maximum of 5 points required.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public Gradient setPoints(List<GradientPoint> points) {
    this.points = points;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
