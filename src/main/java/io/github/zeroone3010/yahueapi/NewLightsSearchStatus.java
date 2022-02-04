package io.github.zeroone3010.yahueapi;

public enum NewLightsSearchStatus {

  /**
   * A search for new lights is currently active, please check again soon.
   */
  ACTIVE,

  /**
   * A search for new lights has not been performed since the last time the Bridge was rebooted.
   */
  NONE,

  /**
   * A search for a new lights has been completed. Check {@link NewLightsResult#getLastSearchTime()} for the
   * time when the search was completed.
   */
  COMPLETED
}
