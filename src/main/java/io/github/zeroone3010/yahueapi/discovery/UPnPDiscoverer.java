package io.github.zeroone3010.yahueapi.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class UPnPDiscoverer implements HueBridgeDiscovererAsync {
  private static final Logger logger = Logger.getLogger("UPnPDiscoverer");

  private static final long SSDP_REQUEST_TIMER_INTERVAL_SECONDS = 1;
  private static InetAddress multicastAddress;
  private static final int PORT = 1900;
  private MulticastSocket socket;
  private ScheduledExecutorService ssdpRequestSender;
  private ScheduledFuture requestSendTask;
  private final DatagramPacket requestPacket;
  private final Set<String> ips = new HashSet<>();
  private DiscoverState state = DiscoverState.IDLE;

  public UPnPDiscoverer() {
    try {
      multicastAddress = InetAddress.getByName("239.255.255.250");
    } catch (final UnknownHostException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    requestPacket = createRequestPacket();
  }

  @Override
  public final void run() {
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
        if (state == DiscoverState.STOPPED) {
          return;
        } else {
          e.printStackTrace();
        }
      }
      packetHandler(packet);
    }
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
      ssdpRequestSender = Executors.newSingleThreadScheduledExecutor();
      requestSendTask = ssdpRequestSender.scheduleAtFixedRate(() -> {
        try {
          logger.info("Sending discover message");
          socket.send(requestPacket);
        } catch (final IOException e) {
          state = DiscoverState.CRASHED;
          e.printStackTrace();
        }
      }, 0, SSDP_REQUEST_TIMER_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }
  }

  public final void stop() {
    state = DiscoverState.STOPPED;
    if (requestSendTask != null) {
      ssdpRequestSender.shutdown();
    }
    if (socket != null && !socket.isClosed()) {
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
        if (ips.add(ip)) {
          onBridgeDiscovered(ip);
        }
      }
    }
  }

  public final Set<String> getIps() {
    return new HashSet<>(this.ips);
  }

  private DatagramPacket createRequestPacket() {
    final StringBuilder sb = new StringBuilder("M-SEARCH * HTTP/1.1\r\n")
        .append("HOST: " + multicastAddress.getHostAddress() + ":" + PORT + "\r\n")
        .append("MAN: ssdp:discover\r\n")
        .append("MX: 3\r\n")
        .append("USER-AGENT: Yet Another Hue API\r\n")
        .append("ST: ssdp:all\r\n");
    final byte[] content = sb.toString().getBytes(UTF_8);
    return new DatagramPacket(content, content.length, multicastAddress, PORT);
  }
}
