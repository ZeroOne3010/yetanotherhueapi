package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public final class HueCommandLineInterface {
  private static final Logger logger = Logger.getLogger("HueCommandLineInterface");

  private static final String IF_ANY_ON_PARAMETER = "ifAnyOn";
  private static final String THEN_ROOM_PARAMETER = "thenRoom";
  private static final String SET_STATE_PARAMETER = "state";

  public static void main(final String... args) throws Exception {
    final String ifAnyOn = getProperty(IF_ANY_ON_PARAMETER);
    final String thenRoom = getProperty(THEN_ROOM_PARAMETER);
    final String state = getProperty(SET_STATE_PARAMETER);

    final Hue hue = initializeHueBridgeConnection(args);
    final IRoom conditionRoom = hue.getRoomByName(ifAnyOn).get();
    if (conditionRoom.isAnyOn()) {
      logger.info("Some light is on in room '" + conditionRoom + "'.");
      final IRoom targetRoom = hue.getRoomByName(thenRoom).get();

      final ObjectMapper objectMapper = new ObjectMapper();
      targetRoom.setState(objectMapper.readValue(state, Action.class));
    } else {
      logger.info("No lights on in room '" + conditionRoom + "'.");
    }
  }

  private static Hue initializeHueBridgeConnection(String[] args) throws Exception {
    final Hue hue;
    if (args.length == 2) {
      hue = new Hue(args[0], args[1]);
    } else if (args.length == 1) {
      final CompletableFuture<String> apiKey = Hue.hueBridgeConnectionBuilder(args[0])
          .initializeApiConnection("testing");
      hue = new Hue(args[0], apiKey.get());
    } else {
      throw new IllegalArgumentException("Enter the Hue Bridge IP address as the first parameter. Enter the Hue API key as the second parameter.");
    }
    return hue;
  }

  private static boolean isBlank(final String string) {
    return string == null || string.trim().isEmpty();
  }

  private static String getProperty(final String property) {
    final String value = System.getProperty(property);
    if (isBlank(value)) {
      throw new IllegalArgumentException("System property '" + property + "' must be set.");
    }
    return value;
  }
}
