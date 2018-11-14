package com.github.zeroone3010.yahueapi;

import java.util.stream.Stream;

public enum DimmerSwitchButton {
  ON(1),
  DIM_UP(2),
  DIM_DOWN(3),
  OFF(4);

  private final int buttonNumber;

  DimmerSwitchButton(final int buttonNumber) {
    this.buttonNumber = buttonNumber;
  }

  public int getButtonNumber() {
    return buttonNumber;
  }

  static DimmerSwitchButton parseFromButtonEventCode(final int buttonEvent) {
    final int buttonNumber = buttonEvent / 1000;
    return Stream.of(values())
        .filter(value -> value.getButtonNumber() == buttonNumber)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Cannot parse button event " + buttonEvent));
  }
}
