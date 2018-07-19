package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class EntertainmentStream {
    @JsonProperty("proxymode")
    private String proxymode;
    @JsonProperty("proxynode")
    private String proxynode;
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("owner")
    private String owner;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
