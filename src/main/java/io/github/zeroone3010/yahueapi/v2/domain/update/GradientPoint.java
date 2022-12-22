package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GradientPoint {

  @JsonProperty("color")
  private Color color;

  public Color getColor() {
    return color;
  }

  /**
   * @param color
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public GradientPoint setColor(Color color) {
    this.color = color;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
