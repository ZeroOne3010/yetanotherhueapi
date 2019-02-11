package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SensorDto {
  @JsonProperty("name")
  private String name;
  @JsonProperty("type")
  private String type;
  @JsonProperty("state")
  private Map<String, Object> state;
  @JsonProperty("swupdate")
  private ComponentSoftwareUpdate softwareUpdate;
  @JsonProperty("config")
  private Map<String, Object> config;
  @JsonProperty("modelid")
  private String modelId;
  @JsonProperty("manufacturername")
  private String manufacturerName;
  @JsonProperty("swversion")
  private String softwareVersion;
  @JsonProperty("uniqueid")
  private String uniqueId;
  @JsonProperty("recycle")
  private Boolean recycle;
  @JsonProperty("productname")
  private String productName;
  @JsonProperty("capabilities")
  private Map<String, Object> capabilities;

  public Map<String, Object> getState() {
    return state;
  }

  public void setState(Map<String, Object> state) {
    this.state = state;
  }

  public ComponentSoftwareUpdate getSoftwareUpdate() {
    return softwareUpdate;
  }

  public void setSoftwareUpdate(ComponentSoftwareUpdate softwareUpdate) {
    this.softwareUpdate = softwareUpdate;
  }

  public Map<String, Object> getConfig() {
    return config;
  }

  public void setConfig(Map<String, Object> config) {
    this.config = config;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getModelId() {
    return modelId;
  }

  public void setModelId(String modelId) {
    this.modelId = modelId;
  }

  public String getManufacturerName() {
    return manufacturerName;
  }

  public void setManufacturerName(String manufacturerName) {
    this.manufacturerName = manufacturerName;
  }

  public String getSoftwareVersion() {
    return softwareVersion;
  }

  public void setSoftwareVersion(String softwareVersion) {
    this.softwareVersion = softwareVersion;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public Boolean getRecycle() {
    return recycle;
  }

  public void setRecycle(Boolean recycle) {
    this.recycle = recycle;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Map<String, Object> getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(Map<String, Object> capabilities) {
    this.capabilities = capabilities;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
