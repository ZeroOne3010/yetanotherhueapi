package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LightResource extends Resource {

  @JsonProperty("owner")
  private ResourceIdentifier owner;

  @JsonProperty("metadata")
  private Metadata metadata;

  @JsonProperty("on")
  private On on;

  @JsonProperty("dimming")
  private Dimming dimming;

  @JsonProperty("color_temperature")
  private ColorTemperature colorTemperature;

  @JsonProperty("color")
  private Color color;

  @JsonProperty("dynamics")
  private Dynamics dynamics;

  @JsonProperty("alert")
  private Alert alert;

  @JsonProperty("mode")
  private String mode;

  @JsonProperty("gradient")
  private Gradient gradient;

  public ResourceIdentifier getOwner() {
    return owner;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public On getOn() {
    return on;
  }

  public Dimming getDimming() {
    return dimming;
  }

  public ColorTemperature getColorTemperature() {
    return colorTemperature;
  }

  public Color getColor() {
    return color;
  }

  public Dynamics getDynamics() {
    return dynamics;
  }

  public Alert getAlert() {
    return alert;
  }

  public String getMode() {
    return mode;
  }

  public Gradient getGradient() {
    return gradient;
  }
}
