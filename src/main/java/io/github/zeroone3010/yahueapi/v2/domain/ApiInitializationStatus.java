package io.github.zeroone3010.yahueapi.v2.domain;

public class ApiInitializationStatus {
  private ApiInitializationError error;
  private ApiInitializationSuccess success;

  public ApiInitializationError getError() {
    return error;
  }

  public ApiInitializationSuccess getSuccess() {
    return success;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
