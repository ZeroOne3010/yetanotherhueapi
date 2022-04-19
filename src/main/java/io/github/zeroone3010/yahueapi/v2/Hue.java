package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.HueBridgeProtocol;
import io.github.zeroone3010.yahueapi.TrustEverythingManager;
import io.github.zeroone3010.yahueapi.v2.domain.LightResource;
import io.github.zeroone3010.yahueapi.v2.domain.LightResourceRoot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;

public class Hue {
  private static final Logger logger = Logger.getLogger("io.github.zeroone3010.yahueapi");

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final LightFactory lightFactory;

  private final URL url;
  private final String apiKey;
  private LightResourceRoot lightsRoot;
  private Map<UUID, Light> lights;

  /**
   * The basic constructor for initializing the Hue Bridge APIv2 connection for this library.
   * Sets up an encrypted but unverified HTTPS connection -- see {@link HueBridgeProtocol#UNVERIFIED_HTTPS}.
   *
   * @param bridgeIp The IP address of the Hue Bridge.
   * @param apiKey   The API key of your application.
   * @since 3.0.0
   */
  public Hue(final String bridgeIp, final String apiKey) {
    try {
      this.url = new URL("https://" + bridgeIp + "/clip/v2/resource");
    } catch (MalformedURLException e) {
      throw new HueApiException(e);
    }
    this.apiKey = apiKey;
    TrustEverythingManager.trustAllSslConnectionsByDisablingCertificateVerification();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    lightFactory = new LightFactory(this, objectMapper);
    refresh();
  }

  /**
   * Refreshes the room, lamp, etc. data from the Hue Bridge, in case
   * it has been updated since the application was started.
   *
   * @since 3.0.0
   */
  public void refresh() {
    try (final InputStream inputStream = getUrlConnection().getInputStream()) {
      lightsRoot = objectMapper.readValue(inputStream, LightResourceRoot.class);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
    lights = Collections.unmodifiableMap(Optional.ofNullable(lightsRoot.getData()).orElse(emptyList()).stream()
        .filter(lr -> "light".equals(lr.getType()))
        .map(this::buildLight)
        .collect(toMap(LightImpl::getId, light -> light)));
  }

  URLConnection getUrlConnection() {
    return getUrlConnection(url);
  }

  URLConnection getUrlConnection(final URL url) {
    final HttpURLConnection urlConnection;
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
    } catch (IOException e) {
      throw new HueApiException(e);
    }
    urlConnection.setRequestProperty("hue-application-key", apiKey);
    return urlConnection;
  }



  private LightImpl buildLight(final LightResource lightResource) {
    return lightFactory.buildLight(lightResource, url);
  }

  public Map<UUID, Light> getLights() {
    return lights;
  }
}
