package com.appiancs.swiftmtconverter.functions;

import com.appiancorp.suiteapi.common.exceptions.AppianException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancs.swiftmtconverter.SwiftMessageCategory;
import com.appiancs.swiftmtconverter.utils.JsonConverter;
import com.appiancs.swiftmtconverter.utils.SwiftMxMessageParser;
import com.prowidesoftware.swift.model.mx.AbstractMX;

/**
 * Appian custom functions for converting SWIFT MX messages to JSON format.
 * These functions use the Prowide ISO20022 library to parse and convert SWIFT MX messages.
 */
@SwiftMessageCategory
public class SwiftMxConverterFunctions {

  /**
   * Converts a SWIFT MX message to JSON format.
   * MX messages are ISO 20022 XML-based messages.
   *
   * @param mxMessage
   *          The raw SWIFT MX message string (XML format)
   * @return JSON string representation of the message
   * @throws AppianException
   *           if the message cannot be parsed or converted
   */
  @Function
  public String swiftmxtojson(@Parameter String mxMessage) throws AppianException {
    try {
      // Parse MX message
      AbstractMX parsedMessage = SwiftMxMessageParser.parseToAbstractMX(mxMessage);

      // Convert to structured JSON
      return JsonConverter.convertMxToStructuredJson(parsedMessage);
    } catch (IllegalArgumentException e) {
      throw new AppianException(e.getMessage(), e);
    } catch (Exception e) {
      throw new AppianException(
        "Unexpected error converting SWIFT MX message to JSON: " + e.getMessage(),
        e);
    }
  }

  /**
   * Converts a specific SWIFT MX message type to JSON format with type validation.
   * This function validates that the message matches the expected identifier before conversion.
   *
   * @param mxMessage
   *          The raw SWIFT MX message string (XML format)
   * @param messageIdentifier
   *          The expected MX message identifier (e.g., "pacs.008.001.09", "camt.053.001.08")
   * @return JSON string representation of the message
   * @throws AppianException
   *           if the message identifier doesn't match, or parsing fails
   */
  @Function
  public String swiftmxtojsonbytype(
    @Parameter String mxMessage,
    @Parameter String messageIdentifier) throws AppianException {
    try {
      // Parse with type validation
      AbstractMX parsedMessage = SwiftMxMessageParser.parseWithTypeValidation(
        mxMessage,
        messageIdentifier);

      // Convert to structured JSON
      return JsonConverter.convertMxToStructuredJson(parsedMessage);
    } catch (IllegalArgumentException e) {
      throw new AppianException(e.getMessage(), e);
    } catch (Exception e) {
      throw new AppianException(
        "Unexpected error converting SWIFT MX " + messageIdentifier + " message to JSON: " + e.getMessage(),
        e);
    }
  }

  /**
   * Extracts the message identifier from a SWIFT MX message.
   * This is useful for determining the type of an MX message before processing.
   *
   * @param mxMessage
   *          The raw SWIFT MX message string (XML format)
   * @return The message identifier (e.g., "pacs.008.001.09")
   * @throws AppianException
   *           if the message cannot be parsed
   */
  @Function
  public String getmxmessageidentifier(@Parameter String mxMessage) throws AppianException {
    try {
      return SwiftMxMessageParser.getMessageIdentifier(mxMessage);
    } catch (IllegalArgumentException e) {
      throw new AppianException(e.getMessage(), e);
    } catch (Exception e) {
      throw new AppianException(
        "Unexpected error extracting MX message identifier: " + e.getMessage(),
        e);
    }
  }
}
