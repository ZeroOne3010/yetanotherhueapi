package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.zeroone3010.yahueapi.HueApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

final class HttpUtil {
  private HttpUtil() {
    // prevent instantiation
  }

  static ObjectMapper buildObjectMapper() {
    return JsonMapper.builder()
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build();
  }

  static String put(final Hue hue, final URL baseUrl, final String path, final String body) {
    return getString(hue, baseUrl, path, body, "PUT");
  }

  private static String getString(final Hue hue,
                                  final URL baseUrl,
                                  final String path,
                                  final String body,
                                  final String method) {
    try {
      System.out.println("body: " + body);
      final HttpURLConnection connection = (HttpURLConnection) hue.getUrlConnection(new URL(baseUrl.toString() + path));
      connection.setDoOutput(true);
      connection.setRequestMethod(method);
      connection.setRequestProperty("Host", connection.getURL().getHost());
      if (body != null) {
        try (final OutputStream outputStream = connection.getOutputStream()) {
          try (final OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8")) {
            writer.write(body);
            writer.flush();
          }
        }
      }
      connection.connect();
      try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        return reader.lines().collect(Collectors.joining("\n"));
      }
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
  }
}
