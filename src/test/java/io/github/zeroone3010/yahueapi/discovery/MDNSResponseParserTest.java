package io.github.zeroone3010.yahueapi.discovery;

import org.junit.jupiter.api.Test;

import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class MDNSResponseParserTest {
  private static final String MDNS_RESPONSE = "be ef 84 00 00 01 " +
      "00 01 00 00 00 07 04 5f 68 75 65 04 5f 74 63 70 " +
      "05 6c 6f 63 61 6c 00 00 0c 00 ff c0 0c 00 0c 00 " +
      "01 00 00 00 0a 00 17 14 50 68 69 6c 69 70 73 20 " +
      "48 75 65 20 2d 20 32 35 42 41 45 39 c0 0c c0 2d " +
      "00 21 00 01 00 00 00 0a 00 14 00 00 00 00 01 bb " +
      "0b 50 68 69 6c 69 70 73 2d 68 75 65 c0 16 c0 2d " +
      "00 10 00 01 00 00 00 0a 00 29 19 62 72 69 64 67 " +
      "65 69 64 3d 30 30 31 37 38 38 66 66 66 65 32 35 " +
      "62 61 65 39 0e 6d 6f 64 65 6c 69 64 3d 42 53 42 " +
      "30 30 32 c0 56 00 01 00 01 00 00 00 0a 00 04 c0 " + // The IP address "192.168.8.164" is the
      "a8 08 a4 c0 56 00 1c 00 01 00 00 00 0a 00 10 fd " + // "c0 a8 08 a4" part on these lines.
      "be 38 d1 6c ba 00 00 00 00 00 00 00 00 00 01 c0 " +
      "56 00 1c 00 01 00 00 00 0a 00 10 fe 80 00 00 00 " +
      "00 00 00 02 17 88 ff fe 25 ba e9 c0 56 00 1c 00 " +
      "01 00 00 00 0a 00 10 20 01 09 99 02 72 c0 4f 02 " +
      "17 88 ff fe 25 ba e9 c0 56 00 1c 00 01 00 00 00 " +
      "0a 00 10 fd 04 d3 b5 9e 3d 7e 00 02 17 88 ff fe " +
      "25 ba e9";

  private static byte[] stringToBytes(final String byteString) {
    final Object[] s = Arrays.stream(
        byteString.toUpperCase(Locale.ROOT).split(" ")
    ).map(b -> (byte) Integer.parseInt(b, 16)).toArray();
    final byte[] bytes = new byte[s.length];
    for (int i = 0; i < s.length; i++) {
      bytes[i] = (Byte) s[i];
    }
    return bytes;
  }

  @Test
  void parse() {
    final byte[] bytes = stringToBytes(MDNS_RESPONSE);
    final DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length);

    final MDNSResponseParser parser = new MDNSResponseParser(packet, Arrays.asList("_hue", "_tcp", "local"));
    final String result = parser.parse();

    assertEquals("192.168.8.164", result);
  }

  @Test
  void parseDifferentIpAddress() {
    final byte[] bytes = stringToBytes(MDNS_RESPONSE.replace("c0 a8 08 a4", "01 02 03 04"));
    final DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length);

    final MDNSResponseParser parser = new MDNSResponseParser(packet, Arrays.asList("_hue", "_tcp", "local"));
    final String result = parser.parse();

    assertEquals("1.2.3.4", result);
  }

  @Test
  void failBecauseOfUnknownMessageId() {
    final byte[] bytes = stringToBytes(MDNS_RESPONSE.replace("be ef", "ff ff"));
    final DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length);

    final MDNSResponseParser parser = new MDNSResponseParser(packet, Arrays.asList("_hue", "_tcp", "local"));
    try {
      parser.parse();
      fail("There should have been an exception");
    } catch (final MDNSException expected) {
      assertEquals("Expected 'be', was 'ff' at 0", expected.getMessage());
    }
  }

  @Test
  void failBecauseOfZeroTtl() {
    final byte[] bytes = stringToBytes(MDNS_RESPONSE.replace("00 00 00 0a", "00 00 00 00"));
    final DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length);

    final MDNSResponseParser parser = new MDNSResponseParser(packet, Arrays.asList("_hue", "_tcp", "local"));
    try {
      parser.parse();
      fail("There should have been an exception");
    } catch (final MDNSException expected) {
      assertEquals("TTL found to be 0", expected.getMessage());
    }
  }

  @Test
  void returnsNullResultWhenNoARecordFound() {
    final byte[] bytes = stringToBytes(MDNS_RESPONSE.replace("c0 56 00 01", "c0 56 00 02"));
    final DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length);

    final MDNSResponseParser parser = new MDNSResponseParser(packet, Arrays.asList("_hue", "_tcp", "local"));
    assertNull(parser.parse());
  }

  @Test
  void failBecauseOfNoMatchingNamesFound() {
    final byte[] bytes = stringToBytes(MDNS_RESPONSE);
    final DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length);

    final MDNSResponseParser parser = new MDNSResponseParser(packet, Arrays.asList("_appletv", "_tcp", "local"));
    try {
      parser.parse();
      fail("There should have been an exception");
    } catch (final MDNSException expected) {
      assertEquals("Expected to see [_appletv, _tcp, local] as labels, got [_hue, _tcp, local] instead.", expected.getMessage());
    }
  }
}
