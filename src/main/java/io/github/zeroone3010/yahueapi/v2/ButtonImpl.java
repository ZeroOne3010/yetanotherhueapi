package io.github.zeroone3010.yahueapi.v2;

import io.github.zeroone3010.yahueapi.v2.domain.ButtonResource;
import io.github.zeroone3010.yahueapi.v2.domain.ButtonSpecifics;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class ButtonImpl implements Button {
  private final Supplier<ButtonResource> stateProvider;
  private final UUID id;
  private int number;

  public ButtonImpl(final Supplier<ButtonResource> stateProvider, final ButtonResource buttonResource) {
    this.stateProvider = stateProvider;
    this.id = buttonResource.getId();
    this.number = buttonResource.getMetadata().getControlId();
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public int getNumber() {
    return number;
  }

  @Override
  public Optional<ButtonEventType> getLatestEvent() {
    return Optional.ofNullable(stateProvider.get().getButton())
        .map(ButtonSpecifics::getLastEvent)
        .map(ButtonEventType::parseFromButtonEventType);
  }

  @Override
  public String toString() {
    return "Button{" + number + '}';
  }
}
