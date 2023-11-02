package io.github.zeroone3010.yahueapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zeroone3010.yahueapi.domain.ApiInitializationStatus;
import io.github.zeroone3010.yahueapi.v2.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


/**
 * Use this class to initialize a connection with your Bridge once you know the IP address of the Bridge.
 */
public class HueBridgeConnectionBuilder {
  private static final Logger logger = LoggerFactory.getLogger(HueBridgeConnectionBuilder.class);

  private static final int MAX_TRIES = 30;
  private final String urlString;
  private final String bridgeIp;

  public HueBridgeConnectionBuilder(final String bridgeIp) {
    this.urlString = "https://" + bridgeIp;
    this.bridgeIp = bridgeIp;
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
        final URL url = new URL(urlString + "/api/config");
        final HttpsURLConnection connection = HttpUtil.getAnonymousUrlConnection(url);
        final int responseCode = connection.getResponseCode();
        return HttpURLConnection.HTTP_OK == responseCode;
      } catch (final IOException e) {
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
