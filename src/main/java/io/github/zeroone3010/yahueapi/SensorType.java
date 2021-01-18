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
   * A motion sensor, i.e. either a ZLLPresence, a CLIPPresence or a Geofence sensor.
   */
  MOTION,

  /**
   * A switch, e.g. a dimmer switch or a Hue Tap switch.
   */
  SWITCH,

  /**
   * A daylight sensor, i.e. the one in the Bridge.
   */
  DAYLIGHT,

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
        return MOTION;
      case "clippresence":
        return MOTION;
      case "geofence":
        return MOTION;
      case "zllswitch":
        return SWITCH;
      case "zgpswitch":
        return SWITCH;
      case "daylight":
        return DAYLIGHT;
      default:
        return UNKNOWN;
    }
  }
}
