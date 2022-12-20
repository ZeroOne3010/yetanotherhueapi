package io.github.zeroone3010.yahueapi;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilTest {
  @Test
  void stringTimestampToZonedDateTime() {
    assertEquals(ZonedDateTime.of(LocalDate.of(2022, 2, 28), LocalTime.of(19, 30), ZoneId.of("UTC")),
        TimeUtil.stringTimestampToZonedDateTime("2022-02-28T19:30"));
  }
}
