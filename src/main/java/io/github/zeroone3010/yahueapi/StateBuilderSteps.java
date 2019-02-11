package io.github.zeroone3010.yahueapi;

import java.awt.Color;
import java.util.List;

public final class StateBuilderSteps {
  private StateBuilderSteps() {
    // prevent instantiation
  }

  public interface InitialStep extends HueStep, XyStep, ColorStep, ColorTemperatureStep {

  }

  public interface HueStep {
    /**
     * Hue, from {@code 0} to {@code 65280}.
     */
    SaturationStep hue(int hue);
  }

  public interface SaturationStep {
    /**
     * Saturation, from 0 (white) to 254 (most saturated).
     */
    BrightnessStep saturation(int saturation);
  }

  public interface BrightnessStep {
    /**
     * The brightness as a value from {@code 1} (minimum brightness) to {@code 254} (maximum brightness).
     */
    BuildStep brightness(int brightness);
  }

  public interface XyStep {
    /**
     * The x and y coordinates of the C.I.E. chromaticity diagram. Exactly two values between 0 and 1 required.
     */
    BrightnessStep xy(List<Float> xy);
  }

  public interface ColorStep {
    /**
     * The color as a {@link java.awt.Color} object.
     */
    BuildStep color(Color color);

    /**
     * The color as a hexadecimal string, for example "#ff0000" for red.
     */
    BuildStep color(String color);
  }

  public interface ColorTemperatureStep {
    /**
     * The color temperature in mireks.
     * Must be between {@code 153} (6500K) and {@code 500} (2000K)
     */
    BrightnessStep colorTemperatureInMireks(int colorTemperature);
  }

  public interface TransitionTimeStep {
    /**
     * Transition time in tenths of seconds, i.e. "4" equals "0.4 seconds".
     */
    OnOffStep transitionTime(int centiseconds);
  }

  public interface BuildStep extends OnOffStep, TransitionTimeStep {

  }

  public interface OnOffStep {
    State on(Boolean on);

    /**
     * Creates a new {@code State} that will turn on the light to which it is assigned.
     * A shorthand method for {@code on(true)}.
     *
     * @return A new {@code State}.
     */
    default State on() {
      return on(true);
    }

    /**
     * Creates a new {@code State} that will turn off the light to which it is assigned.
     * A shorthand method for {@code on(false)}.
     *
     * @return A new {@code State}.
     */
    default State off() {
      return on(false);
    }

    /**
     * Keeps the current state of the light to which this state is assigned: if it's on,
     * it stays on, and if it's off, it stays off.
     * A shorthand method for {@code on(null)}.
     *
     * @return A new {@code State}.
     */
    default State keepCurrentState() {
      return on(null);
    }
  }
}
