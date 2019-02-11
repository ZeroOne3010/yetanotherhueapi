package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigSoftwareUpdate2Bridge {
    @JsonProperty("state")
    private String state;
    @JsonProperty("lastinstall")
    private String lastinstall;

    @Override
    public String toString() {
        return JsonStringUtil.toJsonString(this);
    }
}
