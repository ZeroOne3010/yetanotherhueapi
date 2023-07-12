package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType;
import io.github.zeroone3010.yahueapi.domain.BridgeConfig;
import io.github.zeroone3010.yahueapi.domain.LightConfig;
import io.github.zeroone3010.yahueapi.domain.ResourceLink;
import io.github.zeroone3010.yahueapi.domain.Root;
import io.github.zeroone3010.yahueapi.domain.RuleAction;
import io.github.zeroone3010.yahueapi.domain.RuleCondition;
import io.github.zeroone3010.yahueapi.domain.Scene;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType.HOLD;
import static io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType.INITIAL_PRESS;
import static io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType.LONG_RELEASED;
import static io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType.SHORT_RELEASED;
import static io.github.zeroone3010.yahueapi.domain.StartupMode.BRIGHT_LIGHT;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class HueTest {
  private static final String API_KEY = "abcd1234";
  private static final String API_BASE_PATH = "/api/" + API_KEY + "/";
  private static final String GROUP_0_PATH = API_BASE_PATH + "groups/0";
  private static final String MOTION_SENSOR_NAME = "Hallway sensor";
  private static final String AMBIENT_LIGHT_SENSOR_NAME = "Hue ambient light sensor 1";
  private static final String TEMPERATURE_SENSOR_NAME = "Hue temperature sensor 1";
  private static final String DIMMER_SWITCH_NAME = "Living room door";
  private static final String TAP_SWITCH_NAME = "Hue tap switch 1";

  final WireMockServer wireMockServer = new WireMockServer(wireMockConfig()
      .notifier(new ConsoleNotifier(true))
      .dynamicHttpsPort());

  @BeforeEach
  void startServer() {
    wireMockServer.start();
  }

  @AfterEach
  void stopServer() {
    wireMockServer.stop();
  }

  private Hue createHueAndInitializeMockServer() {
    return createHueAndInitializeMockServer(readFile("hueRoot.json"));
  }

  private Hue createHueAndInitializeMockServer(final String hueRootJsonString) {
    final String hueRoot = readFile("hueRoot.json");
    final String hueRootWithLight900 = readFile("hueRootWithLight900.json");

    final ObjectMapper objectMapper = new ObjectMapper();
    final JsonNode jsonNode;
    try {
      jsonNode = objectMapper.readTree(hueRoot);
      wireMockServer.stubFor(get(API_BASE_PATH).willReturn(okJson(hueRoot)));
      mockIndividualGetResponse(jsonNode, "lights", "100");
      mockIndividualGetResponse(jsonNode, "lights", "101");
      mockIndividualGetResponse(jsonNode, "lights", "200");
      mockIndividualGetResponse(jsonNode, "lights", "300");
      mockIndividualGetResponse(jsonNode, "lights", "400");
      mockIndividualGetResponse(jsonNode, "lights", "500");
      mockIndividualGetResponse(jsonNode, "sensors", "1");
      mockIndividualGetResponse(jsonNode, "sensors", "4");
      mockIndividualGetResponse(jsonNode, "sensors", "15");
      mockIndividualGetResponse(jsonNode, "sensors", "16");
      mockIndividualGetResponse(jsonNode, "sensors", "17");
      mockIndividualGetResponse(jsonNode, "sensors", "20");
      mockIndividualGetResponse(jsonNode, "sensors", "99");
      mockIndividualGetResponse(jsonNode, "groups", "1");
      mockIndividualGetResponse(jsonNode, "groups", "2");
      wireMockServer.stubFor(post(API_BASE_PATH + "lights").willReturn(okJson(
          "[ { \"success\": { \"/lights\": \"Searching for new devices\" }}]"
      )));
      wireMockServer.stubFor(get(API_BASE_PATH + "lights/new").inScenario("scan").whenScenarioStateIs(Scenario.STARTED)
          .willReturn(okJson("{\"lastscan\": \"active\"}")).willSetStateTo("scan1"));
      wireMockServer.stubFor(get(API_BASE_PATH + "lights/new").inScenario("scan").whenScenarioStateIs("scan1")
          .willReturn(okJson("{\"lastscan\": \"active\"}")).willSetStateTo("scan2"));
      wireMockServer.stubFor(get(API_BASE_PATH + "lights/new").inScenario("scan").whenScenarioStateIs("scan2")
          .willReturn(okJson("{\"lastscan\": \"active\"}")).willSetStateTo("scan3"));
      wireMockServer.stubFor(get(API_BASE_PATH + "lights/new").inScenario("scan").whenScenarioStateIs("scan3")
          .willReturn(okJson("{\n" +
              "    \"900\": {\"name\": \"Hue bulb\"},\n" +
              "    \"lastscan\": \"2022-02-04T12:00:00\"\n" +
              "}")));
      wireMockServer.stubFor(get(API_BASE_PATH).inScenario("scan").whenScenarioStateIs("scan3").willReturn(okJson(hueRootWithLight900)));
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return new Hue("localhost:" + wireMockServer.httpsPort(), API_KEY);
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
  void testGetZoneId() {
    final Hue hue = createHueAndInitializeMockServer();
    assertEquals("4", hue.getZoneByName("Path to toilet").get().getId());
  }

  @Test
  void testGetLightId() {
    final Hue hue = createHueAndInitializeMockServer();
    assertEquals("300",
        hue.getRoomByName("Hallway 1").get().getLightByName("LED strip 1").get().getId());
  }

  @Test
  void testGetLightReachability() {
    final Hue hue = createHueAndInitializeMockServer();

    assertTrue(hue.getRoomByName("Living room").get().getLightByName("LR 1").get().isReachable());
    assertFalse(hue.getRoomByName("Bedroom").get().getLightByName("Pendant").get().isReachable());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testBridgeDoesNotSupportApiV2() {
    final Hue hue = createHueAndInitializeMockServer();
    assertFalse(hue.bridgeSupportsApiV2());
  }

  @Test
  void testBridgeSupportsApiV2() {
    final Hue hue = createHueAndInitializeMockServer(
        readFile("hueRoot.json").replace("1806051111", "1948086000L"));
    assertFalse(hue.bridgeSupportsApiV2());
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
    assertEquals(1, sensors.size());
  }

  @Test
  void testGetTemperatureSensorByName() {
    final Hue hue = createHueAndInitializeMockServer();
    assertTrue(hue.getTemperatureSensorByName(TEMPERATURE_SENSOR_NAME).isPresent());
    assertFalse(hue.getTemperatureSensorByName("No such sensor").isPresent());
  }

  @Test
  void testGetPresenceSensorByName() {
    final Hue hue = createHueAndInitializeMockServer();
    assertTrue(hue.getPresenceSensorByName(MOTION_SENSOR_NAME).isPresent());
    assertFalse(hue.getPresenceSensorByName("No such sensor").isPresent());
  }

  @Test
  void testGetAmbientLightSensorByName() {
    final Hue hue = createHueAndInitializeMockServer();
    assertTrue(hue.getAmbientSensorByName(AMBIENT_LIGHT_SENSOR_NAME).isPresent());
    assertFalse(hue.getAmbientSensorByName("No such sensor").isPresent());
  }

  @Test
  void testMotionSensorLastUpdated() {
    final Hue hue = createHueAndInitializeMockServer();
    final ZonedDateTime actual = hue.getPresenceSensorByName(MOTION_SENSOR_NAME).map(Sensor::getLastUpdated).get();
    final ZonedDateTime expected = ZonedDateTime.of(LocalDate.of(2018, Month.JULY, 29),
        LocalTime.of(6, 6, 6), ZoneId.of("UTC"));
    assertEquals(expected, actual);
  }

  @Test
  void testMotionSensorPresence() {
    final Hue hue = createHueAndInitializeMockServer();
    final boolean presence = hue.getPresenceSensorByName(MOTION_SENSOR_NAME).map(PresenceSensor::isPresence).get();
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
  void testTemperatureSensorTemperatureWithDisabledSensor() throws JsonProcessingException {
    final Hue hue = createHueAndInitializeMockServer();
    final String hueRoot = readFile("hueRoot.json").replace("2953", "null");
    wireMockServer.stubFor(get(API_BASE_PATH).willReturn(okJson(hueRoot)));
    final ObjectMapper objectMapper = new ObjectMapper();
    final JsonNode jsonNode = objectMapper.readTree(hueRoot);
    mockIndividualGetResponse(jsonNode, "sensors", "15");
    final TemperatureSensor sensor = hue.getTemperatureSensorByName(TEMPERATURE_SENSOR_NAME).get();
    assertNull(sensor.getDegreesCelsius());
  }

  @Test
  void testDaylightSensor() {
    final Hue hue = createHueAndInitializeMockServer();
    final Boolean daylight = hue.getDaylightSensors().stream()
        .filter(sensor -> "1".equals(sensor.getId()))
        .map(DaylightSensor::isDaylightTime)
        .findFirst().get();
    assertTrue(daylight);
  }

  @Test
  void testUnconfiguredDaylightSensor() {
    final Hue hue = createHueAndInitializeMockServer();
    final Boolean daylight = hue.getDaylightSensors().stream()
        .filter(sensor -> "99".equals(sensor.getId()))
        .map(DaylightSensor::isDaylightTime)
        .findFirst().get();
    assertFalse(daylight);
  }

  @Test
  void testAmbientLightSensor() {
    final Hue hue = createHueAndInitializeMockServer();
    final AmbientLightSensor ambientLightSensor = hue.getAmbientSensorByName(AMBIENT_LIGHT_SENSOR_NAME).get();
    assertEquals(36, ambientLightSensor.getLightLevel());
    assertTrue(ambientLightSensor.isDark());
    assertFalse(ambientLightSensor.isDaylight());
  }

  @Test
  void testDimmerSwitchLastUpdated() {
    final Hue hue = createHueAndInitializeMockServer();
    final ZonedDateTime actual = hue.getSwitchByName(DIMMER_SWITCH_NAME).map(Sensor::getLastUpdated).get();
    final ZonedDateTime expected = ZonedDateTime.of(LocalDate.of(2018, Month.JULY, 28),
        LocalTime.of(21, 12, 00), ZoneId.of("UTC"));
    assertEquals(expected, actual);
  }

  @Test
  void testDimmerSwitchLastButtonEvent() {
    final Hue hue = createHueAndInitializeMockServer();
    final SwitchEvent event = hue.getSwitchByName(DIMMER_SWITCH_NAME).map(Switch::getLatestEvent).get();
    assertEquals(ButtonEventType.SHORT_RELEASED, event.getAction().getEventType());
    assertEquals(4002, event.getAction().getEventCode());
    assertEquals(4, event.getButton().getNumber());
  }

  @Test
  void testHueDimmerSwitchButtons() {
    final Hue hue = createHueAndInitializeMockServer();
    final Switch dimmerSwitch = hue.getSwitchByName(DIMMER_SWITCH_NAME).get();
    final List<Button> buttons = dimmerSwitch.getButtons();

    assertEquals(4, buttons.size());

    assertEquals(1, buttons.get(0).getNumber());
    assertEquals(2, buttons.get(1).getNumber());
    assertEquals(3, buttons.get(2).getNumber());
    assertEquals(4, buttons.get(3).getNumber());

    assertEquals(Arrays.asList(
            new ButtonEvent(INITIAL_PRESS, 1000),
            new ButtonEvent(HOLD, 1001),
            new ButtonEvent(SHORT_RELEASED, 1002),
            new ButtonEvent(LONG_RELEASED, 1003)),
        buttons.get(0).getPossibleEvents());
    assertEquals(Arrays.asList(
            new ButtonEvent(INITIAL_PRESS, 2000),
            new ButtonEvent(HOLD, 2001),
            new ButtonEvent(SHORT_RELEASED, 2002),
            new ButtonEvent(LONG_RELEASED, 2003)),
        buttons.get(1).getPossibleEvents());
    assertEquals(Arrays.asList(
            new ButtonEvent(INITIAL_PRESS, 3000),
            new ButtonEvent(HOLD, 3001),
            new ButtonEvent(SHORT_RELEASED, 3002),
            new ButtonEvent(LONG_RELEASED, 3003)),
        buttons.get(2).getPossibleEvents());
    assertEquals(Arrays.asList(
            new ButtonEvent(INITIAL_PRESS, 4000),
            new ButtonEvent(HOLD, 4001),
            new ButtonEvent(SHORT_RELEASED, 4002),
            new ButtonEvent(LONG_RELEASED, 4003)),
        buttons.get(3).getPossibleEvents());

  }

  @Test
  void testHueTapSwitchLastUpdated() {
    final Hue hue = createHueAndInitializeMockServer();
    final ZonedDateTime actual = hue.getSwitchByName(TAP_SWITCH_NAME).map(Sensor::getLastUpdated).get();
    final ZonedDateTime expected = ZonedDateTime.of(LocalDate.of(2020, Month.DECEMBER, 29),
        LocalTime.of(10, 11, 12), ZoneId.of("UTC"));
    assertEquals(expected, actual);
  }

  @Test
  void testHueTapSwitchLastButtonEvent() {
    final Hue hue = createHueAndInitializeMockServer();
    final SwitchEvent event = hue.getSwitchByName(TAP_SWITCH_NAME).map(Switch::getLatestEvent).get();
    assertEquals(new ButtonEvent(INITIAL_PRESS, 16), event.getAction());
    assertEquals(2, event.getButton().getNumber());
  }

  @Test
  void testHueTapSwitchButtons() {
    final Hue hue = createHueAndInitializeMockServer();
    final Switch tapSwitch = hue.getSwitchByName(TAP_SWITCH_NAME).get();
    final List<Button> buttons = tapSwitch.getButtons();

    assertEquals(4, buttons.size());

    assertEquals(1, buttons.get(0).getNumber());
    assertEquals(2, buttons.get(1).getNumber());
    assertEquals(3, buttons.get(2).getNumber());
    assertEquals(4, buttons.get(3).getNumber());

    assertEquals(Arrays.asList(new ButtonEvent(INITIAL_PRESS, 34)), buttons.get(0).getPossibleEvents());
    assertEquals(Arrays.asList(new ButtonEvent(INITIAL_PRESS, 16)), buttons.get(1).getPossibleEvents());
    assertEquals(Arrays.asList(new ButtonEvent(INITIAL_PRESS, 17)), buttons.get(2).getPossibleEvents());
    assertEquals(Arrays.asList(new ButtonEvent(INITIAL_PRESS, 18)), buttons.get(3).getPossibleEvents());
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
        .withRequestBody(equalToJson("{\"bri\":42}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/bri\":42}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    final Light light = hue.getRoomByName("Living room").get().getLightByName("LR 1").get();
    light.setBrightness(42);

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(new EqualToJsonPattern("{\"bri\":42}", false, false)));
  }

  @Test
  void testTurnLightOff() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .withRequestBody(equalToJson("{\"on\":false}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/on\":false}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    final Light light = hue.getRoomByName("Living room").get().getLightByName("LR 1").get();
    light.turnOff();

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(new EqualToJsonPattern("{\"on\":false}", false, false)));
  }

  @Test
  void testTurnLightOn() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .withRequestBody(equalToJson("{\"on\":true}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/on\":true}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    final Light light = hue.getRoomByName("Living room").get().getLightByName("LR 1").get();
    light.turnOn();

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(new EqualToJsonPattern("{\"on\":true}", false, false)));
  }

  @Test
  void testSetLightState() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .withRequestBody(equalToJson("{\"hue\":1, \"sat\":2, \"bri\":3}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/on\":true}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    final Light light = hue.getRoomByName("Living room").get().getLightByName("LR 1").get();
    light.setState(State.builder().hue(1).saturation(2).brightness(3).keepCurrentState());

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(new EqualToJsonPattern("{\"hue\":1, \"sat\":2, \"bri\":3}", false, false)));
  }

  @Test
  void testSetRoomBrightness() {
    wireMockServer.stubFor(put(API_BASE_PATH + "groups/1/action")
        .willReturn(okJson("[{\"success\":{\"/groups/1/action/bri\":42}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    hue.getRoomByName("Living room").get().setBrightness(42);

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1/action"))
        .withRequestBody(new EqualToPattern("{\"bri\":42}", false)));
  }

  @Test
  void testTurnRoomLightsOff() {
    wireMockServer.stubFor(put(API_BASE_PATH + "groups/1/action")
        .withRequestBody(equalToJson("{\"on\":false}"))
        .willReturn(okJson("[{\"success\":{\"/groups/1/action/on\":false}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    final Room room = hue.getRoomByName("Living room").get();
    room.turnOff();

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1/action"))
        .withRequestBody(new EqualToJsonPattern("{\"on\":false}", false, false)));
  }

  @Test
  void testTurnRoomLightsOn() {
    wireMockServer.stubFor(put(API_BASE_PATH + "groups/1/action")
        .withRequestBody(equalToJson("{\"on\":true}"))
        .willReturn(okJson("[{\"success\":{\"/groups/1/action/on\":true}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    final Room room = hue.getRoomByName("Living room").get();
    room.turnOn();

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1/action"))
        .withRequestBody(new EqualToJsonPattern("{\"on\":true}", false, false)));
  }

  @Test
  void testGetRoomStateQueriesTheBridgeEveryTimeWhenCachingIsOffByDefault() {
    final Hue hue = createHueAndInitializeMockServer();
    final Room room = hue.getRoomByName("Living room").get();
    assertFalse(room.isAnyOn());
    assertFalse(room.isAllOn());
    assertFalse(room.isAnyOn());
    assertFalse(room.isAllOn());
    assertFalse(room.isAnyOn());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1")));
  }

  @Test
  void testGetRoomStateQueriesTheBridgeEveryTimeWhenCachingIsOffExplicitly() {
    final Hue hue = createHueAndInitializeMockServer();
    hue.setCaching(false);
    final Room room = hue.getRoomByName("Living room").get();
    assertFalse(room.isAnyOn());
    assertFalse(room.isAllOn());
    assertFalse(room.isAnyOn());
    assertFalse(room.isAllOn());
    assertFalse(room.isAnyOn());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(5, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1")));
  }

  @Test
  void testGetRoomStateIsNotQueriedFromTheBridgeWhenCachingIsOn() {
    final Hue hue = createHueAndInitializeMockServer();
    hue.setCaching(true);
    final Room room = hue.getRoomByName("Living room").get();
    assertFalse(room.isAnyOn());
    assertFalse(room.isAllOn());
    assertFalse(room.isAnyOn());
    assertFalse(room.isAllOn());
    assertFalse(room.isAnyOn());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1")));
  }

  @Test
  void testGetRoomStateWhenTogglingCachingOnAndOff() {
    final Hue hue = createHueAndInitializeMockServer();

    hue.setCaching(true);
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    final Room room = hue.getRoomByName("Bedroom").get();
    assertTrue(room.isAnyOn());
    assertTrue(room.isAllOn());
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/2")));

    hue.setCaching(false);
    wireMockServer.verify(2, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    assertTrue(room.isAnyOn());
    assertTrue(room.isAllOn());
    assertTrue(room.isAnyOn());
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/2")));

    hue.setCaching(true);
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    assertTrue(room.isAllOn());
    assertTrue(room.isAnyOn());
    assertTrue(room.isAllOn());
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(API_BASE_PATH + "groups/2")));
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
    assertEquals("1.33.0", config.getApiVersion());
    assertEquals("noupdates", config.getSoftwareUpdate2().getBridge().getState());
    assertEquals(true, config.getPortalState().isSignedOn());
    assertEquals(false, config.getPortalState().isIncoming());
    assertEquals(true, config.getPortalState().isOutgoing());
    assertEquals("disconnected", config.getPortalState().getCommunication());
    assertEquals("2015-01-09T19:19:19", config.getWhiteList().get("6655664454522131aaaeeaaeaeaeaea").getCreateDate());
  }

  @Test
  void testGetRawLightConfig() {
    final Hue hue = createHueAndInitializeMockServer();
    final LightConfig config = hue.getRaw().getLights().get("100").getConfig();
    assertEquals("spotbulb", config.getArchetype());
    assertEquals("mixed", config.getFunction());
    assertEquals("downwards", config.getDirection());
    assertEquals(BRIGHT_LIGHT, config.getStartup().getMode());
    assertEquals(true, config.getStartup().isConfigured());
  }

  @Test
  void testGetRawResourcelinks() {
    final Hue hue = createHueAndInitializeMockServer();
    final Map<String, ResourceLink> resourcelinks = hue.getRaw().getResourcelinks();
    ResourceLink resourceLink = resourcelinks.get("27890");
    assertEquals("MotionSensor 16", resourceLink.getName());
    assertEquals("MotionSensor 16 behavior", resourceLink.getDescription());
    assertEquals("Link", resourceLink.getType());
    assertEquals(10020, resourceLink.getClassid());
    assertEquals("6655664454522131aaaeeaaeaeaeaea", resourceLink.getOwner());
    assertEquals(false, resourceLink.isRecycle());
    assertEquals(Arrays.asList("/sensors/16",
        "/sensors/17",
        "/sensors/18",
        "/groups/3",
        "/scenes/sjJk3kjKBJkf3kh"), resourceLink.getLinks());
  }

  @Test
  void testGetRawScenes() {
    final Hue hue = createHueAndInitializeMockServer();
    final Map<String, Scene> scenes = hue.getRaw().getScenes();

    assertEquals(3, scenes.size());
    final Scene scene = scenes.get("sjJk3kjKBJkf3kh");
    assertEquals("Tropical twilight", scene.getName());
    assertEquals(Arrays.asList("100", "101"), scene.getLights());
    assertEquals(true, scene.isRecycle());
    assertEquals(true, scene.isLocked());
    assertEquals(new HashMap() {{
      put("version", 1);
      put("data", "MKoLK_r01_d21");
    }}, scene.getAppdata());
    assertEquals("", scene.getPicture());
    assertEquals(UUID.fromString("9c9435de-1c2f-425c-9351-7e487ab57d79"), scene.getImage());
    assertEquals("2018-01-19T01:23:45", scene.getLastUpdated());
    assertEquals(2, scene.getVersion());
  }

  @Test
  void testGetRoomsAndZones() {
    final Hue hue = createHueAndInitializeMockServer();

    assertEquals(4, hue.getGroupsOfType(GroupType.ROOM, GroupType.ZONE).size());
    hue.getRooms();
    hue.getZones();
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testGetGroupsByDuplicateNames() {
    final Hue hue = createHueAndInitializeMockServer();

    final Collection<Room> lightGroups = hue.getGroupsOfType(GroupType.LIGHT_GROUP);
    assertEquals(2, lightGroups.size());
    assertTrue(lightGroups.stream().allMatch(group -> group.getName().equals("Custom group for $lights")));
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testScenesOfRoom() {
    wireMockServer.stubFor(put(API_BASE_PATH + "groups/1/action")
        .willReturn(okJson("[{\"success\":{\"/groups/1/action/scene\":\"asdasd\"}}]")));
    wireMockServer.stubFor(put(API_BASE_PATH + "groups/2/action")
        .willReturn(okJson("[{\"success\":{\"/groups/2/action/scene\":\"qweqwe\"}}]")));

    final Hue hue = createHueAndInitializeMockServer();

    final Room livingRoom = hue.getRoomByName("Living room").get();
    assertEquals(2, livingRoom.getScenes().size());

    final io.github.zeroone3010.yahueapi.Scene livingRoomTropicalTwilight = livingRoom.getSceneByName("Tropical twilight").get();
    assertEquals("sjJk3kjKBJkf3kh", livingRoomTropicalTwilight.getId());
    livingRoomTropicalTwilight.activate();
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1/action"))
        .withRequestBody(new EqualToJsonPattern("{\"scene\":\"sjJk3kjKBJkf3kh\"}", false, false)));
    livingRoom.getSceneByName("Concentrate").get().activate();
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "groups/1/action"))
        .withRequestBody(new EqualToJsonPattern("{\"scene\":\"qwertyUIOP12345\"}", false, false)));

    final Room bedroom = hue.getRoomByName("Bedroom").get();
    assertEquals(1, bedroom.getScenes().size());
    assertFalse(bedroom.getSceneByName("Concentrate").isPresent());
    bedroom.getSceneByName("Tropical twilight").get().activate();
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "groups/2/action"))
        .withRequestBody(new EqualToJsonPattern("{\"scene\":\"omgbbqASDFmmnnn\"}", false, false)));

    final Room hallway = hue.getRoomByName("Hallway 1").get();
    assertEquals(0, hallway.getScenes().size());

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testUnassignedLights() {
    final Hue hue = createHueAndInitializeMockServer();

    final Collection<Light> lights = hue.getUnassignedLights();
    assertEquals(2, lights.size());
    final Iterator<Light> iterator = lights.iterator();
    Light light = iterator.next();
    assertEquals("400", light.getId());
    assertEquals("Hue Smart plug 1", light.getName());
    assertEquals(LightType.ON_OFF_PLUGIN_UNIT, light.getType());
    assertTrue(light.isReachable());
    assertTrue(light.getState().getOn());
//      assertNull(light.getState().getBri());
//      assertNull(light.getState().getCt());
    assertNull(light.getState().getHue());
    assertNull(light.getState().getSat());
    assertNull(light.getState().getScene());
    assertNull(light.getState().getTransitiontime());
    assertNull(light.getState().getXy());

    light = iterator.next();
    assertEquals("500", light.getId());
    assertEquals("Hue Smart plug 2", light.getName());

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testAddingUnassignedLightsToRoom() {
    final String hueRootWithLight400InGroup1 = readFile("hueRootWithLight400InGroup1.json");
    final String hueRootWithLight400And500InGroup1 = readFile("hueRootWithLights400And500InGroup1.json");
    final Hue hue = createHueAndInitializeMockServer();

    wireMockServer.stubFor(put(API_BASE_PATH + "groups/1").inScenario("adding")
        .whenScenarioStateIs(Scenario.STARTED).willReturn(okJson(
            "[{\"success\":{\"/groups/1/lights\":[\"100\",\"101\",\"400\"]}}]"
        )).willSetStateTo("added400"));

    wireMockServer.stubFor(get(API_BASE_PATH).inScenario("adding").whenScenarioStateIs("added400")
        .willReturn(okJson(hueRootWithLight400InGroup1)));
    wireMockServer.stubFor(put(API_BASE_PATH + "groups/1").inScenario("adding")
        .whenScenarioStateIs("added400").willReturn(okJson(
            "[{\"success\":{\"/groups/1/lights\":[\"100\",\"101\",\"400\", \"500\"]}}]"
        )).willSetStateTo("added500"));

    wireMockServer.stubFor(get(API_BASE_PATH).inScenario("adding").whenScenarioStateIs("added500")
        .willReturn(okJson(hueRootWithLight400And500InGroup1)));

    final Room livingRoom = hue.getRoomByName("Living room").get();
    final Light smartPlug1 = hue.getUnassignedLightByName("Hue Smart plug 1").get();

    Collection<Light> livingRoomLights = livingRoom.addLight(smartPlug1);
    HashSet<Object> expected = new HashSet<>();
    expected.addAll(Arrays.asList("100", "101", "400"));
    // Check with the old Room object first:
    assertEquals(expected, livingRoomLights.stream().map(Light::getId).collect(toSet()));

    // Then create a new object and check with that one too:
    assertEquals(expected, hue.getRoomByName("Living room").get().getLights()
        .stream().map(Light::getId).collect(toSet()));

    final Light smartPlug2 = hue.getUnassignedLightByName("Hue Smart plug 2").get();
    livingRoomLights = livingRoom.addLight(smartPlug2);
    expected = new HashSet<>();
    expected.addAll(Arrays.asList("100", "101", "400", "500"));
    assertEquals(expected, livingRoomLights.stream().map(Light::getId).collect(toSet()));
    assertEquals(expected, hue.getRoomByName("Living room").get().getLights()
        .stream().map(Light::getId).collect(toSet()));
  }

  @Test
  void testRemovingLightsFromRoom() {
    final String hueRootWithLight400InGroup1 = readFile("hueRootWithLight400InGroup1.json");
    final String hueRoot = readFile("hueRoot.json");
    final Hue hue = createHueAndInitializeMockServer();

    wireMockServer.stubFor(get(API_BASE_PATH).inScenario("removing").whenScenarioStateIs(Scenario.STARTED)
        .willReturn(okJson(hueRootWithLight400InGroup1)));
    wireMockServer.stubFor(put(API_BASE_PATH + "groups/1").inScenario("removing")
        .whenScenarioStateIs(Scenario.STARTED).willReturn(okJson(
            "[{\"success\":{\"/groups/1/lights\":[\"100\",\"101\"]}}]"
        )).willSetStateTo("removed400"));

    wireMockServer.stubFor(get(API_BASE_PATH).inScenario("removing").whenScenarioStateIs("removed400")
        .willReturn(okJson(hueRoot)));

    final Room livingRoom = hue.getRoomByName("Living room").get();

    // Assert precondition first:
    HashSet<Object> expected = new HashSet<>(Arrays.asList("100", "101", "400"));
    assertEquals(expected, livingRoom.getLights().stream().map(Light::getId).collect(toSet()));

    final Light smartPlug1 = livingRoom.getLightByName("Hue Smart plug 1").get();
    // Do the action:
    Collection<Light> livingRoomLights = livingRoom.removeLight(smartPlug1);

    expected = new HashSet<>(Arrays.asList("100", "101"));
    // Check with the old Room object first:
    assertEquals(expected, livingRoomLights.stream().map(Light::getId).collect(toSet()));

    // Then create a new object and check with that one too:
    assertEquals(expected, hue.getRoomByName("Living room").get().getLights()
        .stream().map(Light::getId).collect(toSet()));
  }

  @Test
  void testGetAllLights() {
    final Hue hue = createHueAndInitializeMockServer();
    final int expectedTotalNumberOfLights = 6;
    wireMockServer.stubFor(get(GROUP_0_PATH).willReturn(okJson(readFile("group0.json"))));

    hue.setCaching(false);

    Room allLights = hue.getAllLights();
    assertEquals("Group 0", allLights.getName());
    assertEquals(expectedTotalNumberOfLights, allLights.getLights().size());
    assertFalse(allLights.isAllOn());
    assertTrue(allLights.isAnyOn());
    assertTrue(allLights.getScenes().isEmpty());

    // 3 calls: the initial one when created; the "allOn" query; and the "anyOn" query:
    wireMockServer.verify(3, getRequestedFor(urlEqualTo(GROUP_0_PATH)));

    hue.setCaching(true);

    allLights = hue.getAllLights();
    assertEquals("Group 0", allLights.getName());
    assertEquals(expectedTotalNumberOfLights, allLights.getLights().size());
    assertFalse(allLights.isAllOn());
    assertTrue(allLights.isAnyOn());
    assertTrue(allLights.getScenes().isEmpty());

    // Should have 3 more calls, i.e. 6 in total, as caching should have no effect on this method:
    wireMockServer.verify(6, getRequestedFor(urlEqualTo(GROUP_0_PATH)));
  }

  @Test
  void testGetUnassignedLightByName() {
    final Hue hue = createHueAndInitializeMockServer();

    final Light light = hue.getUnassignedLightByName("Hue Smart plug 1").get();
    assertEquals("Hue Smart plug 1", light.getName());
    assertEquals(LightType.ON_OFF_PLUGIN_UNIT, light.getType());
    assertTrue(light.isReachable());
    assertTrue(light.getState().getOn());
//      assertNull(light.getState().getBri());
//      assertNull(light.getState().getCt());
    assertNull(light.getState().getHue());
    assertNull(light.getState().getSat());
    assertNull(light.getState().getScene());
    assertNull(light.getState().getTransitiontime());
    assertNull(light.getState().getXy());

    final Optional<Light> unknown = hue.getUnassignedLightByName("Foo");
    assertFalse(unknown.isPresent());

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testSettingAlert() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .withRequestBody(equalToJson("{\"alert\":\"select\"}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/alert\": \"select\"}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    final Light light = hue.getRoomByName("Living room").get().getLightByName("LR 1").get();
    light.setState(State.SHORT_ALERT);

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(new EqualToJsonPattern("{\"alert\":\"select\"}", false, false)));
  }

  @Test
  void testInvalidApiKey() {
    final Hue hue = createHueAndInitializeMockServer();

    // Interestingly enough, the Bridge does respond with status code "200 OK" even the API key is wrong:
    wireMockServer.stubFor(get(API_BASE_PATH)
        .willReturn(okJson("[{\"error\":{\"type\":1,\"address\":\"/\",\"description\":\"unauthorized user\"}}]")));

    try {
      hue.getRooms(); // This will crash with a MismatchedInputException.
      fail("There should have been an exception.");
    } catch (HueApiException expected) {
      assertTrue(expected.getMessage().startsWith("Unauthorized user."));
    }

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @ParameterizedTest
  @CsvSource({
      "Living room,LR 1,NONE",
      "Living room,LR 2,SHORT_ALERT",
      "Bedroom,Pendant,LONG_ALERT",
      "Hallway 1,LED strip 1,UNKNOWN",
  })
  void testGetAlert(final String roomName, final String lightName, final AlertType expectedAlertType) {
    final Hue hue = createHueAndInitializeMockServer();
    final Light light = hue.getRoomByName(roomName).get().getLightByName(lightName).get();
    assertEquals(expectedAlertType, light.getState().getAlert());
  }

  @Test
  void testBridgeReturningNullCollections() {
    final String hueRoot = readFile("hueRootWithNullCollections.json");

    final ObjectMapper objectMapper = new ObjectMapper();
    final JsonNode jsonNode;
    try {
      jsonNode = objectMapper.readTree(hueRoot);
      wireMockServer.stubFor(get(API_BASE_PATH).willReturn(okJson(hueRoot)));
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    final Hue hue = new Hue("localhost:" + wireMockServer.httpsPort(), API_KEY);
    assertTrue(hue.getRooms().isEmpty());
    assertTrue(hue.getAmbientLightSensors().isEmpty());
    assertTrue(hue.getDaylightSensors().isEmpty());
    assertTrue(hue.getPresenceSensors().isEmpty());
    assertTrue(hue.getSwitches().isEmpty());
    assertTrue(hue.getTemperatureSensors().isEmpty());
    assertTrue(hue.getUnknownSensors().isEmpty());
    assertTrue(hue.getZones().isEmpty());
  }

  @Test
  void testSettingEffect() {
    wireMockServer.stubFor(put(API_BASE_PATH + "lights/100/state")
        .withRequestBody(equalToJson("{\"effect\":\"colorloop\"}"))
        .willReturn(okJson("[{\"success\":{\"/lights/100/state/effect\": \"colorloop\"}}]")));

    final Hue hue = createHueAndInitializeMockServer();
    final Light light = hue.getRoomByName("Living room").get().getLightByName("LR 1").get();
    light.setState(State.COLOR_LOOP_EFFECT);

    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
    wireMockServer.verify(1, putRequestedFor(urlEqualTo(API_BASE_PATH + "lights/100/state"))
        .withRequestBody(new EqualToJsonPattern("{\"effect\":\"colorloop\"}", false, false)));
  }

  @Test
  void testGetMaxLumens() {
    final Hue hue = createHueAndInitializeMockServer();
    wireMockServer.stubFor(get(GROUP_0_PATH).willReturn(okJson(readFile("group0.json"))));
    final Room allLights = hue.getAllLights();
    assertEquals(Integer.valueOf(250), allLights.getLightByName("LR 1").get().getMaxLumens());
    assertEquals(Integer.valueOf(250), allLights.getLightByName("LR 2").get().getMaxLumens());
    assertEquals(Integer.valueOf(3000), allLights.getLightByName("Pendant").get().getMaxLumens());
    assertEquals(Integer.valueOf(120), allLights.getLightByName("LED strip 1").get().getMaxLumens());
    assertNull(allLights.getLightByName("Hue Smart plug 1").get().getMaxLumens());
  }

  @Test
  void testSearchForNewLights() throws ExecutionException, InterruptedException {
    final Hue hue = createHueAndInitializeMockServer();
    final Future<Collection<Light>> newLights = hue.searchForNewLights();
    final Collection<Light> lights = newLights.get();
    assertEquals(Arrays.asList("900"), lights.stream().map(Light::getId).collect(toList()));
  }

  @Test
  void testGetNewLightsSearchStatus() {
    final Hue hue = createHueAndInitializeMockServer();
    NewLightsResult status = hue.getNewLightsSearchStatus();
    assertEquals(NewLightsSearchStatus.ACTIVE, status.getStatus());

    status = hue.getNewLightsSearchStatus();
    assertEquals(NewLightsSearchStatus.ACTIVE, status.getStatus());

    status = hue.getNewLightsSearchStatus();
    assertEquals(NewLightsSearchStatus.ACTIVE, status.getStatus());

    status = hue.getNewLightsSearchStatus();
    assertEquals(NewLightsSearchStatus.COMPLETED, status.getStatus());
    assertEquals(Optional.of(ZonedDateTime.parse("2022-02-04T12:00:00+00:00[UTC]")), status.getLastSearchTime());
    assertEquals(Arrays.asList("900"), status.getNewLights().stream().map(Light::getId).collect(toList()));
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
