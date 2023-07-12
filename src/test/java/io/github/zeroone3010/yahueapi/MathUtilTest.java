package io.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MathUtilTest {

  @ParameterizedTest
  @CsvSource({
      "0,0,1",
      "1,0,1",
      "0.5,0.0,1.0",
      "0.999,0.0,0.999",
      "13,13,42",
      "27,13,42",
      "42,13,42",
      "15,15,15"
  })
  void inRange(float value, float min, float max) {
    assertTrue(MathUtil.isInRange(value, min, max));
  }

  @ParameterizedTest
  @CsvSource({
      "0,0.01,1",
      "1.01,0,1",
      "-0.5,0.0,1.0",
      "0.999,0.0,0.998",
      "12.999,13,42",
      "27,13,13",
      "42.0001,13,42",
      "15,16,14",
  })
  void notInRange(float value, float min, float max) {
    assertFalse(MathUtil.isInRange(value, min, max));
  }

  @Test
  void nullNotInRange() {
    assertFalse(MathUtil.isInRange(null, Float.MIN_VALUE, Float.MAX_VALUE));
  }

}
