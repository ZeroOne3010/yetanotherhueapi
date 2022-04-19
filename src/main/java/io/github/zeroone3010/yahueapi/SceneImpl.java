package io.github.zeroone3010.yahueapi;

import java.util.function.Function;

final class SceneImpl implements Scene {
  private final String id;
  private final String name;
  private final Function<State, String> stateSetter;

  SceneImpl(final String id, final String name, final Function<State, String> stateSetter) {
    this.id = id;
    this.name = name;
    this.stateSetter = stateSetter;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void activate() {
    stateSetter.apply(State.builder().scene(id).keepCurrentState());
  }

  @Override
  public String getName() {
    return name;
  }
}
