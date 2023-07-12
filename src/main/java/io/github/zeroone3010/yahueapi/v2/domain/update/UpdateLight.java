package io.github.zeroone3010.yahueapi.v2.domain.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateLight {

  @JsonProperty("on")
  private On on;

  @JsonProperty("dimming")
  private Dimming dimming;

  @JsonProperty("dimming_delta")
  private DimmingDelta dimmingDelta;

  @JsonProperty("color_temperature")
  private ColorTemperature colorTemperature;

  @JsonProperty("color_temperature_delta")
  private ColorTemperatureDelta colorTemperatureDelta;

  @JsonProperty("color")
  private Color color;

  @JsonProperty("dynamics")
  private Dynamics dynamics;

  @JsonProperty("alert")
  private Alert alert;

  @JsonProperty("gradient")
  private Gradient gradient;

  @JsonProperty("effects")
  private Effects effects;

  @JsonProperty("timed_effects")
  private TimedEffects timedEffects;

  public On getOn() {
    return on;
  }

  public UpdateLight setOn(On on) {
    this.on = on;
    return this;
  }

  public Dimming getDimming() {
    return dimming;
  }

  public UpdateLight setDimming(Dimming dimming) {
    this.dimming = dimming;
    return this;
  }

  public DimmingDelta getDimmingDelta() {
    return dimmingDelta;
  }

  public UpdateLight setDimmingDelta(DimmingDelta dimmingDelta) {
    this.dimmingDelta = dimmingDelta;
    return this;
  }

  public ColorTemperature getColorTemperature() {
    return colorTemperature;
  }

  public UpdateLight setColorTemperature(ColorTemperature colorTemperature) {
    this.colorTemperature = colorTemperature;
    return this;
  }

  public ColorTemperatureDelta getColorTemperatureDelta() {
    return colorTemperatureDelta;
  }

  public void setColorTemperatureDelta(ColorTemperatureDelta colorTemperatureDelta) {
    this.colorTemperatureDelta = colorTemperatureDelta;
  }

  public Color getColor() {
    return color;
  }

  public UpdateLight setColor(Color color) {
    this.color = color;
    return this;
  }

  public Dynamics getDynamics() {
    return dynamics;
  }

  public UpdateLight setDynamics(Dynamics dynamics) {
    this.dynamics = dynamics;
    return this;
  }

  public Alert getAlert() {
    return alert;
  }

  public UpdateLight setAlert(Alert alert) {
    this.alert = alert;
    return this;
  }

  public Gradient getGradient() {
    return gradient;
  }

  public UpdateLight setGradient(Gradient gradient) {
    this.gradient = gradient;
    return this;
  }

  public Effects getEffects() {
    return effects;
  }

  public UpdateLight setEffects(Effects effects) {
    this.effects = effects;
    return this;
  }

  public TimedEffects getTimedEffects() {
    return timedEffects;
  }

  public UpdateLight setTimedEffects(TimedEffects timedEffects) {
    this.timedEffects = timedEffects;
    return this;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
