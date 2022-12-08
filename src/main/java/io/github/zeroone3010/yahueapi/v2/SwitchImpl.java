package io.github.zeroone3010.yahueapi.v2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SwitchImpl implements Switch {
  private final UUID id;
  private final Map<UUID, Button> buttons;
  private final String name;

  public SwitchImpl(final UUID id, final Map<UUID, Button> buttons, final String name) {
    this.id = id;
    this.buttons = buttons;
    this.name = name;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Map<UUID, Button> getButtons() {
    return buttons;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Optional<Button> getLatestPressedButton() {
    return buttons.values().stream().filter(button -> button.getLatestEvent().isPresent()).findFirst();
  }

  @Override
  public String toString() {
    return getId() + ": " + getName();
  }
}
