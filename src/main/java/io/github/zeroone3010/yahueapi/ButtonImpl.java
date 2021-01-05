package io.github.zeroone3010.yahueapi;

import java.util.Collections;
import java.util.List;

final class ButtonImpl implements Button {
  static final ButtonImpl UNKNOWN = new ButtonImpl(-1, Collections.emptyList());

  private final int buttonNumber;
  private final List<ButtonEvent> possibleEvents;

  public ButtonImpl(final int buttonNumber, final List<ButtonEvent> possibleEvents) {
    this.buttonNumber = buttonNumber;
    this.possibleEvents = possibleEvents;
  }

  @Override
  public int getNumber() {
    return buttonNumber;
  }

  @Override
  public List<ButtonEvent> getPossibleEvents() {
    return possibleEvents;
  }

  @Override
  public String toString() {
    return "Button{" +
        "buttonNumber=" + buttonNumber +
        ", possibleEvents=" + possibleEvents +
        '}';
  }
}
