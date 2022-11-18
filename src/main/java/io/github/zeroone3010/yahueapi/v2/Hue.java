package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchdarkly.eventsource.EventSource;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.TrustEverythingManager;
import io.github.zeroone3010.yahueapi.v2.domain.ButtonResource;
import io.github.zeroone3010.yahueapi.v2.domain.DeviceResource;
import io.github.zeroone3010.yahueapi.v2.domain.DeviceResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.GroupResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupedLightResource;
import io.github.zeroone3010.yahueapi.v2.domain.LightResource;
import io.github.zeroone3010.yahueapi.v2.domain.Resource;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceRoot;
import io.github.zeroone3010.yahueapi.v2.domain.ResourceType;
import io.github.zeroone3010.yahueapi.v2.domain.RoomResource;
import io.github.zeroone3010.yahueapi.v2.domain.ZoneResource;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.zeroone3010.yahueapi.TrustEverythingManager.getTrustEverythingHostnameVerifier;
import static io.github.zeroone3010.yahueapi.TrustEverythingManager.getTrustEverythingSocketFactory;
import static io.github.zeroone3010.yahueapi.TrustEverythingManager.getTrustEverythingTrustManager;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;

public class Hue {
  private static final Logger logger = LoggerFactory.getLogger("io.github.zeroone3010.yahueapi");

  public static final String HUE_APPLICATION_KEY_HEADER = "hue-application-key";
  public static final long EVENTS_CONNECTION_TIMEOUT_MINUTES = 1L;
  public static final Duration EVENTS_READ_TIMEOUT = Duration.ofMillis(Integer.MAX_VALUE);

  private final ObjectMapper objectMapper = HttpUtil.buildObjectMapper();

  private final LightFactory lightFactory;
  private final SwitchFactory switchFactory;
  private final GroupFactory groupFactory;

  private final URL resourceUrl;
  private final URL eventUrl;
  private final String apiKey;
  private Map<UUID, Resource> allResources;
  private DeviceResourceRoot devicesRoot;
  private Map<UUID, Light> lights;
  private Map<UUID, Switch> switches;
  private Map<UUID, Group> groups;
  private Map<UUID, GroupedLight> groupedLights;

  /**
   * The basic constructor for initializing the Hue Bridge APIv2 connection for this library.
   * Sets up an encrypted but unverified HTTPS connection,
   * as the Bridge uses a self-signed certificate that cannot be verified.
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
    groupFactory = new GroupFactory(this);
    refresh();
  }

  /**
   * Refreshes the room, lamp, etc. data from the Hue Bridge, in case
   * it has been updated since the application was started.
   *
   * @since 3.0.0
   */
  public void refresh() {
    try (final InputStream inputStream = getUrlConnection("").getInputStream()) {
      final ResourceRoot resourceRoot = objectMapper.readValue(inputStream, ResourceRoot.class);
      allResources = resourceRoot.getData().stream().collect(Collectors.toMap(r -> r.getId(), r -> r));
      System.out.println(resourceRoot);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
    lights = allResources.values().stream()
        .filter(r -> r instanceof LightResource)
        .map(r -> (LightResource) r)
        .map(this::buildLight)
        .collect(toMap(LightImpl::getId, light -> light));

    groupedLights = allResources.values().stream()
        .filter(r -> r instanceof GroupedLightResource)
        .map(r -> (GroupedLightResource) r)
        .map(this::buildGroupedLight)
        .collect(toMap(GroupedLightImpl::getId, light -> light));

    try (final InputStream inputStream = getUrlConnection("/device").getInputStream()) {
      devicesRoot = objectMapper.readValue(inputStream, DeviceResourceRoot.class);
      System.out.println(devicesRoot);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }

    final List<DeviceResource> devices = allResources.values().stream()
        .filter(r -> r instanceof DeviceResource)
        .map(r -> (DeviceResource) r)
        .collect(Collectors.toList());

    final List<ButtonResource> allButtons = allResources.values().stream()
        .filter(r -> r instanceof ButtonResource)
        .map(r -> (ButtonResource) r)
        .collect(Collectors.toList());

    switches = devices.stream()
        .map(device -> buildSwitch(device, allButtons))
        .filter(Objects::nonNull)
        .collect(toMap(Switch::getId, s -> s));

    groups = allResources.values().stream()
        .filter(r -> r instanceof RoomResource || r instanceof ZoneResource)
        .map(r -> (GroupResource) r)
        .map(this::buildGroup)
        .collect(toMap(GroupImpl::getId, group -> group));
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

  private GroupedLightImpl buildGroupedLight(final GroupedLightResource groupedLightResource) {
    return lightFactory.buildGroupedLight(groupedLightResource, resourceUrl);
  }

  private GroupImpl buildGroup(final GroupResource groupResource) {
    return groupFactory.buildGroup(groupResource, resourceUrl);
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

  Resource getResource(final UUID uuid) {
    return allResources.get(uuid);
  }

  /**
   * Returns all the rooms configured into the Bridge.
   *
   * @return A Map of rooms.
   * @since 3.0.0
   */
  public Map<UUID, Group> getRooms() {
    return groups.values().stream()
        .filter(r -> r.getType() == ResourceType.ROOM)
        .collect(toMap(Group::getId, r -> r));
  }

  /**
   * Returns all the zones configured into the Bridge.
   *
   * @return A Map of zones.
   * @since 3.0.0
   */
  public Map<UUID, Group> getZones() {
    return groups.values().stream()
        .filter(r -> r.getType() == ResourceType.ZONE)
        .collect(toMap(Group::getId, r -> r));
  }

  /**
   * Returns all the grouped lights configured into the Bridge.
   *
   * @return A Map of grouped lights.
   * @since 3.0.0
   */
  public Map<UUID, GroupedLight> getGroupedLights() {
    return groupedLights;
  }

  public HueEventSource subscribeToEvents(final HueEventListener eventListener) {
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
      final EventSource eventSource = builder.build();
      eventSource.start();
      return new LaunchDarklyEventSource(eventSource);
    } catch (final Exception e) {
      throw new HueApiException(e);
    }

  }
}
