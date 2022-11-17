package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.ResourceType;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Group {

  /**
   * <p>Returns the technical id of the room, zone, or light group, as assigned by the Bridge.
   * The id stays the same even if the room name is changed by the user.</p>
   *
   * @return Id of the room/zone/light group.
   * @since 3.0.0
   */
  UUID getId();

  /**
   * Returns the name of the room or zone, as set by the user.
   *
   * @return Name of the room or zone.
   * @since 3.0.0
   */
  String getName();

  /**
   * Returns all the lights that have been assigned to this group.
   *
   * @return A Collection of Light objects.
   * @since 3.0.0
   */
  Collection<Light> getLights();

  /**
   * Returns one light, if found by the given name.
   *
   * @param lightName Name of a light in this group.
   * @return Optional.empty() if a light is not found by this name, an Optional&lt;Light&gt; if it is.
   * @since 3.0.0
   */
  Optional<Light> getLightByName(String lightName);

  /**
   * Returns the type of this group, whether it is a room, zone, or grouped light.
   *
   * @return The type of this group of lights.
   * @since 3.0.0
   */
  ResourceType getType();
}
