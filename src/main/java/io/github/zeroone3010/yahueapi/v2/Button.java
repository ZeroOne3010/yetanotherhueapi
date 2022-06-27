package io.github.zeroone3010.yahueapi.v2;

import java.util.Optional;
import java.util.UUID;

public interface Button {

  /**
   * <p>Returns the technical id of the button, as assigned by the Bridge.</p>
   *
   * @return Id of the button.
   * @since 3.0.0
   */
  UUID getId();

  /**
   * <p>The number of this button in a switch.</p>
   *
   * @return An integer describing the number of this button.
   * @since 3.0.0
   */
  int getNumber();

  /**
   * <p>The latest event that this button as emitted.</p>
   *
   * @return An enumeration describing the latest event from this particular button. May be empty if this
   * was not the last button to be pressed in its switch.
   * @since 3.0.0
   */
  Optional<ButtonEventType> getLatestEvent();
}
