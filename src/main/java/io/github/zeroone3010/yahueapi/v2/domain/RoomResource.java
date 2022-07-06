package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RoomResource extends Resource {

  @JsonProperty("grouped_services")
  private List<ResourceIdentifier> groupedServices;

  @JsonProperty("services")
  private List<ResourceIdentifier> services;

  @JsonProperty("metadata")
  private Metadata metadata;

  @JsonProperty("children")
  private List<ResourceIdentifier> children;
}
