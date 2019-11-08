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
  void testNameAndIp() {
    final HueBridge hueBridge = new HueBridge("West Wing", "100.100.200.300");
    assertEquals("West Wing", hueBridge.getName());
    assertEquals("100.100.200.300", hueBridge.getIp());
  }
}
