package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BridgeConfig {
  @JsonProperty("name")
  private String name;
  @JsonProperty("zigbeechannel")
  private int zigbeeChannel;
  @JsonProperty("bridgeid")
  private String bridgeId;
  @JsonProperty("mac")
  private String mac;
  @JsonProperty("dhcp")
  private boolean dhcp;
  @JsonProperty("ipaddress")
  private String ipAddress;
  @JsonProperty("netmask")
  private String netmask;
  @JsonProperty("gateway")
  private String gateway;
  @JsonProperty("proxyaddress")
  private String proxyAddress;
  @JsonProperty("proxyport")
  private int proxyPort;
  @JsonProperty("UTC")
  private String utc;
  @JsonProperty("localtime")
  private String localTime;
  @JsonProperty("timezone")
  private String timeZone;
  @JsonProperty("modelid")
  private String modelId;
  @JsonProperty("datastoreversion")
  private String dataStoreVersion;
  @JsonProperty("swversion")
  private String softwareVersion;
  @JsonProperty("apiversion")
  private String apiVersion;
  @JsonProperty("swupdate")
  private ConfigSoftwareUpdate softwareUpdate;
  @JsonProperty("swupdate2")
  private ConfigSoftwareUpdate2 softwareUpdate2;
  @JsonProperty("linkbutton")
  private boolean linkButton;
  @JsonProperty("portalservices")
  private boolean portalServices;
  @JsonProperty("portalconnection")
  private String portalConnection;
  @JsonProperty("portalstate")
  private PortalState portalState;
  @JsonProperty("internetservices")
  private InternetServices internetServices;
  @JsonProperty("factorynew")
  private boolean factoryNew;
  @JsonProperty("replacesbridgeid")
  private String replacesBridgeId;
  @JsonProperty("backup")
  private ConfigBackup backup;
  @JsonProperty("starterkitid")
  private String starterKitId;
  @JsonProperty("whitelist")
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
