package io.github.zeroone3010.yahueapi.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * MDNS response parser for parsing MDNS responses from the Bridge when using the MDNS discovery protocol.
 */
final class MDNSResponseParser {
  private static final Logger logger = LoggerFactory.getLogger(MDNSResponseParser.class);

  private static final int EXPECTED_QUESTION_COUNT = 0x01;
  private static final int EXPECTED_ANSWER_COUNT = 0x01;
  private static final int A_RECORD_TYPE = 1;

  private int bytePointer = 0;
  private byte[] data;
  private List<String> expectedLabels;
  private int additionalResourceRecordsCount = 0;
  private Map<Integer, List<String>> names = new HashMap<>();
  private int expectedAnswerRdLength;

  public MDNSResponseParser(final DatagramPacket packet, final List<String> expectedLabels) {
    this.data = packet.getData();
    this.expectedLabels = Collections.unmodifiableList(expectedLabels);
  }

  /**
   * Parses an mDNS response and returns the IP address associated with the A record in it.
   *
   * @return IP address of an A record
   */
  public String parse() {
    // Headers:
    matchId();
    matchFlags();
    matchQuestionCount();
    matchAnswerCount();
    matchAuthorityResourceRecords();
    matchAdditionalResourceRecordsCount();

    // Questions:
    matchQuestionName();
    matchQuestionType();
    matchQuestionClass();

    // Answers:
    matchAnswerName();
    matchAnswerType();
    matchAnswerClass();
    matchAnswerTtl();
    matchAnswerRdLength();
    matchAnswerRData();
    final String ipAddress = matchAdditionalResourceRecords();
    return ipAddress;
  }

  private void matchId() {
    expect(data[bytePointer++], 0xbe);
    expect(data[bytePointer++], 0xef);
  }

  private void matchFlags() {
    expect(data[bytePointer++] & 0b10000000, 0b10000000); // "1": response. Skip opcode, AA, TC & RD.
    expect(data[bytePointer++] & 0b00001111, 0b00000000); // Skip RA and Z. "0000" at the end: no error
  }

  private void matchQuestionCount() {
    expect(data[bytePointer++], 0x00);
    expect(data[bytePointer++], EXPECTED_QUESTION_COUNT);
  }

  private void matchAnswerCount() {
    expect(data[bytePointer++], 0x00);
    expect(data[bytePointer++], EXPECTED_ANSWER_COUNT);
  }

  private void matchAuthorityResourceRecords() {
    bytePointer++;
    bytePointer++;
  }

  private void matchAdditionalResourceRecordsCount() {
    additionalResourceRecordsCount = data[bytePointer++] << 8 | data[bytePointer++];
    if (additionalResourceRecordsCount < 1) {
      throw new MDNSException("At least one additional resource record is expected");
    }
  }

  private void matchQuestionName() {
    matchNames(expectedLabels);
  }

  private void matchNames(final List<String> expected) {
    final int questionNameLocation = bytePointer;
    while (true) {
      final int labelLength = data[bytePointer];
      bytePointer++;
      if (labelLength == 0) {
        break;
      }
      final StringBuilder label = new StringBuilder();
      for (int i = 0; i < labelLength; i++) {
        label.append(Character.valueOf((char) data[bytePointer]));
        bytePointer++;
      }
      final List<String> labels = names.getOrDefault(questionNameLocation, new ArrayList<>());
      labels.add(label.toString());
      names.put(questionNameLocation, labels);
    }
    logger.debug("Names: " + names);
    final List<String> actualNames = names.get(questionNameLocation);
    if (expected != null && !Objects.equals(actualNames, expected)) {
      throw new MDNSException("Expected to see " + expected + " as labels, got " + actualNames + " instead.");
    }
  }

  private void matchQuestionType() {
    expect(data[bytePointer++], 0x00);
    expect(data[bytePointer++], 0x0c); // "PTR"
  }

  private void matchQuestionClass() {
    expect(data[bytePointer++], 0x00);
    expect(data[bytePointer++], 0xff); // "any"
  }

  private void matchAnswerName() {
    matchNamesInAnswer(expectedLabels);
  }

  private void matchNamesInAnswer(final List<String> expected) {
    if ((data[bytePointer] & 0b11000000) == 0b11000000) {
      // It's a compressed field, the next byte (and the six least significant bits of this byte) is a pointer
      bytePointer++;
      int pointer = ((data[bytePointer - 1] & 0b00111111) << 8) | data[bytePointer++];
      logger.debug("The answer has a pointer to " + pointer + " which means " + names.get(pointer));
    } else {
      matchNames(expected);
    }
  }

  private void matchAnswerType() {
    expect(data[bytePointer++], 0x00);
    expect(data[bytePointer++], 0x0c); // "PTR"
  }

  private void matchAnswerClass() {
    expect(data[bytePointer++], 0x00);
    expect(data[bytePointer++], 0x01); // "IN", internet address
  }

  private void matchAnswerTtl() {
    final int ttl = data[bytePointer++] << 24 | data[bytePointer++] << 16 | data[bytePointer++] << 8 | data[bytePointer++];
    if (ttl == 0) {
      throw new MDNSException("TTL found to be 0");
    }
  }

  private void matchAnswerRdLength() {
    expectedAnswerRdLength = (data[bytePointer++] << 8) | data[bytePointer++];
    if (expectedAnswerRdLength < 1) {
      throw new MDNSException("Answer length is unacceptable, " + expectedAnswerRdLength);
    }
  }

  private void matchAnswerRData() {
    // Skip this part, advance to the additional records where the A record should exist:
    bytePointer += expectedAnswerRdLength;
  }

  private String matchAdditionalResourceRecords() {
    for (int i = 0; i < additionalResourceRecordsCount; i++) {
      final String ipAddress = readAdditionalResourceAndReturnIfARecord();
      if (ipAddress != null) {
        return ipAddress;
      }
    }
    return null;
  }

  private String readAdditionalResourceAndReturnIfARecord() {
    matchNamesInAnswer(null);
    final int type = data[bytePointer++] << 8 | data[bytePointer++];
    if (type != A_RECORD_TYPE) {
      bytePointer += 2; // Skip answer class
      bytePointer += 4; // Skip TTL
      final int dataLength = data[bytePointer++] << 8 | data[bytePointer++];
      bytePointer += dataLength; // Skip data
      return null;
    }
    matchAnswerClass();
    matchAnswerTtl();
    expect(data[bytePointer++], 0x00); // Expect 'data length' to be 4 bytes,
    expect(data[bytePointer++], 0x04); // i.e. an IPv4 address

    return (data[bytePointer++] & 0xFF) + "." +
        (data[bytePointer++] & 0xFF) + "." +
        (data[bytePointer++] & 0xFF) + "." +
        (data[bytePointer++] & 0xFF);
  }

  private void expect(final int value, final int expectedValue) {
    final int val = value & 0xff;
    if (val != expectedValue) {
      throw new MDNSException(String.format("Expected '%02x', was '%02x' at %d", expectedValue, val, bytePointer - 1));
    }
  }
}
