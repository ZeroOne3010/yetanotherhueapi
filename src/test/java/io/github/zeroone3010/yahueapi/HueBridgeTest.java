package io.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HueBridgeTest {
  @Test
  void testIpOnly() {
    final HueBridge hueBridge = new HueBridge("100.100.200.300");
    assertEquals("100.100.200.300", hueBridge.getName());
    assertEquals("100.100.200.300", hueBridge.getIp());
  }

  @Test
  void testIpAndNameAndMac() {
    final HueBridge hueBridge = new HueBridge("100.100.200.300", "West Wing", "11:22:33:44:55:66");
    assertEquals("West Wing", hueBridge.getName());
    assertEquals("100.100.200.300", hueBridge.getIp());
    assertEquals("11:22:33:44:55:66", hueBridge.getMac());
  }
}
