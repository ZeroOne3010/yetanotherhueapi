package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zeroone3010.yahueapi.domain.LightDto;
import com.github.zeroone3010.yahueapi.domain.LightState;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

final class LightImpl implements Light {
  private static final Logger logger = Logger.getLogger("LightImpl");
  private static final String STATE_PATH = "/state";
  private static final String ACTION_PATH = "/state";
  private static final int DIMMABLE_LIGHT_COLOR_TEMPERATURE = 370;

  private final String id;
  private final String name;
  private final URL baseUrl;
  private final ObjectMapper objectMapper;
  private final LightType type;

  LightImpl(final String id, final LightDto light, final URL url, final ObjectMapper objectMapper) {
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
      final LightState state = objectMapper.readValue(baseUrl, LightDto.class).getState();
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
  public State getState() {
    try {
      final LightState state = objectMapper.readValue(baseUrl, LightDto.class).getState();
      logger.fine(state.toString());
      if (state.getColorMode() == null) {
        return new State(state.isOn(), state.getBrightness(), DIMMABLE_LIGHT_COLOR_TEMPERATURE);
      }
      switch (state.getColorMode()) {
        case "xy":
          return new State(state.isOn(), state.getXy(), state.getBrightness());
        case "ct":
          return new State(state.isOn(), state.getBrightness(), state.getCt());
        case "hs":
          return new State(state.isOn(), state.getHue(), state.getSaturation(), state.getBrightness());
      }
      throw new HueApiException("Unknown color mode '" + state.getColorMode() + "'.");
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
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
