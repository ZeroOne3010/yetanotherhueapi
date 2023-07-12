package io.github.zeroone3010.yahueapi.v2;

public enum HueEventStreamState {
  /**
   * The state of the stream cannot be determined at the moment.
   */
  UNDEFINED,

  /**
   * The stream is connecting for the first time or reconnecting after a failure.
   */
  CONNECTING,

  /**
   * The stream is OK, actively receiving events.
   */
  ACTIVE,

  /**
   * The stream has been permanently closed. Reconnecting is not possible. No more events will be received.
   */
  CLOSED;
}
