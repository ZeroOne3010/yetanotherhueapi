package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.List;
import java.util.UUID;

public class DeviceResource {

  @JsonProperty("type")
  private String type;

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("id_v1")
  private String idV1;

  @JsonProperty("services")
  private List<ResourceIdentifier> services;

  @JsonProperty("product_data")
  private ProductData productData;

  @JsonProperty("metadata")
  private Metadata metadata;

  public String getType() {
    return type;
  }

  public UUID getId() {
    return id;
  }

  public String getIdV1() {
    return idV1;
  }

  public List<ResourceIdentifier> getServices() {
    return services;
  }

  public ProductData getProductData() {
    return productData;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
