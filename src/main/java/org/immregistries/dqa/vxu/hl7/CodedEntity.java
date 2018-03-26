/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 *
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.
 */
package org.immregistries.dqa.vxu.hl7;

public class CodedEntity {

  private String code = "";
  private String text = "";
  private String table = "";
  private String altCode = "";
  private String altText = "";
  private String altTable = "";

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getTable() {
    return table;
  }

  public void setTable(String table) {
    this.table = table;
  }

  public String getAltCode() {
    return altCode;
  }

  public void setAltCode(String altCode) {
    this.altCode = altCode;
  }

  public String getAltText() {
    return altText;
  }

  public void setAltText(String altText) {
    this.altText = altText;
  }

  public String getAltTable() {
    return altTable;
  }

  public void setAltTable(String altTable) {
    this.altTable = altTable;
  }

}
