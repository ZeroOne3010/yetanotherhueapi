package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigSoftwareUpdate {
    @JsonProperty("updatestate")
    private int updatestate;
    @JsonProperty("checkforupdate")
    private boolean checkForUpdate;
    @JsonProperty("devicetypes")
    private DeviceTypes devicetypes;
    @JsonProperty("url")
    private String url;
    @JsonProperty("text")
    private String text;
    @JsonProperty("notify")
    private boolean notify;

    @Override
    public String toString() {
        return JsonStringUtil.toJsonString(this);
    }
}
