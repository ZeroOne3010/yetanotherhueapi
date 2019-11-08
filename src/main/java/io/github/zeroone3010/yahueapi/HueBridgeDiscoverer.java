package io.github.zeroone3010.yahueapi;

import java.util.List;

/**
 * An interface for an automatic Hue Bridge discovery method.
 */
public interface HueBridgeDiscoverer {
  List<HueBridge> discoverBridges();
}
