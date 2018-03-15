package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaNextOfKin;
import org.immregistries.dqa.vxu.DqaPhoneNumber;
import org.immregistries.dqa.vxu.VxuField;

public enum HL7NokParser {
  INSTANCE;
  private HL7ParsingUtil hl7Util = HL7ParsingUtil.INSTANCE;

  /**
   * This will take a map of HL7 values, and build a List of Next Of Kin objects out of it.
   * <p>
   * It will create one object for each NK1 segment, and add it to a list
   * </p>
   * <p>
   * The order of the segments will be preserved in the list.
   * </p>
   *
   * @param map a map that has been built from the VXU parts.
   * @return a list of Next of Kin objects.
   */
  public List<DqaNextOfKin> getNk1List(HL7MessageMap map) {
    List<DqaNextOfKin> nk1s = new ArrayList<DqaNextOfKin>();

    Integer segCount = map.getSegmentCount("NK1");
    if (segCount == 0) {
      return nk1s;
    }

    for (int x = 1; x <= segCount; x++) {
      DqaNextOfKin nextOfKin = getNextOfKin(map, x);
      nk1s.add(nextOfKin);
    }

    return nk1s;
  }

  /**
   * Gets a single next of kin object from the message map.
   * <p>
   * It takes the ordinal given (starting with 1), and gets that 1st,2nd, etc next of kin object
   * </p>
   * 
   * @param map containing all of the data for a VXU message.
   * @param ordinal the ordinal position of the NK1 you're interested in.
   * @return a next of kin object populated with data from the map.
   */
  protected DqaNextOfKin getNextOfKin(HL7MessageMap map, int ordinal) {

    DqaNextOfKin nextOfKin = new DqaNextOfKin();
    nextOfKin.setPositionId(ordinal);
    int nk1Idx = map.getAbsoluteIndexForSegment("NK1", ordinal);
    nextOfKin.setPositionAbsolute(nk1Idx);

    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_PHONE, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_PHONE_AREA_CODE, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_PHONE_LOCAL_NUMBER, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_PHONE_TEL_EQUIP_CODE, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_PHONE_TEL_USE_CODE, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_NAME_FIRST, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_NAME_LAST, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_NAME_MIDDLE, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_NAME_SUFFIX, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_ADDRESS_CITY, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_ADDRESS_COUNTY, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_ADDRESS_COUNTRY, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_ADDRESS_STATE, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_ADDRESS_STREET, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_ADDRESS_STREET2, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_ADDRESS_TYPE, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_ADDRESS_ZIP, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_RELATIONSHIP, map);
    nextOfKin.mapAndSet(VxuField.NEXT_OF_KIN_PRIMARY_LANGUAGE, map);
    return nextOfKin;
  }

  protected String getRespPartyFirstName(HL7MessageMap map, int segIdx) {
    return map.getAtIndex("NK1-2-2", segIdx, 1);
  }

  protected String getRespPartyLastName(HL7MessageMap map, int segIdx) {
    return map.getAtIndex("NK1-2-1", segIdx, 1);
  }

  protected String getRespPartyMiddleName(HL7MessageMap map, int segIdx) {
    return map.getAtIndex("NK1-2-3", segIdx, 1);
  }

  protected String getRespPartySuffixName(HL7MessageMap map, int segIdx) {
    return map.getAtIndex("NK1-2-4", segIdx, 1);
  }

  protected String getNk1_3RelationshipCode(HL7MessageMap map, int index) {
    return map.getAtIndex("NK1-3", index);
  }

  protected String getNk1_20PrimaryLangCode(HL7MessageMap map, int index) {
    return map.getAtIndex("NK1-20", index);
  }

  /**
   * <p>
   * The MCIR implementation guide specifies that the NK1 phone can come from positions 1,6,7
   * <p>
   * It looks like position 1 is a complete phone number, and 6,7 would need to be used together to
   * form a complete phone number.
   *
   * <li>NK1-5 - XTN_IZ - Phone Number (complex)
   * <li>NK1-5-1 - ST - Phone Number:Telephone Number
   * <li>NK1-5-2 - ID - Phone Number:Telecommunication Use Code
   * <li>NK1-5-3 - ID - Phone Number:Telecommunication Equipment Type
   * <li>NK1-5-4 - ST - Phone Number:Email Address
   * <li>NK1-5-5 - NM - Phone Number:Country Code
   * <li>NK1-5-6 - NM - Phone Number:Area/City Code
   * <li>NK1-5-7 - NM - Phone Number:Local Number
   * <li>NK1-5-8 - NM - Phone Number:Extension
   * <li>NK1-5-9 - ST - Phone Number:Any Text
   * <li>NK1-5-10 - ST - Phone Number:Extension Prefix
   * <li>NK1-5-11 - ST - Phone Number:Speed Dial Code
   * <li>NK1-5-12 - ST - Phone Number:Unformatted Telephone number
   * 
   * @param map with message values.
   * @param segIdx index of the segment containing the NK1 you care about.
   * @return a DqaPhoneNumber object
   */
  protected DqaPhoneNumber getNK1Phone(HL7MessageMap map, int segIdx) {
    return hl7Util.getPhoneAt(map, "NK1-5", segIdx);
  }

}
