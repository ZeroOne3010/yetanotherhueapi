package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType;
import org.junit.jupiter.api.Test;

import static io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType.HOLD;
import static io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType.INITIAL_PRESS;
import static io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType.LONG_RELEASED;
import static io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType.SHORT_RELEASED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ButtonEventTest {
  @Test
  void shouldParseInitialPress() {
    assertEquals(INITIAL_PRESS, ButtonEventType.parseFromButtonEventType("initial_press"));
  }

  @Test
  void shouldParseHold() {
    assertEquals(HOLD, ButtonEventType.parseFromButtonEventType("repeat"));
  }

  @Test
  void shouldParseShortReleased() {
    assertEquals(SHORT_RELEASED, ButtonEventType.parseFromButtonEventType("short_release"));
  }

  @Test
  void shouldParseLongReleased() {
    assertEquals(LONG_RELEASED, ButtonEventType.parseFromButtonEventType("long_release"));
  }
}
