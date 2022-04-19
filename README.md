Yet Another Hue API
===================
[![Maven Central](https://img.shields.io/maven-central/v/io.github.zeroone3010/yetanotherhueapi.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.zeroone3010%22%20AND%20a:%22yetanotherhueapi%22)
[![javadoc](https://javadoc.io/badge2/io.github.zeroone3010/yetanotherhueapi/javadoc.svg)](https://javadoc.io/doc/io.github.zeroone3010/yetanotherhueapi)

This is a Java 8 API for the Philips Hue lights.<sup>1</sup> It does not use the official Hue SDK but instead accesses
the REST API of the Philips Hue Bridge directly. This library should also work with Android projects using
[API level 24 or higher](https://developer.android.com/guide/topics/manifest/uses-sdk-element#ApiLevels). This library
has last been confirmed to work with the Philips Hue Bridge API in November 2021.

----

**NOTE: Philips has announced that plain HTTP connections with the bridges will be disabled and
replaced with HTTPS only. HTTPS connections are the default for this library only from version *2.5.0* onwards.
Make sure your dependency version is up to date.**

----

Usage
-----

First, import the classes from this library:

[//]: # (imports)
```java
import io.github.zeroone3010.yahueapi.*;
import io.github.zeroone3010.yahueapi.discovery.*;
```

### Initializing the API with a connection to the Bridge

#### Bridge discovery

If you do not know the IP address of the Bridge, you can use the automatic Bridge discovery functionality.
The `discoverBridges` method of the `HueBridgeDiscoveryService` class accepts a `Consumer`
that is called whenever a new Bridge is found. You may either hook into that or you can supply a no-op consumer
and just use the `Future<List<HueBridge>>` that is returned. Please do note, however, that it may take
approximately five seconds for the discovery process to complete. The `HueBridge` objects hold an IP address
that may be then used to initiate a connection with the Bridge.

Without any parameters besides the consumer the `discoverBridges` method uses all available discovery
methods simultaneously, namely N-UPnP and UPnP. If you wish to change that, the method accepts a varargs
list of discovery method enum values.

[//]: # (throws-InterruptedException|java.util.concurrent.ExecutionException)
[//]: # (import java.util.List;)
[//]: # (import java.util.concurrent.Future;)
```java
Future<List<HueBridge>> bridgesFuture = new HueBridgeDiscoveryService()
        .discoverBridges(bridge -> System.out.println("Bridge found: " + bridge));
final List<HueBridge> bridges = bridgesFuture.get();
if( !bridges.isEmpty() ) {
  final String bridgeIp = bridges.get(0).getIp();
  System.out.println("Bridge found at " + bridgeIp);
  // Then follow the code snippets below under the "Once you have a Bridge IP address" header
}
```

#### Once you have a Bridge IP address

If you already have an API key for your Bridge:

[//]: # (init)
```java
final String bridgeIp = "192.168.1.99"; // Fill in the IP address of your Bridge
final String apiKey = "bn4z908...34jf03jokaf4"; // Fill in an API key to access your Bridge
final Hue hue = new Hue(bridgeIp, apiKey);
```

If you don't have an API key for your bridge:

[//]: # (throws-InterruptedException|java.util.concurrent.ExecutionException)
[//]: # (import java.util.concurrent.CompletableFuture;)
```java
final String bridgeIp = "192.168.1.99"; // Fill in the IP address of your Bridge
final String appName = "MyFirstHueApp"; // Fill in the name of your application
final CompletableFuture<String> apiKey = Hue.hueBridgeConnectionBuilder(bridgeIp).initializeApiConnection(appName);
// Push the button on your Hue Bridge to resolve the apiKey future:
final String key = apiKey.get();
System.out.println("Store this API key for future use: " + key);
final Hue hue = new Hue(bridgeIp, key);
```

### Using the rooms, lights, and scenes

#### A note on setting colors

When setting the color of a light or a room, one must use the `io.github.zeroone3010.yahueapi.Color` class.
There exists several ways to initialize the class using its
factory methods. `Color.of(int)` accepts a color code as an integer of the typical `0xRRGGBB` format.
You may get an integer like this from, for example, from the `java.awt.Color#getRGB()` method.
In Android environments you would use the `android.graphics.Color#toArgb()` method.
Note that in this case the alpha channel will be ignored, because a transparency value does not really
make sense in the context of lights. Alternatively, you may enter the color code as a six digit hexadecimal string
with the `Color.of(String)` method, as integer parts from 0 to 255 with the `Color.of(int, int, int)` method,
or as float parts from 0 to 1 with the `Color.of(float, float, float)` method.
Finally, you can just supply any sensible third party color object into the general `Color.of(Object)` factory method,
which will then attempt to parse it by finding its red, green and blue component methods using reflection.

In the pre-2.x.x versions of this library, one could set the color directly using `java.awt.Color` objects only.
This was all nice and fine, except for the fact that Android environments do not have that class at their disposal.

### Lights that belong to a room or a zone

[//]: # (requires-init)
[//]: # (import java.util.Optional;)
```java
// Get a room or a zone -- returns Optional.empty() if the room does not exist, but
// let's assume we know for a fact it exists and can do the .get() right away:
final Room room = hue.getRoomByName("Basement").get();
final Room zone = hue.getZoneByName("Route to the basement").get();

// Turn the lights on, make them pink:
room.setState(State.builder().color(Color.of(java.awt.Color.PINK)).on());

// Make the entire room dimly lit:
room.setBrightness(10);

// Turn off that single lamp in the corner:
room.getLightByName("Corner").get().turnOff();

// Turn one of the lights green. This also demonstrates the proper use of Optionals:
final Optional<Light> light = room.getLightByName("Ceiling 1");
light.ifPresent(l -> l.setState(State.builder().color(Color.of(java.awt.Color.GREEN.getRGB())).keepCurrentState()));

// Activate a scene:
room.getSceneByName("Tropical twilight").ifPresent(Scene::activate);
```

#### Lights that do not belong to a room or a zone

The lights that have not been assigned to any room or zone can be accessed with the `getUnassignedLights()`
and `getUnassignedLightByName(String)` methods
of the `Hue` object. For example, in order to turn on all the unassigned lights, one would do it like this:

[//]: # (requires-init)
[//]: # (import java.util.Collection;)
```java
final Collection<Light> lights = hue.getUnassignedLights();
lights.forEach(Light::turnOn);
```

### Caching

By default this library always queries the Bridge every time you query the state of a light, a room, or a sensor.
When querying the states of several items in quick succession, it is better to use caching. You can turn it on
by calling the `setCaching(true)` method of the `Hue` object. Subsequent `getState()` calls well *not* trigger a
query to the Bridge. Instead they will return the state that was current when caching was toggled on, or the last time
that the `refresh()` method of the `Hue` object was called. Toggling caching off by calling `setCaching(false)`
will direct subsequent state queries to the Bridge again. Caching is off by default. When toggling caching on/off
there is no need to get the `Light`, `Room` or `Sensor` from the `Hue` object again: you can keep using the same
object reference all the time. Objects that return a cached state will accept and execute state changes (calls to
the `setState` method) just fine, but they will *not* update their cached state with those calls.

### Switches

Switches include, for example, Philips Hue dimmer switchers, Philips Hue Tap switches, and various Friends of Hue switches.

[//]: # (requires-init)
```java
hue.getSwitches().forEach(s -> System.out.println(String.format("Switch: %s; last pressed button: #%d (%s) at %s",
    s.getName(),
    s.getLatestEvent().getAction().getEventType(),
    s.getLatestEvent().getButton().getNumber(),
    s.getLastUpdated())));
```

Depending on your setup, the above snippet will print something along the following lines:

```
Switch: Living room dimmer; last pressed button: #1 (SHORT_RELEASED) at 2021-01-05T12:30:33Z[UTC]
Switch: Kitcher dimmer; last pressed button: #2 (LONG_RELEASED) at 2021-01-05T06:13:18Z[UTC]
Switch: Hue tap switch 1; last pressed button: #4 (INITIAL_PRESS) at 2021-01-05T20:58:10Z[UTC]
```

Unfortunately the Hue Bridge does not allow applications to "listen" for button press events, so the Bridge will not "push"
any events to the library. Instead, one must unfortunately always just poll the statuses of the switches to find
out whether any button is pressed. This is a limitation of the Philips Hue system itself.

### Sensors

You can also use this library to read the states of various sensors in the Hue system. The main `Hue` class
contains methods for getting temperature sensors, presence sensors (i.e. motion sensors and geofence sensors),
daylight sensors, and ambient light sensors.

### Searching for new lights and adding them into rooms

There is a method in the `Hue` class that starts searching for new lights and returns a `Future` that will be
resolved with the found lights (if any) once the scan is finished. The scan seems to last around 45-60 seconds:

[//]: # (requires-init)
[//]: # (throws-InterruptedException|java.util.concurrent.ExecutionException)
```java
Future<Collection<Light>> lightSearch = hue.searchForNewLights();
Collection<Light> foundLights = lightSearch.get();
System.out.println("Lights found: " + foundLights);

// If new lights have been found, you can add them into a room:

hue.getRoomByName("Living Room").ifPresent(room -> foundLights.forEach(room::addLight));
```

If you do not wish to add the new lights into a room, they will be accessible with the `hue.getUnassignedLights()`
and `hue.getUnassignedLightByName(String)` methods.
It is also possible to remove lights from a room with the `room.removeLight(Light)` method.

Including the library using Maven or Gradle
--------------------------------

Add the following dependency to your pom.xml file if you are using Maven:

```xml
<dependency>
    <groupId>io.github.zeroone3010</groupId>
    <artifactId>yetanotherhueapi</artifactId>
    <version>2.7.0</version>
</dependency>
```

This is how you add it to your build.gradle file when using Gradle:

```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation 'io.github.zeroone3010:yetanotherhueapi:2.7.0'
}
```

Scope and philosophy
--------------------

This library is not intended to have all the possible functionality of the SDK
or the REST API. Instead it is focusing on the essentials: querying and setting
the states of the rooms and the lights. And this library should do those
essential functions well: in an intuitive and usable way for the programmer.
The number of external dependencies should be kept to a minimum.
Version numbering follows the [Semantic Versioning](https://semver.org/).

Contributing
------------

See [CONTRIBUTING.md](CONTRIBUTING.md).

Version history
---------------

See [CHANGELOG.md](CHANGELOG.md).

This project elsewhere
----------------------
* [Black Duck Open Hub](https://www.openhub.net/p/yetanotherhueapi)
* [Code Climate](https://codeclimate.com/github/ZeroOne3010/yetanotherhueapi)

Notes
-----

<sup>1</sup> Java 8, while old already, was chosen because it is easy to
install and run it on a Raspberry Pi computer. For the installation instructions,
see, for example, [this blog post](http://wp.brodzinski.net/raspberry-pi-3b/install-latest-java-8-raspbian/).
