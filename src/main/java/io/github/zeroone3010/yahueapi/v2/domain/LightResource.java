package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.UUID;

public class LightResource {

  @JsonProperty("type")
  private String type;

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("id_v1")
  private String idV1;

  @JsonProperty("owner")
  private Owner owner;

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

  public String getType() {
    return type;
  }

  public UUID getId() {
    return id;
  }

  public String getIdV1() {
    return idV1;
  }

  public Owner getOwner() {
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

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}