package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.Temperature;
import io.github.zeroone3010.yahueapi.v2.domain.TemperatureReport;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static java.math.RoundingMode.HALF_UP;

public class TemperatureSensorImpl implements TemperatureSensor {
  private static final BigDecimal ONE_POINT_EIGHT = new BigDecimal("1.8");
  private static final BigDecimal THIRTY_TWO = new BigDecimal("32");
  private static final int SCALE = 2;

  private final UUID id;
  private final String name;
  private final Supplier<Temperature> stateProvider;

  public TemperatureSensorImpl(final UUID id,
                               final String name,
                               final Supplier<Temperature> stateProvider) {
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
  public BigDecimal getDegreesCelsius() {
    return Optional.ofNullable(stateProvider.get().getTemperature())
        .map(celsius -> celsius.setScale(SCALE, HALF_UP))
        .orElse(null);
  }

  @Override
  public BigDecimal getDegreesFahrenheit() {
    return Optional.ofNullable(stateProvider.get().getTemperature())
        .map(celsius -> celsius.multiply(ONE_POINT_EIGHT).add(THIRTY_TWO).setScale(SCALE, HALF_UP))
        .orElse(null);
  }

  @Override
  public ZonedDateTime getLastChanged() {
    return Optional.ofNullable(stateProvider.get())
        .map(Temperature::getTemperatureReport)
        .map(TemperatureReport::getChanged)
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
