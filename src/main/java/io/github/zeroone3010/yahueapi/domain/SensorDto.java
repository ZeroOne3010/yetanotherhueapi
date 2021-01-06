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
  private SensorCapabilities capabilities;

  public Map<String, Object> getState() {
    return state;
  }

  public ComponentSoftwareUpdate getSoftwareUpdate() {
    return softwareUpdate;
  }

  public Map<String, Object> getConfig() {
    return config;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getModelId() {
    return modelId;
  }

  public String getManufacturerName() {
    return manufacturerName;
  }

  public String getSoftwareVersion() {
    return softwareVersion;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public Boolean getRecycle() {
    return recycle;
  }

  public String getProductName() {
    return productName;
  }

  public SensorCapabilities getCapabilities() {
    return capabilities;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
