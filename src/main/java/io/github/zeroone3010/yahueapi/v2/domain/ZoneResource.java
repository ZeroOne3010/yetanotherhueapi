package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("zone")
public class ZoneResource extends Resource implements GroupResource {

  @JsonProperty("services")
  private List<ResourceIdentifier> services;

  @JsonProperty("metadata")
  private Metadata metadata;

  @JsonProperty("children")
  private List<ResourceIdentifier> children;

  @Override
  public List<ResourceIdentifier> getServices() {
    return services;
  }

  @Override
  public Metadata getMetadata() {
    return metadata;
  }

  @Override
  public List<ResourceIdentifier> getChildren() {
    return children;
  }
}
