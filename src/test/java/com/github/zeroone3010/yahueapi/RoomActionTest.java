package com.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomActionTest {
  @Test
  void hexColorAndColorConstructorsShouldYieldSameValues() {
    final RoomAction hexColorRed = new RoomAction(true, "FF0000");
    final RoomAction colorRed = new RoomAction(true, "FF0000");
    assertEquals(hexColorRed, colorRed);

    final RoomAction hexColorBlue = new RoomAction(true, "0000FF");
    final RoomAction colorBlue = new RoomAction(true, "0000FF");
    assertEquals(hexColorBlue, colorBlue);
  }
}