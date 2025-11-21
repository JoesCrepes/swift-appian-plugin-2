package com.appiancs.swiftmtconverter.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.mt.AbstractMT;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for SwiftMessageParser utility class.
 */
class SwiftMessageParserTest {

  private static final String MT103_SAMPLE_PATH = "src/test/resources/sample-mt103.txt";
  private static final String MT202_SAMPLE_PATH = "src/test/resources/sample-mt202.txt";

  @Test
  void testParseToAbstractMT_ValidMT103() throws IOException {
    String mt103Message = new String(Files.readAllBytes(Paths.get(MT103_SAMPLE_PATH)));

    AbstractMT result = SwiftMessageParser.parseToAbstractMT(mt103Message);

    assertNotNull(result);
    assertEquals("103", result.getMessageType());
  }

  @Test
  void testParseToAbstractMT_ValidMT202() throws IOException {
    String mt202Message = new String(Files.readAllBytes(Paths.get(MT202_SAMPLE_PATH)));

    AbstractMT result = SwiftMessageParser.parseToAbstractMT(mt202Message);

    assertNotNull(result);
    assertEquals("202", result.getMessageType());
  }

  @Test
  void testParseToAbstractMT_NullMessage() {
    Exception exception = assertThrows(
      IllegalArgumentException.class,
      () -> SwiftMessageParser.parseToAbstractMT(null)
    );

    assertTrue(exception.getMessage().contains("cannot be null or empty"));
  }

  @Test
  void testParseToAbstractMT_EmptyMessage() {
    Exception exception = assertThrows(
      IllegalArgumentException.class,
      () -> SwiftMessageParser.parseToAbstractMT("")
    );

    assertTrue(exception.getMessage().contains("cannot be null or empty"));
  }

  @Test
  void testParseToAbstractMT_InvalidMessage() {
    Exception exception = assertThrows(
      IllegalArgumentException.class,
      () -> SwiftMessageParser.parseToAbstractMT("INVALID SWIFT MESSAGE")
    );

    assertTrue(exception.getMessage().contains("Failed to parse"));
  }

  @Test
  void testParseToSwiftMessage_ValidMT103() throws IOException {
    String mt103Message = new String(Files.readAllBytes(Paths.get(MT103_SAMPLE_PATH)));

    SwiftMessage result = SwiftMessageParser.parseToSwiftMessage(mt103Message);

    assertNotNull(result);
    assertNotNull(result.getBlock4());
  }

  @Test
  void testParseWithTypeValidation_MatchingType() throws IOException {
    String mt103Message = new String(Files.readAllBytes(Paths.get(MT103_SAMPLE_PATH)));

    AbstractMT result = SwiftMessageParser.parseWithTypeValidation(mt103Message, "103");

    assertNotNull(result);
    assertEquals("103", result.getMessageType());
  }

  @Test
  void testParseWithTypeValidation_MatchingTypeWithMTPrefix() throws IOException {
    String mt103Message = new String(Files.readAllBytes(Paths.get(MT103_SAMPLE_PATH)));

    AbstractMT result = SwiftMessageParser.parseWithTypeValidation(mt103Message, "MT103");

    assertNotNull(result);
    assertEquals("103", result.getMessageType());
  }

  @Test
  void testParseWithTypeValidation_MismatchedType() throws IOException {
    String mt103Message = new String(Files.readAllBytes(Paths.get(MT103_SAMPLE_PATH)));

    Exception exception = assertThrows(
      IllegalArgumentException.class,
      () -> SwiftMessageParser.parseWithTypeValidation(mt103Message, "202")
    );

    assertTrue(exception.getMessage().contains("type mismatch"));
    assertTrue(exception.getMessage().contains("Expected MT202"));
    assertTrue(exception.getMessage().contains("got MT103"));
  }

  @Test
  void testGetMessageType_MT103() throws IOException {
    String mt103Message = new String(Files.readAllBytes(Paths.get(MT103_SAMPLE_PATH)));

    String messageType = SwiftMessageParser.getMessageType(mt103Message);

    assertEquals("103", messageType);
  }

  @Test
  void testGetMessageType_MT202() throws IOException {
    String mt202Message = new String(Files.readAllBytes(Paths.get(MT202_SAMPLE_PATH)));

    String messageType = SwiftMessageParser.getMessageType(mt202Message);

    assertEquals("202", messageType);
  }
}
