package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.UUID;

public class HueEventData {
  @JsonProperty("id")
  private UUID resourceId;

  @JsonProperty("id_v1")
  private String idV1;

  @JsonProperty("owner")
  private ResourceIdentifier owner;

  @JsonProperty("type")
  private String type;

  @JsonProperty("button")
  private ButtonSpecifics button;

  @JsonProperty("light")
  private LightLevel lightLevel;

  @JsonProperty("on")
  private On on;

  @JsonProperty("dimming")
  private Dimming dimming;

  @JsonProperty("motion")
  private Motion motion;

  @JsonProperty("temperature")
  private Temperature temperature;

  public UUID getResourceId() {
    return resourceId;
  }

  public String getIdV1() {
    return idV1;
  }

  public ResourceIdentifier getOwner() {
    return owner;
  }

  public String getType() {
    return type;
  }

  public Optional<ButtonSpecifics> getButton() {
    return Optional.ofNullable(button);
  }

  public Optional<On> getOn() {
    return Optional.ofNullable(on);
  }

  public Optional<Dimming> getDimming() {
    return Optional.ofNullable(dimming);
  }

  public Optional<LightLevel> getLightLevel() {
    return Optional.ofNullable(lightLevel);
  }

  public Optional<Motion> getMotion() {
    return Optional.ofNullable(motion);
  }

  public Optional<Temperature> getTemperature() {
    return Optional.ofNullable(temperature);
  }

  @Override
  public String toString() {
    return "HueEventData{" +
        "resourceId=" + resourceId +
        ", idV1='" + idV1 + '\'' +
        ", owner=" + owner +
        ", type='" + type + '\'' +
        ", button=" + button +
        ", lightLevel=" + lightLevel +
        ", on=" + on +
        ", dimming=" + dimming +
        ", motion=" + motion +
        ", temperature=" + temperature +
        '}';
  }
}
