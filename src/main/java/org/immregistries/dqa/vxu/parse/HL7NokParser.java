package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaNextOfKin;
import org.immregistries.dqa.vxu.DqaAddress;
import org.immregistries.dqa.vxu.DqaPhoneNumber;

public enum HL7NokParser {
	INSTANCE;
	private HL7ParsingUtil hl7Util = HL7ParsingUtil.INSTANCE;
	
	public List<DqaNextOfKin> getNk1List(HL7MessageMap map) {
		List<DqaNextOfKin> nk1s = new ArrayList<DqaNextOfKin>();
		
		Integer segCount = map.getSegmentCount("NK1");
		if (segCount == null || segCount.intValue() == 0) {
			return nk1s; 
		}
		
		for (int x = 1 ; x <= segCount.intValue() ; x++) {
			DqaNextOfKin nextOfKin = getNextOfKin(map, x);
			nextOfKin.setPositionId(x);
			nk1s.add(nextOfKin);
		}
	
		return nk1s;
	}
	
	protected DqaNextOfKin getNextOfKin(HL7MessageMap map, int ordinal) {
		
		DqaNextOfKin nextOfKin = new DqaNextOfKin();
		
		int nk1Idx = map.getAbsoluteIndexForSegment("NK1",  ordinal);
		
//		String nk1Id = map.getAtIndex("NK1-1", nk1Idx);
		
		
		
//		String actionCd = getVaccineAction(map);
		
		//Get the Mother's NK1 segment, if there is one: 
//		int motherIdx = map.findSegmentRepWithValue("MTH", "NK1-3", 1);
		
//		if (motherIdx > 0) {
//			//do all the mother stuff... using this index...
//			String momFirstNm = getNK1FirstNm(map, motherIdx);
//			String momLastNm = getNK1LastNm(map, motherIdx);
////			String momSsnTx = getNK1SSn(map, motherIdx);
//			
//			nextOfKin.setNameFirst(momFirstNm);
//			nextOfKin.setNameLast(momLastNm);
////			trp.setMomSsnTx(momSsnTx);
//		}
		
//		trp.setClientSupportId(clientSupportId);//not used
//		trp.setCreatedBy(createdBy);???
//		trp.setDateCreated(dateCreated);//generated
//		trp.setDateModified(dateModified);//NA
//		trp.setModifiedBy(modifiedBy);//NA
		
//		trp.setRecordId(recordId);//not used???
		
		//This is a transform step. 
//		int respPartIdx = getRespPartyNK1Index(map);
		
		//Interestingly, the RespParty address comes first from the patient, and if it's not there, it comes from the NK1. 
		//I think I'll put that here.  I'll first try to get it from the patient.  And if it comes up blank, I'll get it from the NK1. 
		
		//Changing the patient phone to the NK1 phone is a transform step...
//		String phone = getPatientPhone(map);
		
//		if (phone == null) {
			DqaPhoneNumber phone = getNK1Phone(map, nk1Idx);
//		}
		
		//Then break it apart...  this is strange since we just put it together.
		//This is to be done in the transoform layer. 
//		String[] parts = splitPhoneParts(phone);
//		
//		String respPhoneTx = phone;
//		String respAreaCd = null;
//		if (parts != null && parts.length == 2) {
//			respAreaCd = parts[0];
//			respPhoneTx = parts[1];
//		}
		
		/*
		 * HL7Converter.java
		  protected void readPID11Address(String[] fields, VaccinationUpdateMessage vum, Patient patient, Map<Separator, Character> separators) {
			    if (fields.length <= 11) {
			      return;
			    }
			    String[] repeats = splitRepeats(separators, fields[11]);
			    for (int j = 0; j < repeats.length; j++) {
			      String[] comps = splitComponents(separators, repeats[j]);
			      clean(comps, separators);
			      if (j == 0) {
			        patient.getAddress().setStreet(read(1, comps));
			        patient.getAddress().setStreet2(read(2, comps));
			        patient.getAddress().setCity(read(3, comps));
			        patient.getAddress().setState(read(4, comps));
			        patient.getAddress().setZip(read(5, comps));
			        patient.getAddress().setCountry(read(6, comps));
			        patient.getAddress().setCountyProvince(read(9, comps));
			        vum.addDebugOutput(" + PID-11 address               " + patient.getAddress());
			      } else if (read(7, comps).equals("BDL")) {
			        patient.setBirthCountry(read(6, comps));
			        patient.setBirthCountyProvince(read(9, comps));
			        vum.addDebugOutput(" + PID-11 birth country         " + patient.getBirthCountry());
			        vum.addDebugOutput(" + PID-11 birth county          " + patient.getBirthCountyProvince());
			      }
			    }
			  }
		  
		  */
		
		//Next, let's figure out which address to use. Looks like the rule is: If the patient zip is empty, use the next of kin address. 
		
		//This is a transform step.  This will need to be done in that layer. 
//		respZipTx = getPatientAddressZip(map);
//		if (!StringUtils.isBlank(respZipTx)) {
//			respCityTx = getPatientAddressCity(map);
//			respCntryCd = getPatientAddressCountryCd(map);
//			respStateCd = getPatientAddressStateCd(map);
//			respStreetTx = getPatientAddressStreet(map);
//		} else {
//			respCityTx = getNk1AddressCity(map, respPartIdx);
//			respCntryCd = getNk1AddressCountryCd(map, respPartIdx);
//			respStateCd = getNk1AddressStateCd(map, respPartIdx);
//			respStreetTx = getNk1AddressStreet(map, respPartIdx);
//			respZipTx = getNk1AddressZip(map, respPartIdx);
		
		//NOTE:  We don't actually use the resp party address. 
		
		/* VaccinationUpdateToExtLoader.java
		 *   protected static Address findAddress(VaccinationUpdateMessage message, Patient patient) {
			    Address address = patient.getAddress();
			    if (address.getZip().equals("")) {
			      for (NextOfKin nok : message.getNextOfKins()) {
			        if (!nok.getAddress().getZip().equals("")) {
			          if (nok.getRelationship().equals("") || nok.isRelationshipFather() || nok.isRelationshipGuardian() || nok.isRelationshipParent() || nok.isRelationshipMother()
			              || (nok.isRelationshipSelf() && patient.isChild())) {
			            address = nok.getAddress();
			            break;
			
			          }
			        }
			      }
			    }
			    return address;
			  }
		 */
		
		
		String respFirstNm = getRespPartyFirstName(map, nk1Idx);
		nextOfKin.setNameFirst(respFirstNm);

		String respLastNm = getRespPartyLastName(map, nk1Idx);
		nextOfKin.setNameLast(respLastNm);
		
		String respMiddleNm = getRespPartyMiddleName(map, nk1Idx);
		nextOfKin.setNameMiddle(respMiddleNm);
		
		String respSuffixNm = getRespPartySuffixName(map, nk1Idx);
		nextOfKin.setNameSuffix(respSuffixNm);
		
		//	OR SELF&&patient is child
		//And get that if there isn't a suitable responsible party. 
		
		//Area code - three digits. 
		nextOfKin.setPhone(phone);
//		mother.setRespAreaCd(respAreaCd);
		
		//Seven digits number.
//		mother.setRespPhoneTx(respPhoneTx);
		
		DqaAddress a = this.getNk1Address(map, nk1Idx);
		nextOfKin.setAddress(a);
		
		String relCode = getNk1_3RelationshipCode(map, nk1Idx);
		nextOfKin.setRelationshipCode(relCode);

		String primaryLang = getNk1_20PrimaryLangCode(map, nk1Idx);
		nextOfKin.setPrimaryLanguageCode(primaryLang); 
		
		return nextOfKin;
		
	}
	
