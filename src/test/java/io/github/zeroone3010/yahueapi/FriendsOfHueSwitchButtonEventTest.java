package io.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import static io.github.zeroone3010.yahueapi.FriendsOfHueSwitchButton.BOTTOM;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FriendsOfHueSwitchButtonEventTest {
  @Test
  void testInitializationByButtonEvent() {
    final FriendsOfHueSwitchButtonEvent event = new FriendsOfHueSwitchButtonEvent(99);
    assertEquals(BOTTOM, event.getButton());
  }
}
