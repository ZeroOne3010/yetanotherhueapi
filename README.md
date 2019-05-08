Yet Another Hue API
===================
[![Maven Central](https://img.shields.io/maven-central/v/io.github.zeroone3010/yetanotherhueapi.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.zeroone3010%22%20AND%20a:%22yetanotherhueapi%22)

This is a Java 8 API for the Philips Hue lights.<sup>1</sup> It does not use the official 
Hue SDK but instead accesses the REST API of the Philips Hue Bridge directly.
This library has been confirmed to work with the Philips Hue Bridge API version 1.28.0.

Usage
-----

First, import the classes from this library:

[//]: # (imports)
```java
import io.github.zeroone3010.yahueapi.*;
```

Then, if you already have an API key for your Bridge:

[//]: # (init)
```java
final String bridgeIp = "192.168.1.99"; // Fill in the IP address of your Bridge
final String apiKey = "bn4z908...34jf03jokaf4"; // Fill in an API key to access your Bridge
final Hue hue = new Hue(bridgeIp, apiKey);
```

If you don't have an API key for your bridge:

[//]: # (throws-InterruptedException|java.util.concurrent.ExecutionException)
```java
final String bridgeIp = "192.168.1.99"; // Fill in the IP address of your Bridge
final String appName = "MyFirstHueApp"; // Fill in the name of your application
final java.util.concurrent.CompletableFuture<String> apiKey = Hue.hueBridgeConnectionBuilder(bridgeIp).initializeApiConnection(appName);
// Push the button on your Hue Bridge to resolve the apiKey future:
final Hue hue = new Hue(bridgeIp, apiKey.get());
```

Using the rooms and the lights:

[//]: # (requires-init)
```java
// Get a room -- returns Optional.empty() if the room does not exist, but 
// let's assume we know for a fact it exists and can do the .get() right away:
final Room room = hue.getRoomByName("Basement").get();

// Turn the lights on, make them pink:
room.setState(State.builder().color(java.awt.Color.PINK).on());

// Turn off that single lamp in the corner:
room.getLightByName("Corner").get().turnOff();
```

### Including the library with Maven

Add the following dependency to your pom.xml file:

```xml
<dependency>
    <groupId>io.github.zeroone3010</groupId>
    <artifactId>yetanotherhueapi</artifactId>
    <version>1.0.0</version>
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

Notes
-----

<sup>1</sup> Java 8, while old already, was chosen because it is easy to 
install and run it on a Raspberry Pi computer. For the installation instructions,
see, for example, [this blog post](http://wp.brodzinski.net/raspberry-pi-3b/install-latest-java-8-raspbian/).
