package com.appiancs.swiftmtconverter.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.appiancorp.suiteapi.common.exceptions.AppianException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for SwiftMtConverterFunctions.
 */
class SwiftMtConverterFunctionsTest {

  private static final String MT103_SAMPLE_PATH = "src/test/resources/sample-mt103.txt";
  private static final String MT202_SAMPLE_PATH = "src/test/resources/sample-mt202.txt";

  private SwiftMtConverterFunctions functions;
  private String mt103Message;
  private String mt202Message;

  @BeforeEach
  void setUp() throws IOException {
    functions = new SwiftMtConverterFunctions();
    mt103Message = new String(Files.readAllBytes(Paths.get(MT103_SAMPLE_PATH)));
    mt202Message = new String(Files.readAllBytes(Paths.get(MT202_SAMPLE_PATH)));
  }

  @Test
  void testSwiftmttojson_StructuredFormat_MT103() throws AppianException {
    String result = functions.swiftmttojson(mt103Message, true);

    assertNotNull(result);
    assertTrue(result.contains("\"data\"") || result.contains("\"message\""));
    // Verify it's valid JSON by checking for opening and closing braces
    assertTrue(result.trim().startsWith("{"));
    assertTrue(result.trim().endsWith("}"));
  }

  @Test
  void testSwiftmttojson_StructuredFormat_MT202() throws AppianException {
    String result = functions.swiftmttojson(mt202Message, true);

    assertNotNull(result);
    assertTrue(result.trim().startsWith("{"));
    assertTrue(result.trim().endsWith("}"));
  }

  @Test
  void testSwiftmttojson_GenericFormat() throws AppianException {
    String result = functions.swiftmttojson(mt103Message, false);

    assertNotNull(result);
    assertTrue(result.trim().startsWith("{"));
    assertTrue(result.trim().endsWith("}"));
  }

  @Test
  void testSwiftmttojson_DefaultToStructuredFormat() throws AppianException {
    String result = functions.swiftmttojson(mt103Message, null);

    assertNotNull(result);
    assertTrue(result.trim().startsWith("{"));
    assertTrue(result.trim().endsWith("}"));
  }

  @Test
  void testSwiftmttojson_NullMessage() {
    Exception exception = assertThrows(
      AppianException.class,
      () -> functions.swiftmttojson(null, true)
    );

    assertTrue(exception.getMessage().contains("cannot be null or empty"));
  }

  @Test
  void testSwiftmttojson_EmptyMessage() {
    Exception exception = assertThrows(
      AppianException.class,
      () -> functions.swiftmttojson("", true)
    );

    assertTrue(exception.getMessage().contains("cannot be null or empty"));
  }

  @Test
  void testSwiftmttojson_InvalidMessage() {
    Exception exception = assertThrows(
      AppianException.class,
      () -> functions.swiftmttojson("INVALID MESSAGE", true)
    );

    assertTrue(exception.getMessage().contains("Failed to parse"));
  }

  @Test
  void testSwiftmttojsonbytype_MatchingType_MT103() throws AppianException {
    String result = functions.swiftmttojsonbytype(mt103Message, "103");

    assertNotNull(result);
    assertTrue(result.trim().startsWith("{"));
    assertTrue(result.trim().endsWith("}"));
  }

  @Test
  void testSwiftmttojsonbytype_MatchingType_WithMTPrefix() throws AppianException {
    String result = functions.swiftmttojsonbytype(mt103Message, "MT103");

    assertNotNull(result);
    assertTrue(result.trim().startsWith("{"));
    assertTrue(result.trim().endsWith("}"));
  }

  @Test
  void testSwiftmttojsonbytype_MismatchedType() {
    Exception exception = assertThrows(
      AppianException.class,
      () -> functions.swiftmttojsonbytype(mt103Message, "202")
    );

    assertTrue(exception.getMessage().contains("mismatch"));
  }

  @Test
  void testSwiftmttojsonbytype_NullMessageType() {
    Exception exception = assertThrows(
      AppianException.class,
      () -> functions.swiftmttojsonbytype(mt103Message, null)
    );

    assertTrue(exception.getMessage().contains("cannot be null or empty"));
  }

  @Test
  void testValidateswiftmt_ValidMT103() throws AppianException {
    Map<String, Object> result = functions.validateswiftmt(mt103Message);

    assertNotNull(result);
    assertTrue((Boolean) result.get("valid"));
    assertEquals("103", result.get("messageType"));
    assertTrue((Boolean) result.get("isCommonType"));
    assertNotNull(result.get("message"));
  }

  @Test
  void testValidateswiftmt_ValidMT202() throws AppianException {
    Map<String, Object> result = functions.validateswiftmt(mt202Message);

    assertNotNull(result);
    assertTrue((Boolean) result.get("valid"));
    assertEquals("202", result.get("messageType"));
    assertTrue((Boolean) result.get("isCommonType"));
  }

  @Test
  void testValidateswiftmt_NullMessage() throws AppianException {
    Map<String, Object> result = functions.validateswiftmt(null);

    assertNotNull(result);
    assertFalse((Boolean) result.get("valid"));
    assertTrue(result.get("message").toString().contains("null or empty"));
  }

  @Test
  void testValidateswiftmt_EmptyMessage() throws AppianException {
    Map<String, Object> result = functions.validateswiftmt("");

    assertNotNull(result);
    assertFalse((Boolean) result.get("valid"));
    assertTrue(result.get("message").toString().contains("null or empty"));
  }

  @Test
  void testValidateswiftmt_InvalidMessage() throws AppianException {
    Map<String, Object> result = functions.validateswiftmt("INVALID SWIFT MESSAGE");

    assertNotNull(result);
    assertFalse((Boolean) result.get("valid"));
    assertTrue(result.get("message").toString().contains("Failed to parse"));
  }
}
