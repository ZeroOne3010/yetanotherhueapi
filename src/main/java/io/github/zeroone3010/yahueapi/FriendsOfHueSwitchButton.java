package io.github.zeroone3010.yahueapi;

import java.util.stream.Stream;

public enum FriendsOfHueSwitchButton {
  LEFT_TOP(20),
  LEFT_BOTTOM(21),
  RIGHT_TOP(23),
  RIGHT_BOTTOM(22),
  TOP(101),
  BOTTOM(99);

  private final int buttonNumber;

  FriendsOfHueSwitchButton(final int buttonNumber) {
    this.buttonNumber = buttonNumber;
  }

  public int getButtonNumber() {
    return buttonNumber;
  }

  static FriendsOfHueSwitchButton parseFromButtonEventCode(final int buttonEvent) {
    return Stream.of(values())
        .filter(value -> value.getButtonNumber() == buttonEvent)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Cannot parse button event " + buttonEvent));
  }
}
