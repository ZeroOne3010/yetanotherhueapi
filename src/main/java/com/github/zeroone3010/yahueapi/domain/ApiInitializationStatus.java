package com.github.zeroone3010.yahueapi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
