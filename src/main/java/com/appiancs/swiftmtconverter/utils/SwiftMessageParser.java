package com.appiancs.swiftmtconverter.utils;

import com.prowidesoftware.swift.model.mt.AbstractMT;
import com.prowidesoftware.swift.model.SwiftMessage;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for parsing SWIFT MT messages.
 * Provides methods to parse raw SWIFT message strings into Prowide Core objects.
 */
public class SwiftMessageParser {

  /**
   * Parses a raw SWIFT MT message string into an AbstractMT object.
   * This provides structured access to message fields.
   *
   * @param mtMessage The raw SWIFT MT message string
   * @return AbstractMT object representing the parsed message
   * @throws IllegalArgumentException if the message is null, empty, or invalid
   */
  public static AbstractMT parseToAbstractMT(String mtMessage) {
    validateMessageInput(mtMessage);

    try {
      return AbstractMT.parse(mtMessage);
    } catch (Exception e) {
      throw new IllegalArgumentException(
        "Failed to parse SWIFT MT message: " + e.getMessage(), e
      );
    }
  }

  /**
   * Parses a raw SWIFT MT message string into a SwiftMessage object.
   * This provides generic access to message blocks.
   *
   * @param mtMessage The raw SWIFT MT message string
   * @return SwiftMessage object representing the parsed message
   * @throws IllegalArgumentException if the message is null, empty, or invalid
   */
  public static SwiftMessage parseToSwiftMessage(String mtMessage) {
    validateMessageInput(mtMessage);

    try {
      return SwiftMessage.parse(mtMessage);
    } catch (Exception e) {
      throw new IllegalArgumentException(
        "Failed to parse SWIFT message: " + e.getMessage(), e
      );
    }
  }

  /**
   * Parses a SWIFT MT message and validates it matches the expected type.
   *
   * @param mtMessage The raw SWIFT MT message string
   * @param expectedType The expected message type (e.g., "103", "202")
   * @return AbstractMT object representing the parsed message
   * @throws IllegalArgumentException if the message type doesn't match or parsing fails
   */
  public static AbstractMT parseWithTypeValidation(String mtMessage, String expectedType) {
    validateMessageInput(mtMessage);

    if (StringUtils.isBlank(expectedType)) {
      throw new IllegalArgumentException("Message type cannot be null or empty");
    }

    AbstractMT parsedMessage = parseToAbstractMT(mtMessage);
    String actualType = parsedMessage.getMessageType();

    // Normalize type comparison (remove "MT" prefix if present)
    String normalizedExpected = expectedType.replaceFirst("(?i)^MT", "");
    String normalizedActual = actualType.replaceFirst("(?i)^MT", "");

    if (!normalizedExpected.equals(normalizedActual)) {
      throw new IllegalArgumentException(
        String.format(
          "Message type mismatch. Expected MT%s but got MT%s",
          normalizedExpected,
          normalizedActual
        )
      );
    }

    return parsedMessage;
  }

  /**
   * Extracts the message type from a raw SWIFT MT message.
   *
   * @param mtMessage The raw SWIFT MT message string
   * @return The message type (e.g., "103", "202")
   * @throws IllegalArgumentException if the message is invalid
   */
  public static String getMessageType(String mtMessage) {
    AbstractMT parsedMessage = parseToAbstractMT(mtMessage);
    return parsedMessage.getMessageType();
  }

  /**
   * Validates that the message input is not null or empty.
   *
   * @param mtMessage The message to validate
   * @throws IllegalArgumentException if validation fails
   */
  private static void validateMessageInput(String mtMessage) {
    if (StringUtils.isBlank(mtMessage)) {
      throw new IllegalArgumentException("SWIFT MT message cannot be null or empty");
    }
  }
}
