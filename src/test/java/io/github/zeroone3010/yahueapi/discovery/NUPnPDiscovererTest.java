package io.github.zeroone3010.yahueapi.discovery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.github.zeroone3010.yahueapi.HueBridge;
import io.github.zeroone3010.yahueapi.discovery.NUPnPDiscoverer.NUPnPDeserializer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NUPnPDiscovererTest {

  final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());

  @Test
  void testDiscovery() throws ExecutionException, InterruptedException {
    final List<HueBridge> foundBridges = new ArrayList<>();
    try {
      wireMockServer.start();

      wireMockServer.stubFor(get(urlMatching(".*")).atPriority(10)
          .willReturn(okJson("[{\"internalipaddress\":\"1.2.3.45\"}]")
          ));

      final NUPnPDiscoverer discoverer = new NUPnPDiscoverer(
          "http://localhost:" + wireMockServer.port(), foundBridges::add);
      discoverer.discoverBridges().get();

    } finally {
      wireMockServer.stop();
    }

    assertEquals(1, foundBridges.size());
    assertEquals(new HueBridge("1.2.3.45"), foundBridges.get(0));
  }

  @Test
  void testDeserializationForNoBridges() throws JsonProcessingException {
    final List<HueBridge> result = readValue("[]");
    assertEquals(emptyList(), result);
  }

  @Test
  void testDeserializationForOneBridge() throws JsonProcessingException {
    final List<HueBridge> result = readValue("[{\"internalipaddress\":\"12.22.32.42\"}]");
    assertEquals(singletonList(new HueBridge("12.22.32.42")), result);
  }

  @Test
  void testDeserializationForTwoBridges() throws JsonProcessingException {
    final List<HueBridge> result = readValue("[{\"internalipaddress\":\"12.22.32.42\"},{\"internalipaddress\":\"5.4.3.210\"}]");
    assertEquals(asList(new HueBridge("12.22.32.42"), new HueBridge("5.4.3.210")), result);
  }

  private List<HueBridge> readValue(final String inputJson) throws JsonProcessingException {
    final ObjectMapper objectMapper = new ObjectMapper();
    final SimpleModule module = new SimpleModule();
    module.addDeserializer(HueBridge.class, new NUPnPDeserializer());
    objectMapper.registerModule(module);
    return objectMapper.readValue(inputJson, new TypeReference<ArrayList<HueBridge>>() {});
  }
}
