package io.github.zeroone3010.yahueapi.v2.domain.event;

import io.github.zeroone3010.yahueapi.v2.Button;
import io.github.zeroone3010.yahueapi.v2.ButtonEventType;
import io.github.zeroone3010.yahueapi.v2.Switch;

import java.util.UUID;

public class ButtonEvent {
  private String eventTime;
  private Switch theSwitch;
  private Button button;
  private ButtonEventType eventType;
  private UUID eventGroupId;

  public ButtonEvent(final String eventTime,
                     final Switch theSwitch,
                     final Button button,
                     final ButtonEventType buttonEventType,
                     final UUID eventGroupId) {
    this.eventTime = eventTime;
    this.theSwitch = theSwitch;
    this.button = button;
    this.eventType = buttonEventType;
    this.eventGroupId = eventGroupId;
  }

  public String getEventTime() {
    return eventTime;
  }

  /**
   * The {@link Switch} that was activated.
   *
   * @return The {@link Switch} whose button press triggered this event.
   */
  public Switch getSwitch() {
    return theSwitch;
  }

  /**
   * The {@link Button} that was pressed.
   *
   * @return The {@link Button} whose press triggered this event.
   */
  public Button getButton() {
    return button;
  }

  /**
   * Type of the button press that triggered this event.
   *
   * @return A {@link ButtonEventType}.
   */
  public ButtonEventType getEventType() {
    return eventType;
  }

  /**
   * The Bridge may emit several events simultaneously. In that case all of those events
   * will have the same {@code eventGroupId}.
   *
   * @return An {@link UUID}.
   */
  public UUID getEventGroupId() {
    return eventGroupId;
  }

  @Override
  public String toString() {
    return "SwitchEvent{" +
        "eventTime='" + eventTime + '\'' +
        ", switch=" + theSwitch +
        ", button=" + button +
        ", eventType=" + eventType +
        ", eventGroupId=" + eventGroupId +
        '}';
  }
}
