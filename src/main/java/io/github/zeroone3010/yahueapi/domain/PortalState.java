package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

public class PortalState {
  @SerializedName("signedon")
  private boolean signedOn;
  @SerializedName("incoming")
  private boolean incoming;
  @SerializedName("outgoing")
  private boolean outgoing;
  @SerializedName("communication")
  private String communication;

  public boolean isSignedOn() {
    return signedOn;
  }

  public void setSignedOn(final boolean signedOn) {
    this.signedOn = signedOn;
  }

  public boolean isIncoming() {
    return incoming;
  }

  public void setIncoming(final boolean incoming) {
    this.incoming = incoming;
  }

  public boolean isOutgoing() {
    return outgoing;
  }

  public void setOutgoing(final boolean outgoing) {
    this.outgoing = outgoing;
  }

  public String getCommunication() {
    return communication;
  }

  public void setCommunication(final String communication) {
    this.communication = communication;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