	protected DqaAddress getNk1Address(HL7MessageMap map, int index) {
		return hl7Util.getAddressFromIndex(map, "NK1-4", index, 1);
	}
	
	protected String getRespPartySSN(HL7MessageMap map, int segIdx) {
/*HL7Converter.java code to get the SSN: 
 *   
 *   readNK133Id(fields, vum, nok, separators);
 *   readNK137Ssn(fields, vum, nok, separators);
    
    
 *  protected void readNK137Ssn(String[] fields, VaccinationUpdateMessage vum, NextOfKin nok, Map<Separator, Character> separators) {
    if (fields.length <= 37) {
      return;
    }
    if (nok.getSsn().equals("")) {
      nok.setSsn(cleanAndReadField(fields[37], separators));
      if (nok.getSsn().equals("")) {
        vum.addDebugOutput(" + NK1-37 ssn                   " + nok.getSsn());
      }
    }
  }

  protected void readNK133Id(String[] fields, VaccinationUpdateMessage vum, NextOfKin nok, Map<Separator, Character> separators) {
    if (fields.length <= 33) {
      return;
    }
    String[] comps;
    String[] repeats = splitRepeats(separators, fields[33]);
    for (int j = 0; j < repeats.length; j++) {
      comps = splitComponents(separators, repeats[j]);
      clean(comps, separators);
      if (read(5, comps).equals("SS")) {
        nok.setSsn(comps[0]);
        if (nok.getSsn().equals("")) {
          vum.addDebugOutput(" + NK1-33 ssn                   " + nok.getSsn());
        }
        break;
      }
    }
  }
 */
		String ssn = null;
		int fieldRep = map.findFieldRepWithValue("SS", "NK1-33-5", segIdx);
		if (fieldRep > 0) {
			ssn = map.getAtIndex("NK1-33-1", segIdx, fieldRep);
		}
		
		if (StringUtils.isBlank(ssn)) {
			ssn = map.getAtIndex("NK1-37", segIdx, 1);
		}
		
		return ssn;
	}
	
