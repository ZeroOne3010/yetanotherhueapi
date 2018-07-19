package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PortalState {
    @JsonProperty("signedon")
    private boolean signedOn;
    @JsonProperty("incoming")
    private boolean incoming;
    @JsonProperty("outgoing")
    private boolean outgoing;
    @JsonProperty("communication")
    private String communication;

    public boolean isSignedOn() {
        return signedOn;
    }

    public void setSignedOn(boolean signedOn) {
        this.signedOn = signedOn;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    public boolean isOutgoing() {
        return outgoing;
    }

    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
