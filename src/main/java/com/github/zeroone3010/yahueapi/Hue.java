package com.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zeroone3010.yahueapi.domain.ApiInitializationStatus;
import com.github.zeroone3010.yahueapi.domain.Group;
import com.github.zeroone3010.yahueapi.domain.Light;
import com.github.zeroone3010.yahueapi.domain.Root;
import com.github.zeroone3010.yahueapi.domain.Sensor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public final class Hue {
  private static final String ROOM_TYPE_GROUP = "Room";

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final String uri;
  private Root root;
  private Map<String, IRoom> rooms;

  /**
   * The basic constructor for initializing the Hue Bridge connection for this library.
   * Use the {@code hueBridgeConnectionBuilder} method if you do not have an API key yet.
   *
   * @param bridgeIp The IP address of the Hue Bridge.
   * @param apiKey   The API key of your application.
   */
  public Hue(final String bridgeIp, final String apiKey) {
    this.uri = "http://" + bridgeIp + "/api/" + apiKey + "/";
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private void doInitialDataLoadIfRequired() {
    if (root == null) {
      refresh();
    }
  }

  /**
   * Refreshes the room, lamp, etc. data from the Hue Bridge, in case
   * it has been updated since the application was started.
   */
  public void refresh() {
    try {
      root = objectMapper.readValue(new URL(uri), Root.class);
    } catch (final IOException e) {
      throw new HueApiException(e);
    }
    this.rooms = Collections.unmodifiableMap(root.getGroups().entrySet().stream()
        .filter(g -> g.getValue().getType().equals(ROOM_TYPE_GROUP))
        .map(group -> buildRoom(group.getKey(), group.getValue(), root))
        .collect(toMap(IRoom::getName, room -> room)));
  }

  /**
   * Returns all the rooms configured into the Bridge.
   *
   * @return A Collection of rooms.
   */
  public Collection<IRoom> getRooms() {
    doInitialDataLoadIfRequired();
    return Collections.unmodifiableCollection(this.rooms.values());
  }

  /**
   * Returns a specific room by its name.
   *
   * @return A room or {@code Optional.empty()} if a room with the given name does not exist.
   */
  public Optional<IRoom> getRoomByName(final String roomName) {
    doInitialDataLoadIfRequired();
    return Optional.ofNullable(this.rooms.get(roomName));
  }

  private IRoom buildRoom(final String groupId, final Group group, final Root root) {
    final Set<ILight> lights = group.getLights().stream()
        .map(lightId -> buildLight(lightId, root))
        .collect(toSet());
    try {
      return new RoomImpl(objectMapper, new URL(uri + "groups/" + groupId), group, lights);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  private ILight buildLight(final String lightId, final Root root) {
    try {
      return new LightImpl(lightId,
          root.getLights().get(lightId),
          new URL(uri + "lights/" + lightId),
          objectMapper);
    } catch (final MalformedURLException e) {
      throw new HueApiException(e);
    }
  }

  /**
   * Returns the raw root node information of the REST API. Not required for anything but querying the most
   * technical details of the Bridge setup.
   *
   * @return A Root element, as received from the Bridge REST API.
   */
  public Root getRaw() {
    doInitialDataLoadIfRequired();
    return this.root;
  }

  /**
   * For development/debugging purposes only. Will be removed.
   *
   * @return Sensor types.
   */
  @Deprecated
  public Set<String> getAllSensorTypes() {
    doInitialDataLoadIfRequired();
    return this.root.getSensors().values().stream().map(Sensor::getType)
        .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
  }

  /**
   * For development/debugging purposes only. Will be removed.
   *
   * @return Sensors.
   */
  @Deprecated
  public Map<String, Sensor> getSensors() {
    doInitialDataLoadIfRequired();
    return this.root.getSensors();
  }

  /**
   * For development/debugging purposes only. Will be removed.
   *
   * @return Lights.
   */
  @Deprecated
  public Map<String, Light> getLights() {
    doInitialDataLoadIfRequired();
    return this.root.getLights();
  }

  /**
   * The method to be used if you do not have an API key for your application yet.
   * Returns a {@code HueBridgeConnectionBuilder} that initializes the process of
   * adding a new application to the Bridge.
   *
   * @param bridgeIp The IP address of the Bridge.
   * @return A connection builder that initializes the application for the Bridge.
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
