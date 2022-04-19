package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.List;
import java.util.UUID;

public class RoomResource {

  @JsonProperty("type")
  private String type;

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("id_v1")
  private String idV1;

  @JsonProperty("grouped_services")
  private List<ResourceIdentifier> groupedServices;

  @JsonProperty("services")
  private List<ResourceIdentifier> services;

  @JsonProperty("metadata")
  private Metadata metadata;

  @JsonProperty("children")
  private List<ResourceIdentifier> children;

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
