package com.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class StateTest {

  private static final String HEX_COLOR = "00FF00";
  private static final Integer SAT = 123;
  private static final Integer BRI = 456;
  private static final Integer HUE = 789;
  private static final Integer CT = 10;
  private static final boolean ON = true;

  @Test
  void hexColorAndColorConstructorsShouldYieldSameValues() {
    final State hexColorRed = new State(true, "FF0000");
    final State colorRed = new State(true, Color.RED);
    assertEquals(hexColorRed, colorRed);

    final State hexColorBlue = new State(true, "0000FF");
    final State colorBlue = new State(true, Color.BLUE);
    assertEquals(hexColorBlue, colorBlue);
  }

  @Test
  void jsonCreatorConstructorPrioritizesCtOverOther() {
    final State ct = new State(ON, HEX_COLOR, SAT, BRI, HUE, CT);
    assertEquals(ON, ct.getOn());
    assertEquals(BRI, ct.getBri());
    assertEquals(CT, ct.getCt());
    assertNull(ct.getHue());
    assertNull(ct.getSat());
    assertNull(ct.getXy());
  }

  @Test
  void jsonCreatorConstructorPrioritizesHexColorOverHueSatBri() {
    final State color = new State(ON, HEX_COLOR, SAT, BRI, HUE, null);
    assertEquals(ON, color.getOn());
    assertNotNull(color.getBri());
    assertNotNull(color.getXy());
    assertNull(color.getCt());
    assertNull(color.getHue());
    assertNull(color.getSat());
  }

  @Test
  void jsonCreatorConstructorUsesHueSatBriIfNothingElseGiven() {
    final State color = new State(ON, null, SAT, BRI, HUE, null);
    assertEquals(ON, color.getOn());
    assertEquals(HUE, color.getHue());
    assertEquals(SAT, color.getSat());
    assertEquals(BRI, color.getBri());
    assertNull(color.getXy());
    assertNull(color.getCt());
  }
}