package org.immregistries.mqe.vxu;

import org.immregistries.mqe.hl7util.model.MetaFieldInfo;
import org.immregistries.mqe.util.validation.MqeValidatedObject;
import org.immregistries.mqe.vxu.code.NokRelationship;
import org.immregistries.mqe.vxu.hl7.Name;

public class MqeNextOfKin extends MqeValidatedObject {
  @Override
  public TargetType getTargetType() { return TargetType.NextOfKin;}

  private MqeAddress address = new MqeAddress();
  private Name name = new Name();
  private long nextOfKinId;
  private MqePhoneNumber phone = new MqePhoneNumber();
  private String relationship = "";// new CodedEntity(CodesetType.PERSON_RELATIONSHIP);
  private boolean skipped = false;
  private String primaryLanguageCode;
  private String email = "";

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public MqeAddress getAddress() {
    return address;
  }

  public void setAddress(MqeAddress a) {
    this.address = a;
  }

  public Name getName() {
    return name;
  }

  public String getNameFirst() {
    return name.getFirst();
  }

  public String getNameLast() {
    return name.getLast();
  }

  public String getNameMiddle() {
    return name.getMiddle();
  }

  public String getNamePrefix() {
    return name.getPrefix();
  }

  public String getNameSuffix() {
    return name.getSuffix();
  }

  public String getNameTypeCode() {
    return name.getTypeCode();
  }

  public long getNextOfKinId() {
    return nextOfKinId;
  }

  public MqePhoneNumber getPhone() {
    return phone;
  }

  public String getPhoneNumber() {
    return phone.getFormattedNumber();
  }

  public String getRelationship() {
    return relationship;
  }

  public String getRelationshipCode() {
    return relationship;
  }

  public boolean isSkipped() {
    return skipped;
  }

  public void setNameFirst(String nameFirst) {
    name.setFirst(nameFirst);
  }

  public void setNameLast(String nameLast) {
    name.setLast(nameLast);
  }

  public void setNameMiddle(String nameMiddle) {
    name.setMiddle(nameMiddle);
  }

  public void setNamePrefix(String namePrefix) {
    name.setPrefix(namePrefix);
  }

  public void setNameSuffix(String nameSuffix) {
    name.setSuffix(nameSuffix);
  }

  public void setNameTypeCode(String nameTypeCode) {
    name.setTypeCode(nameTypeCode);
  }

  public void setNextOfKinId(long nextOfKinId) {
    this.nextOfKinId = nextOfKinId;
  }

  public void setPhoneNumber(MqePhoneNumber phoneNumber) {
    this.phone = phoneNumber;
  }

  public void setPhone(MqePhoneNumber phoneIn) {
    this.phone = phoneIn;
  }

  public void setRelationshipCode(String relationshipCode) {
    relationship = relationshipCode;
  }

  public void setSkipped(boolean skipped) {
    this.skipped = skipped;
  }

  /**
   * @return the primaryLanguageCode
   */
  public String getPrimaryLanguageCode() {
    return primaryLanguageCode;
  }

  /**
   * @param primaryLanguageCode the primaryLanguageCode to set
   */
  public void setPrimaryLanguageCode(String primaryLanguageCode) {
    this.primaryLanguageCode = primaryLanguageCode;
  }

  public NokRelationship getNokRelationship() {
    return NokRelationship.get(this.getRelationshipCode());
  }

  public boolean isResponsibleRelationship() {
    return getNokRelationship().isResponsibleRelationship();
  }


  public boolean isChildRelationship() {
    return getNokRelationship().isChildRelationship();
  }

  @Override
  public String toString() {
    return "\nMqeNextOfKin{" +
        "\naddress=" + address +
        "\n, name=" + name +
        "\n, nextOfKinId=" + nextOfKinId +
        "\n, phone=" + phone +
        "\n, relationship='" + relationship + '\'' +
        "\n, skipped=" + skipped +
        "\n, primaryLanguageCode='" + primaryLanguageCode + '\'' +
        "\n, email='" + email + '\'' +
        '}';
  }

