package io.github.zeroone3010.yahueapi;

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

  static String put(final URL baseUrl, final String path, final String body) {
    return getString(baseUrl, path, body, "PUT");
  }

  static String post(final URL baseUrl, final String path, final String body) {
    return getString(baseUrl, path, body, "POST");
  }

  private static String getString(final URL baseUrl, final String path, final String body, final String method) {
    try {
      URL url = new URL(baseUrl.toString() + path);
      HttpURLConnection connection;
      if (url.getProtocol().equals("https")) {
        connection = TrustEverythingManager.createAllTrustedConnection(url);
      } else {
        connection = (HttpURLConnection) url.openConnection();
      }

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
