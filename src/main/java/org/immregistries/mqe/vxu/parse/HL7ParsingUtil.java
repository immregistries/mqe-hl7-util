package org.immregistries.mqe.vxu.parse;

import org.immregistries.mqe.hl7util.parser.HL7MessageMap;
import org.immregistries.mqe.vxu.MqeAddress;
import org.immregistries.mqe.vxu.MqePhoneNumber;
import org.immregistries.mqe.vxu.hl7.Id;

public enum HL7ParsingUtil {
  INSTANCE;

  public MqeAddress getAddressFor(HL7MessageMap map, String locator, int seq, int fieldrep) {
    int index = map.getLineFromSequence(locator, seq);
    return getAddressFromLine(map, locator, index, fieldrep);
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
  public MqePhoneNumber getPhoneAt(HL7MessageMap map, String fieldLocator, int line) {
    MqePhoneNumber ph = new MqePhoneNumber();

    //These aren't often set. But we should capture them if they are.
    String telUseCode = map.getValue(fieldLocator + "-2", line, 1);
    String telEquipCode = map.getValue(fieldLocator + "-3", line, 1);
    String email = map.getValue(fieldLocator + "-4", line, 1);

    ph.setTelEquipCode(telEquipCode);
    ph.setTelUseCode(telUseCode);
    ph.setEmail(email);

    //As of version 2.3, the number should not be present in the first field.  it is deprecated.
    //		we will check the current positions first.
    String localNumber = map.getValue(fieldLocator + "-7", line, 1);

    if (localNumber != null) {
      ph.setLocalNumber(localNumber);

      String areaCode = map.getValue(fieldLocator + "-6", line, 1);
      ph.setAreaCode(areaCode);

    } else {
      //This is what was originally happening.
      String phone = map.getValue(fieldLocator, line, 1);
      ph.setNumber(phone);
    }

    return ph;
  }

  public Id getId(HL7MessageMap map, String field, int line, int fieldRep) {
    Id ce = new Id();
    ce.setNumber(map.getValue(field + "-1", line, fieldRep));
    ce.setAssigningAuthorityCode(map.getValue(field + "-4", line,
        fieldRep));
    ce.setTypeCode(map.getValue(field + "-5", line, fieldRep));
    return ce;
  }

  public MqeAddress getAddressFromLine(HL7MessageMap map, String locator, int line,
      int fieldRep) {
    MqeAddress address = new MqeAddress();
    address.setStreet(map.getValue(locator + "-1", line, fieldRep));
    address.setStreet2(map.getValue(locator + "-2", line, fieldRep));
    address.setCity(map.getValue(locator + "-3", line, fieldRep));
    address.setStateCode(map.getValue(locator + "-4", line, fieldRep));
    address.setZip(map.getValue(locator + "-5", line, fieldRep));
    address.setCountryCode(map.getValue(locator + "-6", line, fieldRep));
    address.setTypeCode(map.getValue(locator + "-7", line, fieldRep));
    address.setCountyParishCode(map.getValue(locator + "-8", line, fieldRep));
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
