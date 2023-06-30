package io.github.zeroone3010.yahueapi.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.zeroone3010.yahueapi.domain.JsonStringUtil;

import java.util.UUID;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    defaultImpl = Resource.class,
    visible = true,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ButtonResource.class, name = "button"),
    @JsonSubTypes.Type(value = DeviceResource.class, name = "device"),
    @JsonSubTypes.Type(value = LightResource.class, name = "light"),
    @JsonSubTypes.Type(value = RoomResource.class, name = "room"),
    @JsonSubTypes.Type(value = ZoneResource.class, name = "zone"),
    @JsonSubTypes.Type(value = GroupedLightResource.class, name = "grouped_light"),
    @JsonSubTypes.Type(value = SceneResource.class, name = "scene"),
    @JsonSubTypes.Type(value = MotionResource.class, name = "motion"),
})
public class Resource {

  @JsonProperty("type")
  private ResourceType type;

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("id_v1")
  private String idV1;

  public ResourceType getType() {
    return type;
  }

  public UUID getId() {
    return id;
  }

  public String getIdV1() {
    return idV1;
  }

  public ResourceIdentifier identifier() {
    final ResourceIdentifier identifier = new ResourceIdentifier();
    identifier.setResourceId(id);
    identifier.setResourceType(type);
    return identifier;
  }

  @Override
  public String toString() {
    return JsonStringUtil.toJsonString(this);
  }
}
