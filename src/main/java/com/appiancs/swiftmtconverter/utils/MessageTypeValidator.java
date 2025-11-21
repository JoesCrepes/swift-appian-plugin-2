package com.appiancs.swiftmtconverter.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.prowidesoftware.swift.model.mt.AbstractMT;

/**
 * Utility class for validating SWIFT MT message types and syntax.
 * Provides methods to check message validity and supported types.
 */
public class MessageTypeValidator {

  /**
   * Common SWIFT MT message types supported by Prowide Core.
   * This is not exhaustive but covers the most common message types.
   */
  private static final Set<String> COMMON_MT_TYPES = new HashSet<>(Arrays.asList(
    // Category 1: Customer Payments and Cheques
    "101", "102", "103", "103_STP", "103_REMIT", "104", "107", "110", "111", "112", "121",
    // Category 2: Financial Institution Transfers
    "200", "201", "202", "202_COV", "203", "204", "205", "205_COV", "210", "256",
    // Category 3: Treasury Markets - Foreign Exchange, Money Markets and Derivatives
    "300", "304", "305", "306", "320", "330", "340", "341", "350", "360", "361", "362",
    "364", "365", "380", "381",
    // Category 4: Collections and Cash Letters
    "400", "405", "410", "412", "416", "420", "422", "430", "450", "455", "456",
    // Category 5: Securities Markets
    "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510",
    "513", "514", "515", "516", "517", "518", "524", "526", "527", "528", "529",
    "530", "535", "536", "537", "538", "540", "541", "542", "543", "544", "545",
    "546", "547", "548", "549", "558", "559", "564", "565", "566", "567", "568",
    "569", "575", "576", "577", "578", "579", "581", "582", "584", "586", "587",
    "588", "589", "590", "591", "592", "595", "596", "598", "599",
    // Category 6: Treasury Markets - Precious Metals and Syndications
    "600", "601", "604", "605", "606", "607", "608", "609", "620", "643", "644", "645",
    "646", "649",
    // Category 7: Documentary Credits and Guarantees
    "700", "701", "705", "707", "710", "711", "720", "721", "730", "732", "734",
    "740", "742", "747", "750", "752", "754", "756", "759", "760", "767", "768",
    "769", "790", "791", "792", "795", "796", "798", "799",
    // Category 8: Travellers Cheques
    "800", "801", "802", "810", "812", "813", "820", "821", "822", "823", "824",
    "890", "891", "892", "895", "896", "898", "899",
    // Category 9: Cash Management and Customer Status
    "900", "910", "920", "935", "940", "941", "942", "950", "960", "961", "962",
    "963", "964", "965", "966", "967", "970", "971", "972", "973", "985", "986",
    "990", "991", "992", "995", "996", "998", "999"));

  /**
   * Validates a SWIFT MT message and returns validation details.
   *
   * @param mtMessage
   *          The raw SWIFT MT message string
   * @return ValidationResult containing validation status and details
   */
  public static ValidationResult validate(String mtMessage) {
    if (StringUtils.isBlank(mtMessage)) {
      return new ValidationResult(false, "Message is null or empty", null);
    }

    try {
      AbstractMT parsedMessage = SwiftMessageParser.parseToAbstractMT(mtMessage);
      String messageType = parsedMessage.getMessageType();

      // Check if message type is in common types
      boolean isCommonType = COMMON_MT_TYPES.contains(messageType);

      // If parsing succeeded, consider the message valid
      return new ValidationResult(
        true,
        "Message is valid",
        messageType,
        isCommonType);
    } catch (Exception e) {
      return new ValidationResult(
        false,
        "Failed to parse message: " + e.getMessage(),
        null);
    }
  }

  /**
   * Checks if a message type is in the list of common MT types.
   *
   * @param messageType
   *          The message type to check (e.g., "103", "MT103")
   * @return true if the type is commonly supported
   */
  public static boolean isCommonMessageType(String messageType) {
    if (StringUtils.isBlank(messageType)) {
      return false;
    }
    String normalized = messageType.replaceFirst("(?i)^MT", "");
    return COMMON_MT_TYPES.contains(normalized);
  }

  /**
   * Class to hold validation results.
   */
  public static class ValidationResult {
    private final boolean valid;
    private final String message;
    private final String messageType;
    private final boolean isCommonType;
    private final List<String> validationErrors;

    public ValidationResult(boolean valid, String message, String messageType) {
      this(valid, message, messageType, false, null);
    }

    public ValidationResult(
      boolean valid,
      String message,
      String messageType,
      boolean isCommonType) {
      this(valid, message, messageType, isCommonType, null);
    }

    public ValidationResult(
      boolean valid,
      String message,
      String messageType,
      boolean isCommonType,
      List<String> validationErrors) {
      this.valid = valid;
      this.message = message;
      this.messageType = messageType;
      this.isCommonType = isCommonType;
      this.validationErrors = validationErrors;
    }

    public boolean isValid() {
      return valid;
    }

    public String getMessage() {
      return message;
    }

    public String getMessageType() {
      return messageType;
    }

    public boolean isCommonType() {
      return isCommonType;
    }

    public List<String> getValidationErrors() {
      return validationErrors;
    }
  }
}