  @Override
  protected void setFieldFromMetaFieldInfo(MetaFieldInfo metaFieldInfo) {
    String value = metaFieldInfo.getValue();
    switch (metaFieldInfo.getVxuField()) {
      case NEXT_OF_KIN_ADDRESS:
      case PATIENT_GUARDIAN_ADDRESS:
        break;
      case NEXT_OF_KIN_ADDRESS_CITY:
      case PATIENT_GUARDIAN_ADDRESS_CITY:
        address.setCity(value);
        break;
      case NEXT_OF_KIN_ADDRESS_COUNTRY:
      case PATIENT_GUARDIAN_ADDRESS_COUNTRY:
        address.setCountryCode(value);
        break;
      case NEXT_OF_KIN_ADDRESS_COUNTY:
      case PATIENT_GUARDIAN_ADDRESS_COUNTY:
        address.setCountyParishCode(value);
        break;
      case NEXT_OF_KIN_ADDRESS_STATE:
      case PATIENT_GUARDIAN_ADDRESS_STATE:
        address.setStateCode(value);
        break;
      case NEXT_OF_KIN_ADDRESS_STREET:
      case PATIENT_GUARDIAN_ADDRESS_STREET:
        address.setStreet(value);
        break;
      case NEXT_OF_KIN_ADDRESS_STREET2:
      case PATIENT_GUARDIAN_ADDRESS_STREET2:
        address.setStreet2(value);
        break;
      case NEXT_OF_KIN_ADDRESS_TYPE:
//      case PATIENT_GUARDIAN_ADDRESS_TYPE:
        address.setTypeCode(value);
        break;
      case NEXT_OF_KIN_ADDRESS_ZIP:
      case PATIENT_GUARDIAN_ADDRESS_ZIP:
        address.setZip(value);
        break;
      case NEXT_OF_KIN_NAME:
      case PATIENT_GUARDIAN_NAME:
        break;
      case NEXT_OF_KIN_NAME_SUFFIX:
//      case PATIENT_GUARDIAN_NAME_SUFFIX:
        name.setSuffix(value);
        break;
      case NEXT_OF_KIN_NAME_FIRST:
      case PATIENT_GUARDIAN_NAME_FIRST:
        name.setFirst(value);
        break;
      case NEXT_OF_KIN_NAME_LAST:
      case PATIENT_GUARDIAN_NAME_LAST:
        name.setLast(value);
        break;
      case NEXT_OF_KIN_NAME_MIDDLE:
      case PATIENT_GUARDIAN_NAME_MIDDLE:
        name.setMiddle(value);
        break;
      case NEXT_OF_KIN_PHONE:
      case PATIENT_GUARDIAN_PHONE:
        phone.setSingleFieldinput(value);
//        phone.setLocalNumber(phone.getLocalNumberFrom(value));
//        phone.setAreaCode(phone.getAreaCodeFrom(value));
        break;
      case NEXT_OF_KIN_PHONE_LOCAL_NUMBER:
      case PATIENT_GUARDIAN_PHONE_LOCAL_NUMBER:
        phone.setLocalNumber(value);
        break;
      case NEXT_OF_KIN_PHONE_AREA_CODE:
      case PATIENT_GUARDIAN_PHONE_AREA_CODE:
        phone.setAreaCode(value);
        break;
      case NEXT_OF_KIN_PHONE_TEL_EQUIP_CODE:
      case PATIENT_GUARDIAN_PHONE_TEL_EQUIP_CODE:
        phone.setTelEquipCode(value);
        break;
      case NEXT_OF_KIN_PHONE_TEL_USE_CODE:
      case PATIENT_GUARDIAN_PHONE_TEL_USE_CODE:
        phone.setTelUseCode(value);
        break;
      case NEXT_OF_KIN_RELATIONSHIP:
      case PATIENT_GUARDIAN_RELATIONSHIP:
        relationship = value;
        break;
      case NEXT_OF_KIN_SSN:
        break;
      case NEXT_OF_KIN_EMAIL:
      case PATIENT_GUARDIAN_EMAIL:
        email = value;
        break;
      case NEXT_OF_KIN_PRIMARY_LANGUAGE:
      case PATIENT_GUARDIAN_PRIMARY_LANGUAGE:
        primaryLanguageCode = value;
        break;
      default:
        break;
    }
  }
}

