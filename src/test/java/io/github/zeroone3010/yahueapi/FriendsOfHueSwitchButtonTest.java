package io.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import static io.github.zeroone3010.yahueapi.FriendsOfHueSwitchButton.TOP;
import static io.github.zeroone3010.yahueapi.FriendsOfHueSwitchButton.BOTTOM;
import static io.github.zeroone3010.yahueapi.FriendsOfHueSwitchButton.LEFT_TOP;
import static io.github.zeroone3010.yahueapi.FriendsOfHueSwitchButton.LEFT_BOTTOM;
import static io.github.zeroone3010.yahueapi.FriendsOfHueSwitchButton.RIGHT_TOP;
import static io.github.zeroone3010.yahueapi.FriendsOfHueSwitchButton.RIGHT_BOTTOM;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FriendsOfHueSwitchButtonTest {
  @Test
  void shouldParseTop() {
    assertEquals(TOP, FriendsOfHueSwitchButton.parseFromButtonEventCode(101));
  }

  @Test
  void shouldParseBottom() {
    assertEquals(BOTTOM, FriendsOfHueSwitchButton.parseFromButtonEventCode(99));
  }

  @Test
  void shouldParseLeftTop() {
    assertEquals(LEFT_TOP, FriendsOfHueSwitchButton.parseFromButtonEventCode(20));
  }

  @Test
  void shouldParseLeftBottom() {
    assertEquals(LEFT_BOTTOM, FriendsOfHueSwitchButton.parseFromButtonEventCode(21));
  }

  @Test
  void shouldParseRightTop() {
    assertEquals(RIGHT_TOP, FriendsOfHueSwitchButton.parseFromButtonEventCode(23));
  }

  @Test
  void shouldParseRightBottom() {
    assertEquals(RIGHT_BOTTOM, FriendsOfHueSwitchButton.parseFromButtonEventCode(22));
  }

}
