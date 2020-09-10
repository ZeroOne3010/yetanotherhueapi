package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SensorDto {
  @SerializedName("name")
  private String name;
  @SerializedName("type")
  private String type;
  @SerializedName("state")
  private Map<String, Object> state;
  @SerializedName("swupdate")
  private ComponentSoftwareUpdate softwareUpdate;
  @SerializedName("config")
  private Map<String, Object> config;
  @SerializedName("modelid")
  private String modelId;
  @SerializedName("manufacturername")
  private String manufacturerName;
  @SerializedName("swversion")
  private String softwareVersion;
  @SerializedName("uniqueid")
  private String uniqueId;
  @SerializedName("recycle")
  private Boolean recycle;
  @SerializedName("productname")
  private String productName;
  @SerializedName("capabilities")
  private Map<String, Object> capabilities;

  public Map<String, Object> getState() {
    return state;
  }

  public void setState(final Map<String, Object> state) {
    this.state = state;
  }

  public ComponentSoftwareUpdate getSoftwareUpdate() {
    return softwareUpdate;
  }

  public void setSoftwareUpdate(final ComponentSoftwareUpdate softwareUpdate) {
    this.softwareUpdate = softwareUpdate;
  }

  public Map<String, Object> getConfig() {
    return config;
  }

  public void setConfig(final Map<String, Object> config) {
    this.config = config;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getModelId() {
    return modelId;
  }

  public void setModelId(final String modelId) {
    this.modelId = modelId;
  }

  public String getManufacturerName() {
    return manufacturerName;
  }

  public void setManufacturerName(final String manufacturerName) {
    this.manufacturerName = manufacturerName;
  }

  public String getSoftwareVersion() {
    return softwareVersion;
  }

  public void setSoftwareVersion(final String softwareVersion) {
    this.softwareVersion = softwareVersion;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(final String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public Boolean getRecycle() {
    return recycle;
  }

  public void setRecycle(final Boolean recycle) {
    this.recycle = recycle;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(final String productName) {
    this.productName = productName;
  }

  public Map<String, Object> getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(final Map<String, Object> capabilities) {
    this.capabilities = capabilities;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
