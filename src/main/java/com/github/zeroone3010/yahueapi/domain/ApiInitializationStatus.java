package com.github.zeroone3010.yahueapi.domain;

public class ApiInitializationStatus {
  private ApiInitializationError error;
  private ApiInitializationSuccess success;

  public ApiInitializationError getError() {
    return error;
  }

  public void setError(ApiInitializationError error) {
    this.error = error;
  }

  public ApiInitializationSuccess getSuccess() {
    return success;
  }

  public void setSuccess(ApiInitializationSuccess success) {
    this.success = success;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
