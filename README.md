# SWIFT MT Message to JSON Converter

An Appian plugin that provides custom expression functions to convert SWIFT MT messages to JSON format using the Prowide Core library.

## Overview

This plugin enables Appian applications to parse and convert SWIFT MT (Message Type) financial messages into JSON format for easy integration with modern applications. It leverages the [Prowide Core](https://github.com/prowide/prowide-core) library, a production-ready open source Java framework for managing SWIFT FIN messages.

## Features

- **Convert MT to JSON**: Parse any SWIFT MT message and convert to structured or generic JSON
- **Type-Safe Conversion**: Validate message type before conversion to ensure data integrity
- **Message Validation**: Validate SWIFT MT message syntax and structure
- **Comprehensive Coverage**: Supports all common SWIFT MT message types (categories 1-9)
- **Flexible Output**: Choose between structured JSON (with business labels) or generic block format

## Functions

### swiftmttojson

Converts a SWIFT MT message to JSON format.

**Syntax:**
```
swiftmttojson(mtMessage, useStructuredFormat)
```

**Inputs:**

| Input               | Data Type | Required | Multiple | Description |
| ------------------- |:---------:|:--------:|:--------:| ----------- |
| mtMessage           | Text      | Yes      | No       | The raw SWIFT MT message text to convert |
| useStructuredFormat | Boolean   | No       | No       | If true (default), uses structured JSON with business field labels. If false, uses generic block format |

**Returns:**
Text (JSON string)

**Example:**
```
swiftmttojson(
  local!mtMessage,
  true
)
```

**Sample Output:**
```json
{
  "data": {
    "transactionReferenceNumber": "REFERENCE123",
    "bankOperationCode": "CRED",
    "valueDate": "2023-11-21",
    "currency": "USD",
    "amount": "1000.00",
    "orderingCustomer": {
      "account": "12345678901234567890",
      "name": "ACME CORPORATION",
      "address": "123 MAIN STREET NEW YORK NY 10001 US"
    },
    "beneficiaryCustomer": {
      "account": "98765432109876543210",
      "name": "SUPPLIER COMPANY LIMITED",
      "address": "456 HIGH STREET LONDON EC1A 1BB GB"
    }
  }
}
```

---

### swiftmttojsonbytype

Converts a specific SWIFT MT message type to JSON format with type validation.

**Syntax:**
```
swiftmttojsonbytype(mtMessage, messageType)
```

**Inputs:**

| Input       | Data Type | Required | Multiple | Description |
| ----------- |:---------:|:--------:|:--------:| ----------- |
| mtMessage   | Text      | Yes      | No       | The raw SWIFT MT message text to convert |
| messageType | Text      | Yes      | No       | Expected MT message type (e.g., "103", "MT103", "202") |

**Returns:**
Text (JSON string)

**Example:**
```
swiftmttojsonbytype(
  local!mtMessage,
  "103"
)
```

This function will throw an error if the message type doesn't match the expected type, ensuring data integrity.

---

### validateswiftmt

Validates a SWIFT MT message and returns detailed validation results.

**Syntax:**
```
validateswiftmt(mtMessage)
```

**Inputs:**

| Input     | Data Type | Required | Multiple | Description |
| --------- |:---------:|:--------:|:--------:| ----------- |
| mtMessage | Text      | Yes      | No       | The raw SWIFT MT message text to validate |

**Returns:**
Map containing validation results with the following keys:
- `valid` (Boolean): true if message is syntactically valid
- `message` (Text): validation status message
- `messageType` (Text): detected MT message type (e.g., "103")
- `isCommonType` (Boolean): true if message type is commonly used
- `errors` (Text): concatenated validation errors, if any

**Example:**
```
a!localVariables(
  local!validationResult: validateswiftmt(local!mtMessage),
  if(
    local!validationResult.valid,
    /* Process valid message */
    swiftmttojson(local!mtMessage, true),
    /* Handle invalid message */
    local!validationResult.errors
  )
)
```

## Supported Message Types

This plugin supports all common SWIFT MT message types across all categories:

- **Category 1**: Customer Payments and Cheques (MT101-MT112, MT121)
- **Category 2**: Financial Institution Transfers (MT200-MT256)
- **Category 3**: Treasury Markets (MT300-MT381)
- **Category 4**: Collections and Cash Letters (MT400-MT456)
- **Category 5**: Securities Markets (MT500-MT599)
- **Category 6**: Precious Metals and Syndications (MT600-MT649)
- **Category 7**: Documentary Credits and Guarantees (MT700-MT799)
- **Category 8**: Travellers Cheques (MT800-MT899)
- **Category 9**: Cash Management and Customer Status (MT900-MT999)

## Installation

1. Build the plugin using Gradle:
   ```bash
   ./gradlew jar
   ```

2. The plugin JAR file will be created in `build/libs/swift-mt-json-converter-1.0.0.jar`

3. Deploy the plugin to your Appian environment:
   - Copy the JAR file to the `<APPIAN_HOME>/_admin/plugins/` directory
   - Restart the Appian application server

4. After deployment, the functions will appear in the **SWIFT Message Functions** category in the Expression Editor

## Requirements

- Appian 24.2 or higher
- Java 17

## Dependencies

This plugin includes the following libraries:
- Prowide Core (SRU2024-10.2.4): SWIFT message parsing and conversion
- Google GSON (2.10.1): JSON formatting

## Development

### Building from Source

```bash
# Clone the repository
git clone <repository-url>

# Build the plugin
./gradlew jar

# Run tests
./gradlew test

# Run code quality checks
./gradlew check
```

### Code Quality

This plugin includes the following quality checks:
- **Checkstyle**: Code style validation
- **SpotBugs**: Static code analysis
- **PMD**: Code quality checks
- **JaCoCo**: Test coverage reporting

Run all checks with:
```bash
./gradlew check
```

### Running Tests

```bash
./gradlew test
```

Test reports will be generated in `build/reports/tests/test/index.html`

## Error Handling

All functions throw `AppianException` with descriptive error messages when:
- Input message is null or empty
- Message format is invalid
- Message type validation fails (for `swiftmttojsonbytype`)
- Parsing or conversion errors occur

Error messages are user-friendly and suitable for display in Appian interfaces.

## Best Practices

1. **Validate Before Converting**: Use `validateswiftmt()` to check message validity before processing
2. **Use Type Validation**: When you know the expected message type, use `swiftmttojsonbytype()` for additional safety
3. **Handle Errors Gracefully**: Always wrap function calls in error handling logic
4. **Choose Appropriate Format**: Use structured format (default) for business data access, generic format for raw message inspection

## Example Use Case

```
a!localVariables(
  /* Validate the incoming SWIFT message */
  local!validation: validateswiftmt(ri!swiftMessage),

  if(
    local!validation.valid,
    /* Convert valid message to JSON */
    a!localVariables(
      local!jsonData: swiftmttojson(ri!swiftMessage, true),
      /* Process the JSON data in your application */
      a!textField(
        label: "Message Type: " & local!validation.messageType,
        value: local!jsonData,
        readOnly: true
      )
    ),
    /* Display validation errors */
    a!textField(
      label: "Validation Failed",
      value: local!validation.errors,
      validations: "Please provide a valid SWIFT MT message"
    )
  )
)
```

## Troubleshooting

### Functions Not Appearing in Expression Editor

Due to browser caching, users may need to sign out and sign back in for new functions to appear.

### "Failed to parse SWIFT MT message" Error

- Verify the message follows SWIFT MT format with proper blocks (1-5)
- Check that the message includes required fields for the message type
- Ensure line breaks and formatting match SWIFT specifications

### Message Type Mismatch Error

When using `swiftmttojsonbytype()`, ensure the `messageType` parameter matches the actual message type. You can use `validateswiftmt()` first to detect the message type.

## License

This plugin uses the Prowide Core library which is licensed under Apache License 2.0.

## Support

For issues or questions:
- Check the [Prowide Core documentation](https://dev.prowidesoftware.com)
- Review the [Appian Plugin documentation](https://docs.appian.com/suite/help/25.4/Custom_Function_Plug-ins.html)
- Contact your Appian administrator

## Version History

### 1.0.0 (Initial Release)
- Convert SWIFT MT messages to JSON (structured or generic format)
- Validate message type during conversion
- Comprehensive message validation with detailed results
- Support for all common SWIFT MT message types (categories 1-9)
