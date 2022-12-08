package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import io.github.zeroone3010.yahueapi.v2.domain.HueEvent;
import io.github.zeroone3010.yahueapi.v2.domain.event.ButtonEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BasicHueEventHandler implements EventHandler {
  private static final Logger logger = LoggerFactory.getLogger(BasicHueEventHandler.class);
  public static final TypeReference<List<HueEvent>> EVENT_LIST_TYPE_REF = new TypeReference<List<HueEvent>>() {
  };

  private final ObjectMapper objectMapper = HttpUtil.buildObjectMapper();
  private final Hue hue;
  private final HueEventListener eventListener;

  public BasicHueEventHandler(final Hue hue, final HueEventListener eventListener) {
    this.hue = hue;
    this.eventListener = eventListener;
  }

  @Override
  public void onOpen() throws Exception {
    logger.trace("Connection opened.");
    eventListener.connectionOpened();
  }

  @Override
  public void onClosed() throws Exception {
    logger.trace("Connection closed.");
    eventListener.connectionClosed();
  }

  @Override
  public void onMessage(final String event, final MessageEvent messageEvent) throws Exception {
    logger.debug("Message: " + messageEvent.getData());
    final List<HueEvent> hueEvents = objectMapper.readValue(messageEvent.getData(), EVENT_LIST_TYPE_REF);
    eventListener.receive(hueEvents);
    parseAndAnnounceButtonEvents(hueEvents);
  }

  private void parseAndAnnounceButtonEvents(final List<HueEvent> hueEvents) {
    hueEvents.stream().flatMap(eventsItem ->
        eventsItem.getData().stream()
            .filter(data -> data.getButton().isPresent())
            .map(data -> {
              final Switch theSwitch = hue.getSwitches().get(data.getOwner().getResourceId());
              return new ButtonEvent(eventsItem.getCreationTime(),
                  theSwitch,
                  theSwitch.getButtons().get(data.getResourceId()),
                  ButtonEventType.parseFromButtonEventType(data.getButton().get().getLastEvent()),
                  eventsItem.getId());
            })
    ).forEach(eventListener::receiveButtonEvent);
  }

  @Override
  public void onComment(final String comment) {
    logger.trace("Comment received: " + comment);
  }

  @Override
  public void onError(final Throwable t) {
    logger.info("onError: " + t);
  }
}
