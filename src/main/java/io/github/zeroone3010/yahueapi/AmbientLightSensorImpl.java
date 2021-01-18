package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.net.URL;
import java.util.Map;
import java.util.function.Supplier;

public class AmbientLightSensorImpl extends BasicSensor implements AmbientLightSensor {

  AmbientLightSensorImpl(final String id, final SensorDto sensor, final URL url, final Supplier<Map<String, Object>> stateProvider) {
    super(id, sensor, url, stateProvider);
  }

  @Override
  public String toString() {
    return "AmbientLightSensor{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        '}';
  }

  @Override
  public int getLightLevel() {
    return calculateLux(readStateValue("lightlevel", Integer.class));
  }

  // Formula taken from //https://developers.meethue.com/develop/hue-api/supported-devices/#clip_zll_lightlevel
  static int calculateLux(final int lightLevel) {
    return (int) Math.round(Math.pow(10, (lightLevel - 1) / 10000D));
  }

  @Override
  public boolean isDaylight() {
    return readStateValue("daylight", Boolean.class);
  }

  @Override
  public boolean isDark() {
    return readStateValue("dark", Boolean.class);
  }
}
