package io.github.zeroone3010.yahueapi.domain;

import com.google.gson.annotations.SerializedName;
import io.github.zeroone3010.yahueapi.State;

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
  @SerializedName("productname")
  private String productName;
  private LightConfig config;

  public LightState getState() {
    return state;
  }

  /**
   * @param state
   * @deprecated This method does not actually affect the state of the light.
   * Use the {@link io.github.zeroone3010.yahueapi.Light#setState(State)} instead.
   * Acquire these {@code Light} objects with the {@link io.github.zeroone3010.yahueapi.Room#getLights()} and
   * {@link io.github.zeroone3010.yahueapi.Room#getLightByName(String)} methods.
   */
  public void setState(final LightState state) {
    this.state = state;
  }

  public ComponentSoftwareUpdate getSwupdate() {
    return swupdate;
  }

  /**
   * @param swupdate
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setSwupdate(final ComponentSoftwareUpdate swupdate) {
    this.swupdate = swupdate;
  }

  public String getType() {
    return type;
  }

  /**
   * @param type
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setType(final String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  /**
   * @param name
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setName(final String name) {
    this.name = name;
  }

  public String getModelid() {
    return modelid;
  }

  /**
   * @param modelid
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setModelid(final String modelid) {
    this.modelid = modelid;
  }

  public String getManufacturername() {
    return manufacturername;
  }

  /**
   * @param manufacturername
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setManufacturername(final String manufacturername) {
    this.manufacturername = manufacturername;
  }

  public Map<String, Object> getCapabilities() {
    return capabilities;
  }

  /**
   * @param capabilities
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setCapabilities(final Map<String, Object> capabilities) {
    this.capabilities = capabilities;
  }

  public String getUniqueid() {
    return uniqueid;
  }

  /**
   * @param uniqueid
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setUniqueid(final String uniqueid) {
    this.uniqueid = uniqueid;
  }

  public String getSwversion() {
    return swversion;
  }

  /**
   * @param swversion
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setSwversion(final String swversion) {
    this.swversion = swversion;
  }

  public String getSwconfigid() {
    return swconfigid;
  }

  /**
   * @param swconfigid
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setSwconfigid(final String swconfigid) {
    this.swconfigid = swconfigid;
  }

  public String getProductid() {
    return productid;
  }

  /**
   * @param productid
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setProductid(final String productid) {
    this.productid = productid;
  }

  public String getProductName() {
    return productName;
  }

  /**
   * @param productName
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setProductName(final String productName) {
    this.productName = productName;
  }

  public LightConfig getConfig() {
    return config;
  }

  /**
   * @param config
   * @deprecated The properties of the light cannot be changed with this method.
   */
  public void setConfig(final LightConfig config) {
    this.config = config;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
