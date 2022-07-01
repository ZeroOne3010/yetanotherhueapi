package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.HueBridgeProtocol;
import io.github.zeroone3010.yahueapi.TrustEverythingManager;
import io.github.zeroone3010.yahueapi.v2.domain.ButtonResource;
import io.github.zeroone3010.yahueapi.v2.domain.ButtonResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.DeviceResource;
import io.github.zeroone3010.yahueapi.v2.domain.DeviceResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.LightResource;
import io.github.zeroone3010.yahueapi.v2.domain.LightResourceRoot;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.github.zeroone3010.yahueapi.TrustEverythingManager.getTrustEverythingHostnameVerifier;
import static io.github.zeroone3010.yahueapi.TrustEverythingManager.getTrustEverythingSocketFactory;
import static io.github.zeroone3010.yahueapi.TrustEverythingManager.getTrustEverythingTrustManager;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;

public class Hue {
  private static final Logger logger = Logger.getLogger("io.github.zeroone3010.yahueapi");

  public static final String HUE_APPLICATION_KEY_HEADER = "hue-application-key";
  public static final long EVENTS_CONNECTION_TIMEOUT_MINUTES = 1L;
  public static final Duration EVENTS_READ_TIMEOUT = Duration.ofMillis(Integer.MAX_VALUE);

  private final ObjectMapper objectMapper = HttpUtil.buildObjectMapper();

  private final LightFactory lightFactory;
  private final SwitchFactory switchFactory;

  private final URL resourceUrl;
  private final URL eventUrl;
  private final String apiKey;
  private LightResourceRoot lightsRoot;
  private DeviceResourceRoot devicesRoot;
  private ButtonResourceRoot buttonsRoot;
  private Map<UUID, Light> lights;
  private Map<UUID, Switch> switches;

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
      this.resourceUrl = new URL("https://" + bridgeIp + "/clip/v2/resource");
    } catch (MalformedURLException e) {
      throw new HueApiException(e);
    }
    try {
      this.eventUrl = new URL("https://" + bridgeIp + "/eventstream/clip/v2");
    } catch (MalformedURLException e) {
      throw new HueApiException(e);
    }
    this.apiKey = apiKey;
    TrustEverythingManager.trustAllSslConnectionsByDisablingCertificateVerification();
    lightFactory = new LightFactory(this, objectMapper);
    switchFactory = new SwitchFactory(this, objectMapper);
    refresh();
  }

  /**
   * Refreshes the room, lamp, etc. data from the Hue Bridge, in case
   * it has been updated since the application was started.
   *
   * @since 3.0.0
   */
  public void refresh() {
    try (final InputStream inputStream = getUrlConnection("/light").getInputStream()) {
      lightsRoot = objectMapper.readValue(inputStream, LightResourceRoot.class);
      System.out.println(lightsRoot);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
    lights = Collections.unmodifiableMap(Optional.ofNullable(lightsRoot.getData()).orElse(emptyList()).stream()
        .filter(lr -> "light".equals(lr.getType()))
        .map(this::buildLight)
        .collect(toMap(LightImpl::getId, light -> light)));

    try (final InputStream inputStream = getUrlConnection("/device").getInputStream()) {
      devicesRoot = objectMapper.readValue(inputStream, DeviceResourceRoot.class);
      System.out.println(devicesRoot);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }

    try (final InputStream inputStream = getUrlConnection("/button").getInputStream()) {
      buttonsRoot = objectMapper.readValue(inputStream, ButtonResourceRoot.class);
      System.out.println(buttonsRoot);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }

    switches = Collections.unmodifiableMap(Optional.ofNullable(devicesRoot.getData()).orElse(emptyList()).stream()
        .map(device -> buildSwitch(device, buttonsRoot.getData()))
        .filter(Objects::nonNull)
        .collect(toMap(Switch::getId, s -> s)));

  }

  URLConnection getUrlConnection(final String path) {
    try {
      final URL url = new URL(this.resourceUrl.toString() + path);
      return getUrlConnection(url);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  URLConnection getUrlConnection(final URL url) {
    final HttpURLConnection urlConnection;
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
    } catch (IOException e) {
      throw new HueApiException(e);
    }
    urlConnection.setRequestProperty(HUE_APPLICATION_KEY_HEADER, apiKey);
    return urlConnection;
  }

  private LightImpl buildLight(final LightResource lightResource) {
    return lightFactory.buildLight(lightResource, resourceUrl);
  }

  private Switch buildSwitch(final DeviceResource deviceResource, final List<ButtonResource> allButtons) {
    final Map<UUID, ButtonResource> buttons = Optional.ofNullable(allButtons).orElse(emptyList()).stream()
        .collect(Collectors.toMap(ButtonResource::getId, button -> button));
    return switchFactory.buildSwitch(deviceResource, buttons);
  }

  public Map<UUID, Light> getLights() {
    return lights;
  }

  public Map<UUID, Switch> getSwitches() {
    return switches;
  }

  public void subscribeToEvents(final HueEventListener eventListener) {
    try {

      final OkHttpClient client = new OkHttpClient.Builder()
          .sslSocketFactory(
              getTrustEverythingSocketFactory(),
              getTrustEverythingTrustManager()
          )
          .connectTimeout(Duration.ofMinutes(EVENTS_CONNECTION_TIMEOUT_MINUTES))
          .readTimeout(EVENTS_READ_TIMEOUT)
          .hostnameVerifier(getTrustEverythingHostnameVerifier())
          .build();

      final BasicHueEventHandler eventHandler = new BasicHueEventHandler(eventListener);
      final EventSource.Builder builder = new EventSource.Builder(eventHandler, eventUrl.toURI())
          .client(client)
          .headers(Headers.of(HUE_APPLICATION_KEY_HEADER, apiKey))
          .reconnectTime(Duration.ofMillis(3000));
      try (final EventSource eventSource = builder.build()) {
        eventSource.start();
        TimeUnit.MINUTES.sleep(10);
      }
    } catch (final Exception e) {
      throw new HueApiException(e);
    }

  }
}
