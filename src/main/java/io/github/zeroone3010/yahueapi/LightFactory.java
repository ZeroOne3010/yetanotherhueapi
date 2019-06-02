package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.Root;

interface LightFactory {
  Light buildLight(String lightId, Root root, String bridgeUri, ObjectMapper objectMapper);
}
