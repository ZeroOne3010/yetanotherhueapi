package io.github.zeroone3010.yahueapi.discovery;

import io.github.zeroone3010.yahueapi.HueBridge;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UPnPDiscovererTest {

  @Test
  void testDiscovery() throws Exception {
    final TestServer testServer = new TestServer();
    testServer.start();

    final List<HueBridge> foundBridges = new ArrayList<>();

    final UPnPDiscoverer discoverer = new UPnPDiscoverer(
        "localhost", foundBridges::add);
    discoverer.discoverBridges().get();

    assertEquals(1, foundBridges.size());
    assertEquals(new HueBridge("12.34.56.7"), foundBridges.get(0));
  }


  public class TestServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public TestServer() throws SocketException, UnknownHostException {
      socket = new DatagramSocket(19001, InetAddress.getByName("localhost"));
    }

    public void run() {
      running = true;

      while (running) {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
          socket.receive(packet);
        } catch (final IOException e) {
          throw new RuntimeException(e);
        }

        final InetAddress address = packet.getAddress();
        final int port = packet.getPort();
        packet = new DatagramPacket(buf, buf.length, address, port);
        final String received = new String(packet.getData(), 0, packet.getLength());

        if (received.startsWith("M-SEARCH * HTTP/1.1\r\n") && received.contains("USER-AGENT: Yet Another Hue API")) {
          try {
            final String response = "HTTP/1.1 200 OK\r\n" +
                "HOST: localhost:1900\r\n" +
                "EXT:\r\n" +
                "CACHE-CONTROL: max-age=100\r\n" +
                "LOCATION: http://12.34.56.7:80/description.xml\r\n" +
                "SERVER: Linux/3.14.0 UPnP/1.0 IpBridge/1.35.0\r\n" +
                "hue-bridgeid: 001122334455ff\r\n" +
                "ST: upnp:rootdevice\r\n" +
                "USN: uuid:11223344-5566-7777-8888-abcdef111111::upnp:rootdevice\r\n";
            final DatagramPacket reply = new DatagramPacket(response.getBytes(UTF_8), response.length(),
                InetAddress.getByName("localhost"), packet.getPort());
            socket.send(reply);
          } catch (final IOException e) {
            throw new RuntimeException(e);
          }
          running = false;
        }
      }
      socket.close();
    }
  }
}
