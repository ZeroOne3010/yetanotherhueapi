package io.github.zeroone3010.yahueapi;

/**
 * See https://developers.meethue.com/documentation/supported-sensors for further specifications.
 */
public enum SensorType {
  /**
   * A temperature sensor. Either ZLLTemperature or CLIPTemperature.
   */
  TEMPERATURE,

  /**
   * A presence sensor, i.e. a motion sensor or a geofence sensor.
   */
  PRESENCE,

  /**
   * A switch, e.g. a dimmer switch or a Hue Tap switch.
   */
  SWITCH,

  /**
   * A daylight sensor, i.e. the one in the Bridge.
   */
  DAYLIGHT,

  /**
   * A ambient light sensor, e.g. the one bundled with a Hue motion sensor.
   */
  AMBIENT_LIGHT,

  /**
   * Other kind of a sensor, not recognized by this library.
   */
  UNKNOWN;


  public static SensorType parseTypeString(final String type) {
    if (type == null) {
      return UNKNOWN;
    }
    switch (type.toLowerCase()) {
      case "zlltemperature":
        return TEMPERATURE;
      case "cliptemperature":
        return TEMPERATURE;
      case "zllpresence":
        return PRESENCE;
      case "clippresence":
        return PRESENCE;
      case "geofence":
        return PRESENCE;
      case "zllswitch":
        return SWITCH;
      case "zgpswitch":
        return SWITCH;
      case "daylight":
        return DAYLIGHT;
      case "zlllightlevel":
        return AMBIENT_LIGHT;
      default:
        return UNKNOWN;
    }
  }
}
