package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import io.github.zeroone3010.yahueapi.domain.Root;

import java.io.IOException;

/**
 * In case the API key is wrong, the Bridge returns status code "200 OK" and an array of errors.
 */
class UnauthorizedUserHandler extends DeserializationProblemHandler {
  public Object handleUnexpectedToken(DeserializationContext ctxt,
                                      JavaType targetType, JsonToken t, JsonParser p,
                                      String failureMsg) throws IOException {
    if (Root.class.equals(targetType.getRawClass()) && t == JsonToken.START_ARRAY) {
      JsonToken token;
      boolean error = false;
      while ((token = p.nextToken()) != null) {
        if (JsonToken.FIELD_NAME.equals(token) && "error".equals(p.getText())) {
          error = true;
        } else if (error && JsonToken.VALUE_STRING.equals(token) && "unauthorized user".equalsIgnoreCase(p.getText())) {
          throw new HueApiException("Unauthorized user. Either your API key is wrong, " +
              "or, if you have multiple Bridges, your API key is correct but you are calling the wrong Bridge.");
        }
      }
    }
    return handleUnexpectedToken(ctxt, targetType.getRawClass(), t, p, failureMsg);
  }
}
