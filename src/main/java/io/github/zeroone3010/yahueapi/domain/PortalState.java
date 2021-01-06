package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

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

  public boolean isIncoming() {
    return incoming;
  }

  public boolean isOutgoing() {
    return outgoing;
  }

  public String getCommunication() {
    return communication;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
