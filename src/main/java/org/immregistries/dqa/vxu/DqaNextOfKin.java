package org.immregistries.dqa.vxu;

import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.vxu.code.NokRelationship;
import org.immregistries.dqa.vxu.hl7.Name;

public class DqaNextOfKin extends MetaFieldInfoHolder {

  private DqaAddress address = new DqaAddress();
  private Name name = new Name();
  private long nextOfKinId;
  private DqaPhoneNumber phone = new DqaPhoneNumber();
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


  public DqaAddress getAddress() {
    return address;
  }


  public void setAddress(DqaAddress a) {
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

  public DqaPhoneNumber getPhone() {
    return phone;
  }

  public String getPhoneNumber() {
    return phone.getNumber();
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

  public void setPhoneNumber(String phoneNumber) {
    phone.setNumber(phoneNumber);
  }

  public void setPhone(DqaPhoneNumber phoneIn) {
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
    return "DqaNextOfKin{" + "address=" + address + ", name=" + name + ", nextOfKinId="
        + nextOfKinId + ", phone=" + phone + ", positionId=" + getPositionId() + ", relationship='"
        + relationship + '\'' + ", skipped=" + skipped + ", primaryLanguageCode='"
        + primaryLanguageCode + '\'' + '}';
  }


  @Override
  protected void readRegisteredMetaFieldInfo(MetaFieldInfo metaFieldInfo) {
    String value = metaFieldInfo.getValue();
    switch (metaFieldInfo.getVxuField()) {
      case NEXT_OF_KIN_ADDRESS:
        break;
      case NEXT_OF_KIN_ADDRESS_CITY:
        address.setCity(value);
        break;
      case NEXT_OF_KIN_ADDRESS_COUNTRY:
        address.setCountryCode(value);
        break;
      case NEXT_OF_KIN_ADDRESS_COUNTY:
        address.setCountyParishCode(value);
        break;
      case NEXT_OF_KIN_ADDRESS_STATE:
        address.setStateCode(value);
        break;
      case NEXT_OF_KIN_ADDRESS_STREET:
        address.setStreet(value);
        break;
      case NEXT_OF_KIN_ADDRESS_STREET2:
        address.setStreet2(value);
        break;
      case NEXT_OF_KIN_ADDRESS_TYPE:
        address.setTypeCode(value);
        break;
      case NEXT_OF_KIN_ADDRESS_ZIP:
        address.setZip(value);
        break;
      case NEXT_OF_KIN_NAME:
        break;
      case NEXT_OF_KIN_NAME_SUFFIX:
        name.setSuffix(value);
        break;
      case NEXT_OF_KIN_NAME_FIRST:
        name.setFirst(value);
        break;
      case NEXT_OF_KIN_NAME_LAST:
        name.setLast(value);
        break;
      case NEXT_OF_KIN_NAME_MIDDLE:
        name.setMiddle(value);
        break;
      case NEXT_OF_KIN_PHONE:
        break;
      case NEXT_OF_KIN_PHONE_LOCAL_NUMBER:
        phone.setLocalNumber(value);
        break;
      case NEXT_OF_KIN_PHONE_AREA_CODE:
        phone.setAreaCode(value);
        break;
      case NEXT_OF_KIN_PHONE_TEL_EQUIP_CODE:
        phone.setTelEquipCode(value);
        break;
      case NEXT_OF_KIN_PHONE_TEL_USE_CODE:
        phone.setTelUseCode(value);
        break;
      case NEXT_OF_KIN_RELATIONSHIP:
        relationship = value;
        break;
      case NEXT_OF_KIN_SSN:
        break;
      case NEXT_OF_KIN_EMAIL:
        email = value;
        break;
      case NEXT_OF_KIN_PRIMARY_LANGUAGE:
        primaryLanguageCode = value;
      break;
      default:
        break;
    }

  }
}
