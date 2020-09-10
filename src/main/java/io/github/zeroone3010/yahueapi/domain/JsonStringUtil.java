package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.Gson;

final class JsonStringUtil {
  private static final Gson objectMapper = new Gson();

  private JsonStringUtil() { /* prevent */ }

  static String toJsonString(final Object object) {
    return objectMapper.toJson(object);
  }
}
