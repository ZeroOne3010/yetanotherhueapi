Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

Unreleased
----------

### Added

* Support for events! Use the new `io.github.zeroone3010.yahueapi.v2.Hue` class
  and its `subscribeToEvents(HueEventListener)` method to get real time updates from the Bridge
  when something happens in the system.

### Removed

* Removed UPNP from Bridge discovery methods, as Philips is deprecating it.
* Removed plain HTTP as a possible Bridge connection protocol, as Philips is deprecating it. With this removal
  also removed the constructor from the `Hue` class that had `HueBridgeProtocol` as a parameter, as the enum
  now only has one value. Also changed said enum from public to package private.

### Deprecated

* `getRooms()`, `getZones()`,  `getRoomByName()`, and `getZoneByName()` methods from the
  `io.github.zeroone3010.yahueapi.Hue` class in favor of the methods with the same names (if not the same signatures)
  in the `io.github.zeroone3010.yahueapi.v2.Hue` class.

v2.7.0 (2022-04-19)
----------

### Added

* A new method, `HueBridgeConnectionBuilder.isHueBridgeEndpoint()`, to test whether you are indeed
  trying to connect to a Hue bridge and not some other server by accident (contributed by @Kakifrucht)
* `getId()` method for `Scene` objects, to get the technical ids of scenes.

### Changed

* The `java.util.logging` based loggers have been scrapped in favor of SLF4J (contributed by @Kakifrucht)

v2.6.0 (2022-02-16)
----------

### Added

* Added a way to search for new lights in the system: the `searchForNewLights()` method in the `Hue` class.
  It is accompanied by the `getNewLightsSearchStatus()` method, which, however, you do not need to use
  when searching for new lights, as the first method will resolve a `Future` with a collection of the new
  lights found, if any.
* Added mDNS (multicast DNS) as a new Bridge discovery method. This will be used instead of UPNP which Philips will
  disable in the future.
* Added `Room.addLight(Light)` and  `Room.removeLight(Light)` methods for adding and removing lights to and from rooms.

### Changed

* Changed System.out.println commands into proper logger calls.
* Changed logger names into `io.github.zeroone3010.yahueapi`.

### Fixed

* Fixed a possible `NullPointerException` when querying temperature from a temperature sensor if the sensor is disabled
  in the app (now it just returns `null` instead).

v2.5.0 (2021-11-24)
----------

### Changed

* Bridge connections are now using HTTPS by default, for the plain HTTP connections have been
  scheduled for deprecation by Philips. Note, however, that as many bridges still have self-signed
  certificates which cannot really be validated anyway, this means that by default all certificate
  validations will be turned off. If the Hue bridge is not the only system that your application talks to,
  you should consider whether this is an issue for you. Eventually Philips will update the bridges to
  use a Signify root certificate, after which sensible validation can be performed.

### Deprecated

* Deprecated the UPnP bridge discovery method,
  for Philips has announced it has been scheduled to be disabled in Q2 2022.
* Deprecated plain HTTP as a communication protocol with the bridge, again due to Philips deprecating it too

v2.4.0 (2021-11-17)
----------

### Added

* `getAllLights()` method for the `Hue` object, returning a `Room` object with all the lights that the Bridge knows
about. This is a convenience method provided by the Hue API itself, and it allows you to easily toggle all the lights
in the system at once.
* `getMaxLumens()` method for `Light` objects. With the recent addition of 1600 lumen bulbs into the Hue family,
it seemed like a good idea to be able to differentiate the brighter lamps from the dimmer ones.
* `getId()` method for `Light` and `Room` objects. Requested by users, these methods may be needed for
advanced use cases.

v2.3.0 (2021-09-09)
----------

### Added

* Support for effects. Just like alerts, effects can be activated with the light `State`. The Hue API currently supports
only one type of effect: the color loop. It cycles indefinitely through all the hues using the current brightness and
saturation settings. The color loop effect can be activated with the `.setState(State.COLOR_LOOP_EFFECT)` method of
a light, and subsequently stopped with `.setState(State.NO_EFFECTS)`.
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
