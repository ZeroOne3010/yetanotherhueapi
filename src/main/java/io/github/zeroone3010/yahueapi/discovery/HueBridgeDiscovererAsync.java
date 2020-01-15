package io.github.zeroone3010.yahueapi.discovery;

import io.github.zeroone3010.yahueapi.HueBridge;

/**
 * An interface for code that acts when a Hue Bridge is discovered.
 */
@FunctionalInterface
public interface HueBridgeDiscovererAsync {
  void discover(HueBridge bridge);
}
