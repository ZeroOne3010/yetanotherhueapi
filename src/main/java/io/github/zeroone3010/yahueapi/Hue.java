package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.ApiInitializationStatus;
import io.github.zeroone3010.yahueapi.domain.Group;
import io.github.zeroone3010.yahueapi.domain.Root;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class Hue {
  private static final String ROOM_TYPE_GROUP = "Room";
  private static final String ZONE_TYPE_GROUP = "Zone";

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final SensorFactory sensorFactory = new SensorFactory(this, objectMapper);
  private final RoomFactory roomFactory;
  private final LightFactory lightFactory;

  private final String uri;
  private Root root;
  private Map<String, Light> lights;
  private Map<String, Room> rooms;
  private Map<String, Room> zones;
  private Map<String, Sensor> sensors;
  private boolean caching = false;

  /**
   * The basic constructor for initializing the Hue Bridge connection for this library.
   * Use the {@code hueBridgeConnectionBuilder} method if you do not have an API key yet.
   *
   * @param bridgeIp The IP address of the Hue Bridge.
   * @param apiKey   The API key of your application.
   * @since 1.0.0
   */
  public Hue(final String bridgeIp, final String apiKey) {
    this(HueBridgeProtocol.HTTP, bridgeIp, apiKey);
  }

  /**
   * <p>Controls whether cached states of objects, such as lights, should be used.
   * Off ({@code false}) by default. If set on ({@code true}), querying light states will
   * NOT actually relay the query to the Bridge. Instead, it uses the state that was valid
   * when this method was called, or the state that was valid when subsequent calls to
   * the {@link #refresh()} method were made.</p>
   *
   * <p>One should use caching when performing multiple queries in a quick succession,
   * such as when querying the states of all the individual lights in a Hue setup.</p>
   *
   * <p>If caching is already on and you try to enable it again, this method does nothing.
   * Similarly nothing happens if caching is already disabled and one tries to disable it again.</p>
   *
   * @param enabled Set to {@code true} to have the lights cache their results from the Bridge.
   *                Remember to call {@link #refresh()} first when you need to retrieve the
   *                absolutely current states.
   * @since 1.2.0
   */
  public void setCaching(final boolean enabled) {
    if (caching != enabled) {
      caching = enabled;
      refresh();
    }
  }

  /**
   * Tells whether this instance caches the states of objects, such as lights.
   *
   * @return {@code true} if cached results are returned, {@code false} if all queries are directed to the Bridge.
   * @since 1.2.0
   */
  public boolean isCaching() {
    return caching;
  }

  /**
   * A basic constructor for initializing the Hue Bridge connection for this library.
   * Use the {@code hueBridgeConnectionBuilder} method if you do not have an API key yet.
   *
   * @param protocol The desired protocol for the Bridge connection. HTTP or UNVERIFIED_HTTPS,
   *                 as the certificate that the Bridge uses cannot be verified. Defaults to HTTP
   *                 when using the other constructor.
   * @param bridgeIp The IP address of the Hue Bridge.
   * @param apiKey   The API key of your application.
   * @since 1.0.0
   */
  public Hue(final HueBridgeProtocol protocol, final String bridgeIp, final String apiKey) {
    this.uri = protocol.getProtocol() + "://" + bridgeIp + "/api/" + apiKey + "/";
    if (HueBridgeProtocol.UNVERIFIED_HTTPS.equals(protocol)) {
      TrustEverythingManager.trustAllSslConnectionsByDisablingCertificateVerification();
    }
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    roomFactory = new RoomFactory(this, objectMapper, uri);
    lightFactory = new LightFactory(this, objectMapper);
  }

  private void doInitialDataLoadIfRequired() {
    if (root == null) {
      refresh();
    }
  }

  /**
   * Refreshes the room, lamp, etc. data from the Hue Bridge, in case
   * it has been updated since the application was started.
   *
   * This method is particularly useful if caching is enabled
   * with the {@link #setCaching(boolean)} method. Calls to {@code refresh()}
   * will, in that case, refresh the states of the lights.
   *
   * @since 1.0.0
   */
  public void refresh() {
    try {
      root = objectMapper.readValue(new URL(uri), Root.class);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
    this.lights = Collections.unmodifiableMap(root.getLights().entrySet().stream()
        .map(light -> buildLight(light.getKey(), root))
        .collect(toMap(LightImpl::getId, light -> light)));
    this.rooms = Collections.unmodifiableMap(findGroupsOfType(ROOM_TYPE_GROUP));
    this.zones = Collections.unmodifiableMap(findGroupsOfType(ZONE_TYPE_GROUP));
    this.sensors = Collections.unmodifiableMap(root.getSensors().entrySet().stream()
        .map(sensor -> buildSensor(sensor.getKey(), root))
        .collect(toMap(Sensor::getId, sensor -> sensor)));
  }

  private Map<String, Room> findGroupsOfType(final String groupType) {
    return root.getGroups().entrySet().stream()
        .filter(g -> g.getValue().getType().equals(groupType))
        .map(group -> buildRoom(group.getKey(), group.getValue()))
        .collect(toMap(Room::getName, room -> room));
  }

  Light getLightById(final String id) {
    doInitialDataLoadIfRequired();
    return lights.get(id);
  }

  /**
   * Returns all the rooms configured into the Bridge.
   *
   * @return A Collection of rooms.
   * @since 1.0.0
   */
  public Collection<Room> getRooms() {
    doInitialDataLoadIfRequired();
    return Collections.unmodifiableCollection(this.rooms.values());
  }

  /**
   * Returns all the zones configured into the Bridge.
   *
   * @return A Collection of zones as Room objects.
   * @since 1.1.0
   */
  public Collection<Room> getZones() {
    doInitialDataLoadIfRequired();
    return Collections.unmodifiableCollection(this.zones.values());
  }

  /**
   * Returns a specific room by its name.
   *
   * @param roomName The name of a room
   * @return A room or {@code Optional.empty()} if a room with the given name does not exist.
   * @since 1.0.0
   */
  public Optional<Room> getRoomByName(final String roomName) {
    doInitialDataLoadIfRequired();
    return Optional.ofNullable(this.rooms.get(roomName));
  }

  /**
   * Returns a specific zone by its name.
   *
   * @param zoneName The name of a zone
   * @return A zone or {@code Optional.empty()} if a zone with the given name does not exist.
   * @since 1.1.0
   */
  public Optional<Room> getZoneByName(final String zoneName) {
    doInitialDataLoadIfRequired();
    return Optional.ofNullable(this.zones.get(zoneName));
  }

  private Room buildRoom(final String groupId, final Group group) {
    return roomFactory.buildRoom(groupId, group);
  }

  private Sensor buildSensor(final String sensorId, final Root root) {
    return sensorFactory.buildSensor(sensorId, root.getSensors().get(sensorId), uri);
  }

  private LightImpl buildLight(final String lightId, final Root root) {
    return lightFactory.buildLight(lightId, root, uri);
  }

  /**
   * Returns the raw root node information of the REST API. Not required for anything but querying the most
   * technical details of the Bridge setup. Note that it is not possible to change the state of the Bridge or
   * the lights by using any values returned by this method: the results are read-only.
   *
   * The results of this method are also always cached, so a call to this method never triggers a query to the Bridge
   * (unless no other queries have been made to the Bridge since this instance of {@code Hue} was constructed).
   * To refresh the cache call the {@link #refresh()} method.
   *
   * @return A Root element, as received from the Bridge REST API. Always returns a cached version of the data.
   *
   * @since 1.0.0
   */
  public Root getRaw() {
    doInitialDataLoadIfRequired();
    return this.root;
  }

  /**
   * Returns all the sensors configured into the Bridge.
   *
   * @return A Collection of sensors.
   * @since 1.0.0
   */
  public Collection<Sensor> getUnknownSensors() {
    return getSensorsByType(SensorType.UNKNOWN, Sensor.class);
  }

  /**
   * Returns all the temperature sensors configured into the Bridge.
   *
   * @return A Collection of temperature sensors.
   * @since 1.0.0
   */
  public Collection<TemperatureSensor> getTemperatureSensors() {
    return getSensorsByType(SensorType.TEMPERATURE, TemperatureSensor.class);
  }

  /**
   * Returns all the dimmer switches configured into the Bridge.
   *
   * @return A Collection of dimmer switches.
   * @since 1.0.0
   */
  public Collection<DimmerSwitch> getDimmerSwitches() {
    return getSensorsByType(SensorType.DIMMER_SWITCH, DimmerSwitch.class);
  }

  /**
   * Returns all the motion sensors configured into the Bridge.
   *
   * @return A Collection of motion sensors.
   * @since 1.0.0
   */
  public Collection<MotionSensor> getMotionSensors() {
    return getSensorsByType(SensorType.MOTION, MotionSensor.class);
  }

  /**
   * Returns all the daylight sensors configured into the Bridge.
   *
   * @return A Collection of daylight sensors.
   * @since 1.0.0
   */
  public Collection<DaylightSensor> getDaylightSensors() {
    return getSensorsByType(SensorType.DAYLIGHT, DaylightSensor.class);
  }

  private <T> Collection<T> getSensorsByType(final SensorType type, final Class<T> sensorClass) {
    doInitialDataLoadIfRequired();
    return Collections.unmodifiableCollection(this.sensors.values().stream()
        .filter(s -> type.equals(s.getType()))
        .map(sensorClass::cast)
        .collect(toList()));
  }

  /**
   * Returns a specific temperature sensor by its name.
   *
   * @param sensorName The name of a sensor
   * @return A sensor or {@code Optional.empty()} if a sensor with the given name does not exist.
   * @since 1.0.0
   */
  public Optional<TemperatureSensor> getTemperatureSensorByName(final String sensorName) {
    doInitialDataLoadIfRequired();
    return getTemperatureSensors().stream()
        .filter(sensor -> Objects.equals(sensor.getName(), sensorName))
        .map(TemperatureSensor.class::cast)
        .findFirst();
  }

  /**
   * Returns a specific motion sensor by its name.
   *
   * @param sensorName The name of a sensor
   * @return A sensor or {@code Optional.empty()} if a sensor with the given name does not exist.
   * @since 1.0.0
   */
  public Optional<MotionSensor> getMotionSensorByName(final String sensorName) {
    doInitialDataLoadIfRequired();
    return getMotionSensors().stream()
        .filter(sensor -> Objects.equals(sensor.getName(), sensorName))
        .map(MotionSensor.class::cast)
        .findFirst();
  }

  /**
   * Returns a specific dimmer switch by its name.
   *
   * @param switchName The name of a switch
   * @return A dimmer switch or {@code Optional.empty()} if a dimmer switch with the given name does not exist.
   * @since 1.0.0
   */
  public Optional<DimmerSwitch> getDimmerSwitchByName(final String switchName) {
    doInitialDataLoadIfRequired();
    return getDimmerSwitches().stream()
        .filter(sensor -> Objects.equals(sensor.getName(), switchName))
        .map(DimmerSwitch.class::cast)
        .findFirst();
  }

  /**
   * The method to be used if you do not have an API key for your application yet.
   * Returns a {@code HueBridgeConnectionBuilder} that initializes the process of
   * adding a new application to the Bridge.
   *
   * @param bridgeIp The IP address of the Bridge.
   * @return A connection builder that initializes the application for the Bridge.
   * @since 1.0.0
   */
  public static HueBridgeConnectionBuilder hueBridgeConnectionBuilder(final String bridgeIp) {
    return new HueBridgeConnectionBuilder(bridgeIp);
  }

  public static class HueBridgeConnectionBuilder {
    private static final int MAX_TRIES = 30;
    private String bridgeIp;

    private HueBridgeConnectionBuilder(final String bridgeIp) {
      this.bridgeIp = bridgeIp;
    }

    /**
     * Returns a {@code CompletableFuture} that completes once you push the button on the Hue Bridge. Returns an API
     * key that you should use for any subsequent calls to the Bridge API.
     *
     * @param appName The name of your application.
     * @return A {@code CompletableFuture} with an API key for your application. You should store this key for future usage.
     * @since 1.0.0
     */
    public CompletableFuture<String> initializeApiConnection(final String appName) {
      final Supplier<String> apiKeySupplier = () -> {
        final String body = "{\"devicetype\":\"yetanotherhueapi#" + appName + "\"}";
        final URL baseUrl;
        try {
          baseUrl = new URL("http://" + bridgeIp + "/api");
        } catch (final MalformedURLException e) {
          throw new HueApiException(e);
        }

        String latestError = null;
        for (int triesLeft = MAX_TRIES; triesLeft > 0; triesLeft--) {
          try {
            System.out.println("Please push the button on the Hue Bridge now (" + triesLeft + " seconds left).");

            final String result = HttpUtil.post(baseUrl, "", body);
            System.out.println(result);
            final ApiInitializationStatus status = new ObjectMapper().<ArrayList<ApiInitializationStatus>>readValue(result,
                new TypeReference<ArrayList<ApiInitializationStatus>>() {
                }).get(0);

            if (status.getSuccess() != null) {
              return status.getSuccess().getUsername();
            }
            latestError = status.getError().getDescription();
            TimeUnit.SECONDS.sleep(1L);

          } catch (final Exception e) {
            throw new HueApiException(e);
          }
        }
        throw new HueApiException(latestError);
      };
      return CompletableFuture.supplyAsync(apiKeySupplier);
    }
  }
}
