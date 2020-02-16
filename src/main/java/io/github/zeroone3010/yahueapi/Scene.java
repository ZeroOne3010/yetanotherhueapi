package io.github.zeroone3010.yahueapi;

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
