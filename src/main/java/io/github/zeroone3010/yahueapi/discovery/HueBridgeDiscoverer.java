package io.github.zeroone3010.yahueapi.discovery;

import io.github.zeroone3010.yahueapi.HueBridge;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * An interface for an automatic Hue Bridge discovery method.
 */
public interface HueBridgeDiscoverer {
  CompletableFuture<List<HueBridge>> discoverBridges();
}
