package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zeroone3010.yahueapi.domain.Group;
import com.github.zeroone3010.yahueapi.domain.GroupState;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

final class RoomImpl implements IRoom {
  private static final Logger logger = Logger.getLogger("RoomImpl");

  private static final String ACTION_PATH = "/action";

  private final ObjectMapper objectMapper;
  private final URL baseUrl;
  private final Set<ILight> lights;
  private final String name;

  RoomImpl(final ObjectMapper objectMapper, final URL baseUrl, final Group group, final Set<ILight> lights) {
    this.objectMapper = objectMapper;
    this.baseUrl = baseUrl;
    this.lights = lights;
    this.name = group.getName();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Collection<ILight> getLights() {
    return lights;
  }

  @Override
  public Optional<ILight> getLightByName(final String lightName) {
    return lights.stream()
        .filter(light -> Objects.equals(light.getName(), lightName))
        .findFirst();
  }

  @Override
  public boolean isAnyOn() {
    return getGroupState().isAnyOn();
  }

  @Override
  public boolean isAllOn() {
    return getGroupState().isAllOn();
  }

  @Override
  public void setState(final State state) {
    final String body;
    try {
      body = objectMapper.writeValueAsString(state);
    } catch (final JsonProcessingException e) {
      throw new HueApiException(e);
    }
    final String result = HttpUtil.put(baseUrl, ACTION_PATH, body);
    logger.fine(result);
  }

  private GroupState getGroupState() {
    try {
      final GroupState state = objectMapper.readValue(baseUrl, Group.class).getState();
      logger.fine(state.toString());
      return state;
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
  }

  @Override
  public String toString() {
    return "Room{" +
        "name='" + name + '\'' +
        '}';
  }
}
