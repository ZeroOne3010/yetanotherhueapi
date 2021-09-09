Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

Unreleased
----------

### Added

* Support for effects. Just like alerts, effects can be activated with the light `State`. The Hue API currently supports
only one type of effect: the color loop. It cycles indefinitely through all the hues using the current brightness and
saturation settings.
* `turnOn()` and `turnOff()` methods for the `Room` interface, for toggling all the lights in a room or zone.

v2.2.0 (2021-03-15)
----------

### Added

* More Javadoc documentation

### Changed

* Fixed possible null pointer exceptions, should the Bridge ever return any collections (lights, sensors, etc) as `null`.
This is probably not possible with the actual Hue Bridge, but there are emulator systems that may behave like this.
* Handle *unauthorized user error* more gracefully, explaining the situation in the exception.

v2.1.0 (2021-02-21)
----------

### Added

* Support for alerts. Alerts can be activated with the light `State`. There are two types of alerts currently supported
by the Hue API: short alerts, i.e. one "breath cycle", and long alerts, where the light blinks for 15 seconds,
or until the alert type `NONE` is issued.
* More Javadoc documentation

### Changed

* Fixed a `NullPointerException` when trying to read the daylight status from a daylight sensor that had not been configured.
Now it will just return `false` and log a warning message when queried. The need to configure such a sensor has also
been explained in the Javadoc of the `DaylightSensorImpl` class.

v2.0.0 (2021-01-29)
----------

This release contains several breaking changes. This means that if you have been using a previous version of this library,
you may need to change your code to account for the changes in this version. See below for the details.
Also, many thanks to everyone who contributed issues and pull requests!

### Added

* Support for other kinds of switches than just the Philips Hue dimmer switches.
* `getUnassignedLightByName(String)` method, to accompany the `getUnassignedLights()` method added in the previous release.
* Added geofence support as presence sensors. They can be retrieved like all motion sensors with `getPresenceSensors()` or `getPresenceSensorByName(String)` with the name of a registered device.
* Added ambient light sensor support.
* Support for Android, thanks to the removal of the dependency to the `java.awt.Color` class (see below).

### Changed

* Changed how switches are handled: Philips Hue dimmer switches are no longer considered special in any way.
Instead, all switches are equal and can be accessed with the new `Switch` interface.
The `Hue` class now has `getSwitches()` and `getSwitchByName(String)` methods instead of the old `getDimmerSwitches()` and `getDimmerSwitchByName(String)` methods.
* `LightType.ON_OFF` enum value was renamed to `LightType.ON_OFF_LIGHT` to better distinguish it from the `ON_OFF_PLUGIN_UNIT` value.
* Light color is to be set with the new `io.github.zeroone3010.yahueapi.Color` class. The class contains a multitude
of factory methods, so that the transition from the old `java.awt.Color` would be as easy as possible. This change
was made to remove the dependency on the `java.awt` package, which is not available in the Android environment.
* Motion sensors renamed to presence sensors to account for the addition of geofence sensors. This means that in the
`Hue` class `getMotionSensors()` and `getMotionSensorByName(String)` methods have been renamed to
`getPresenceSensors()` and `getPresenceSensorByName(String)` respectively.

### Removed

* `DimmerSwitch`, `DimmerSwitchAction`, `DimmerSwitchButton`, and `DimmerSwitchButtonEvent` classes.
These have basically been replaced with the `Switch`, `SwitchEvent`, `Button`, and `ButtonEvent` classes.
* Setter methods accessible with the `getRaw()` method. Some of these were deprecated in v1.1.0 already.

v1.4.0 (2020-12-08)
----------

### Added

* Support for Smart Plugs: they will appear in the API with the type `LightType.ON_OFF_PLUGIN_UNIT`
* Support for lights that do not belong to any group or zone: accessible using the `getUnassignedLights()` of the `Hue` object

### Deprecations

* Deprecate the `LightType.ON_OFF` enumeration value: this one will be renamed to `ON_OFF_LIGHT` in the future

v1.3.1 (2020-02-27)
----------

### Fixed

* Fixed [issue #15](https://github.com/ZeroOne3010/yetanotherhueapi/issues/15):
  the `HueBridgeDiscoveryService` returned `HueBridge` objects with their name and IP address reversed

v1.3.0 (2020-02-16)
----------

### Added

* Bridge discovery: possibility to find any Bridges in the current LAN. UPnP and N-UPnP methods supported.
* Resourcelinks, accessible with the `getRaw()` method
* Scenes, accessible with the `getRaw()` method
* Possibility to activate a scene for a room
* Other types of light groups besides just lights and zones are now supported too

v1.2.0 (2019-07-19)
----------

### Added

* `setCaching(boolean)` method. This caches the Bridge state (i.e. light, sensor and room states) and stops querying the Bridge
  unless explicitly told to do that by using the `refresh()` method, or until caching is
  disabled again.
* `setBrightness(int)` method for `Room` objects.

v1.1.0 (2019-05-12)
-------------------

### Added
* Support for zones
* Getter methods for certain properties accessible with the `getRaw()` method

### Deprecated
* Certain setter methods accessible with the `getRaw()` method

v1.0.0 (2019-03-22)
-------------------

* Initial release
