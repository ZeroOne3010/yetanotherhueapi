package io.github.zeroone3010.yahueapi.v2;

import com.launchdarkly.eventsource.EventSource;

class LaunchDarklyEventSource implements HueEventSource {
  private EventSource eventSource;

  public LaunchDarklyEventSource(final EventSource eventSource) {
    this.eventSource = eventSource;
  }

  @Override
  public void close() {
    eventSource.close();
  }

  @Override
  public HueEventStreamState getState() {
    switch (eventSource.getState()) {
      case OPEN:
        return HueEventStreamState.ACTIVE;
      case CLOSED:
        // fall through
      case CONNECTING:
        return HueEventStreamState.CONNECTING;
      case SHUTDOWN:
        return HueEventStreamState.CLOSED;
      case RAW:
        // fall through:
      default:
        return HueEventStreamState.UNDEFINED;
    }
  }
}
