package io.github.zeroone3010.yahueapi.v2;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchdarkly.eventsource.ConnectStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import io.github.zeroone3010.yahueapi.HueApiException;
import io.github.zeroone3010.yahueapi.HueBridgeConnectionBuilder;
import io.github.zeroone3010.yahueapi.SecureJsonFactory;
import io.github.zeroone3010.yahueapi.v2.domain.BridgeResource;
import io.github.zeroone3010.yahueapi.v2.domain.ButtonResource;
import io.github.zeroone3010.yahueapi.v2.domain.DeviceResource;
import io.github.zeroone3010.yahueapi.v2.domain.GroupResource;
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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.MOTION;
import static io.github.zeroone3010.yahueapi.v2.domain.ResourceType.TEMPERATURE;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Hue {
  private static final Logger logger = LoggerFactory.getLogger("io.github.zeroone3010.yahueapi");

  public static final String HUE_APPLICATION_KEY_HEADER = "hue-application-key";
  public static final long EVENTS_CONNECTION_TIMEOUT_MINUTES = 1L;
  public static final Duration EVENTS_READ_TIMEOUT = Duration.ofMillis(Integer.MAX_VALUE);

  final ObjectMapper objectMapper;

  private final LightFactory lightFactory;
  private final SwitchFactory switchFactory;
  private final GroupFactory groupFactory;
  private final MotionSensorFactory motionSensorFactory;
  private final TemperatureSensorFactory temperatureSensorFactory;

  private final URL resourceUrl;
  private final URL eventUrl;
  private final String apiKey;
  private Map<UUID, Resource> allResources;
  private Map<UUID, Light> lights;
  private Map<UUID, Switch> switches;
  private Map<UUID, Group> groups;
  private Map<UUID, MotionSensor> motionSensors;
  private Map<UUID, TemperatureSensor> temperatureSensors;
  private final String bridgeIp;
  private String bridgeId;

  /**
   * The basic constructor for initializing the Hue Bridge APIv2 connection for this library.
   *
   * @param bridgeIp The IP address of the Hue Bridge.
   * @param apiKey   The API key of your application.
   * @since 3.0.0
   */
  public Hue(final String bridgeIp, final String apiKey) {
    this.bridgeIp = bridgeIp;
    try {
      this.resourceUrl = new URL("https://" + this.bridgeIp + "/clip/v2/resource");
    } catch (MalformedURLException e) {
      throw new HueApiException(e);
    }
    try {
      this.eventUrl = new URL("https://" + this.bridgeIp + "/eventstream/clip/v2");
    } catch (MalformedURLException e) {
      throw new HueApiException(e);
    }

    this.apiKey = apiKey;
    this.objectMapper = HttpUtil.buildObjectMapper(this.bridgeIp);

    lightFactory = new LightFactory(this, objectMapper);
    switchFactory = new SwitchFactory(this, objectMapper);
    groupFactory = new GroupFactory(this, objectMapper);
    motionSensorFactory = new MotionSensorFactory(this, objectMapper);
    temperatureSensorFactory = new TemperatureSensorFactory(this, objectMapper);
    refresh();
  }

  URL getResourceUrl() {
    return resourceUrl;
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
      logger.trace("Resource root: " + resourceRoot);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
    lights = allResources.values().stream()
        .filter(r -> r instanceof LightResource)
        .map(r -> (LightResource) r)
        .map(this::buildLight)
        .collect(toMap(LightImpl::getId, light -> light));

    final List<DeviceResource> devices = allResources.values().stream()
        .filter(r -> r instanceof DeviceResource)
        .map(r -> (DeviceResource) r)
        .collect(toList());

    final List<ButtonResource> allButtons = allResources.values().stream()
        .filter(r -> r instanceof ButtonResource)
        .map(r -> (ButtonResource) r)
        .collect(toList());

    switches = devices.stream()
        .map(device -> buildSwitch(device, allButtons))
        .filter(Objects::nonNull)
        .collect(toMap(Switch::getId, s -> s));

    motionSensors = devices.stream()
        .filter(device -> device.getServices().stream().anyMatch(service -> MOTION == service.getResourceType()))
        .map(device -> buildMotionSensor(device))
        .collect(Collectors.toMap(MotionSensor::getId, d -> d));

    temperatureSensors = devices.stream()
        .filter(device -> device.getServices().stream().anyMatch(service -> TEMPERATURE == service.getResourceType()))
        .map(device -> buildTemperatureSensor(device))
        .collect(Collectors.toMap(TemperatureSensor::getId, d -> d));

    groups = allResources.values().stream()
        .filter(r -> r instanceof RoomResource || r instanceof ZoneResource)
        .map(r -> (GroupResource) r)
        .map(this::buildGroup)
        .collect(toMap(GroupImpl::getId, group -> group));

    bridgeId = allResources.values().stream()
        .filter(r -> r instanceof BridgeResource)
        .map(r -> (BridgeResource) r)
        .map(bridge -> bridge.getBridgeId())
        .findFirst()
        .orElse(null);
  }

  URLConnection getUrlConnection(final String path) {
    try {
      final URL url = new URL(this.resourceUrl.toString() + path);
      return getUrlConnection(url);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  HttpsURLConnection getUrlConnection(final URL url) {
    try {
      final HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
      final SecureJsonFactory factory = (SecureJsonFactory) objectMapper.getFactory();
      urlConnection.setSSLSocketFactory(factory.getSocketFactory());
      urlConnection.setHostnameVerifier(factory.getHostnameVerifier());
      urlConnection.setRequestProperty(HUE_APPLICATION_KEY_HEADER, apiKey);
      return urlConnection;
    } catch (IOException e) {
      throw new HueApiException(e);
    }
  }

  private LightImpl buildLight(final LightResource lightResource) {
    return lightFactory.buildLight(lightResource, resourceUrl);
  }

  private GroupImpl buildGroup(final GroupResource groupResource) {
    return groupFactory.buildGroup(groupResource, allResources);
  }

  private Switch buildSwitch(final DeviceResource deviceResource, final List<ButtonResource> allButtons) {
    final Map<UUID, ButtonResource> buttons = Optional.ofNullable(allButtons).orElse(emptyList()).stream()
        .collect(Collectors.toMap(ButtonResource::getId, button -> button));
    return switchFactory.buildSwitch(deviceResource, buttons);
  }

  private MotionSensorImpl buildMotionSensor(final DeviceResource device) {
    return motionSensorFactory.buildMotionSensor(device, resourceUrl);
  }

  private TemperatureSensorImpl buildTemperatureSensor(final DeviceResource device) {
    return temperatureSensorFactory.buildTemperatureSensor(device, resourceUrl);
  }

  /**
   * Returns all the lights configured into the Bridge.
   *
   * @return A Map of lights, the keys being their ids.
   */
  public Map<UUID, Light> getLights() {
    return lights;
  }

  /**
   * Returns all the switches configured into the Bridge.
   * Different kinds of switches include, for example, the Philips Hue dimmer switch and the Philips Hue Tap switch.
   *
   * @return A Map of switches, the keys being the IDs of the switches.
   * @since 3.0.0
   */
  public Map<UUID, Switch> getSwitches() {
    return switches;
  }

  /**
   * Returns all the motion sensors configured into the Bridge.
   *
   * @return A Map of motion sensors, the keys being the IDs of the sensors.
   * @since 3.0.0
   */
  public Map<UUID, MotionSensor> getMotionSensors() {
    return motionSensors;
  }

  /**
   * Returns all the temperature sensors configured into the Bridge.
   *
   * @return A Map of motion sensors, the keys being the IDs of the sensors.
   * @since 3.0.0
   */
  public Map<UUID, TemperatureSensor> getTemperatureSensors() {
    return temperatureSensors;
  }

  Resource getResource(final UUID uuid) {
    return allResources.get(uuid);
  }

  List<DeviceResource> getDevices() {
    return allResources.values().stream()
        .filter(resource -> resource.getType() == ResourceType.DEVICE)
        .map(resource -> (DeviceResource) resource)
        .collect(toList());
  }

  /**
   * Returns all the rooms configured into the Bridge.
   *
   * @return A Map of rooms, the keys being the IDs of the rooms.
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
   * @return A Map of zones, the keys being the IDs of the zones.
   * @since 3.0.0
   */
  public Map<UUID, Group> getZones() {
    return groups.values().stream()
        .filter(r -> r.getType() == ResourceType.ZONE)
        .collect(toMap(Group::getId, r -> r));
  }

  /**
   * Returns a specific room by its name.
   *
   * @param roomName The name of a room
   * @return A room or {@code Optional.empty()} if a room with the given name does not exist.
   * @since 3.0.0
   */
  public Optional<Group> getRoomByName(final String roomName) {
    return getRooms().values().stream().filter(group -> Objects.equals(group.getName(), roomName)).findFirst();
  }

  /**
   * Returns a specific zone by its name.
   *
   * @param zoneName The name of a zone
   * @return A zone or {@code Optional.empty()} if a zone with the given name does not exist.
   * @since 3.0.0
   */
  public Optional<Group> getZoneByName(final String zoneName) {
    return getZones().values().stream().filter(group -> Objects.equals(group.getName(), zoneName)).findFirst();
  }

  /**
   * Returns the technical ID of the Bridge.
   *
   * @return A String such as "00173321ae25bae8"
   * @since 3.0.0
   */
  public String getBridgeId() {
    return bridgeId;
  }

  public HueEventSource subscribeToEvents(final HueEventListener eventListener) {
    try {
      SSLSocketFactory factory;
      X509TrustManager trustManager;

      JsonFactory jsonFactory = objectMapper.getFactory();
      SecureJsonFactory secureJsonFactory = (SecureJsonFactory) jsonFactory;
      factory = secureJsonFactory.getSocketFactory();
      trustManager = secureJsonFactory.getTrustManager();

      final OkHttpClient client = new OkHttpClient.Builder()
          .sslSocketFactory(factory, trustManager)
          .connectTimeout(Duration.ofMinutes(EVENTS_CONNECTION_TIMEOUT_MINUTES))
          .readTimeout(EVENTS_READ_TIMEOUT)
          .hostnameVerifier(secureJsonFactory.getHostnameVerifier())
          .build();

      final BasicHueEventHandler eventHandler = new BasicHueEventHandler(this, eventListener);

      final BackgroundEventSource.Builder builder = new BackgroundEventSource.Builder(eventHandler,
          new EventSource.Builder(ConnectStrategy.http(eventUrl.toURI())
              .httpClient(client)
              .headers(Headers.of(HUE_APPLICATION_KEY_HEADER, apiKey))
              .connectTimeout(5000, TimeUnit.MILLISECONDS)
          ).retryDelay(3000, TimeUnit.MILLISECONDS)
      );

      final BackgroundEventSource eventSource = builder.build();
      eventSource.start();
      return new LaunchDarklyEventSource(eventSource);
    } catch (final Exception e) {
      throw new HueApiException(e);
    }

  }

  /**
   * The method to be used if you do not have an API key for your application yet.
   * Returns a {@code HueBridgeConnectionBuilder} that initializes the process of
   * adding a new application to the Bridge. You can test if you are connecting to
   * a Hue Bridge endpoint before initializing the connection.
   *
   * @param bridgeIp The IP address of the Bridge.
   * @return A connection builder that initializes the application for the Bridge.
   * @since 3.0.0
   */
  public static HueBridgeConnectionBuilder hueBridgeConnectionBuilder(final String bridgeIp) {
    return new HueBridgeConnectionBuilder(bridgeIp);
  }
}
