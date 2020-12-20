package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.net.URL;
import java.util.Map;
import java.util.function.Supplier;

final class FriendsOfHueSwitchImpl extends BasicSensor implements FriendsOfHueSwitch {

  FriendsOfHueSwitchImpl(final String id, final SensorDto sensor, final URL url, final Supplier<Map<String, Object>> stateProvider) {
    super(id, sensor, url, stateProvider);
  }

  @Override
  public String toString() {
    return "FriendsOfHueSwitch{" +
        "id='" + super.id + '\'' +
        ", name='" + super.name + '\'' +
        ", type=" + super.type +
        '}';
  }

  @Override
  public FriendsOfHueSwitchButtonEvent getLatestButtonEvent() {
    return new FriendsOfHueSwitchButtonEvent(readStateValue("buttonevent", Integer.class));
  }
}
