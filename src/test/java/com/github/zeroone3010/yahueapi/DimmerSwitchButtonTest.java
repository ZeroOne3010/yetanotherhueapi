package com.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import static com.github.zeroone3010.yahueapi.DimmerSwitchButton.DIM_DOWN;
import static com.github.zeroone3010.yahueapi.DimmerSwitchButton.DIM_UP;
import static com.github.zeroone3010.yahueapi.DimmerSwitchButton.OFF;
import static com.github.zeroone3010.yahueapi.DimmerSwitchButton.ON;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DimmerSwitchButtonTest {
  @Test
  void shouldParseOn() {
    assertEquals(ON, DimmerSwitchButton.parseFromButtonEventCode(1000));
  }

  @Test
  void shouldParseDimUp() {
    assertEquals(DIM_UP, DimmerSwitchButton.parseFromButtonEventCode(2000));
  }

  @Test
  void shouldParseDimDown() {
    assertEquals(DIM_DOWN, DimmerSwitchButton.parseFromButtonEventCode(3002));
  }

  @Test
  void shouldParseOff() {
    assertEquals(OFF, DimmerSwitchButton.parseFromButtonEventCode(4003));
  }
}