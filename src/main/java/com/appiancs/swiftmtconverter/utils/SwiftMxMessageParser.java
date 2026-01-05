package com.appiancs.swiftmtconverter.utils;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.prowidesoftware.swift.model.mx.AbstractMX;

/**
 * Utility class for parsing SWIFT MX messages.
 * Provides methods to parse raw SWIFT MX message strings into Prowide ISO20022 objects.
 */
public class SwiftMxMessageParser {

  /**
   * Parses a raw SWIFT MX message string into an AbstractMX object.
   * This provides structured access to MX message fields.
   *
   * @param mxMessage
   *          The raw SWIFT MX message string (XML format)
   * @return AbstractMX object representing the parsed message
   * @throws IllegalArgumentException
   *           if the message is null, empty, or invalid
   */
  public static AbstractMX parseToAbstractMX(String mxMessage) {
    validateMessageInput(mxMessage);

    try {
      AbstractMX parsedMessage = AbstractMX.parse(mxMessage);
      if (parsedMessage == null) {
        throw new IllegalArgumentException("Failed to parse SWIFT MX message: Invalid message format");
      }
      return parsedMessage;
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalArgumentException(
        "Failed to parse SWIFT MX message: " + e.getMessage(), e);
    }
  }

  /**
   * Parses a SWIFT MX message and validates it matches the expected message identifier.
   *
   * @param mxMessage
   *          The raw SWIFT MX message string (XML format)
   * @param expectedIdentifier
   *          The expected message identifier (e.g., "pacs.008.001.09", "camt.053.001.08")
   * @return AbstractMX object representing the parsed message
   * @throws IllegalArgumentException
   *           if the message type doesn't match or parsing fails
   */
  public static AbstractMX parseWithTypeValidation(String mxMessage, String expectedIdentifier) {
    validateMessageInput(mxMessage);

    if (StringUtils.isBlank(expectedIdentifier)) {
      throw new IllegalArgumentException("Message identifier cannot be null or empty");
    }

    AbstractMX parsedMessage = parseToAbstractMX(mxMessage);
    String actualIdentifier = parsedMessage.getMxId().id();

    // Normalize identifier comparison (convert to lowercase for case-insensitive comparison)
    String normalizedExpected = expectedIdentifier.toLowerCase(Locale.ROOT).trim();
    String normalizedActual = actualIdentifier.toLowerCase(Locale.ROOT).trim();

    if (!normalizedExpected.equals(normalizedActual)) {
      throw new IllegalArgumentException(
        String.format(
          "Message identifier mismatch. Expected %s but got %s",
          expectedIdentifier,
          actualIdentifier));
    }

    return parsedMessage;
  }

  /**
   * Extracts the message identifier from a raw SWIFT MX message.
   *
   * @param mxMessage
   *          The raw SWIFT MX message string (XML format)
   * @return The message identifier (e.g., "pacs.008.001.09")
   * @throws IllegalArgumentException
   *           if the message is invalid
   */
  public static String getMessageIdentifier(String mxMessage) {
    AbstractMX parsedMessage = parseToAbstractMX(mxMessage);
    return parsedMessage.getMxId().id();
  }

  /**
   * Validates that the message input is not null or empty.
   *
   * @param mxMessage
   *          The message to validate
   * @throws IllegalArgumentException
   *           if validation fails
   */
  private static void validateMessageInput(String mxMessage) {
    if (StringUtils.isBlank(mxMessage)) {
      throw new IllegalArgumentException("SWIFT MX message cannot be null or empty");
    }
  }
}
