package io.github.zeroone3010.yahueapi;

import io.github.zeroone3010.yahueapi.ButtonEvent.ButtonEventType;
import io.github.zeroone3010.yahueapi.domain.SensorCapabilities;
import io.github.zeroone3010.yahueapi.domain.SensorDto;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

final class SwitchImpl extends BasicSensor implements Switch {
  private final List<Button> buttons;
  private final Map<Integer, Button> eventCodeToButton = new HashMap<>();

  SwitchImpl(final String id, final SensorDto sensor, final URL url, final Supplier<Map<String, Object>> stateProvider) {
    super(id, sensor, url, stateProvider);
    this.buttons = parseButtonsFromInputs(sensor.getCapabilities());
    this.buttons
        .forEach(button -> button.getPossibleEvents()
            .forEach(event -> this.eventCodeToButton.put(event.getEventCode(), button)));
  }

  private static List<Button> parseButtonsFromInputs(final SensorCapabilities capabilities) {
    if (capabilities == null || capabilities.getInputs() == null || capabilities.getInputs().isEmpty()) {
      return Collections.emptyList();
    }

    final List<Button> buttonList = new ArrayList<>();
    for (int buttonIndex = 0; buttonIndex < capabilities.getInputs().size(); buttonIndex++) {
      final List<ButtonEvent> possibleEvents = capabilities.getInputs().get(buttonIndex).getEvents().stream()
          .map(event -> new ButtonEvent(
                  ButtonEventType.parseFromButtonEventType(event.getEventType()),
                  event.getButtonEvent()
              )
          )
          .collect(toList());
      buttonList.add(new ButtonImpl(buttonIndex + 1, possibleEvents));
    }
    return Collections.unmodifiableList(buttonList);
  }

  @Override
  public List<Button> getButtons() {
    return buttons;
  }

  @Override
  public String toString() {
    return "Switch{" +
        "id='" + super.id + '\'' +
        ", name='" + super.name + '\'' +
        ", type=" + super.type +
        '}';
  }

  @Override
  public SwitchEvent getLatestEvent() {
    final Integer eventCode = readStateValue("buttonevent", Integer.class);
    final Button button = this.eventCodeToButton.getOrDefault(eventCode, ButtonImpl.UNKNOWN);
    final ButtonEventType eventType = button.getPossibleEvents().stream()
        .filter(event -> Objects.equals(event.getEventCode(), eventCode))
        .findFirst()
        .map(ButtonEvent::getEventType)
        .orElse(ButtonEventType.UNKNOWN);
    final ButtonEvent event = new ButtonEvent(eventType, eventCode);
    return new SwitchEvent(button, event);
  }
}
