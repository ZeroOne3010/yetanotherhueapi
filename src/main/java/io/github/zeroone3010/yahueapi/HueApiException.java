package io.github.zeroone3010.yahueapi;

public final class HueApiException extends RuntimeException {
  public HueApiException(final Throwable cause) {
    super(cause);
  }

  public HueApiException(final String message) {
    super(message);
  }

  HueApiException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
