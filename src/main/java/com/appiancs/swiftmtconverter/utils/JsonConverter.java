package com.appiancs.swiftmtconverter.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.mt.AbstractMT;

/**
 * Utility class for converting SWIFT messages to JSON format.
 * Provides methods to convert Prowide Core objects to formatted JSON strings.
 */
public class JsonConverter {

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  /**
   * Converts an AbstractMT message to JSON with structured field formatting.
   * This produces JSON with business field labels and structured data.
   *
   * @param message
   *          The AbstractMT message to convert
   * @return Pretty-printed JSON string
   * @throws IllegalArgumentException
   *           if conversion fails
   */
  public static String convertToStructuredJson(AbstractMT message) {
    if (message == null) {
      throw new IllegalArgumentException("Message cannot be null");
    }

    try {
      String jsonString = message.toJson();
      return formatJson(jsonString);
    } catch (Exception e) {
      throw new IllegalArgumentException(
        "Failed to convert message to structured JSON: " + e.getMessage(), e);
    }
  }

  /**
   * Converts a SwiftMessage to JSON with generic block formatting.
   * This produces JSON with plain name/value tuples for message blocks.
   *
   * @param message
   *          The SwiftMessage to convert
   * @return Pretty-printed JSON string
   * @throws IllegalArgumentException
   *           if conversion fails
   */
  public static String convertToGenericJson(SwiftMessage message) {
    if (message == null) {
      throw new IllegalArgumentException("Message cannot be null");
    }

    try {
      String jsonString = message.toJson();
      return formatJson(jsonString);
    } catch (Exception e) {
      throw new IllegalArgumentException(
        "Failed to convert message to generic JSON: " + e.getMessage(), e);
    }
  }

  /**
   * Formats a JSON string with pretty printing.
   *
   * @param jsonString
   *          The JSON string to format
   * @return Pretty-printed JSON string
   */
  private static String formatJson(String jsonString) {
    try {
      Object jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
      return GSON.toJson(jsonObject);
    } catch (Exception e) {
      // If pretty printing fails, return original string
      return jsonString;
    }
  }
}
