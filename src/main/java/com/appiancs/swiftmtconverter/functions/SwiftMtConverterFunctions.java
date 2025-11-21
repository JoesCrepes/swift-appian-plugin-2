package com.appiancs.swiftmtconverter.functions;

import java.util.HashMap;
import java.util.Map;

import com.appiancs.swiftmtconverter.SwiftMessageCategory;
import com.appiancs.swiftmtconverter.utils.JsonConverter;
import com.appiancs.swiftmtconverter.utils.MessageTypeValidator;
import com.appiancs.swiftmtconverter.utils.SwiftMessageParser;
import com.appiancorp.suiteapi.common.exceptions.AppianException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.mt.AbstractMT;

/**
 * Appian custom functions for converting SWIFT MT messages to JSON format.
 * These functions use the Prowide Core library to parse and convert SWIFT messages.
 */
@SwiftMessageCategory
public class SwiftMtConverterFunctions {

  /**
   * Converts a SWIFT MT message to JSON format.
   * By default, uses structured JSON with business field labels.
   *
   * @param mtMessage The raw SWIFT MT message string
   * @param useStructuredFormat Optional. If true (default), uses structured JSON
   *                            with business labels. If false, uses generic block format.
   * @return JSON string representation of the message
   * @throws AppianException if the message cannot be parsed or converted
   */
  @Function
  public String swiftmttojson(
    @Parameter String mtMessage,
    @Parameter(required = false) Boolean useStructuredFormat
  ) throws AppianException {
    try {
      // Default to structured format if not specified
      boolean useStructured = useStructuredFormat == null || useStructuredFormat;

      if (useStructured) {
        // Parse to AbstractMT for structured JSON with business labels
        AbstractMT parsedMessage = SwiftMessageParser.parseToAbstractMT(mtMessage);
        return JsonConverter.convertToStructuredJson(parsedMessage);
      } else {
        // Parse to SwiftMessage for generic block format
        SwiftMessage parsedMessage = SwiftMessageParser.parseToSwiftMessage(mtMessage);
        return JsonConverter.convertToGenericJson(parsedMessage);
      }
    } catch (IllegalArgumentException e) {
      throw new AppianException(e.getMessage(), e);
    } catch (Exception e) {
      throw new AppianException(
        "Unexpected error converting SWIFT MT message to JSON: " + e.getMessage(),
        e
      );
    }
  }

  /**
   * Converts a specific SWIFT MT message type to JSON format with type validation.
   * This function validates that the message matches the expected type before conversion.
   *
   * @param mtMessage The raw SWIFT MT message string
   * @param messageType The expected MT message type (e.g., "103", "MT103", "202")
   * @return JSON string representation of the message
   * @throws AppianException if the message type doesn't match, or parsing fails
   */
  @Function
  public String swiftmttojsonbytype(
    @Parameter String mtMessage,
    @Parameter String messageType
  ) throws AppianException {
    try {
      // Parse with type validation
      AbstractMT parsedMessage = SwiftMessageParser.parseWithTypeValidation(
        mtMessage,
        messageType
      );

      // Convert to structured JSON
      return JsonConverter.convertToStructuredJson(parsedMessage);
    } catch (IllegalArgumentException e) {
      throw new AppianException(e.getMessage(), e);
    } catch (Exception e) {
      throw new AppianException(
        "Unexpected error converting SWIFT MT" + messageType + " message to JSON: "
        + e.getMessage(),
        e
      );
    }
  }

  /**
   * Validates a SWIFT MT message and returns validation details.
   * Returns a map containing validation status, message type, and any errors.
   *
   * @param mtMessage The raw SWIFT MT message string to validate
   * @return Map containing validation results with keys:
   *         - "valid" (Boolean): true if message is valid
   *         - "message" (String): validation status message
   *         - "messageType" (String): detected MT message type (e.g., "103")
   *         - "isCommonType" (Boolean): true if message type is commonly used
   *         - "errors" (String): concatenated validation errors, if any
   * @throws AppianException if an unexpected error occurs during validation
   */
  @Function
  public Map<String, Object> validateswiftmt(@Parameter String mtMessage)
    throws AppianException {
    try {
      MessageTypeValidator.ValidationResult result =
        MessageTypeValidator.validate(mtMessage);

      Map<String, Object> resultMap = new HashMap<>();
      resultMap.put("valid", result.isValid());
      resultMap.put("message", result.getMessage());
      resultMap.put("messageType", result.getMessageType());
      resultMap.put("isCommonType", result.isCommonType());

      if (result.getValidationErrors() != null && !result.getValidationErrors().isEmpty()) {
        resultMap.put("errors", String.join("; ", result.getValidationErrors()));
      }

      return resultMap;
    } catch (Exception e) {
      throw new AppianException(
        "Unexpected error validating SWIFT MT message: " + e.getMessage(),
        e
      );
    }
  }
}
