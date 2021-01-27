package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.StateBuilderSteps.BrightnessStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.OnOffStep;
import io.github.zeroone3010.yahueapi.StateBuilderSteps.SaturationStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class StateTest {

  @Test
  void testColorBuilder() {
    final State hexColorRed1 = State.builder().color(Color.of("FF0000")).on();
    final State hexColorRed2 = State.builder().color(Color.of("#ff0000")).on();
    final State intColorRed = State.builder().color(Color.of(0xFF0000)).on();
    final State floatColorRed = State.builder().color(Color.of(1.0f, 0.0f, 0.0f)).on();
    assertEquals(hexColorRed1, hexColorRed2);
    assertEquals(intColorRed, hexColorRed1);
    assertEquals(intColorRed, hexColorRed2);
    assertEquals(hexColorRed1, floatColorRed);
    assertEquals(hexColorRed2, floatColorRed);
    assertEquals(intColorRed, floatColorRed);
  }

  @Test
  void testHueSatBriBuilder() {
    final State state = State.builder().hue(11).saturation(22).brightness(33).transitionTime(1000).on();
    assertEquals(11, state.getHue());
    assertEquals(22, state.getSat());
    assertEquals(33, state.getBri());
    assertEquals(1000, state.getTransitiontime());
    assertTrue(state.getOn());
  }

  @Test
  void testXyBrightnessBuilder() {
    final State state = State.builder().xy(Arrays.asList(0.33f, 0.66f)).brightness(100).keepCurrentState();
    assertEquals(0.33f, state.getXy().get(0));
    assertEquals(0.66f, state.getXy().get(1));
    assertEquals(100, state.getBri());
    assertNull(state.getOn());
  }

  private static Stream<Arguments> provideIllegalInputsForXyBuilder() {
    return Stream.of(
        Arguments.of(Arrays.asList(-0.33f, 0.66f)),
        Arguments.of(Arrays.asList(0.33f, -0.66f)),
        Arguments.of(Arrays.asList(-0.33f, -0.66f)),
        Arguments.of(Arrays.asList(1.33f, 0.66f)),
        Arguments.of(Arrays.asList(0.33f, 1.66f)),
        Arguments.of(Arrays.asList(1.33f, 1.66f)),
        Arguments.of(Arrays.asList()),
        Arguments.of(Arrays.asList(1.33f)),
        Arguments.of(Arrays.asList(1.33f, 1.66f, 1.99f))
    );
  }

  @ParameterizedTest
  @MethodSource("provideIllegalInputsForXyBuilder")
  @NullSource
  void testXyBuilderWithIllegalValues(List<Float> illegalInput) {
    try {
      State.builder().xy(illegalInput);
      fail("There should have been an exception");
    } catch (IllegalArgumentException expected) {
      assertEquals("The xy list must contain exactly 2 values, between 0 and 1.", expected.getMessage());
    }
  }

  @Test
  void testColorTemperatureInMireksBuilder() {
    final State state = State.builder().colorTemperatureInMireks(123).brightness(42).off();
    assertEquals(123, state.getCt());
    assertEquals(42, state.getBri());
    assertFalse(state.getOn());
  }

  @Test
  void testSettingBrightnessOnly() {
    final State state = ((BrightnessStep) State.builder()).brightness(99).keepCurrentState();
    assertEquals(99, state.getBri());
    assertNull(state.getCt());
    assertNull(state.getXy());
    assertNull(state.getTransitiontime());
    assertNull(state.getSat());
    assertNull(state.getHue());
    assertNull(state.getOn());
    assertNull(state.getScene());
  }

  @Test
  void testSettingHueOnly() {
    final State state = ((OnOffStep) State.builder().hue(99)).keepCurrentState();
    assertEquals(99, state.getHue());
    assertNull(state.getCt());
    assertNull(state.getXy());
    assertNull(state.getTransitiontime());
    assertNull(state.getSat());
    assertNull(state.getBri());
    assertNull(state.getOn());
    assertNull(state.getScene());
  }

  @Test
  void testSettingSaturationOnly() {
    final State state = ((OnOffStep) ((SaturationStep) State.builder()).saturation(99)).keepCurrentState();
    assertEquals(99, state.getSat());
    assertNull(state.getCt());
    assertNull(state.getXy());
    assertNull(state.getTransitiontime());
    assertNull(state.getHue());
    assertNull(state.getBri());
    assertNull(state.getOn());
    assertNull(state.getScene());
  }

  @Test
  void testScene() {
    final State state = State.builder().scene("AB34EF5").keepCurrentState();
    assertEquals("AB34EF5", state.getScene());
    assertNull(state.getCt());
    assertNull(state.getXy());
    assertNull(state.getTransitiontime());
    assertNull(state.getHue());
    assertNull(state.getSat());
    assertNull(state.getBri());
    assertNull(state.getOn());
  }

}
