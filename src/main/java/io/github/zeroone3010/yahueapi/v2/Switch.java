package io.github.zeroone3010.yahueapi.v2;

import java.util.List;
import java.util.UUID;

public interface Switch {
  /**
   * <p>Returns the technical id of the switch, as assigned by the Bridge. The id stays the same even if the switch name
   * is changed by the user.</p>
   *
   * @return Id of the switch.
   * @since 3.0.0
   */
  UUID getId();

  /**
   * Lists the buttons of this switch.
   *
   * @return A list of buttons on this switch.
   */
  List<Button> getButtons();

  /**
   * Returns the name of the switch, as set by the user.
   *
   * @return Name of the switch.
   */
  String getName();
}
