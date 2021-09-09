package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Light effect, as supported by the Hue API.
 *
 * @since 2.3.0
 */
public enum EffectType {
  /**
   * Color loop, cycling through all the colors with the current brightness and saturation.
   */
  COLOR_LOOP("colorloop"),

  /**
   * No effect. Disables an ongoing effect.
   */
  NONE("none"),

  /**
   * A type of effect not recognized by this version of the library.
   */
  UNKNOWN(null);

  private String apiString;

  EffectType(final String apiString) {
    this.apiString = apiString;
  }

  public static EffectType parseTypeString(final String type) {
    if (type == null) {
      return null;
    }
    return Stream.of(values())
        .filter(effectType -> Objects.equals(effectType.getApiString(), type))
        .findFirst()
        .orElse(EffectType.UNKNOWN);
  }

  @JsonValue
  public String getApiString() {
    return apiString;
  }
}
