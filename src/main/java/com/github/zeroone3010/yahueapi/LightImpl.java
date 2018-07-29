package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zeroone3010.yahueapi.domain.Light;
import com.github.zeroone3010.yahueapi.domain.LightState;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

final class LightImpl implements ILight {
  private static final Logger logger = Logger.getLogger("LightImpl");
  private static final String STATE_PATH = "/state";
  private static final String ACTION_PATH = "/state";

  private final String id;
  private final String name;
  private final URL baseUrl;
  private final ObjectMapper objectMapper;
  private final LightType type;

  LightImpl(final String id, final Light light, final URL url, final ObjectMapper objectMapper) {
    this.id = id;
    if (light == null) {
      throw new HueApiException("Light " + id + " cannot be found.");
    }
    this.name = light.getName();
    this.baseUrl = url;
    this.objectMapper = objectMapper;
    this.type = LightType.parseTypeString(light.getType());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void turnOn() {
    final String body = "{\"on\":true}";
    final String result = HttpUtil.put(baseUrl, STATE_PATH, body);
    logger.fine(result);
  }

  @Override
  public void turnOff() {
    final String body = "{\"on\":false}";
    final String result = HttpUtil.put(baseUrl, STATE_PATH, body);
    logger.fine(result);
  }

  @Override
  public boolean isOn() {
    try {
      final LightState state = objectMapper.readValue(baseUrl, Light.class).getState();
      logger.fine(state.toString());
      return state.isOn();
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
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

  @Override
  public LightType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Light{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        '}';
  }
}
