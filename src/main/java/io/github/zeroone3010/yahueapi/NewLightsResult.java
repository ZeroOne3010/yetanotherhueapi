package io.github.zeroone3010.yahueapi;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

public final class NewLightsResult {
  private final Collection<Light> newLights;
  private final NewLightsSearchStatus status;
  private final ZonedDateTime lastSearchTime;

  public NewLightsResult(final Collection<Light> newLights,
                         final NewLightsSearchStatus status,
                         final ZonedDateTime lastSearchTime) {
    this.newLights = newLights;
    this.status = status;
    this.lastSearchTime = lastSearchTime;
  }

  /**
   * The lights that were found the last time new lights were searched for, if any.
   *
   * @return A collection of lights found the last time new lights were searched for.
   */
  public Collection<Light> getNewLights() {
    return newLights;
  }

  /**
   * Status of searching for new lights: {@code ACTIVE} if there is a search currently going on,
   * [@code NONE} if no search has been performed since the Bridge was last rebooted, and
   * {@code COMPLETED} if a search has been finished. In the case of a {@code COMPLETED} search,
   * see {@link #getLastSearchTime()} for the time when the search was finished.
   *
   * @return Status of searching for new lights.
   */
  public NewLightsSearchStatus getStatus() {
    return status;
  }

  /**
   * Time of the last finished search, or {@code Optional.empty()} if no search has been completed since the
   * last time the Bridge was rebooted.
   *
   * @return The time when the last search was finished, if one has been finished.
   */
  public Optional<ZonedDateTime> getLastSearchTime() {
    return Optional.ofNullable(lastSearchTime);
  }

  @Override
  public String toString() {
    return "NewLightsResult{" +
        "newLights=" + newLights +
        ", status=" + status +
        ", lastSearchTime=" + lastSearchTime +
        '}';
  }
}
