package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public enum StartupMode {
  @SerializedName("safety")
  BRIGHT_LIGHT,
  @SerializedName("powerfail")
  KEEP_STATE,
  @SerializedName("lastonstate")
  LAST_ON_STATE,
  @SerializedName("custom")
  CUSTOM,
  @SerializedName("unknown")
  UNKNOWN;
}
