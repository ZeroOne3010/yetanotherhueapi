package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.XAndYAndBrightness;
import io.github.zeroone3010.yahueapi.v2.domain.Xy;
import io.github.zeroone3010.yahueapi.v2.domain.update.Dimming;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;

import static io.github.zeroone3010.yahueapi.v2.domain.update.On.OFF;
import static io.github.zeroone3010.yahueapi.v2.domain.update.On.ON;

public class UpdateState {

  public static final int LOWEST_POSSIBLE_BRIGHTNESS = 0;
  public static final int MAX_BRIGHTNESS = 100;

  private final UpdateLight updateLight;

  public UpdateState() {
    this.updateLight = new UpdateLight();
  }

  /**
   * Makes this state turn the light(s) on.
   *
   * @return This state, for easy chaining of different methods.
   * @see #off()
   * @see #on(boolean)
   */
  public UpdateState on() {
    updateLight.setOn(ON);
    return this;
  }

  /**
   * Makes this state turn the light(s) off.
   *
   * @return This state, for easy chaining of different methods.
   * @see #on()
   * @see #on(boolean)
   */
  public UpdateState off() {
    updateLight.setOn(OFF);
    return this;
  }

  /**
   * Toggles the on/off status of the light(s).
   *
   * @return This state, for easy chaining of different methods.
   * @see #on()
   * @see #off()
   * @see #on(boolean)
   */
  public UpdateState on(final boolean on) {
    updateLight.setOn(on ? ON : OFF);
    return this;
  }

  /**
   * @param brightness Brightness, from 0 to 100, 0 setting brightness to the lowest possible value.
   * @return This state, for easy chaining of different methods.
   */
  public UpdateState brightness(final int brightness) {
    final int clippedBrightness = Math.min(Math.max(brightness, LOWEST_POSSIBLE_BRIGHTNESS), MAX_BRIGHTNESS);
    updateLight.setDimming(new Dimming().setBrightness(clippedBrightness));
    return this;
  }

  /**
   * One way of setting the color of the light(s).
   *
   * @param color A {@link Color} object to specify a color.
   * @return This state, for easy chaining of different methods.
   * @see #xy(float, float)
   */
  public UpdateState color(final Color color) {
    final XAndYAndBrightness xy = XAndYAndBrightness.rgbToXy(color);
    updateLight.setColor(new io.github.zeroone3010.yahueapi.v2.domain.update.Color()
            .setXy(new Xy().setX(xy.getX()).setY(xy.getY())))
        .setDimming(new Dimming().setBrightness(xy.getBrightnessMax100()));
    return this;
  }

  /**
   * Other way of setting the color of the light(s): CIE XY gamut position.
   *
   * @param x A value from 0 to 1.
   * @param y A value from 0 to 1.
   * @return This state, for easy chaining of different methods.
   * @see #color(Color)
   */
  public UpdateState xy(final float x, final float y) {
    updateLight.setColor(new io.github.zeroone3010.yahueapi.v2.domain.update.Color().setXy(new Xy().setX(x).setY(y)));
    return this;
  }

  public UpdateLight getUpdateLight() {
    return updateLight;
  }
}
