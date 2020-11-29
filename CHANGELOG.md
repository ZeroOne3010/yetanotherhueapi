Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

Unreleased
----------

### Deprecations

* Deprecate the `LightType.ON_OFF` enumeration value: this one will be renamed to ON_OFF_LIGHT in the future

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
