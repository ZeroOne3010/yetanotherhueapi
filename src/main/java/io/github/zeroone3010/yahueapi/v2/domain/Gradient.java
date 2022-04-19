package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.List;

public class Gradient {

  @JsonProperty("points")
  private List<GradientPointGet> points;

  @JsonProperty("points_capable")
  private int pointsCapable;

  public List<GradientPointGet> getPoints() {
    return points;
  }

  public int getPointsCapable() {
    return pointsCapable;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
