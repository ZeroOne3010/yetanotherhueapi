package io.github.zeroone3010.yahueapi;

/**
 * See https://developers.meethue.com/documentation/supported-lights for further specifications.
 */
public enum LightType {
  ON_OFF_LIGHT,
  DIMMABLE,
  COLOR_TEMPERATURE,
  COLOR,
  EXTENDED_COLOR,
  ON_OFF_PLUGIN_UNIT,
  UNKNOWN;

  static LightType parseTypeString(final String type) {
    if (type == null) {
      return UNKNOWN;
    }
    switch (type.toLowerCase()) {
      case "on/off light":
        return ON_OFF_LIGHT;
      case "dimmable light":
        return DIMMABLE;
      case "color temperature light":
        return COLOR_TEMPERATURE;
      case "color light":
        return COLOR;
      case "extended color light":
        return EXTENDED_COLOR;
      case "on/off plug-in unit":
        return ON_OFF_PLUGIN_UNIT;
      default:
        return UNKNOWN;
    }
  }
}
