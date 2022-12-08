package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.ApiInitializationStatus;
import io.github.zeroone3010.yahueapi.domain.Group;
import io.github.zeroone3010.yahueapi.domain.Root;
import io.github.zeroone3010.yahueapi.domain.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.zeroone3010.yahueapi.HueBridgeProtocol.UNVERIFIED_HTTPS;
import static io.github.zeroone3010.yahueapi.RoomFactory.ALL_LIGHTS_GROUP_ID;
import static java.util.Collections.emptyMap;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * The main class. This class creates and holds the connection with the Bridge. This class also holds all the methods
 * with which one can get all the lights, sensors, rooms, etc. to interact with them.
 */
public final class Hue {
  private static final Logger logger = LoggerFactory.getLogger(Hue.class);

  private static final int EXPECTED_NEW_LIGHTS_SEARCH_TIME_IN_SECONDS = 50;

  private static final long MIN_API_V2_VERSION = 1948086000L;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final SensorFactory sensorFactory = new SensorFactory(this, objectMapper);
  private final RoomFactory roomFactory;
  private final LightFactory lightFactory;

  private final String uri;
  private Root root;
  private Map<String, Light> lights;
  private Map<String, Room> groups;
  private Map<String, Sensor> sensors;
  private boolean caching = false;
  private Collection<Light> unassignedLights;

