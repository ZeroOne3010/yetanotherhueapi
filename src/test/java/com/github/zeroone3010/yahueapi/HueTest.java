package com.github.zeroone3010.yahueapi;

import com.github.jknack.handlebars.internal.Files;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class HueTest {
  private static final String API_KEY = "abcd1234";

  final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());

  @BeforeEach
  void startServer() {
    wireMockServer.start();
  }

  @AfterEach
  void stopServer() {
    wireMockServer.stop();
  }

  @Test
  void testInitialLoading() throws IOException {
    final String read = readFile("hueRoot.json");
    wireMockServer.stubFor(get(urlEqualTo("/api/" + API_KEY + "/"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(read)));

    final Hue hue = new Hue("localhost:" + wireMockServer.port(), API_KEY);
    hue.refresh();

    wireMockServer.verify(getRequestedFor(urlEqualTo("/api/" + API_KEY + "/")));
  }

  private String readFile(final String fileName) throws IOException {
    final ClassLoader classLoader = getClass().getClassLoader();
    final File file = new File(classLoader.getResource(fileName).getFile());
    return Files.read(file);
  }
}