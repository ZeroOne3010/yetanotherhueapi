package com.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import static com.github.zeroone3010.yahueapi.DimmerSwitchButton.DIM_UP;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DimmerSwitchButtonEventTest {
  @Test
  void testInitializationByButtonEvent() {
    final DimmerSwitchButtonEvent event = new DimmerSwitchButtonEvent(2003);
    assertEquals(DIM_UP, event.getButton());
    assertEquals(DimmerSwitchAction.LONG_RELEASED, event.getAction());
  }
}