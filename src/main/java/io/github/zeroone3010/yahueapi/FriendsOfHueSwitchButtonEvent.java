package io.github.zeroone3010.yahueapi;

public class FriendsOfHueSwitchButtonEvent {

  final FriendsOfHueSwitchButton button;

  FriendsOfHueSwitchButtonEvent(final FriendsOfHueSwitchButton button) {
    this.button = button;
  }

  FriendsOfHueSwitchButtonEvent(final int buttonEventCode) {
    this(FriendsOfHueSwitchButton.parseFromButtonEventCode(buttonEventCode));
  }

  public FriendsOfHueSwitchButton getButton() {
    return button;
  }

  @Override
  public String toString() {
    return "FriendsOfHueSwitchButtonEvent{" +
        "button=" + button +
        '}';
  }
}
