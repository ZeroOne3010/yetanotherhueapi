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

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class UpnpDiscoverer implements HueBridgeDiscovererAsync{

  private static final long SSDP_REQUEST_TIMER = 10;
  private static InetAddress multicastAddress;
  private static final int port = 1900;
  private MulticastSocket socket;
  private ScheduledExecutorService ssdpRequestSender;
  private ScheduledFuture requestSendTask;
  private final DatagramPacket requestPacket;
  private final Set<String> ips = new HashSet<>();
  private DiscoverState state = DiscoverState.IDLE;

  public UpnpDiscoverer() throws IOException {
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
      byte[] buffer = new byte[8192];
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      try {
        socket.receive(packet);
      } catch (IOException e) {
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
        System.out.println("sending discover message");
        try {
          socket.send(requestPacket);
        } catch (IOException e) {
          state = DiscoverState.CRASHED;
          e.printStackTrace();
        }
      }, 0, SSDP_REQUEST_TIMER, TimeUnit.SECONDS);
    }
  }

  public void stop() {
    state = DiscoverState.STOPPED;
    if (requestSendTask != null) {
      ssdpRequestSender.shutdown();
    }
    if ( socket != null && !socket.isClosed()) {
      socket.close();
    }
  }

  private void packetHandler(DatagramPacket packet) {
    String data = new String(packet.getData());
    if (data.contains("IpBridge")) {
      int startIndex = data.indexOf("http://");
      int endIndex = data.indexOf("/description.xml");
      if (startIndex != -1 && endIndex != -1) {
        String ip = data.substring(startIndex + 7, endIndex);
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
    StringBuilder sb = new StringBuilder("M-SEARCH * HTTP/1.1\r\n");
    sb.append("HOST: " + multicastAddress.getHostAddress() + ":" + port + "\r\n");
    sb.append("MAN: ssdp:discover\r\n");
    sb.append("MX: 3\r\n");
    sb.append("USER-AGENT: Resourcepool SSDP Client\r\n");
    sb.append("ST: ssdp:all\r\n");
    byte[] content = sb.toString().getBytes(UTF_8);
    return new DatagramPacket(content, content.length, multicastAddress, port);
  }
}
