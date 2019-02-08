package com.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StateTest {

  @Test
  void hexColorAndColorConstructorsShouldYieldSameValues() {
    final State hexColorRed = new State(true, "FF0000");
    final State colorRed = new State(true, Color.RED);
    assertEquals(hexColorRed, colorRed);

    final State hexColorBlue = new State(true, "0000FF");
    final State colorBlue = new State(true, Color.BLUE);
    assertEquals(hexColorBlue, colorBlue);
  }

}