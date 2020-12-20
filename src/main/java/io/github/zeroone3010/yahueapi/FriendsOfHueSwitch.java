package io.github.zeroone3010.yahueapi;

public interface FriendsOfHueSwitch extends Sensor {
  /**
   * The latest button event of this switch.
   *
   * @return the last button event.
   */
  FriendsOfHueSwitchButtonEvent getLatestButtonEvent();
}
