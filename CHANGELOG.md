Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

Unreleased v2.0.0
----------

This release contains breaking changes. This means that if you have been using a previous version of this library,
you _may_ need to change your code to account for the changes in this version. See below for the details.

### Added

* Support for other kinds of switches than just the Philips Hue dimmer switches.
* `getUnassignedLightByName(String)` method, to accompany the `getUnassignedLights()` method added in the previous release.
* Added geofence support as motion sensors. They can be retrieved like all motion sensors with `getMotionSensors()` or `getMotionSensorByName(String)` with the name of a registered device.
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
