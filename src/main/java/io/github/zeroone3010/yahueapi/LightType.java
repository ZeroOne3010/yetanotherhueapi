package io.github.zeroone3010.yahueapi;

/**
 * See https://developers.meethue.com/documentation/supported-lights for further specifications.
 */
public enum LightType {
  ON_OFF, DIMMABLE, COLOR_TEMPERATURE, COLOR, EXTENDED_COLOR,
  UNKNOWN;

  static LightType parseTypeString(final String type) {
    if (type == null) {
      return UNKNOWN;
    }
    switch (type.toLowerCase()) {
      case "on/off light":
        return ON_OFF;
      case "dimmable light":
        return DIMMABLE;
      case "color temperature light":
        return COLOR_TEMPERATURE;
      case "color light":
        return COLOR;
      case "extended color light":
        return EXTENDED_COLOR;
      default:
        return UNKNOWN;
    }
  }
}
