package com.github.zeroone3010.yahueapi;

import java.util.stream.Stream;

public enum DimmerSwitchAction {
  INITIAL_PRESS(0),
  HOLD(1),
  SHORT_RELEASED(2),
  LONG_RELEASED(3);

  private final int eventCode;

  DimmerSwitchAction(final int eventCode) {
    this.eventCode = eventCode;
  }

  public int getEventCode() {
    return eventCode;
  }

  static DimmerSwitchAction parseFromButtonEventCode(final int buttonEvent) {
    final int eventCode = buttonEvent % 10;
    return Stream.of(values())
        .filter(value -> value.getEventCode() == eventCode)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Cannot parse button event " + buttonEvent));
  }
}
