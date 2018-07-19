package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
