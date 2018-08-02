package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BridgeConfig {
    @JsonProperty("name")
    private String name;
    @JsonProperty("zigbeechannel")
    private String zigbeeChannel;
    @JsonProperty("bridgeid")
    private String bridgeId;
    @JsonProperty("mac")
    private String mac;
    @JsonProperty("dhcp")
    private String dhcp;
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

    @Override
    public String toString() {
        return JsonStringUtil.toJsonString(this);
    }
}
