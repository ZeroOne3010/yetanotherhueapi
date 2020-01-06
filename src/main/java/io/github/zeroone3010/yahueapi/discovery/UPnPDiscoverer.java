package io.github.zeroone3010.yahueapi.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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

  private static final long SSDP_REQUEST_TIMER_INTERVAL_SECONDS = 10;
  private static InetAddress multicastAddress;
  private static final int PORT = 1900;
  private MulticastSocket socket;
  private ScheduledExecutorService ssdpRequestSender;
  private ScheduledFuture requestSendTask;
  private final DatagramPacket requestPacket;
  private final Set<String> ips = new HashSet<>();
  private DiscoverState state = DiscoverState.IDLE;

  public UPnPDiscoverer() throws IOException {
    multicastAddress = InetAddress.getByName("239.255.255.250");
    requestPacket = createRequestPacket();
  }

  @Override
  public void run() {
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
        state = DiscoverState.SEARCHING;
      } catch (IOException e) {
        state = DiscoverState.CRASHED;
        e.printStackTrace();
      }
    }
  }

  private void scheduleMessages() {
    if (state == DiscoverState.SEARCHING) {
      ssdpRequestSender = Executors.newSingleThreadScheduledExecutor();
      requestSendTask = ssdpRequestSender.scheduleAtFixedRate(() -> {
        logger.info("Sending discover message");
        try {
          socket.send(requestPacket);
        } catch (IOException e) {
          state = DiscoverState.CRASHED;
          e.printStackTrace();
        }
      }, 0, SSDP_REQUEST_TIMER_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }
  }

  public void stop() {
    state = DiscoverState.STOPPED;
    if (requestSendTask != null) {
      ssdpRequestSender.shutdown();
    }
    if (socket != null && !socket.isClosed()) {
      socket.close();
    }
  }

  private void packetHandler(DatagramPacket packet) {
    final String data = new String(packet.getData());
    if (data.contains("IpBridge")) {
      final int startIndex = data.indexOf("http://");
      final int endIndex = data.indexOf("/description.xml");
      if (startIndex != -1 && endIndex != -1) {
        final String ip = data.substring(startIndex + 7, endIndex);
        if (ips.add(ip)) {
          onBridgeDiscovered(ip);
        }
      }
    }
  }

  public Set<String> getIps() {
    return new HashSet<>(this.ips);
  }

  private DatagramPacket createRequestPacket() {
    final StringBuilder sb = new StringBuilder("M-SEARCH * HTTP/1.1\r\n");
    sb.append("HOST: " + multicastAddress.getHostAddress() + ":" + PORT + "\r\n");
    sb.append("MAN: ssdp:discover\r\n");
    sb.append("MX: 3\r\n");
    sb.append("USER-AGENT: Resourcepool SSDP Client\r\n");
    sb.append("ST: ssdp:all\r\n");
    final byte[] content = sb.toString().getBytes(UTF_8);
    return new DatagramPacket(content, content.length, multicastAddress, PORT);
  }
}
