Yet Another Hue API
===================
[![Maven Central](https://img.shields.io/maven-central/v/io.github.zeroone3010/yetanotherhueapi.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.zeroone3010%22%20AND%20a:%22yetanotherhueapi%22)

This is a Java 8 API for the Philips Hue lights.<sup>1</sup> It does not use the official 
Hue SDK but instead accesses the REST API of the Philips Hue Bridge directly.
This library has been confirmed to work with the Philips Hue Bridge API version 1.33.0.

Usage
-----

First, import the classes from this library:

[//]: # (imports)
```java
import io.github.zeroone3010.yahueapi.*;
import io.github.zeroone3010.yahueapi.discovery.*;
```

### Initializing the API with a connection to the Bridge

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

#### Bridge discovery (upcoming!)

If you do not know the IP address of the Bridge, in a future version of this library 
you will be able to use an automatic Bridge discovery method, like this (subject to change):

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
  // Then follow the code snippets above
}
```

### Using the rooms and the lights

[//]: # (requires-init)
[//]: # (import java.util.Optional;)
```java
// Get a room -- returns Optional.empty() if the room does not exist, but 
// let's assume we know for a fact it exists and can do the .get() right away:
final Room room = hue.getRoomByName("Basement").get();

// Turn the lights on, make them pink:
room.setState(State.builder().color(java.awt.Color.PINK).on());

// Make the entire room dimly lit:
room.setBrightness(10);

// Turn off that single lamp in the corner:
room.getLightByName("Corner").get().turnOff();

// Turn one of the lights green. This also demonstrates the proper use of Optionals:
final Optional<Light> light = room.getLightByName("Ceiling 1");
light.ifPresent(l -> l.setState(State.builder().color(java.awt.Color.GREEN).keepCurrentState()));
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

Including the library with Maven
--------------------------------

Add the following dependency to your pom.xml file:

```xml
<dependency>
    <groupId>io.github.zeroone3010</groupId>
    <artifactId>yetanotherhueapi</artifactId>
    <version>1.2.0</version>
</dependency>
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
