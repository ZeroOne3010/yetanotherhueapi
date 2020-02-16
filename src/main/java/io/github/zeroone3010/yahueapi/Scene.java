package io.github.zeroone3010.yahueapi;

/**
 * A scene that has been configured into the Hue Bridge.
 *
 * @since 1.3.0
 */
public interface Scene {
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
