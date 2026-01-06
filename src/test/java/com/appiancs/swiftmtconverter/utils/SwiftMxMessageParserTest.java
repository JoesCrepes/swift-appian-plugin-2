package com.appiancs.swiftmtconverter.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.prowidesoftware.swift.model.mx.AbstractMX;

/**
 * Unit tests for SwiftMxMessageParser utility class.
 */
class SwiftMxMessageParserTest {

  private static final String SAMPLE_PACS_008 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pacs.008.001.08\">\n" + "  <FIToFICstmrCdtTrf>\n" + "    <GrpHdr>\n" +
    "      <MsgId>MSGID001</MsgId>\n" + "      <CreDtTm>2024-01-01T12:00:00</CreDtTm>\n" + "      <NbOfTxs>1</NbOfTxs>\n" +
    "      <SttlmInf>\n" + "        <SttlmMtd>INDA</SttlmMtd>\n" + "      </SttlmInf>\n" + "    </GrpHdr>\n" + "  </FIToFICstmrCdtTrf>\n" +
    "</Document>";

  @Test
  void testParseToAbstractMX_ValidPacs008() {
    AbstractMX parsedMessage = SwiftMxMessageParser.parseToAbstractMX(SAMPLE_PACS_008);

    assertNotNull(parsedMessage);
    assertEquals("pacs.008.001.08", parsedMessage.getMxId().id());
  }

  @Test
  void testParseToAbstractMX_NullInput() {
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> SwiftMxMessageParser.parseToAbstractMX(null));

    assertEquals("SWIFT MX message cannot be null or empty", exception.getMessage());
  }

  @Test
  void testParseToAbstractMX_EmptyInput() {
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> SwiftMxMessageParser.parseToAbstractMX(""));

    assertEquals("SWIFT MX message cannot be null or empty", exception.getMessage());
  }

  @Test
  void testParseToAbstractMX_InvalidXml() {
    String invalidXml = "This is not valid XML";

    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> SwiftMxMessageParser.parseToAbstractMX(invalidXml));

    assertNotNull(exception.getMessage());
  }

  @Test
  void testParseWithTypeValidation_MatchingType() {
    AbstractMX parsedMessage = SwiftMxMessageParser.parseWithTypeValidation(
      SAMPLE_PACS_008,
      "pacs.008.001.08");

    assertNotNull(parsedMessage);
    assertEquals("pacs.008.001.08", parsedMessage.getMxId().id());
  }

  @Test
  void testParseWithTypeValidation_CaseInsensitive() {
    AbstractMX parsedMessage = SwiftMxMessageParser.parseWithTypeValidation(
      SAMPLE_PACS_008,
      "PACS.008.001.08");

    assertNotNull(parsedMessage);
    assertEquals("pacs.008.001.08", parsedMessage.getMxId().id());
  }

  @Test
  void testParseWithTypeValidation_MismatchedType() {
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> SwiftMxMessageParser.parseWithTypeValidation(SAMPLE_PACS_008, "camt.053.001.08"));

    assertNotNull(exception.getMessage());
  }

  @Test
  void testGetMessageIdentifier_Pacs008() {
    String identifier = SwiftMxMessageParser.getMessageIdentifier(SAMPLE_PACS_008);

    assertEquals("pacs.008.001.08", identifier);
  }

  @Test
  void testParseWithTypeValidation_NullType() {
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> SwiftMxMessageParser.parseWithTypeValidation(SAMPLE_PACS_008, null));

    assertEquals("Message identifier cannot be null or empty", exception.getMessage());
  }
}
