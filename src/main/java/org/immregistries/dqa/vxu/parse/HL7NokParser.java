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
  private MetaParser mp = MetaParser.INSTANCE;
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

    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_PHONE, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_PHONE_AREA_CODE, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_PHONE_LOCAL_NUMBER, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_PHONE_TEL_EQUIP_CODE, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_PHONE_TEL_USE_CODE, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_NAME_FIRST, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_NAME_LAST, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_NAME_MIDDLE, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_NAME_SUFFIX, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_ADDRESS_CITY, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_ADDRESS_COUNTY, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_ADDRESS_COUNTRY, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_ADDRESS_STATE, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_ADDRESS_STREET, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_ADDRESS_STREET2, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_ADDRESS_TYPE, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_ADDRESS_ZIP, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_RELATIONSHIP, map));
    nextOfKin.setField(mp.mapValue(nk1Idx, VxuField.NEXT_OF_KIN_PRIMARY_LANGUAGE, map));
    return nextOfKin;
  }

}
