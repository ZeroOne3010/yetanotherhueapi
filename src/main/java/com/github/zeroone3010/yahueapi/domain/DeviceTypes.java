package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class DeviceTypes {
    @JsonProperty("bridge")
    private boolean bridge;
    @JsonProperty("lights")
    private List<Object> lights;
    @JsonProperty("sensors")
    private List<Object> sensors;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
