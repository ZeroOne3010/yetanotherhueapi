package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import io.github.zeroone3010.yahueapi.v2.domain.HueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BasicHueEventHandler implements EventHandler {
  private static final Logger logger = LoggerFactory.getLogger(BasicHueEventHandler.class);
  public static final TypeReference<List<HueEvent>> EVENT_LIST_TYPE_REF = new TypeReference<List<HueEvent>>() {
  };

  private final ObjectMapper objectMapper = HttpUtil.buildObjectMapper();
  private final HueEventListener eventListener;

  public BasicHueEventHandler(final HueEventListener eventListener) {
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
  }

  @Override
  public void onComment(final String comment) throws Exception {
    logger.trace("Comment received: " + comment);
  }

  @Override
  public void onError(final Throwable t) {
    logger.info("onError: " + t);
  }
}
