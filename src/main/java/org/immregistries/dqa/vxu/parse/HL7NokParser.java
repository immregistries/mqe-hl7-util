package org.immregistries.dqa.vxu.parse;

import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_ADDRESS_CITY;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_ADDRESS_COUNTRY;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_ADDRESS_COUNTY;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_ADDRESS_STATE;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_ADDRESS_STREET;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_ADDRESS_STREET2;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_ADDRESS_TYPE;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_ADDRESS_ZIP;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_NAME_FIRST;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_NAME_LAST;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_NAME_MIDDLE;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_NAME_SUFFIX;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_PHONE;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_PHONE_AREA_CODE;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_PHONE_LOCAL_NUMBER;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_PHONE_TEL_EQUIP_CODE;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_PHONE_TEL_USE_CODE;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_PRIMARY_LANGUAGE;
import static org.immregistries.dqa.vxu.VxuField.NEXT_OF_KIN_RELATIONSHIP;

import java.util.ArrayList;
import java.util.List;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaNextOfKin;

public enum HL7NokParser {
  INSTANCE;

  /**
   * This will take a map of HL7 values, and build a List of Next Of Kin objects out of it. <p> It
   * will create one object for each NK1 segment, and add it to a list </p> <p> The order of the
   * segments will be preserved in the list. </p>
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
   * Gets a single next of kin object from the message map. <p> It takes the ordinal given (starting
   * with 1), and gets that 1st,2nd, etc next of kin object </p>
   *
   * @param map containing all of the data for a VXU message.
   * @param segSequence the ordinal position of the NK1 you're interested in.
   * @return a next of kin object populated with data from the map.
   */
  protected DqaNextOfKin getNextOfKin(HL7MessageMap map, int segSequence) {
    int line = map.getLineFromSequence("NK1", segSequence);
//    int nk1Idx = map.getLineFromSequence("NK1", ordinal);
    MetaParser mp = new MetaParser(map);
    List<MetaFieldInfo> fieldInfo = mp.mapValues(line,
        NEXT_OF_KIN_PHONE
        , NEXT_OF_KIN_PHONE_AREA_CODE
        , NEXT_OF_KIN_PHONE_LOCAL_NUMBER
        , NEXT_OF_KIN_PHONE_TEL_EQUIP_CODE
        , NEXT_OF_KIN_PHONE_TEL_USE_CODE
        , NEXT_OF_KIN_NAME_FIRST
        , NEXT_OF_KIN_NAME_LAST
        , NEXT_OF_KIN_NAME_MIDDLE
        , NEXT_OF_KIN_NAME_SUFFIX
        , NEXT_OF_KIN_ADDRESS_CITY
        , NEXT_OF_KIN_ADDRESS_COUNTY
        , NEXT_OF_KIN_ADDRESS_COUNTRY
        , NEXT_OF_KIN_ADDRESS_STATE
        , NEXT_OF_KIN_ADDRESS_STREET
        , NEXT_OF_KIN_ADDRESS_STREET2
        , NEXT_OF_KIN_ADDRESS_TYPE
        , NEXT_OF_KIN_ADDRESS_ZIP
        , NEXT_OF_KIN_RELATIONSHIP
        , NEXT_OF_KIN_PRIMARY_LANGUAGE);

    DqaNextOfKin nextOfKin = new DqaNextOfKin();
    nextOfKin.setPositionId(segSequence);
    nextOfKin.setFields(fieldInfo);
    return nextOfKin;
  }

}
