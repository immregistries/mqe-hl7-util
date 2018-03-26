package org.immregistries.dqa.hl7util.builder;

public enum AckResult {
  APP_ACCEPT("AA"),
  APP_ERROR("AE"),
  APP_REJECT("AR");

  private String code;

  private AckResult(String codeIn) {
    this.code = codeIn;
  }

  public String getCode() {
    return code;
  }

}
