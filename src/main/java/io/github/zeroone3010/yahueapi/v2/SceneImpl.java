package io.github.zeroone3010.yahueapi.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.function.Supplier;

public class SceneImpl implements Scene {
  private static final Logger logger = LoggerFactory.getLogger(SceneImpl.class);

  private UUID id;
  private String name;
  private Supplier<String> stateSetter;

  public SceneImpl(final UUID id, final String name, final Supplier<String> stateSetter) {

    this.id = id;
    this.name = name;
    this.stateSetter = stateSetter;
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
  public void activate() {
    final String result = stateSetter.get();
    logger.info(result);
  }

  @Override
  public String toString() {
    return "SceneImpl{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
