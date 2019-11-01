package io.github.zeroone3010.yahueapi;

/**
 * See https://developers.meethue.com/develop/hue-api/groupds-api/ for further specifications.
 */
public enum GroupType {

  LUMINAIRE,
  LIGHT_GROUP,
  LIGHT_SOURCE,
  ROOM,
  ENTERTAINMENT,
  ZONE,

  /**
   * Other kind of a group, not recognized by this library.
   */
  UNKNOWN;

  static GroupType parseTypeString(final String type) {
    if (type == null) {
      return UNKNOWN;
    }
    switch (type.toLowerCase()) {
      case "luminaire":
        return LUMINAIRE;
      case "lightgroup":
        return LIGHT_GROUP;
      case "lightsource":
        return LIGHT_SOURCE;
      case "room":
        return ROOM;
      case "entertainment":
        return ENTERTAINMENT;
      case "zone":
        return ZONE;
      default:
        return UNKNOWN;
    }
  }
}
