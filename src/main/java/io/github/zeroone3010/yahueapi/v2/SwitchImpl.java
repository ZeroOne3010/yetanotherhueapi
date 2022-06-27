package io.github.zeroone3010.yahueapi.v2;

import java.util.List;
import java.util.UUID;

public class SwitchImpl implements Switch {
  private final UUID id;
  private final List<Button> buttons;
  private final String name;

  public SwitchImpl(final UUID id, final List<Button> buttons, final String name) {
    this.id = id;
    this.buttons = buttons;
    this.name = name;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public List<Button> getButtons() {
    return buttons;
  }

  @Override
  public String getName() {
    return name;
  }
}
