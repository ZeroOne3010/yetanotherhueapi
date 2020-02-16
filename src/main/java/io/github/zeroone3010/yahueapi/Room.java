package io.github.zeroone3010.yahueapi;

import java.util.Collection;
import java.util.Optional;

/**
 * A room, a zone, or another type of group that has been configured into the Hue Bridge.
 */
public interface Room {
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
   * Queries the state of the group of lights.
   *
   * @return True if any light is on in this group, false if not.
   */
  boolean isAnyOn();

  /**
   * Queries the state of the group.
   *
   * @return True if all lights in this group are on, false if they are not.
   */
  boolean isAllOn();

  /**
   * Sets a state for the group.
   *
   * @param state A state to be set for this group.
   */
  void setState(State state);

  /**
   * Sets the brightness of the group. If the lights in the group are off, does not turn them on,
   * nor does {@code 0} turn them off.
   *
   * @param brightness A value from {@code 0} (minimum brightness) to {@code 254} (maximum brightness).
   * @since 1.2.0
   */
  void setBrightness(int brightness);

  /**
   * Returns the type of this group of lights, whether it is a room, a zone, or something else.
   *
   * @return The type of this group of lights.
   * @since 1.3.0
   */
  GroupType getType();

  /**
   * Returns the scenes that belong to this group.
   *
   * @return The scenes of this group.
   * @since 1.3.0
   */
  Collection<Scene> getScenes();

  /**
   * Returns one scene, if found by the given name.
   *
   * @param sceneName Name of the scene, e.g. "Tropical twilight" or "Concentrate".
   * @return Optional.empty() if a scene is not found by this name, an Optional&lt;Scene&gt; if it is.
   */
  Optional<Scene> getSceneByName(String sceneName);
}
