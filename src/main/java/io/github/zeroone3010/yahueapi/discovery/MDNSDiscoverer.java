package io.github.zeroone3010.yahueapi.discovery;

import io.github.zeroone3010.yahueapi.HueBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Discovers Hue Bridges using the mDNS protocol, i.e. by sending out multicast DNS queries
 * and waiting for any Bridges to respond.
 */
final class MDNSDiscoverer implements HueBridgeDiscoverer {
  private static final Logger logger = LoggerFactory.getLogger(MDNSDiscoverer.class);

  private static final int DISCOVERY_MESSAGE_COUNT = 5;
  private static final int PORT = 5353;
  private static final long MILLISECONDS_BETWEEN_DISCOVERY_MESSAGES = 950L;

  private final InetAddress multicastAddress;
  private MulticastSocket socket;
  private ScheduledExecutorService scheduledExecutorService;
  private ScheduledFuture requestSendTask;
  private final DatagramPacket requestPacket;
  private DiscoverState state = DiscoverState.IDLE;
  private final Consumer<HueBridge> discoverer;

  MDNSDiscoverer(final Consumer<HueBridge> discoverer) {
    this.discoverer = discoverer;
    try {
      multicastAddress = InetAddress.getByName("224.0.0.251");
    } catch (final UnknownHostException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    requestPacket = createRequestPacket();
  }

  @Override
  public CompletableFuture<Void> discoverBridges() {
    return CompletableFuture.supplyAsync(() -> {
          try {
            startSocket();
            scheduleMessages();
            if (state == DiscoverState.IDLE) {
              state = DiscoverState.SEARCHING;
            }
            logger.info("mDNS discoverer started");
            while (state == DiscoverState.SEARCHING) {
              final byte[] buffer = new byte[8192];
              final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
              try {
                socket.receive(packet);
              } catch (final Exception e) {
                if (state == DiscoverState.STOPPED) {
                  // socket.receive(DatagramPacket) will throw a SocketException once the socket is closed,
                  // but then the state is also set to STOPPED, so this is a valid exit point for this method.
                  return null;
                } else {
                  e.printStackTrace();
                }
              }
              packetHandler(packet);
            }
          } finally {
            stop();
          }
          return null;
        }
    );
  }

  private void startSocket() {
    if (state == DiscoverState.IDLE) {
      try {
        socket = new MulticastSocket();
        socket.setTimeToLive(100);
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
            logger.info("Sending a discovery message");
            socket.send(requestPacket);
            TimeUnit.MILLISECONDS.sleep(MILLISECONDS_BETWEEN_DISCOVERY_MESSAGES);
          }
        } catch (final IOException | InterruptedException e) {
          state = DiscoverState.CRASHED;
          e.printStackTrace();
        } catch (final MDNSException e) {
          logger.warn(e.getMessage());
        } finally {
          stop();
        }
      }, 0, TimeUnit.MILLISECONDS);
    }
  }

  private void stop() {
    logger.info("mDNS discoverer stopped");
    state = DiscoverState.STOPPED;
    if (requestSendTask != null) {
      requestSendTask.cancel(true);
      scheduledExecutorService.shutdown();
    }
    if (socket != null) {
      socket.close();
    }
  }

  private void packetHandler(final DatagramPacket packet) {
    final MDNSResponseParser parser = new MDNSResponseParser(packet, Arrays.asList("_hue", "_tcp", "local"));
    final String ip = parser.parse();
    logger.debug("Got MDNS response '{}' from '{}'", ip, packet.getAddress());
    discoverer.accept(new HueBridge(ip));
  }

  private DatagramPacket createRequestPacket() {
    final byte[] content = new byte[]{
        (byte) 0xBE, (byte) 0xEF, // transaction ID
        0x00, 0x00, // flags
        0x00, 0x01, // number of questions
        0x00, 0x00, // number of answers
        0x00, 0x00, // number of authority resource records
        0x00, 0x00, // number of additional resource records
        0x04, '_', 'h', 'u', 'e',
        0x04, '_', 't', 'c', 'p',
        0x05, 'l', 'o', 'c', 'a', 'l',
        0x00, // terminator
        0x00, 0x0C, // type
        0x00, (byte) 0xFF  // class
    };
    return new DatagramPacket(content, content.length, multicastAddress, PORT);
  }
}
