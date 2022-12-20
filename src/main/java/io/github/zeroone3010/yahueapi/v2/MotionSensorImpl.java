package io.github.zeroone3010.yahueapi.v2;

import java.util.UUID;

public class MotionSensorImpl implements Device {

  private final UUID id;
  private final String name;

  public MotionSensorImpl(final UUID id, final String name) {

    this.id = id;
    this.name = name;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "MotionSensorImpl{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
