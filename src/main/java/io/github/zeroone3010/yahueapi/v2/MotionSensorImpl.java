package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.Motion;
import io.github.zeroone3010.yahueapi.v2.domain.MotionReport;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class MotionSensorImpl implements MotionSensor {

  private final UUID id;
  private final String name;
  private final Supplier<Motion> stateProvider;

  public MotionSensorImpl(final UUID id,
                          final String name,
                          final Supplier<Motion> stateProvider) {
    this.id = id;
    this.name = name;
    this.stateProvider = stateProvider;
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
  public boolean isMotion() {
    return stateProvider.get().isMotion();
  }

  @Override
  public ZonedDateTime getLastChanged() {
    return Optional.ofNullable(stateProvider.get())
        .map(Motion::getMotionReport)
        .map(MotionReport::getChanged)
        .map(ZonedDateTime::parse)
        .orElse(null);
  }

  @Override
  public String toString() {
    return "MotionSensorImpl{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