	protected String  getRespPartyFirstName(HL7MessageMap map, int segIdx) {
		String first = map.getAtIndex("NK1-2-2", segIdx, 1);
		return first;
	}
	protected String  getRespPartyLastName(HL7MessageMap map, int segIdx) {
		String last = map.getAtIndex("NK1-2-1", segIdx, 1);
		return last;		
	}
	protected String  getRespPartyMiddleName(HL7MessageMap map, int segIdx) {
		String middle = map.getAtIndex("NK1-2-3", segIdx, 1);
		return middle;
	}
	protected String  getRespPartySuffixName(HL7MessageMap map, int segIdx) {
		String suffix = map.getAtIndex("NK1-2-4", segIdx, 1);
		return suffix;
	}
	
	protected String getNk1_3RelationshipCode(HL7MessageMap map, int index) {
		return map.getAtIndex("NK1-3", index);
	}
	
	protected String getNk1_20PrimaryLangCode(HL7MessageMap map, int index) {
		return map.getAtIndex("NK1-20", index);
	}
	
	/**
	 * <p>The MCIR implentation guide specifies that the NK1 phone can come from positions 1,6,7
	 * <p>It looks like position 1 is a complete phone number, and 6,7 would need to be used together to 
	 * form a complete phone number. 
	 *
	<li>NK1-5 - XTN_IZ - Phone Number (complex)
	<li>NK1-5-1 - ST - Phone Number:Telephone Number  
	<li>NK1-5-2 - ID - Phone Number:Telecommunication Use Code  
	<li>NK1-5-3 - ID - Phone Number:Telecommunication Equipment Type 
	<li>NK1-5-4 - ST - Phone Number:Email Address 
	<li>NK1-5-5 - NM - Phone Number:Country Code  
	<li>NK1-5-6 - NM - Phone Number:Area/City Code  
	<li>NK1-5-7 - NM - Phone Number:Local Number  
	<li>NK1-5-8 - NM - Phone Number:Extension 
	<li>NK1-5-9 - ST - Phone Number:Any Text  
	<li>NK1-5-10 - ST - Phone Number:Extension Prefix 
	<li>NK1-5-11 - ST - Phone Number:Speed Dial Code  
	<li>NK1-5-12 - ST - Phone Number:Unformatted Telephone number 

	 * @param map
	 * @param segIdx
	 * @return
	 */
	protected DqaPhoneNumber getNK1Phone(HL7MessageMap map, int segIdx) {
		return hl7Util.getPhoneAt(map, "NK1-5", segIdx);
	}

}
