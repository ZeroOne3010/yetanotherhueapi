package io.github.zeroone3010.yahueapi.v2;

import java.util.UUID;

/**
 * Any device that the Bridge knows. There are more specific subclasses for devices
 * that this library supports properly, e.g. {@link Light} and {@link Switch}.
 *
 * @since 3.0.0
 */
public interface Device {
  /**
   * <p>Returns the technical id of the device, as assigned by the Bridge.
   * The id stays the same even if the device name is changed by the user.</p>
   *
   * @return Id of the device.
   */
  UUID getId();

  /**
   * Returns the name of the device, as set by the user.
   *
   * @return Name of the device.
   */
  String getName();
}
