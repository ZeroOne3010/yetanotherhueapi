package io.github.zeroone3010.yahueapi.v2;

import java.util.UUID;

/**
 * A scene.
 *
 * @since 3.0.0
 */
public interface Scene {

  /**
   * <p>Returns the technical id of the scene, as assigned by the Bridge.
   * The id stays the same even if the scene name is changed by the user.</p>
   *
   * @return Id of the scene.
   */
  UUID getId();

  /**
   * Returns the name of the scene, as set by the user.
   *
   * @return Name of the scene.
   */
  String getName();

  /**
   * Activates this scene.
   */
  void activate();
}
