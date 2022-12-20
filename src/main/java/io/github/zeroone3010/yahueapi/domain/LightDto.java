package io.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class LightDto {
  private LightState state;
  private ComponentSoftwareUpdate swupdate;
  private String type;
  private String name;
  private String modelid;
  private String manufacturername;
  private Map<String, Object> capabilities;
  private String uniqueid;
  private String swversion;
  private String swconfigid;
  private String productid;
  @JsonProperty("productname")
  private String productName;
  private LightConfig config;

  public LightState getState() {
    return state;
  }

  public ComponentSoftwareUpdate getSwupdate() {
    return swupdate;
  }

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public String getModelid() {
    return modelid;
  }

  public String getManufacturername() {
    return manufacturername;
  }

  public Map<String, Object> getCapabilities() {
    return capabilities;
  }

  public String getUniqueid() {
    return uniqueid;
  }

  public String getSwversion() {
    return swversion;
  }

  public String getSwconfigid() {
    return swconfigid;
  }

  public String getProductid() {
    return productid;
  }

  public String getProductName() {
    return productName;
  }

  public LightConfig getConfig() {
    return config;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