  /**
   * The basic constructor for initializing the Hue Bridge connection for this library.
   * Sets up an encrypted but unverified HTTPS connection -- see {@link HueBridgeProtocol#UNVERIFIED_HTTPS}.
   * Use the {@link #hueBridgeConnectionBuilder(String)} method instead if you do not have an API key yet.
   *
   * @param bridgeIp The IP address of the Hue Bridge.
   * @param apiKey   The API key of your application.
   * @since 1.0.0
   */
  public Hue(final String bridgeIp, final String apiKey) {
    final HueBridgeProtocol protocol = UNVERIFIED_HTTPS;
    this.uri = protocol.getProtocol() + "://" + bridgeIp + "/api/" + apiKey + "/";
    TrustEverythingManager.trustAllSslConnectionsByDisablingCertificateVerification();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.addHandler(new UnauthorizedUserHandler());
    roomFactory = new RoomFactory(this, objectMapper, uri);
    lightFactory = new LightFactory(this, objectMapper);
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

  private void doInitialDataLoadIfRequired() {
    if (root == null) {
      refresh();
    }
  }

  /**
   * Refreshes the room, lamp, etc. data from the Hue Bridge, in case
   * it has been updated since the application was started.
   * <p>
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
    this.lights = Collections.unmodifiableMap(Optional.ofNullable(root.getLights()).orElse(emptyMap()).entrySet().stream()
        .map(light -> buildLight(light.getKey(), root))
        .collect(toMap(LightImpl::getId, light -> light)));

    final Collection<Room> tempGroups = Optional.ofNullable(root.getGroups()).orElse(emptyMap()).entrySet().stream()
        .map(group -> buildRoom(group.getKey(), group.getValue(), findScenesOfGroup(group.getKey(), root.getScenes())))
        .collect(toSet());
    final Map<String, Long> groupNameCounts = tempGroups.stream()
        .collect(Collectors.groupingBy(Room::getName, Collectors.counting()));
    this.groups = Collections.unmodifiableMap(
        tempGroups.stream().collect(toMap(room -> groupNameCounts.get(room.getName()) > 1
                ? ((RoomImpl) room).getId() + ": " + room.getName()
                : room.getName(),
            room -> room)));

    this.sensors = Collections.unmodifiableMap(Optional.ofNullable(root.getSensors()).orElse(emptyMap()).entrySet().stream()
        .map(sensor -> buildSensor(sensor.getKey(), root))
        .collect(toMap(Sensor::getId, sensor -> sensor)));

    final Collection<String> lightsInUse = Optional.ofNullable(getRaw().getGroups()).orElse(emptyMap()).values().stream()
        .flatMap(group -> group.getLights().stream())
        .collect(toSet());
    this.unassignedLights = lights.entrySet().stream()
        .filter(light -> !lightsInUse.contains(light.getKey()))
        .map(light -> buildLight(light.getKey(), root))
        .sorted(comparing(Light::getName))
        .collect(toList());
  }

  private Map<String, Scene> findScenesOfGroup(final String groupId, final Map<String, Scene> scenes) {
    return Optional.ofNullable(scenes).orElse(emptyMap()).entrySet().stream()
        .filter(e -> Objects.equals(groupId, e.getValue().getGroup()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * Returns all groups of lights that match any of the specified group types.
   *
   * @param groupTypes Type or types of groups to get.
   * @return A Collection of groups matching the requested types.
   * @since 1.3.0
   */
  public Collection<Room> getGroupsOfType(final GroupType... groupTypes) {
    doInitialDataLoadIfRequired();
    return Collections.unmodifiableCollection(groups.values().stream()
        .filter(room -> Arrays.stream(groupTypes)
            .filter(Objects::nonNull)
            .anyMatch(type -> type.equals(room.getType())))
        .collect(toSet()));
  }

  Light getLightById(final String id) {
    doInitialDataLoadIfRequired();
    return lights.get(id);
  }

  /**
   * Returns all the rooms configured into the Bridge.
   * Acts as a shorthand version of calling {@link #getGroupsOfType(GroupType...)} with value {@code GroupType.ROOM}.
   *
   * @return A Collection of rooms.
   * @since 1.0.0
   * @deprecated Use {@link io.github.zeroone3010.yahueapi.v2.Hue#getRooms()} instead.
   */
  @Deprecated
  public Collection<Room> getRooms() {
    return getGroupsOfType(GroupType.ROOM);
  }

  /**
   * Returns all the zones configured into the Bridge.
   * Acts as a shorthand version of calling {@link #getGroupsOfType(GroupType...)} with value {@code GroupType.ZONE}.
   *
   * @return A Collection of zones as Room objects.
   * @since 1.1.0
   * @deprecated Use {@link io.github.zeroone3010.yahueapi.v2.Hue#getZones()} instead.
   */
  @Deprecated
  public Collection<Room> getZones() {
    return getGroupsOfType(GroupType.ZONE);
  }

  /**
   * Returns a specific room by its name.
   *
   * @param roomName The name of a room
   * @return A room or {@code Optional.empty()} if a room with the given name does not exist.
   * @since 1.0.0
   * @deprecated Use {@link io.github.zeroone3010.yahueapi.v2.Hue#getRoomByName(String)} instead.
   */
  @Deprecated
  public Optional<Room> getRoomByName(final String roomName) {
    doInitialDataLoadIfRequired();
    return Optional.ofNullable(this.groups.get(roomName)).filter(g -> g.getType() == GroupType.ROOM);
  }

  /**
   * Returns a specific zone by its name.
   *
   * @param zoneName The name of a zone
   * @return A zone or {@code Optional.empty()} if a zone with the given name does not exist.
   * @since 1.1.0
   * @deprecated Use {@link io.github.zeroone3010.yahueapi.v2.Hue#getZoneByName(String)} instead.
   */
  @Deprecated
  public Optional<Room> getZoneByName(final String zoneName) {
    doInitialDataLoadIfRequired();
    return Optional.ofNullable(this.groups.get(zoneName)).filter(g -> g.getType() == GroupType.ZONE);
  }

  private Room buildRoom(final String groupId, final Group group, final Map<String, Scene> scenes) {
    return roomFactory.buildRoom(groupId, group, scenes);
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
   * <p>
   * The results of this method are also always cached, so a call to this method never triggers a query to the Bridge
   * (unless no other queries have been made to the Bridge since this instance of {@code Hue} was constructed).
   * To refresh the cache call the {@link #refresh()} method.
   *
   * @return A Root element, as received from the Bridge REST API. Always returns a cached version of the data.
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
   * Returns all the switches configured into the Bridge.
   * Different kinds of switches include, for example, the Philips Hue dimmer switch and the Philips Hue Tap switch.
   *
   * @return A Collection of switches.
   * @since 2.0.0
   */
  public Collection<Switch> getSwitches() {
    return getSensorsByType(SensorType.SWITCH, Switch.class);
  }

  /**
   * Returns all the presence sensors configured into the Bridge.
   *
   * @return A Collection of presence sensors.
   * @since 2.0.0
   */
  public Collection<PresenceSensor> getPresenceSensors() {
    return getSensorsByType(SensorType.PRESENCE, PresenceSensor.class);
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

  /**
   * Returns all the ambient light sensors configured into the Bridge.
   *
   * @return A Collection of ambient light sensors.
   * @since 2.0.0
   */
  public Collection<AmbientLightSensor> getAmbientLightSensors() {
    return getSensorsByType(SensorType.AMBIENT_LIGHT, AmbientLightSensor.class);
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
   * Returns a specific presence sensor by its name.
   *
   * @param sensorName The name of a sensor
   * @return A sensor or {@code Optional.empty()} if a sensor with the given name does not exist.
   * @since 2.0.0
   */
  public Optional<PresenceSensor> getPresenceSensorByName(final String sensorName) {
    doInitialDataLoadIfRequired();
    return getPresenceSensors().stream()
        .filter(sensor -> Objects.equals(sensor.getName(), sensorName))
        .map(PresenceSensor.class::cast)
        .findFirst();
  }

  /**
   * Returns a specific ambient sensor by its name.
   *
   * @param sensorName The name of a sensor
   * @return A sensor or {@code Optional.empty()} if a sensor with the given name does not exist.
   * @since 2.0.0
   */
  public Optional<AmbientLightSensor> getAmbientSensorByName(final String sensorName) {
    doInitialDataLoadIfRequired();
    return getAmbientLightSensors().stream()
        .filter(sensor -> Objects.equals(sensor.getName(), sensorName))
        .map(AmbientLightSensor.class::cast)
        .findFirst();
  }

  /**
   * Returns a specific switch by its name.
   *
   * @param switchName The name of a switch
   * @return A switch or {@code Optional.empty()} if a switch with the given name does not exist.
   * @since 2.0.0
   */
  public Optional<Switch> getSwitchByName(final String switchName) {
    doInitialDataLoadIfRequired();
    return getSwitches().stream()
        .filter(sensor -> Objects.equals(sensor.getName(), switchName))
        .map(Switch.class::cast)
        .findFirst();
  }

  /**
   * Returns all lights that do not belong to any group or zone.
   *
   * @return A collection of lights.
   * @since 1.4.0
   */
  public Collection<Light> getUnassignedLights() {
    doInitialDataLoadIfRequired();
    return unassignedLights;
  }

  /**
   * Returns a specific unassigned light by its name.
   *
   * @param lightName The name of a light
   * @return A light or {@code Optional.empty()} if an unassigned light with the given name does not exist.
   * @since 2.0.0
   */
  public Optional<Light> getUnassignedLightByName(final String lightName) {
    doInitialDataLoadIfRequired();
    return getUnassignedLights().stream()
        .filter(light -> Objects.equals(light.getName(), lightName))
        .findFirst();
  }

  /**
   * <p>Returns all lights known by the Bridge. This is a convenience method provided by the API, making it easy to,
   * for example, turn on or off all lights at once.</p>
   *
   * <p>Note that due to the special nature of this group, caching has no effect on this method. Every time this method
   * is called or the state of this group is queried, a call is made directly into the Bridge.</p>
   *
   * @return A {@link Room} object containing all the lights known by the Bridge.
   * @since 2.4.0
   */
  public Room getAllLights() {
    final Group group0;
    try {
      group0 = objectMapper.readValue(new URL(uri + "groups/" + ALL_LIGHTS_GROUP_ID), Group.class);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
    return buildRoom("0", group0, emptyMap());
  }

  /**
   * Orders the Bridge to search for new lights. The operation takes some 40-60 seconds -- longer, if there are many
   * new lights found. Returns a {@code Future} that is resolved with a collection of new lights found, if any.
   *
   * @return A {@code Future} that is resolved in some 40-60 seconds with the new lights that have been found, if any.
   * @since 2.6.0
   */
  public Future<Collection<Light>> searchForNewLights() {
    try {
      final String searchStartResult = HttpUtil.post(new URL(uri), "lights", null);
      logger.info("Starting to search for new lights: " + searchStartResult);
      final Supplier<Collection<Light>> newLightsSupplier = () -> {
        NewLightsResult newLightsResult = getNewLightsSearchStatus();
        int seconds = EXPECTED_NEW_LIGHTS_SEARCH_TIME_IN_SECONDS;
        while (newLightsResult.getStatus() != NewLightsSearchStatus.COMPLETED) {
          try {
            TimeUnit.SECONDS.sleep(1L);
            logger.info("Searching for new lights. Approximately " + (seconds--) + " seconds left.");
            newLightsResult = getNewLightsSearchStatus();
          } catch (final InterruptedException e) {
            throw new HueApiException("Search for new lights was interrupted unexpectedly");
          }
        }
        return newLightsResult.getNewLights();
      };
      return CompletableFuture.supplyAsync(newLightsSupplier);
    } catch (final Exception e) {
      throw new HueApiException("Failed to search for new lights", e);
    }
  }

  /**
   * Returns the status of new lights search -- see {@link #searchForNewLights()}. Note that
   * you do not need to call this method manually when you are using the {@link #searchForNewLights()}
   * method: internally it checks whether the search is finished by calling this exact method.
   * This method is provided as public for convenience in case you need to return to the results
   * later or need to find out about searches performed by some other means than this library.
   *
   * @return Status of the last search for new lights, and the new lights, if any.
   * @see #searchForNewLights()
   * @since 2.6.0
   */
  public NewLightsResult getNewLightsSearchStatus() {
    doInitialDataLoadIfRequired();
    final JsonNode result;
    try {
      result = objectMapper.readTree(new URL(uri + "lights/new"));
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
    final String rawLastScan = result.get("lastscan").textValue();
    final NewLightsSearchStatus status;
    final ZonedDateTime lastScanTime;
    final Collection<Light> newLights = new ArrayList<>();
    switch (rawLastScan) {

      case "active":
        status = NewLightsSearchStatus.ACTIVE;
        lastScanTime = null;
        break;

      case "none":
        status = NewLightsSearchStatus.NONE;
        lastScanTime = null;
        break;

      default:
        status = NewLightsSearchStatus.COMPLETED;
        lastScanTime = TimeUtil.stringTimestampToZonedDateTime(rawLastScan);
        refresh();
        final Iterator<String> fieldNameIterator = result.fieldNames();
        while (fieldNameIterator.hasNext()) {
          final String lightIdField = fieldNameIterator.next();
          if ("lastscan".equals(lightIdField)) {
            continue;
          }
          final Light light = getLightById(lightIdField);
          if (light != null) {
            newLights.add(light);
          } else {
            logger.warn("New light {} not found, but it was expected.", lightIdField);
          }
        }
        break;
    }
    return new NewLightsResult(newLights, status, lastScanTime);
  }

  /**
   * Tells whether the Bridge supports the CLIP API v2, introduced in November 2021.
   *
   * @return {@code true} if there is a support for API v2, {@code false} if not.
   * @since 3.0.0
   */
  public boolean bridgeSupportsApiV2() {
    doInitialDataLoadIfRequired();
    return Long.parseLong(root.getConfig().getSoftwareVersion()) >= MIN_API_V2_VERSION;
  }

  /**
   * The method to be used if you do not have an API key for your application yet.
   * Returns a {@code HueBridgeConnectionBuilder} that initializes the process of
   * adding a new application to the Bridge. You can test if you are connecting to
   * a Hue Bridge endpoint before initializing the connection.
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
    private String urlString;

    private HueBridgeConnectionBuilder(final String bridgeIp) {
      this.urlString = "https://" + bridgeIp;
    }

    /**
     * Returns a {@code CompletableFuture} that calls the /api/config path of given Hue Bridge to verify
     * that you are connecting to a Hue bridge.
     *
     * @return A {@code CompletableFuture} with a boolean that is true when the call to the bridge was successful.
     * @since 2.7.0
     */
    public CompletableFuture<Boolean> isHueBridgeEndpoint() {
      final Supplier<Boolean> isBridgeSupplier = () -> {
        try {
          TrustEverythingManager.trustAllSslConnectionsByDisablingCertificateVerification();
          HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlString + "/api/config").openConnection();
          int responseCode = urlConnection.getResponseCode();
          return HttpURLConnection.HTTP_OK == responseCode;
        } catch (IOException e) {
          return false;
        }
      };
      return CompletableFuture.supplyAsync(isBridgeSupplier);
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
          TrustEverythingManager.trustAllSslConnectionsByDisablingCertificateVerification();
          baseUrl = new URL(urlString + "/api");
        } catch (final MalformedURLException e) {
          throw new HueApiException(e);
        }

        String latestError = null;
        for (int triesLeft = MAX_TRIES; triesLeft > 0; triesLeft--) {
          try {
            logger.info("Please push the button on the Hue Bridge now (" + triesLeft + " seconds left).");

            final String result = HttpUtil.post(baseUrl, "", body);
            logger.info(result);
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
