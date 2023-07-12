package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

public class ProductData {
  @JsonProperty("model_id")
  private String modelId;

  @JsonProperty("manufacturer_name")
  private String manufacturerName;

  @JsonProperty("product_name")
  private String productName;

  @JsonProperty("product_archetype")
  private String productArchetype;

  @JsonProperty("certified")
  private boolean certified;

  @JsonProperty("software_version")
  private String softwareVersion;

  @JsonProperty("hardware_platform_type")
  private String hardwarePlatformType;

  public String getModelId() {
    return modelId;
  }

  public String getManufacturerName() {
    return manufacturerName;
  }

  public String getProductName() {
    return productName;
  }

  public String getProductArchetype() {
    return productArchetype;
  }

  public boolean isCertified() {
    return certified;
  }

  public String getSoftwareVersion() {
    return softwareVersion;
  }

  public String getHardwarePlatformType() {
    return hardwarePlatformType;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
