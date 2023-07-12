package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.Temperature;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureSensorImplTest {
  @Test
  void celsiusVsFahrenheit() throws NoSuchFieldException {
    Mutable<BigDecimal> value = new MutableObject<>(null);
    Supplier<Temperature> temperatureSupplier = () -> {
      try {
        Temperature temperature = new Temperature();
        Field field = Temperature.class.getDeclaredField("temperature");
        field.setAccessible(true);
        field.set(temperature, value.getValue());
        return temperature;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    };
    TemperatureSensorImpl sensor = new TemperatureSensorImpl(UUID.randomUUID(), "test", temperatureSupplier);

    assertNull(sensor.getDegreesCelsius());
    assertNull(sensor.getDegreesFahrenheit());

    value.setValue(new BigDecimal("-40.0009"));
    assertEquals(new BigDecimal("-40.00"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("-40.00"), sensor.getDegreesFahrenheit());

    value.setValue(new BigDecimal("-18.00"));
    assertEquals(new BigDecimal("-18.00"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("-0.40"), sensor.getDegreesFahrenheit());

    value.setValue(new BigDecimal("-1.23"));
    assertEquals(new BigDecimal("-1.23"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("29.79"), sensor.getDegreesFahrenheit());

    value.setValue(new BigDecimal("0.00"));
    assertEquals(new BigDecimal("0.00"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("32.00"), sensor.getDegreesFahrenheit());

    value.setValue(new BigDecimal("10.00"));
    assertEquals(new BigDecimal("10.00"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("50.00"), sensor.getDegreesFahrenheit());

    value.setValue(new BigDecimal("37"));
    assertEquals(new BigDecimal("37.00"), sensor.getDegreesCelsius());
    assertEquals(new BigDecimal("98.60"), sensor.getDegreesFahrenheit());
  }
}
