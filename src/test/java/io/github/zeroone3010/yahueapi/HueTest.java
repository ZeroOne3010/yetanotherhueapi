package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import io.github.zeroone3010.yahueapi.domain.BridgeConfig;
import io.github.zeroone3010.yahueapi.domain.Root;
import io.github.zeroone3010.yahueapi.domain.RuleAction;
import io.github.zeroone3010.yahueapi.domain.RuleCondition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HueTest {
  private static final String API_KEY = "abcd1234";
  private static final String API_BASE_PATH = "/api/" + API_KEY + "/";
  private static final String MOTION_SENSOR_NAME = "Hallway sensor";
  private static final String TEMPERATURE_SENSOR_NAME = "Hue temperature sensor 1";
  private static final String DIMMER_SWITCH_NAME = "Living room door";

  final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());

  @BeforeEach
  void startServer() {
    wireMockServer.start();
  }

  @AfterEach
  void stopServer() {
    wireMockServer.stop();
  }

  private Hue createHueAndInitializeMockServer() {
    final String hueRoot = readFile("hueRoot.json");

    final ObjectMapper objectMapper = new ObjectMapper();
    final JsonNode jsonNode;
    try {
      jsonNode = objectMapper.readTree(hueRoot);
      wireMockServer.stubFor(get(API_BASE_PATH).willReturn(okJson(hueRoot)));
      mockIndividualGetResponse(jsonNode, "lights", "100");
      mockIndividualGetResponse(jsonNode, "lights", "101");
      mockIndividualGetResponse(jsonNode, "lights", "200");
      mockIndividualGetResponse(jsonNode, "lights", "300");
      mockIndividualGetResponse(jsonNode, "sensors", "1");
      mockIndividualGetResponse(jsonNode, "sensors", "4");
      mockIndividualGetResponse(jsonNode, "sensors", "15");
      mockIndividualGetResponse(jsonNode, "sensors", "16");
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return new Hue("localhost:" + wireMockServer.port(), API_KEY);
  }

  private void mockIndividualGetResponse(final JsonNode hueRoot, final String itemClass, final String id) throws JsonProcessingException {
    final ObjectMapper objectMapper = new ObjectMapper();
    final String json = objectMapper.writeValueAsString(hueRoot.get(itemClass).get(id));
    wireMockServer.stubFor(get(API_BASE_PATH + itemClass + "/" + id).willReturn(okJson(json)));
  }

  @Test
  void testInitializationAndRefresh() {
    final Hue hue = createHueAndInitializeMockServer();
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH)));

    hue.refresh();
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));

    hue.refresh();
    wireMockServer.verify(2, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testGetRooms() {
    final Hue hue = createHueAndInitializeMockServer();

    assertEquals(3, hue.getRooms().size());
    hue.getRooms();
    hue.getRooms();
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testGetRoomByName() {
    final Hue hue = createHueAndInitializeMockServer();

    assertEquals(2, hue.getRoomByName("Living room").get().getLights().size());
    assertEquals(1, hue.getRoomByName("Bedroom").get().getLights().size());
    assertFalse(hue.getRoomByName("No such room").isPresent());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testGetZones() {
    final Hue hue = createHueAndInitializeMockServer();

    assertEquals(1, hue.getZones().size());
    hue.getZones();
    hue.getZones();
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testGetZoneByName() {
    final Hue hue = createHueAndInitializeMockServer();

    assertEquals(2, hue.getZoneByName("Path to toilet").get().getLights().size());
    assertFalse(hue.getZoneByName("No such zone").isPresent());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testGetLightReachability() {
    final Hue hue = createHueAndInitializeMockServer();

    assertTrue(hue.getRoomByName("Living room").get().getLightByName("LR 1").get().isReachable());
    assertFalse(hue.getRoomByName("Bedroom").get().getLightByName("Pendant").get().isReachable());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testLightTypes() {
    final Hue hue = createHueAndInitializeMockServer();
    assertEquals(LightType.EXTENDED_COLOR,
        hue.getRoomByName("Living room").get().getLightByName("LR 1").get().getType());
    assertEquals(LightType.COLOR_TEMPERATURE,
        hue.getRoomByName("Living room").get().getLightByName("LR 2").get().getType());
    assertEquals(LightType.COLOR,
        hue.getRoomByName("Hallway 1").get().getLightByName("LED strip 1").get().getType());
  }

  @Test
  void testGetLightStateWhenXyMode() {
    final Hue hue = createHueAndInitializeMockServer();
    final State state = hue.getRoomByName("Living room").get().getLightByName("LR 1").get().getState();
    assertFalse(state.getOn());
    assertEquals(0.3689f, state.getXy().get(0).floatValue());
    assertEquals(0.3719f, state.getXy().get(1).floatValue());
    assertEquals(254, state.getBri().intValue());
    assertNull(state.getHue());
    assertNull(state.getSat());
    assertNull(state.getCt());

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100")));
  }

  @Test
  void testGetLightStateWhenCtMode() {
    final Hue hue = createHueAndInitializeMockServer();
    final State state = hue.getRoomByName("Living room").get().getLightByName("LR 2").get().getState();
    assertFalse(state.getOn());
    assertNull(state.getXy());
    assertEquals(123, state.getBri().intValue());
    assertEquals(230, state.getCt().intValue());
    assertNull(state.getHue());
    assertNull(state.getSat());

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/101")));
  }

  @Test
  void testGetLightStateWhenHsMode() {
    final Hue hue = createHueAndInitializeMockServer();
    final State state = hue.getRoomByName("Hallway 1").get()
        .getLightByName("LED strip 1").get().getState();

    assertTrue(state.getOn());
    assertNull(state.getXy());
    assertEquals(42, state.getBri().intValue());
    assertEquals(38677, state.getHue().intValue());
    assertEquals(61, state.getSat().intValue());
    assertNull(state.getCt());

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")));
  }

  @Test
  void testGetLightStateQueriesTheBridgeEveryTimeWhenCachingIsOffByDefault() {
    final Hue hue = createHueAndInitializeMockServer();
    final Light light = hue.getRoomByName("Hallway 1").get().getLightByName("LED strip 1").get();
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")));
  }

  @Test
  void testGetLightStateQueriesTheBridgeEveryTimeWhenCachingIsOffExplicitly() {
    final Hue hue = createHueAndInitializeMockServer();
    hue.setCaching(false);
    final Light light = hue.getRoomByName("Hallway 1").get().getLightByName("LED strip 1").get();
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")));
  }

  @Test
  void testGetLightStateIsNotQueriedFromTheBridgeWhenCachingIsOn() {
    final Hue hue = createHueAndInitializeMockServer();
    hue.setCaching(true);
    final Light light = hue.getRoomByName("Hallway 1").get().getLightByName("LED strip 1").get();
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    assertTrue(light.getState().getOn());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")));
  }

  @Test
  void testGetLightStateWhenTogglingCachingOnAndOff() {
    final Hue hue = createHueAndInitializeMockServer();

    hue.setCaching(true);
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    final Light light = hue.getRoomByName("Hallway 1").get().getLightByName("LED strip 1").get();
    light.getState();
    light.getState();
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")));

    hue.setCaching(false);
    wireMockServer.verify(2, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    light.getState();
    light.getState();
    light.getState();
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")));

    hue.setCaching(true);
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    light.getState();
    light.getState();
    light.getState();
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "lights/300")));
  }

  @Test
  void testGetUnknownSensors() {
    final Hue hue = createHueAndInitializeMockServer();
    final Collection<Sensor> sensors = hue.getUnknownSensors();
    assertEquals(2, sensors.size());
  }

  @Test
  void testGetTemperatureSensorByName() {
    final Hue hue = createHueAndInitializeMockServer();
    assertTrue(hue.getTemperatureSensorByName(TEMPERATURE_SENSOR_NAME).isPresent());
    assertFalse(hue.getTemperatureSensorByName("No such sensor").isPresent());
  }

  @Test
  void testGetMotionSensorByName() {
    final Hue hue = createHueAndInitializeMockServer();
    assertTrue(hue.getMotionSensorByName(MOTION_SENSOR_NAME).isPresent());
    assertFalse(hue.getMotionSensorByName("No such sensor").isPresent());
  }

  @Test
  void testMotionSensorLastUpdated() {
    final Hue hue = createHueAndInitializeMockServer();
    final ZonedDateTime actual = hue.getMotionSensorByName(MOTION_SENSOR_NAME).map(Sensor::getLastUpdated).get();
    final ZonedDateTime expected = ZonedDateTime.of(LocalDate.of(2018, Month.JULY, 29),
        LocalTime.of(6, 6, 6), ZoneId.of("UTC"));
    assertEquals(expected, actual);
  }

  @Test
  void testMotionSensorPresence() {
    final Hue hue = createHueAndInitializeMockServer();
    final boolean presence = hue.getMotionSensorByName(MOTION_SENSOR_NAME).map(MotionSensor::isPresence).get();
    assertTrue(presence);
  }

  @Test
  void testTemperatureSensorTemperature() {
    final Hue hue = createHueAndInitializeMockServer();
    final double temperature = hue.getTemperatureSensorByName(TEMPERATURE_SENSOR_NAME)
        .map(TemperatureSensor::getDegreesCelsius)
        .map(BigDecimal::doubleValue).get();
    assertEquals(29.53d, temperature);
  }

  @Test
  void testDaylightSensor() {
    final Hue hue = createHueAndInitializeMockServer();
    final Boolean daylight = hue.getDaylightSensors().stream()
        .map(DaylightSensor::isDaylightTime)
        .findFirst().get();
    assertTrue(daylight);
  }

  @Test
  void testDimmerSwitchLastUpdated() {
    final Hue hue = createHueAndInitializeMockServer();
    final ZonedDateTime actual = hue.getDimmerSwitchByName(DIMMER_SWITCH_NAME).map(Sensor::getLastUpdated).get();
    final ZonedDateTime expected = ZonedDateTime.of(LocalDate.of(2018, Month.JULY, 28),
        LocalTime.of(21, 12, 00), ZoneId.of("UTC"));
    assertEquals(expected, actual);
  }

  @Test
  void testDimmerSwitchLastButtonEvent() {
    final Hue hue = createHueAndInitializeMockServer();
    final DimmerSwitchButtonEvent event = hue.getDimmerSwitchByName(DIMMER_SWITCH_NAME).map(DimmerSwitch::getLatestButtonEvent).get();
    assertEquals(DimmerSwitchAction.SHORT_RELEASED, event.getAction());
    assertEquals(DimmerSwitchButton.OFF, event.getButton());
  }

  @Test
  void testGetSensorStateQueriesTheBridgeEveryTimeWhenCachingIsOffByDefault() {
    final Hue hue = createHueAndInitializeMockServer();
    final TemperatureSensor sensor = hue.getTemperatureSensorByName("Hue temperature sensor 1").get();
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")));
  }

  @Test
  void testGetSensorStateQueriesTheBridgeEveryTimeWhenCachingIsOffExplicitly() {
    final Hue hue = createHueAndInitializeMockServer();
    hue.setCaching(false);
    final TemperatureSensor sensor = hue.getTemperatureSensorByName("Hue temperature sensor 1").get();
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")));
  }

  @Test
  void testGetSensorStateIsNotQueriedFromTheBridgeWhenCachingIsOn() {
    final Hue hue = createHueAndInitializeMockServer();
    hue.setCaching(true);
    final TemperatureSensor sensor = hue.getTemperatureSensorByName("Hue temperature sensor 1").get();
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.53"), sensor.getDegreesCelsius());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")));
  }

  @Test
  void testGetSensorStateWhenTogglingCachingOnAndOff() {
    final Hue hue = createHueAndInitializeMockServer();

    hue.setCaching(true);
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    final TemperatureSensor sensor = hue.getTemperatureSensorByName("Hue temperature sensor 1").get();
    sensor.getDegreesCelsius();
    sensor.getDegreesCelsius();
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")));

    hue.setCaching(false);
    wireMockServer.verify(2, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    sensor.getDegreesCelsius();
    sensor.getDegreesCelsius();
    sensor.getDegreesCelsius();
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")));

    hue.setCaching(true);
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    sensor.getDegreesCelsius();
    sensor.getDegreesCelsius();
    sensor.getDegreesCelsius();
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "sensors/15")));
  }

  @Test
  void testSetLightBrightness() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/bri\":123}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    final Light light = hue.getRoomByName("Living room").get().getLightByName("LR 1").get();
    light.setBrightness(42);

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(new EqualToPattern("{\"bri\":42}", false)));
  }

  @Test
  void testSetRoomBrightness() {
    wireMockServer.stubFor(put(API_BASE_PATH + "groups/1/action")
        .willReturn(okJson("[{\"success\":{\"/gruops/1/action/bri\":42}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    hue.getRoomByName("Living room").get().setBrightness(42);

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1/action"))
        .withRequestBody(new EqualToPattern("{\"bri\":42}", false)));
  }

  @Test
  void testGetRawRules() {
    final Hue hue = createHueAndInitializeMockServer();
    final Root root = hue.getRaw();
    assertEquals(2, root.getRules().size());
    assertEquals("Dimmer Switch 4 on0", root.getRules().get("1").getName());
    assertEquals("6655664454522131aaaeeaaeaeaeaea", root.getRules().get("1").getOwner());
    assertEquals("2017-07-14T14:04:04", root.getRules().get("1").getCreated());
    assertEquals("2018-07-27T13:00:13", root.getRules().get("1").getLastTriggered());
    assertEquals(5, root.getRules().get("1").getTimesTriggered());
    assertEquals("enabled", root.getRules().get("1").getStatus());
    assertEquals(true, root.getRules().get("1").isRecycle());
    final List<RuleCondition> conditions = root.getRules().get("1").getConditions();
    assertEquals(3, conditions.size());
    assertEquals("/sensors/4/state/buttonevent", conditions.get(0).getAddress());
    assertEquals("eq", conditions.get(0).getOperator());
    assertEquals("1000", conditions.get(0).getValue());
    final List<RuleAction> actions = root.getRules().get("1").getActions();
    assertEquals(1, actions.size());
    assertEquals("/groups/1/action", actions.get(0).getAddress());
    assertEquals("PUT", actions.get(0).getMethod());
    assertEquals(Collections.singletonMap("on", true), actions.get(0).getBody());
  }

  @Test
  void testGetRawConfig() {
    final Hue hue = createHueAndInitializeMockServer();
    final BridgeConfig config = hue.getRaw().getConfig();
    assertEquals(11, config.getZigbeeChannel());
    assertEquals("00:17:11:22:33:aa", config.getMac());
    assertEquals(true, config.isDhcp());
    assertEquals("1.26.0", config.getApiVersion());
    assertEquals("noupdates", config.getSoftwareUpdate2().getBridge().getState());
    assertEquals(true, config.getPortalState().isSignedOn());
    assertEquals(false, config.getPortalState().isIncoming());
    assertEquals(true, config.getPortalState().isOutgoing());
    assertEquals("disconnected", config.getPortalState().getCommunication());
    assertEquals("2015-01-09T19:19:19", config.getWhiteList().get("6655664454522131aaaeeaaeaeaeaea").getCreateDate());
  }

  private String readFile(final String fileName) {
    final ClassLoader classLoader = getClass().getClassLoader();
    final File file = new File(classLoader.getResource(fileName).getFile());
    try {
      return Files.lines(file.toPath()).collect(Collectors.joining());
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
