package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("device")
public class DeviceResource extends Resource {

  @JsonProperty("services")
  private List<ResourceIdentifier> services;

  @JsonProperty("product_data")
  private ProductData productData;

  @JsonProperty("metadata")
  private Metadata metadata;

  public List<ResourceIdentifier> getServices() {
    return services;
  }

  public ProductData getProductData() {
    return productData;
  }

  public Metadata getMetadata() {
    return metadata;
  }
}
