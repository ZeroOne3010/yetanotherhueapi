package com.github.zeroone3010.yahueapi;

import com.github.jknack.handlebars.internal.Files;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class HueTest {
  private static final String API_KEY = "abcd1234";
  public static final String API_BASE_PATH = "/api/" + API_KEY + "/";

  final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());

  @BeforeEach
  void startServer() {
    wireMockServer.start();
  }

  @AfterEach
  void stopServer() {
    wireMockServer.stop();
  }

  private Hue createHueAndInitializeMockServer() throws IOException {
    final String hueRoot = readFile("hueRoot.json");
    wireMockServer.stubFor(get(API_BASE_PATH).willReturn(okJson(hueRoot)));

    return new Hue("localhost:" + wireMockServer.port(), API_KEY);
  }

  @Test
  void testInitializationAndRefresh() throws IOException {
    final Hue hue = createHueAndInitializeMockServer();
    wireMockServer.verify(0, getRequestedFor(urlEqualTo(API_BASE_PATH)));

    hue.refresh();
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));

    hue.refresh();
    wireMockServer.verify(2, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testGetRooms() throws IOException {
    final Hue hue = createHueAndInitializeMockServer();

    assertEquals(2, hue.getRooms().size());
    hue.getRooms();
    hue.getRooms();
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  @Test
  void testGetRoomByName() throws IOException {
    final Hue hue = createHueAndInitializeMockServer();

    assertEquals(2, hue.getRoomByName("Living room").get().getLights().size());
    assertEquals(1, hue.getRoomByName("Bedroom").get().getLights().size());
    assertFalse(hue.getRoomByName("No such room").isPresent());
    wireMockServer.verify(1, getRequestedFor(urlEqualTo(API_BASE_PATH)));
  }

  private String readFile(final String fileName) throws IOException {
    final ClassLoader classLoader = getClass().getClassLoader();
    final File file = new File(classLoader.getResource(fileName).getFile());
    return Files.read(file);
  }
}