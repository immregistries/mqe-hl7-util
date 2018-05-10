package org.immregistries.dqa.vxu.parse;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaAddress;
import org.immregistries.dqa.vxu.DqaPhoneNumber;
import org.immregistries.dqa.vxu.hl7.Id;

public enum HL7ParsingUtil {
  INSTANCE;

  public DqaAddress getAddressFromOrdinal(HL7MessageMap map, String locator,
      int ordinal, int fieldrep) {
    int index = map.getLineNumberForSegmentSequenceInLoc(locator, ordinal);
    return getAddressFromIndex(map, locator, index, fieldrep);
  }

  /*  Values from value set HL7 0201 for tel-use-code (component 2)
   *  PRN Primary Residence Number R
   *	ORN Other Residence Number R
   *	WPN Work Number R
   *	VHN Vacation Home Number R
   *	ASN Answering Service Number R
   *	EMR Emergency Number R
   *	NET Network (email) address
   *	PRS Personal R
   *	BPN Beeper number
   */
  /*
   * Values from value set HL7 0202 for tel-equipment-code (component 3)
   * PH Telephone
   * FX Fax
   * MD Modem
   * CP Cellular or Mobile Phone
   * BP Beeper
   * Internet Internet Address
   * X.400 X.400 email address
   * TDD Telecommunications Device for the Deaf
   * TTY Teletypewriter
   */
  public DqaPhoneNumber getPhoneAt(HL7MessageMap map, String fieldLocator, int segIdx) {
    DqaPhoneNumber ph = new DqaPhoneNumber();

    //These aren't often set. But we should capture them if they are.
    String telUseCode = map.getAtLine(fieldLocator + "-2", segIdx, 1);
    String telEquipCode = map.getAtLine(fieldLocator + "-3", segIdx, 1);
    String email = map.getAtLine(fieldLocator + "-4", segIdx, 1);

    ph.setTelEquipCode(telEquipCode);
    ph.setTelUseCode(telUseCode);
    ph.setEmail(email);

    //As of version 2.3, the number should not be present in the first field.  it is deprecated.
    //		we will check the current positions first.
    String localNumber = map.getAtLine(fieldLocator + "-7", segIdx, 1);

    if (localNumber != null) {
      ph.setLocalNumber(localNumber);

      String areaCode = map.getAtLine(fieldLocator + "-6", segIdx, 1);
      ph.setAreaCode(areaCode);

    } else {
      //This is what was originally happening.
      String phone = map.getAtLine(fieldLocator, segIdx, 1);
      ph.setNumber(phone);
    }

    return ph;
  }

  public Id getId(HL7MessageMap map, String field, int segIdx, int fieldRep) {
    Id ce = new Id();
    ce.setNumber(map.getAtLine(field + "-1", segIdx, fieldRep));
    ce.setAssigningAuthorityCode(map.getAtLine(field + "-4", segIdx,
        fieldRep));
    ce.setTypeCode(map.getAtLine(field + "-5", segIdx, fieldRep));
    return ce;
  }

  public DqaAddress getAddressFromIndex(HL7MessageMap map, String locator, int segmentIndex,
      int fieldRep) {
    DqaAddress address = new DqaAddress();
    address.setStreet(map.getAtLine(locator + "-1", segmentIndex, fieldRep));
    address.setStreet2(map.getAtLine(locator + "-2", segmentIndex, fieldRep));
    address.setCity(map.getAtLine(locator + "-3", segmentIndex, fieldRep));
    address.setStateCode(map.getAtLine(locator + "-4", segmentIndex, fieldRep));
    address.setZip(map.getAtLine(locator + "-5", segmentIndex, fieldRep));
    address.setCountryCode(map.getAtLine(locator + "-6", segmentIndex, fieldRep));
    address.setTypeCode(map.getAtLine(locator + "-7", segmentIndex, fieldRep));
    address.setCountyParishCode(map.getAtLine(locator + "-8", segmentIndex, fieldRep));
    return address;
  }

  public static String escapeHL7Chars(String s) {
    if (s == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (char c : s.toCharArray()) {
      if (c >= ' ') {
        switch (c) {
          case '~':
            sb.append("\\R\\");
            break;
          case '\\':
            sb.append("\\E\\");
            break;
          case '|':
            sb.append("\\F\\");
            break;
          case '^':
            sb.append("\\S\\");
            break;
          case '&':
            sb.append("\\T\\");
            break;
          default:
            sb.append(c);
        }
      }
    }
    return sb.toString();
  }

}
