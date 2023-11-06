package io.github.zeroone3010.yahueapi.v2;

import java.time.ZonedDateTime;

final class TimeUtil {
  private static final String UTC_SUFFIX = "+00:00[UTC]";

  private TimeUtil() {
    // prevent
  }

  /**
   * The Bridge tends to return times without an explicit zone, but it's implicitly UTC.
   *
   * @param timestamp A timestamp without a time zone, such as "2022-01-29T12:00:00".
   * @return A {@link ZonedDateTime} object corresponding to the given timestamp and assuming it is UTC.
   */
  static ZonedDateTime stringTimestampToZonedDateTime(final String timestamp) {
    return ZonedDateTime.parse(timestamp + UTC_SUFFIX);
  }
}
