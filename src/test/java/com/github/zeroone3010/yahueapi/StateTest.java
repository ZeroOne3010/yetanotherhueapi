package com.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StateTest {

  @Test
  void hexColorAndColorBuildersShouldYieldSameValues() {
    final State hexColorRed = State.builder().color("FF0000").on();
    final State colorRed = State.builder().color(Color.RED).on();
    assertEquals(hexColorRed, colorRed);

    final State hexColorBlue = State.builder().color("0000FF").on();
    final State colorBlue = State.builder().color(Color.BLUE).on();
    assertEquals(hexColorBlue, colorBlue);
  }

}