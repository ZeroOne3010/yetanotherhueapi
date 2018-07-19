Yet Another Hue API
===================

This is a Java 8 API for the Philips Hue lights. It does not use the official 
Hue SDK but instead accesses the REST API of the Philips Hue Bridge directly.

This library can be used in two ways: 
1. As a library, included in another project
2. As a stand-alone software, with a very simple and very limited command line interface

Usage
-----

### As a library 

If you already have an API key for your Bridge:

```java
final String bridgeIp = "192.168.1.99"; // Fill in the IP address of your Bridge
final String apiKey = "bn4z908...34jf03jokaf4"; // Fill in an API key to access your Bridge
final Hue hue = new Hue(bridgeIp, apiKey);
```

If you don't have an API key for your bridge:
```java
final String bridgeIp = "192.168.1.99"; // Fill in the IP address of your Bridge
final String appName = "MyFirstHueApp"; // Fill in the name of your application
final CompletableFuture<String> apiKey = Hue.hueBridgeConnectionBuilder(bridgeIp).initializeApiConnection(appName);
// Push the button on your Hue Bridge to resolve the apiKey future:
final Hue hue = new Hue(bridgeIp, apiKey.get());
```

Using the rooms and the lights:

```java
// Get a room -- returns Optional.empty() if the room does not exist, but 
// let's assume we know for a fact it exists and can do the .get() right away:
final IRoom room = hue.getRoomByName("Basement").get();

// Turn the lights on, make them bright and pink:
room.setState(new GroupAction(true, 254, java.awt.Color.PINK));

// Turn off that single lamp in the corner:
room.getLightByName("Corner").get().turnOff();
```

#### Including the library with Maven

Add the following to your pom.xml file:

```xml
    <repositories>
        <repository>
            <id>ZeroOne3010-snapshots</id>
            <url>https://github.com/ZeroOne3010/mvn-repo/raw/master/snapshots</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.zeroone3010</groupId>
            <artifactId>yetanotherhueapi</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

### As stand-alone software

Enter the Bridge IP as the first command line argument. 
Enter the API key as the second command line argument.

Use the following environment variables to create simple conditional actions:
* `ifAnyOn`
* `thenRoom`
* `state`

For example, to set a red color to Bedroom when there are
any lights on in the Hallway, use these parameters:

```
-DifAnyOn=Hallway 
-DthenRoom=Bedroom 
-Dstate={\"on\":true,\"color\":\"FF0000\"}
```

A complete example of a command line:

```
java -DifAnyOn="Hallway" -DthenRoom="Bedroom" -Dstate="{\"on\":true,\"color\":\"FF0000\"}" -jar yetanotherhueapi-0.0.1-jar-with-dependencies.jar 192.168.1.99 d3908jOKd208jLKJaD8jd2l2djlkncbbMBN39918
```

Scope and philosophy
--------------------

This library is not intended to have all the possible functionality of the SDK
or the REST API. Instead it is focusing on the essentials: querying and setting
the states of the rooms and the lights. And this library should do those 
essential functions well: in an intuitive and usable way for the programmer.
The number of external dependencies should also be kept to a minimum.