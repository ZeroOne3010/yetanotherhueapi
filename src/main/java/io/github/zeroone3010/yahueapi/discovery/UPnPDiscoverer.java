package io.github.zeroone3010.yahueapi.discovery;

import io.github.zeroone3010.yahueapi.HueBridge;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Discovers Hue Bridges using the UPnP protocol, i.e. by sending out Simple Service Discovery Protocol (SSDP)
 * packets and waiting for any Bridges to respond.
 */
public final class UPnPDiscoverer implements HueBridgeDiscoverer {
  private static final Logger logger = Logger.getLogger("UPnPDiscoverer");

  private static final int DISCOVERY_MESSAGE_COUNT = 5;
  private static final int PORT = 1900;
  private static final long MILLISECONDS_BETWEEN_DISCOVERY_MESSAGES = 950L;

  private final InetAddress multicastAddress;
  private MulticastSocket socket;
  private ScheduledExecutorService scheduledExecutorService;
  private ScheduledFuture requestSendTask;
  private final DatagramPacket requestPacket;
  private final Collection<HueBridge> bridges = new HashSet<>();
  private DiscoverState state = DiscoverState.IDLE;
  private final Consumer<HueBridge> discoverer;

  public UPnPDiscoverer(final Consumer<HueBridge> discoverer) {
    this.discoverer = discoverer;
    try {
      multicastAddress = InetAddress.getByName("239.255.255.250");
    } catch (final UnknownHostException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    requestPacket = createRequestPacket();
  }

  @Override
  public CompletableFuture<List<HueBridge>> discoverBridges() {
    return CompletableFuture.supplyAsync(() -> {
          try {
            startSocket();
            scheduleMessages();
            if (state == DiscoverState.IDLE) {
              state = DiscoverState.SEARCHING;
            }
            while (state == DiscoverState.SEARCHING) {
              final byte[] buffer = new byte[8192];
              final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
              try {
                socket.receive(packet);
              } catch (final IOException e) {
                // socket.receive(DatagramPacket) will throw a SocketException once the socket is closed,
                // but then the state is also set to STOPPED, so this is a valid exit point for this method.
                if (state == DiscoverState.STOPPED) {
                  return new ArrayList<>(bridges);
                } else {
                  e.printStackTrace();
                }
              }
              packetHandler(packet);
            }
          } finally {
            stop();
          }
          return new ArrayList<>(bridges);
        }
    );
  }

  private void startSocket() {
    if (state == DiscoverState.IDLE) {
      try {
        socket = new MulticastSocket();
        TimeUnit.MILLISECONDS.sleep(100L);
        state = DiscoverState.SEARCHING;
      } catch (final Exception e) {
        state = DiscoverState.CRASHED;
        e.printStackTrace();
      }
    }
  }

  private void scheduleMessages() {
    if (state == DiscoverState.SEARCHING) {
      scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
      requestSendTask = scheduledExecutorService.schedule(() -> {
        try {
          for (int i = 0; i < DISCOVERY_MESSAGE_COUNT; i++) {
            logger.info("Sending discover message");
            socket.send(requestPacket);
            TimeUnit.MILLISECONDS.sleep(MILLISECONDS_BETWEEN_DISCOVERY_MESSAGES);
          }
        } catch (final IOException | InterruptedException e) {
          state = DiscoverState.CRASHED;
          e.printStackTrace();
        } finally {
          stop();
        }
      }, 0, TimeUnit.SECONDS);
    }
  }

  private void stop() {
    state = DiscoverState.STOPPED;
    if (requestSendTask != null) {
      scheduledExecutorService.shutdown();
    }
    if (socket != null) {
      socket.close();
    }
  }

  private void packetHandler(final DatagramPacket packet) {
    final String data = new String(packet.getData());
    if (data.contains("IpBridge")) {
      final int startIndex = data.indexOf("http://");
      final int endIndex = data.indexOf("/description.xml");
      if (startIndex != -1 && endIndex != -1) {
        String ip = data.substring(startIndex + 7, endIndex);
        final int portIndex = ip.indexOf(':');
        if (portIndex > -1) {
          ip = ip.substring(0, portIndex);
        }
        final HueBridge bridge = new HueBridge(ip);
        if (bridges.add(bridge)) {
          discoverer.accept(bridge);
        }
      }
    }
  }

  private DatagramPacket createRequestPacket() {
    final StringBuilder sb = new StringBuilder("M-SEARCH * HTTP/1.1\r\n")
        .append("HOST: ").append(multicastAddress.getHostAddress()).append(':').append(PORT).append("\r\n")
        .append("MAN: ssdp:discover\r\n")
        .append("MX: 3\r\n")
        .append("USER-AGENT: Yet Another Hue API\r\n")
        .append("ST: ssdp:all\r\n");
    final byte[] content = sb.toString().getBytes(UTF_8);
    return new DatagramPacket(content, content.length, multicastAddress, PORT);
  }
}
