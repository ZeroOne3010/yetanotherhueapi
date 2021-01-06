package io.github.zeroone3010.yahueapi;

/**
 * This class describes an event of a {@link Switch}. An event in this case consists of a {@link Button}
 * and the action that was performed with the button, i.e. a {@link ButtonEvent}.
 */
public final class SwitchEvent {
  private final Button button;
  private final ButtonEvent action;

  SwitchEvent(final Button button, final ButtonEvent action) {
    this.button = button;
    this.action = action;
  }

  public Button getButton() {
    return button;
  }

  public ButtonEvent getAction() {
    return action;
  }

  @Override
  public String toString() {
    return "SwitchButtonEvent{" +
        "button=" + button +
        ", action=" + action +
        '}';
  }
}
