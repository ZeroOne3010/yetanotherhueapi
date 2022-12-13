package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.XAndYAndBrightness;
import io.github.zeroone3010.yahueapi.v2.domain.Xy;
import io.github.zeroone3010.yahueapi.v2.domain.update.Dimming;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;

import static io.github.zeroone3010.yahueapi.v2.domain.update.On.OFF;
import static io.github.zeroone3010.yahueapi.v2.domain.update.On.ON;

public class UpdateState extends UpdateLight {

  public static final int LOWEST_POSSIBLE_BRIGHTNESS = 0;
  public static final int MAX_BRIGHTNESS = 100;

  /**
   * Makes this state turn the light(s) on.
   *
   * @return This state, for easy chaining of different methods.
   */
  public UpdateState on() {
    super.setOn(ON);
    return this;
  }

  /**
   * Makes this state turn the light(s) off.
   *
   * @return This state, for easy chaining of different methods.
   */
  public UpdateState off() {
    super.setOn(OFF);
    return this;
  }

  /**
   * Toggles the on/off status of the light(s).
   *
   * @return This state, for easy chaining of different methods.
   */
  public UpdateState setOn(final boolean on) {
    super.setOn(on ? ON : OFF);
    return this;
  }

  public UpdateState brightness(final int brightness) {
    final int clippedBrightness = Math.min(Math.max(brightness, LOWEST_POSSIBLE_BRIGHTNESS), MAX_BRIGHTNESS);
    super.setDimming(new Dimming().setBrightness(clippedBrightness));
    return this;
  }

  public UpdateState color(final Color color) {
    final XAndYAndBrightness xy = XAndYAndBrightness.rgbToXy(color);
    super.setColor(new io.github.zeroone3010.yahueapi.v2.domain.update.Color()
            .setXy(new Xy().setX(xy.getX()).setY(xy.getY())))
        .setDimming(new Dimming().setBrightness(xy.getBrightnessMax100()));
    return this;
  }
}
