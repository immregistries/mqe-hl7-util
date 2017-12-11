package org.immregistries.dqa.vxu.parse;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaAddress;
import org.immregistries.dqa.vxu.DqaPhoneNumber;
import org.immregistries.dqa.vxu.hl7.Id;

public enum HL7ParsingUtil {
	INSTANCE;

	public DqaAddress getAddressFromOrdinal(HL7MessageMap map, String locator,
			int ordinal, int fieldrep) {
		int index = map.getAbsoluteIndexForLocationOrdinal(locator, ordinal);
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
		/*  Previously...
		 *  protected void readNK15Phone(String[] fields, VaccinationUpdateMessage vum, NextOfKin nok, Map<Separator, Character> separators) {
			    if (fields.length <= 5) {
			      return;
			    }
			    nok.setPhone(cleanAndReadField(fields[5], separators));
			    vum.addDebugOutput(" + NK1-5  phone                 " + nok.getPhone());
			  }
		 */
		
		DqaPhoneNumber ph = new DqaPhoneNumber();
		
		//These aren't often set. But we should capture them if they are. 
		String telUseCode = map.getAtIndex(fieldLocator + "-2", segIdx, 1);
		String telEquipCode = map.getAtIndex(fieldLocator + "-3", segIdx, 1);
		String email = map.getAtIndex(fieldLocator + "-4", segIdx, 1);
		
		ph.setTelEquipCode(telEquipCode);
		ph.setTelUseCode(telUseCode);
		ph.setEmail(email);
		
		//As of version 2.3, the number should not be present in the first field.  it is deprecated. 
		//		we will check the current positions first. 
		String localNumber = map.getAtIndex(fieldLocator + "-7", segIdx, 1);
		
		if (localNumber != null) {
			ph.setLocalNumber(localNumber);
			
			String areaCode = map.getAtIndex(fieldLocator + "-6", segIdx, 1);
			ph.setAreaCode(areaCode);
			
		} else {
			
			//This is what was originally happening. 
			String phone = map.getAtIndex(fieldLocator, segIdx, 1);
			ph.setNumber(phone);
		}

		return ph;
	}
	
	protected String[] splitPhoneParts(String value) {
		//this is taken from VaccinationUpdateToExtLoader.java method called "breakPhoneApart"
			    String[] values = new String[2];
			    String phone = "";
			    char[] digits = value.toCharArray();
			    for (char c : digits) {
			      if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
			        break;
			      }
			      if (c >= '0' && c <= '9') {
			        phone += c;
			      }
			    }
			    if (phone.length() <= 7) {
			      values[0] = "";
			      values[1] = phone;
			    } else {
			      if (phone.length() == 11 && phone.startsWith("1")) {
			        // remove initial 1, it is not needed
			        phone = phone.substring(1);
			      }
			      values[0] = phone.substring(0, phone.length() - 7);
			      values[1] = phone.substring(phone.length() - 7);
			    }
			    return values;
	}
	
	/**
	 * Taken directly from VaccinationUpdateToExtLoader.java in oder to preserve the treatment of dates.
	 * <br /><br />This always returns YYYYMMDD.  There are two possible incoming formats (ignoring spaces): 
	 * <ol><li>All numeric<li>MM/DD/YYYY
	 * </ol>
	 * <br />
	 * What this does: 
	 * <li>removes spaces
	 * <li>removes non-numbers
	 * <li>if there aren't 8 numbers, returns ""
	 * <li>Only uses the first 8 digits
	 * <li>If there are "/" in the non-numeric chars, It must be of the format: {1-2}/{1-2}/{4}. Meaning 1 to 2 digits in the first and second section. four in the third.  
	 * @param date
	 * @return
	 */
		public String dateNormalizer(String date) {
			
			if (date == null) {
				return "";
			}
			
		    String nonNumbers = "";//date.replaceAll("\\d+", "");
		    String numbers = "";//date.replaceAll("\\D+", "");;
		    char[] digits = date.toCharArray();
		    
		    //Loop through the characters in the date string. 
		    for (char c : digits) {
		      //Remove spaces by no adding them to the list. 
		      if (c == ' ') {
		        continue;
		      }
		      if (c >= '0' && c <= '9') {
		    	//Add any numbers. 
		        numbers += c;
		        continue;
		      }
		      nonNumbers += c;
		    }
		    
		    if (nonNumbers.length() == 0) {
		      // Assumed to be YYYYMMDD format
		      if (numbers.length() < 8) {
		        return "";
		      }
		      if (numbers.length() > 8) {
		        numbers = numbers.substring(0, 8);
		      }
		      return numbers;//return the first 8 numbers in the character string. 
		      
		    } else if (nonNumbers.startsWith("//")) {
		      //Figure out how it's formatted.  
		      int i = date.indexOf("/");
		      String month = date.substring(0, i).trim();
		      if (month.length() == 1) {
		        month = "0" + month;
		      } else if (month.length() != 2) {
		    	//assumes it's formatted wrong?
		        return "";
		      }
		      
		      //Get the next digits. 
		      i++;
		      int j = date.indexOf("/", i);
		      String day = date.substring(i, j).trim();
		      
		      //second chunk has to be 1 or 2 chars long. 
		      if (day.length() == 1) {
		        day = "0" + day;
		      } else if (day.length() != 2) {
		        return "";
		      }
		      
		      i = j + 1;
		      j = i + 4;
		      if (j > date.length()) {
		        j = date.length();
		      }
		      String year = date.substring(i, j);
		      //Year has to be 4 digits long. 
		      if (year.length() != 4) {
		        return "";
		      }
		      try {
		        Integer.parseInt(year);
		        Integer.parseInt(month);
		        Integer.parseInt(day);
		      } catch (NumberFormatException nfe) {
		        return "";
		      }
		      return year + month + day;
		    } else {
		      return "";
		    }
		  }	
	
	public Id getId(HL7MessageMap map, String field, int segIdx, int fieldRep) {
		Id ce = new Id();
		ce.setNumber(map.getAtIndex(field + "-1", segIdx, fieldRep));
		ce.setAssigningAuthorityCode(map.getAtIndex(field + "-4", segIdx,
				fieldRep));
		ce.setTypeCode(map.getAtIndex(field + "-5", segIdx, fieldRep));
		return ce;
	}

	public DqaAddress getAddressFromIndex(HL7MessageMap map, String locator, int segmentIndex, int fieldRep) {
		DqaAddress address = new DqaAddress();
        address.setStreet(map.getAtIndex(locator+ "-1", segmentIndex, fieldRep));
        address.setStreet2(map.getAtIndex(locator+ "-2", segmentIndex, fieldRep));
        address.setCity(map.getAtIndex(locator+ "-3", segmentIndex, fieldRep));
        address.setStateCode(map.getAtIndex(locator+ "-4", segmentIndex, fieldRep));
        address.setZip(map.getAtIndex(locator+ "-5", segmentIndex, fieldRep));
        address.setCountryCode(map.getAtIndex(locator+ "-6", segmentIndex, fieldRep));
        address.setTypeCode(map.getAtIndex(locator+ "-7", segmentIndex, fieldRep));
        address.setCountyParishCode(map.getAtIndex(locator+ "-8", segmentIndex, fieldRep));
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
