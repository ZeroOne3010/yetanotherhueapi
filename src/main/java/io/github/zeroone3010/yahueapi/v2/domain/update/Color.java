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

  /**
   * Sets the color for this object and returns self.
   *
   * @param xy X and Y coordinates of the color to be set.
   * @return Self, so that one can also use this method like a fluent builder.
   */
  public Color setXy(Xy xy) {
    this.xy = xy;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }

}
