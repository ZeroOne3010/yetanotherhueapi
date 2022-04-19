package io.github.zeroone3010.yahueapi;

/**
 * A scene that has been configured into the Hue Bridge.
 *
 * @since 1.3.0
 */
public interface Scene {
  /**
   * <p>Returns the technical id of the scene, as assigned by the Bridge. The id stays the same even if the scene name
   * is changed by the user.</p>
   *
   * <p>Note that the id is only unique within the context of a single bridge.</p>
   *
   * @return Id of the scene.
   * @since 2.7.0
   */
  String getId();

  /**
   * Activates this scene.
   */
  void activate();

  /**
   * Returns the name of the scene, e.g. "Tropical twilight" or "Concentrate".
   *
   * @return Name of the scene.
   */
  String getName();
}
