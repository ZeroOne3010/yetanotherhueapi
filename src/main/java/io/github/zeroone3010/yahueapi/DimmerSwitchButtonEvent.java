package io.github.zeroone3010.yahueapi;

public class DimmerSwitchButtonEvent {
  final DimmerSwitchButton button;
  final DimmerSwitchAction action;

  DimmerSwitchButtonEvent(final DimmerSwitchButton button, final DimmerSwitchAction action) {
    this.button = button;
    this.action = action;
  }

  DimmerSwitchButtonEvent(final int buttonEventCode) {
    this(DimmerSwitchButton.parseFromButtonEventCode(buttonEventCode),
        DimmerSwitchAction.parseFromButtonEventCode(buttonEventCode));
  }

  public DimmerSwitchButton getButton() {
    return button;
  }

  public DimmerSwitchAction getAction() {
    return action;
  }

  @Override
  public String toString() {
    return "DimmerSwitchButtonEvent{" +
        "button=" + button +
        ", action=" + action +
        '}';
  }
}
