Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

Unreleased
----------

### Added

* Resourcelinks, accessible with the `getRaw()` method
* Scenes, accessible with the `getRaw()` method
* Possibility to activate a scene for a room

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
