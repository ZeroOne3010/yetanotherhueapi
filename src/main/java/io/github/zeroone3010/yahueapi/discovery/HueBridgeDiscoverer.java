package io.github.zeroone3010.yahueapi.discovery;

import io.github.zeroone3010.yahueapi.HueBridge;

import java.util.List;

/**
 * An interface for an automatic Hue Bridge discovery method.
 */
public interface HueBridgeDiscoverer {
  List<HueBridge> discoverBridges();
}
