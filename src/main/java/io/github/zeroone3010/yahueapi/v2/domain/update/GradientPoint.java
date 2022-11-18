package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GradientPoint {

  @JsonProperty("points")
  private List<Color> points;

  public List<Color> getPoints() {
    return points;
  }

  /**
   *
   * @param points
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public void setPoints(List<Color> points) {
    this.points = points;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
