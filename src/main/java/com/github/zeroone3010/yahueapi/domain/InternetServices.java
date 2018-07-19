package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class InternetServices {
    @JsonProperty("internet")
    private String internet;
    @JsonProperty("remoteaccess")
    private String remoteaccess;
    @JsonProperty("time")
    private String time;
    @JsonProperty("swupdate")
    private String swupdate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
