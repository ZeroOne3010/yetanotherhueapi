package io.github.zeroone3010.yahueapi.v2;

import java.util.Optional;
import java.util.UUID;

/**
 * A button of a {@link Switch}.
 *
 * @since 3.0.0
 */
public interface Button {

  /**
   * <p>Returns the technical id of the button, as assigned by the Bridge.</p>
   *
   * @return Id of the button.
   */
  UUID getId();

  /**
   * <p>The number of this button in a switch.</p>
   *
   * @return An integer describing the number of this button.
   */
  int getNumber();

  /**
   * Returns the {@link Switch} that this {@code Button} belongs to.
   *
   * @return The {@code Switch} that owns this button.
   */
  Switch getOwner();

  /**
   * <p>The latest event that this button as emitted.</p>
   *
   * @return An enumeration describing the latest event from this particular button. May be empty if this
   * was not the last button to be pressed in its switch.
   */
  Optional<ButtonEventType> getLatestEvent();
}
