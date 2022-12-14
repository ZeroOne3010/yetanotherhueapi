package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.ResourceType;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * A group of lights, i.e. a room, a zone, or a "grouped light", such as a ceiling fixture with multiple bulbs.
 *
 * @since 3.0.0
 */
public interface Group {

  /**
   * <p>Returns the technical id of the room, zone, or light group, as assigned by the Bridge.
   * The id stays the same even if the room name is changed by the user.</p>
   *
   * @return Id of the room/zone/light group.
   */
  UUID getId();

  /**
   * Returns the name of the room or zone, as set by the user.
   *
   * @return Name of the room or zone.
   */
  String getName();

  /**
   * Returns all the lights that have been assigned to this group.
   *
   * @return A Collection of Light objects.
   */
  Collection<Light> getLights();

  /**
   * Returns one light, if found by the given name.
   *
   * @param lightName Name of a light in this group.
   * @return Optional.empty() if a light is not found by this name, an Optional&lt;Light&gt; if it is.
   */
  Optional<Light> getLightByName(String lightName);

  /**
   * Returns the type of this group, whether it is a room, zone, or grouped light.
   *
   * @return The type of this group of lights.
   */
  ResourceType getType();

  /**
   * Queries the state of the group of lights.
   *
   * @return True if any light is on in this group, false if not.
   */
  boolean isAnyOn();

  /**
   * Turns the lights on.
   */
  void turnOn();

  /**
   * Turns the lights off.
   */
  void turnOff();

  /**
   * Sets the brightness of the lights. If the lights are off, does not turn them on, nor does {@code 0} turn them off.
   *
   * @param brightness A value from {@code 1} (minimum brightness) to {@code 100} (maximum brightness).
   */
  void setBrightness(int brightness);

  /**
   * Sets a state for the light.
   *
   * @param state A state to be set for this light.
   */
  void setState(UpdateState state);
}
