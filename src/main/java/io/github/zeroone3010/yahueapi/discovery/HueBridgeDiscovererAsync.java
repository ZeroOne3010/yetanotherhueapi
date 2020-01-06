package io.github.zeroone3010.yahueapi.discovery;

/**
 * An interface for an automatic asynchronous Hue Bridge discovery.
 */
public interface HueBridgeDiscovererAsync extends Runnable {
  void onBridgeDiscovered(String ip);
}
