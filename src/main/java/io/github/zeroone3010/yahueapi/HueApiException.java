package io.github.zeroone3010.yahueapi;

final class HueApiException extends RuntimeException {
  HueApiException(final Throwable cause) {
    super(cause);
  }

  HueApiException(final String message) {
    super(message);
  }

  HueApiException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
