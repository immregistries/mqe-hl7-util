package org.immregistries.mqe.vxu;

import org.apache.commons.lang3.StringUtils;

/**
 * This class represents a phone number, and includes a type
 *
 * @author Josh
 */
public class MqePhoneNumber {
  private String singleFieldinput;
  private String telUse = "";// new CodedEntity(CodesetType.TELECOMMUNICATION_USE);
  //HL7-defined Table 0201 - Telecommunication use code
  private String telEquip = "";// new CodedEntity(CodesetType.TELECOMMUNICATION_EQUIPMENT);
  //HL7-defined Table 0202 - Telecommunication equipment type
  private String email = "";
  private String countryCode = "";
  private String areaCode = "";
  private String localNumber = "";
  private String extension = "";

  public MqePhoneNumber() {
    // default
  }

  public MqePhoneNumber(MqePhoneNumber toCopy) {
    this.singleFieldinput = toCopy.singleFieldinput;
    this.telUse           = toCopy.telUse;
    this.telEquip         = toCopy.telEquip;
    this.email            = toCopy.email;
    this.countryCode      = toCopy.countryCode;
    this.areaCode         = toCopy.areaCode;
    this.localNumber      = toCopy.localNumber;
    this.extension        = toCopy.extension;
  }

  public String getSingleFieldinput() {
    return singleFieldinput;
  }

  public void setSingleFieldinput(String singleFieldinput) {
    this.singleFieldinput = singleFieldinput;
  }

  public MqePhoneNumber(String phoneNumberString) {
    this.singleFieldinput = phoneNumberString;
    this.areaCode = getAreaCodeFrom(phoneNumberString);
    this.localNumber = getLocalNumberFrom(phoneNumberString);
  }

  public MqePhoneNumber(String areaCode, String localNumber) {
    this.areaCode = areaCode;
    this.localNumber = localNumber;
  }

  public String getFormattedNumber() {
    if (StringUtils.isNotBlank(localNumber)) {
      StringBuilder sb = new StringBuilder();
      if (StringUtils.isNotBlank(areaCode)) {
        sb.append("(");
        sb.append(areaCode);
        sb.append(")");
      }
      if (localNumber.length() == 7) {
        sb.append(localNumber, 0, 3);
        sb.append("-");
        sb.append(localNumber, 3, 7);
      } else {
        sb.append(localNumber);
      }
      return sb.toString();
    }
    return "";
  }

  public String onlyDigits(String text) {
    //FYI - tried this first, but it's about 9x slower.
    //return text.replaceAll("[^0-9]", "");
    StringBuilder onlyDigits = new StringBuilder();
    for (char c : text.toCharArray()) {
      if (c >= '0' && c <= '9') {
        onlyDigits.append(c);
      }
    }
    return onlyDigits.toString();
  }

  public String getLocalNumberFrom(String numberText) {
    if (StringUtils.isNotBlank(numberText)) {
      String onlyDigits = onlyDigits(numberText);
      if (onlyDigits.length() == 7) {
        return onlyDigits;
      } 
      if (onlyDigits.length() == 10) {
        return onlyDigits.substring(3, 10);
      }
    }
    return "";
  }

  public String getAreaCodeFrom(String numberText) {
    if (StringUtils.isNotBlank(numberText)) {
        String onlyDigits = onlyDigits(numberText);
        if (onlyDigits.length() == 10) {
          return onlyDigits.substring(0, 3);
        }
    }
    return "";
  }

  public String getTelUse() {
    return telUse;
  }

  public String getTelUseCode() {
    return telUse;
  }

  public void setTelUseCode(String telUseCode) {
    this.telUse = telUseCode;
  }

  public String getTelEquipCode() {
    return telEquip;
  }

  public String getTelEquip() {
    return telEquip;
  }

  public void setTelEquipCode(String telEquipCode) {
    this.telEquip = telEquipCode;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public String getLocalNumber() {
    return localNumber;
  }

  public void setLocalNumber(String localNumber) {
    this.localNumber = localNumber;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  @Override
  public String toString() {
    return "MqePhoneNumber{" +
        ", getFormattedNumber='" + this.getFormattedNumber() + '\'' +
        ", telUse='" + telUse + '\'' +
        ", telEquip='" + telEquip + '\'' +
        ", email='" + email + '\'' +
        ", countryCode='" + countryCode + '\'' +
        ", areaCode='" + areaCode + '\'' +
        ", localNumber='" + localNumber + '\'' +
        ", extension='" + extension + '\'' +
        '}';
  }
}
