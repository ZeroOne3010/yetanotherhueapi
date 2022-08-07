package io.github.zeroone3010.yahueapi.v2;

/**
 * Source of streaming events from the Hue Bridge.
 */
public interface HueEventSource extends AutoCloseable {
  /**
   * Closes the event stream. No more events will be received.
   */
  @Override
  void close();

  /**
   * Returns the state of this event stream.
   */
  HueEventStreamState getState();
}
