package org.immregistries.dqa.hl7util.builder;

import org.immregistries.dqa.hl7util.model.CodedWithExceptions;

public enum AckERRCode {
  //@formatter:off
  CODE_0_MESSAGE_ACCEPTED(              "Success",   "0", "Message accepted", "Success. Optional, as the AA conveys this. Used for systems that must always return a status code."),
  CODE_100_SEGMENT_SEQUENCE_ERROR(        "Error", "100", "Segment sequence error", "The message segments were not in the proper order or required segments are missing."),
  CODE_101_REQUIRED_FIELD_MISSING(        "Error", "101", "Required field missing", "A required field is missing from the segment."),
  CODE_102_DATA_TYPE_ERROR(               "Error", "102", "Data type error", "The field contained data of the wrong data type, e.g., an NM field contained letters of the alphabet."),
  CODE_103_TABLE_VALUE_NOT_FOUND(         "Error", "103", "Table value not found", "A field of data type ID or IS was compared against the corresponding table, and no match was found."),
  CODE_200_UNSUPPORTED_MESSAGE_TYPE(  "Rejection", "200", "Unsupported message type", "The Message type is not supported."),
  CODE_201_UNSUPPORTED_EVENT_CODE(    "Rejection", "201", "Unsupported event code", "The Event Code is not supported."),
  CODE_202_UNSUPPORTED_PROCESSING_ID( "Rejection", "202", "Unsupported processing ID", "The Processing ID is not supported."),
  CODE_203_UNSUPPORTED_VERSION_ID(    "Rejection", "203", "Unsupported version ID", "The Version ID is not supported."),
  CODE_204_UNKNOWN_KEY_IDENTIFIER(    "Rejection", "204", "Unknown key identifier", "The ID of the patient, order, etc. was not found. Used for transactions other than additions, e.g., transfer of a non-existent patient."),
  CODE_205_DUPLICATE_KEY_IDENTIFIER(  "Rejection", "205", "Duplicate key identifier", "The ID of the patient, order, etc. already exists. Used in response to addition transactions (Admit, New Order, etc.)."),
  CODE_206_APPLICATION_RECORD_LOCKED( "Rejection", "206", "Application record locked", "The transaction could not be performed at the application storage level, e.g., database locked."),
  CODE_207_APPLICATION_INTERNAL_ERROR("Rejection", "207", "Application internal error", "A catch all for internal errors not explicitly covered by other codes.");
  //@formatter:on
  public static final String TABLE = "HL70357";
  public final String category;
  public final String identifier;
  public final String text;
  public final String description;

  AckERRCode(String categoryIn, String identifierIn, String textIn, String descriptionIn) {
    this.category = categoryIn;
    this.identifier = identifierIn;
    this.text = textIn;
    this.description = descriptionIn;
  }

  public static AckERRCode getFromString(String codeCandidate) {
    for (AckERRCode code : AckERRCode.values()) {
      if (code.identifier.equalsIgnoreCase(codeCandidate)) {
        return code;
      }
    }
    return null;
  }

  public CodedWithExceptions getCWE() {
    CodedWithExceptions cwe = new CodedWithExceptions();
    cwe.setIdentifier(this.identifier);
    cwe.setText(this.text);
    cwe.setNameOfCodingSystem(AckERRCode.TABLE);
    return cwe;
  }

  public String getIdentifier() {
    return this.identifier;
  }
}
