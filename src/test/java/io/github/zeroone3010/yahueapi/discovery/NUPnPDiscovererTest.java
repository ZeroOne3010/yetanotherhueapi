package io.github.zeroone3010.yahueapi.discovery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.zeroone3010.yahueapi.HueBridge;
import io.github.zeroone3010.yahueapi.discovery.NUPnPDiscoverer.UPnPDeserializer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NUPnPDiscovererTest {
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
    module.addDeserializer(HueBridge.class, new UPnPDeserializer());
    objectMapper.registerModule(module);
    return objectMapper.readValue(inputJson, new TypeReference<ArrayList<HueBridge>>() {});
  }
}
