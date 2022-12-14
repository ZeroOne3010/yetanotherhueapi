package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.Color;
import io.github.zeroone3010.yahueapi.XAndYAndBrightness;
import io.github.zeroone3010.yahueapi.v2.domain.Xy;
import io.github.zeroone3010.yahueapi.v2.domain.update.Alert;
import io.github.zeroone3010.yahueapi.v2.domain.update.AlertType;
import io.github.zeroone3010.yahueapi.v2.domain.update.Dimming;
import io.github.zeroone3010.yahueapi.v2.domain.update.EffectType;
import io.github.zeroone3010.yahueapi.v2.domain.update.Effects;
import io.github.zeroone3010.yahueapi.v2.domain.update.TimedEffectType;
import io.github.zeroone3010.yahueapi.v2.domain.update.TimedEffects;
import io.github.zeroone3010.yahueapi.v2.domain.update.UpdateLight;

import java.time.Duration;

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

  /**
   * Starts an effect, or stops it with the {@link EffectType#NO_EFFECT}.
   * Note that not all lights, not even all the color ones, support effects.
   * You may check the supported effects with the {@link Light#getSupportedEffects()} method.
   * Also note that rooms and zones do not support effects.
   *
   * @param effectType Type of effect.
   * @return This state, for easy chaining of different methods.
   */
  public UpdateState effect(final EffectType effectType) {
    updateLight.setEffects(new Effects().setEffect(effectType));
    return this;
  }

  /**
   * Alerts, i.e. flashes the light a few times.
   *
   * @return This state, for easy chaining of different methods.
   */
  public UpdateState alert() {
    updateLight.setAlert(new Alert().setAction(AlertType.BREATHE));
    return this;
  }

  /**
   * <p>Starts the timed sunrise effect. The maximum duration is six hours.
   * The timed effect may be stopped with the {@link #clearTimedEffect()} method.</p>
   *
   * <p>Note that not all lights, not even all the color ones, support timed effects.
   * Also note that rooms and zones do not support timed effects.</p>
   *
   * @param duration Duration of the sunrise effect.
   * @return This state, for easy chaining of different methods.
   * @see {@link #clearTimedEffect()}
   */
  public UpdateState sunrise(final Duration duration) {
    timedEffect(TimedEffectType.SUNRISE, duration);
    return this;
  }

  /**
   * <p>Stops the timed effect.</p>
   *
   * <p>Note that not all lights, not even all the color ones, support timed effects.
   * Also note that rooms and zones do not support timed effects.</p>
   *
   * @return This state, for easy chaining of different methods.
   * @see {@link #sunrise(Duration)}
   */
  public UpdateState clearTimedEffect() {
    timedEffect(TimedEffectType.NO_EFFECT, null);
    return this;
  }

  /**
   * <p>Starts a timed effect. As of December 2022, "sunrise" and "no effect" are the only two options
   * that the Bridge offers. There are helper methods for both of those in this class, but this method
   * exists here for extra convenience so that you can start and stop the effect with the same method,
   * just changing the parameters.</p>
   *
   * <p>Note that not all lights, not even all the color ones, support timed effects.
   * Also note that rooms and zones do not support timed effects.</p>
   *
   * @return This state, for easy chaining of different methods.
   * @see {@link #sunrise(Duration)}
   * @see {@link #clearTimedEffect()}
   */
  public UpdateState timedEffect(final TimedEffectType effect, final Duration duration) {
    updateLight.setTimedEffects(new TimedEffects().setDuration(duration).setEffect(effect));
    return this;
  }

  UpdateLight getUpdateLight() {
    return updateLight;
  }
}
