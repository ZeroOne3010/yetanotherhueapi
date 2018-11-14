package com.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import static com.github.zeroone3010.yahueapi.DimmerSwitchAction.HOLD;
import static com.github.zeroone3010.yahueapi.DimmerSwitchAction.INITIAL_PRESS;
import static com.github.zeroone3010.yahueapi.DimmerSwitchAction.LONG_RELEASED;
import static com.github.zeroone3010.yahueapi.DimmerSwitchAction.SHORT_RELEASED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DimmerSwitchActionTest {
  @Test
  void shouldParseInitialPress() {
    assertEquals(INITIAL_PRESS, DimmerSwitchAction.parseFromButtonEventCode(1000));
  }

  @Test
  void shouldParseHold() {
    assertEquals(HOLD, DimmerSwitchAction.parseFromButtonEventCode(1001));
  }

  @Test
  void shouldParseShortReleased() {
    assertEquals(SHORT_RELEASED, DimmerSwitchAction.parseFromButtonEventCode(2002));
  }

  @Test
  void shouldParseLongReleased() {
    assertEquals(LONG_RELEASED, DimmerSwitchAction.parseFromButtonEventCode(4003));
  }
}