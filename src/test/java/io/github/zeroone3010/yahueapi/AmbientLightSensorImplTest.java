package io.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmbientLightSensorImplTest {

  @Test
  void overcastMoon() {
    assertEquals(1, AmbientLightSensorImpl.calculateLux(0));
  }

  @Test
  void brightMoon() {
    assertEquals(1, AmbientLightSensorImpl.calculateLux(1));
  }

  @Test
  void nightLight() {
    assertEquals(2, AmbientLightSensorImpl.calculateLux(3000));
  }

  @Test
  void dimmedLight() {
    assertEquals(10, AmbientLightSensorImpl.calculateLux(10000));
  }

  @Test
  void reading() {
    assertEquals(350, AmbientLightSensorImpl.calculateLux(25441));
  }

  @Test
  void directSunlight() {
    assertEquals(120005, AmbientLightSensorImpl.calculateLux(50793));
  }
}
