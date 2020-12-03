package io.github.zeroone3010.yahueapi;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LightTypeTest {
  @ParameterizedTest
  @CsvSource({
      ",UNKNOWN",
      "Foo,UNKNOWN",
      "ON/OFF LIGHT,ON_OFF",
      "Dimmable light,DIMMABLE",
      "Color temperature light,COLOR_TEMPERATURE",
      "Extended Color Light,EXTENDED_COLOR",
      "On/off plug-in unit,ON_OFF_PLUGIN_UNIT"
  })
  void testStringToEnumConversion(final String input, final LightType expectedOutput) {
    assertEquals(expectedOutput, LightType.parseTypeString(input));
  }
}
