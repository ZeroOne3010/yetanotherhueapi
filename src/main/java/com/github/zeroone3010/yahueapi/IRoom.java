package com.github.zeroone3010.yahueapi;

import java.util.Collection;
import java.util.Optional;

public interface IRoom {
  /**
   * Returns the name of the room, as set by the user.
   * @return Name of the room.
   */
  String getName();

  /**
   * Returns all the lights that have been assigned to this room.
   * @return A Collection of ILight objects.
   */
  Collection<ILight> getLights();

  /**
   * Returns one light, if found by the given name.
   * @param lightName Name of a light in this room.
   * @return Optional.empty() if a light is not found by this name, an Optional<ILight> if it is.
   */
  Optional<ILight> getLightByName(String lightName);

  /**
   * Queries the state of the room.
   * @return True if any light is on in this room, false if not.
   */
  boolean isAnyOn();

  /**
   * Queries the state of the room.
   * @return True if all lights in this room are on, false if they are not.
   */
  boolean isAllOn();

  /**
   * Sets a state for the room.
   * @param state An action to perform on this room.
   */
  void setState(Action state);
}
