package com.github.zeroone3010.yahueapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

public class Light {
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

  public void setState(LightState state) {
    this.state = state;
  }

  public ComponentSoftwareUpdate getSwupdate() {
    return swupdate;
  }

  public void setSwupdate(ComponentSoftwareUpdate swupdate) {
    this.swupdate = swupdate;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getModelid() {
    return modelid;
  }

  public void setModelid(String modelid) {
    this.modelid = modelid;
  }

  public String getManufacturername() {
    return manufacturername;
  }

  public void setManufacturername(String manufacturername) {
    this.manufacturername = manufacturername;
  }

  public Map<String, Object> getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(Map<String, Object> capabilities) {
    this.capabilities = capabilities;
  }

  public String getUniqueid() {
    return uniqueid;
  }

  public void setUniqueid(String uniqueid) {
    this.uniqueid = uniqueid;
  }

  public String getSwversion() {
    return swversion;
  }

  public void setSwversion(String swversion) {
    this.swversion = swversion;
  }

  public String getSwconfigid() {
    return swconfigid;
  }

  public void setSwconfigid(String swconfigid) {
    this.swconfigid = swconfigid;
  }

  public String getProductid() {
    return productid;
  }

  public void setProductid(String productid) {
    this.productid = productid;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public LightConfig getConfig() {
    return config;
  }

  public void setConfig(LightConfig config) {
    this.config = config;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
