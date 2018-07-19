package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GroupState {
    @JsonProperty("all_on")
    private boolean allOn;
    @JsonProperty("any_on")
    private boolean anyOn;

    public boolean isAllOn() {
        return allOn;
    }

    public void setAllOn(boolean allOn) {
        this.allOn = allOn;
    }

    public boolean isAnyOn() {
        return anyOn;
    }

    public void setAnyOn(boolean anyOn) {
        this.anyOn = anyOn;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
