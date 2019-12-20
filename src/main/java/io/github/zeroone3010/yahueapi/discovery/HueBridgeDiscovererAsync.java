package io.github.zeroone3010.yahueapi.discovery;

public interface HueBridgeDiscovererAsync extends Runnable{
  void onBridgeDiscovered(String ip);

  void onTimeout();
}
