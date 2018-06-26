package org.immregistries.mqe.hl7util;

public enum ApplicationErrorCode {
  ILLOGICAL_DATE_ERROR("1", "Illogical Date error"),
  INVALID_DATE("2", "Invalid Date"),
  ILLOGICAL_VALUE_ERROR("3", "Illogical Value error"),
  INVALID_VALUE("4", "Invalid value"),
  TABLE_VALUE_NOT_FOUND("5", "Table value not found"),
  REQUIRED_OBSERVATION_MISSING("6", "Required observation missing");
  private String id;
  private String text;

  public String getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  ApplicationErrorCode(String id, String text) {
    this.id = id;
    this.text = text;
  }
}
