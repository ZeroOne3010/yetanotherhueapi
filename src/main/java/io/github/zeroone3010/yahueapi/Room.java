package io.github.zeroone3010.yahueapi;

import java.util.Collection;
import java.util.Optional;

/**
 * A room or a zone that has been configured into the Hue Bridge.
 */
public interface Room {
  /**
   * Returns the name of the room or zone, as set by the user.
   *
   * @return Name of the room or zone.
   */
  String getName();

  /**
   * Returns all the lights that have been assigned to this room or zone.
   *
   * @return A Collection of Light objects.
   */
  Collection<Light> getLights();

  /**
   * Returns one light, if found by the given name.
   *
   * @param lightName Name of a light in this room or zone.
   * @return Optional.empty() if a light is not found by this name, an Optional&lt;Light&gt; if it is.
   */
  Optional<Light> getLightByName(String lightName);

  /**
   * Queries the state of the room or zone.
   *
   * @return True if any light is on in this room or zone, false if not.
   */
  boolean isAnyOn();

  /**
   * Queries the state of the room or zone.
   *
   * @return True if all lights in this room or zone are on, false if they are not.
   */
  boolean isAllOn();

  /**
   * Sets a state for the room or zone.
   *
   * @param state A state to be set for this room or zone.
   */
  void setState(State state);
}
