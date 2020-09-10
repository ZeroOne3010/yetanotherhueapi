package io.github.zeroone3010.yahueapi.domain;


import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class BridgeConfig {
  @SerializedName("name")
  private String name;
  @SerializedName("zigbeechannel")
  private int zigbeeChannel;
  @SerializedName("bridgeid")
  private String bridgeId;
  @SerializedName("mac")
  private String mac;
  @SerializedName("dhcp")
  private boolean dhcp;
  @SerializedName("ipaddress")
  private String ipAddress;
  @SerializedName("netmask")
  private String netmask;
  @SerializedName("gateway")
  private String gateway;
  @SerializedName("proxyaddress")
  private String proxyAddress;
  @SerializedName("proxyport")
  private int proxyPort;
  @SerializedName("UTC")
  private String utc;
  @SerializedName("localtime")
  private String localTime;
  @SerializedName("timezone")
  private String timeZone;
  @SerializedName("modelid")
  private String modelId;
  @SerializedName("datastoreversion")
  private String dataStoreVersion;
  @SerializedName("swversion")
  private String softwareVersion;
  @SerializedName("apiversion")
  private String apiVersion;
  @SerializedName("swupdate")
  private ConfigSoftwareUpdate softwareUpdate;
  @SerializedName("swupdate2")
  private ConfigSoftwareUpdate2 softwareUpdate2;
  @SerializedName("linkbutton")
  private boolean linkButton;
  @SerializedName("portalservices")
  private boolean portalServices;
  @SerializedName("portalconnection")
  private String portalConnection;
  @SerializedName("portalstate")
  private PortalState portalState;
  @SerializedName("internetservices")
  private InternetServices internetServices;
  @SerializedName("factorynew")
  private boolean factoryNew;
  @SerializedName("replacesbridgeid")
  private String replacesBridgeId;
  @SerializedName("backup")
  private ConfigBackup backup;
  @SerializedName("starterkitid")
  private String starterKitId;
  @SerializedName("whitelist")
  private Map<String, WhiteListItem> whiteList;

  public String getName() {
    return name;
  }

  public int getZigbeeChannel() {
    return zigbeeChannel;
  }

  public String getBridgeId() {
    return bridgeId;
  }

  public String getMac() {
    return mac;
  }

  public boolean isDhcp() {
    return dhcp;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public String getNetmask() {
    return netmask;
  }

  public String getGateway() {
    return gateway;
  }

  public String getProxyAddress() {
    return proxyAddress;
  }

  public int getProxyPort() {
    return proxyPort;
  }

  public String getUtc() {
    return utc;
  }

  public String getLocalTime() {
    return localTime;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public String getModelId() {
    return modelId;
  }

  public String getDataStoreVersion() {
    return dataStoreVersion;
  }

  public String getSoftwareVersion() {
    return softwareVersion;
  }

  public String getApiVersion() {
    return apiVersion;
  }

  public ConfigSoftwareUpdate getSoftwareUpdate() {
    return softwareUpdate;
  }

  public ConfigSoftwareUpdate2 getSoftwareUpdate2() {
    return softwareUpdate2;
  }

  public boolean isLinkButton() {
    return linkButton;
  }

  public boolean isPortalServices() {
    return portalServices;
  }

  public String getPortalConnection() {
    return portalConnection;
  }

  public PortalState getPortalState() {
    return portalState;
  }

  public InternetServices getInternetServices() {
    return internetServices;
  }

  public boolean isFactoryNew() {
    return factoryNew;
  }

  public String getReplacesBridgeId() {
    return replacesBridgeId;
  }

  public ConfigBackup getBackup() {
    return backup;
  }

  public String getStarterKitId() {
    return starterKitId;
  }

  public Map<String, WhiteListItem> getWhiteList() {
    return whiteList;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
