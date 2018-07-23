package com.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ActionTest {

  private static final String HEX_COLOR = "00FF00";
  private static final Integer SAT = 123;
  private static final Integer BRI = 456;
  private static final Integer HUE = 789;
  private static final Integer CT = 10;
  private static final boolean ON = true;

  @Test
  void hexColorAndColorConstructorsShouldYieldSameValues() {
    final Action hexColorRed = new Action(true, "FF0000");
    final Action colorRed = new Action(true, Color.RED);
    assertEquals(hexColorRed, colorRed);

    final Action hexColorBlue = new Action(true, "0000FF");
    final Action colorBlue = new Action(true, Color.BLUE);
    assertEquals(hexColorBlue, colorBlue);
  }

  @Test
  void jsonCreatorConstructorPrioritizesCtOverOther() {
    final Action ct = new Action(ON, HEX_COLOR, SAT, BRI, HUE, CT);
    assertEquals(ON, ct.getOn());
    assertEquals(BRI, ct.getBri());
    assertEquals(CT, ct.getCt());
    assertNull(ct.getHue());
    assertNull(ct.getSat());
    assertNull(ct.getXy());
  }

  @Test
  void jsonCreatorConstructorPrioritizesHexColorOverHueSatBri() {
    final Action color = new Action(ON, HEX_COLOR, SAT, BRI, HUE, null);
    assertEquals(ON, color.getOn());
    assertNotNull(color.getBri());
    assertNotNull(color.getXy());
    assertNull(color.getCt());
    assertNull(color.getHue());
    assertNull(color.getSat());
  }

  @Test
  void jsonCreatorConstructorUsesHueSatBriIfNothingElseGiven() {
    final Action color = new Action(ON, null, SAT, BRI, HUE, null);
    assertEquals(ON, color.getOn());
    assertEquals(HUE, color.getHue());
    assertEquals(SAT, color.getSat());
    assertEquals(BRI, color.getBri());
    assertNull(color.getXy());
    assertNull(color.getCt());
  }
}