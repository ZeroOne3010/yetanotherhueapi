package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

final class JsonStringUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private JsonStringUtil() { /* prevent */ }

  static String toJsonString(final Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
