package com.github.zeroone3010.yahueapi;

public enum SensorType {
  /**
   * A temperature sensor. Either ZLLTemperature or CLIPTemperature.
   */
  TEMPERATURE,

  /**
   * A motion sensor, i.e. either a ZLLPresence or a CLIPPresence sensor.
   */
  MOTION,

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
      case "daylight":
        return DAYLIGHT;
      default:
        return UNKNOWN;
    }
  }
}
