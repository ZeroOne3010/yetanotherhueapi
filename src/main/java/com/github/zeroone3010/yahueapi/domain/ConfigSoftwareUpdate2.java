package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ConfigSoftwareUpdate2 {
    @JsonProperty("checkforupdate")
    private boolean checkforupdate;
    @JsonProperty("lastchange")
    private String lastchange;
    @JsonProperty("bridge")
    private ConfigSoftwareUpdate2Bridge bridge;
    @JsonProperty("state")
    private String state;
    @JsonProperty("autoinstall")
    private ConfigSoftwareUpdate2AutoInstall autoinstall;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
