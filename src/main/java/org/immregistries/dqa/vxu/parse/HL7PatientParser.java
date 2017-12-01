package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.DqaAddress;
import org.immregistries.dqa.vxu.hl7.Id;
import org.immregistries.dqa.vxu.DqaPatientAddress;
import org.immregistries.dqa.vxu.hl7.PatientIdNumber;
import org.immregistries.dqa.vxu.DqaPhoneNumber;

public enum HL7PatientParser {

	INSTANCE;
	
	private HL7ParsingUtil hl7Util = HL7ParsingUtil.INSTANCE;
	
	public DqaPatient getPatient(HL7MessageMap map) {
		DqaPatient patient = new DqaPatient();
		
		
		//pull all the single value and coded fields. 
		//Get the ID's out of PID-3-5
		PatientIdNumber childId 	= getSRPatId(map);//PID-3
		PatientIdNumber medicaidId 	= getMAPatId(map);//PID-3
		PatientIdNumber patId 		= getMRPatId(map);//PID-3
		PatientIdNumber ssnTx 		= getSSPatId(map);//PID-3
		PatientIdNumber wicId		= getWCPatId(map);//PID-3
		
		String legalLastNm 		= map.get("PID-5-1");//getLegalLastNm(map);
		String legalFirstNm 	= map.get("PID-5-2");//getLegalFirstNm(map);
		String legalMiddleNm 	= map.get("PID-5-3");//getLegalMiddleNm(map);
		String legalSuffixNm 	= map.get("PID-5-4");//getLegalSuffixNm(map);
		String legalNmTypeCd    = map.get("PID-5-7");//name type code. 
		String momsMaidenNm 	= map.get("PID-6");//getMomsMaidenNm(map);
		String birthDt 			= map.get("PID-7");//getPID7BirthDt(map);
		String sexCd 			= map.get("PID-8");//getSexCd(map);
		
		String raceCd 		= map.get("PID-10");//getCodedEntity(map, "PID-10", CodesetType.PATIENT_RACE);//getPID10Race(map);
		
		//Get the address list.  PID-11 
		List<DqaPatientAddress> patientAddresses = getPID_11PatientAddressList(map);
		patient.getPatientAddressList().addAll(patientAddresses);
		
		String language 	= map.get("PID-15");//getCodedEntity(map, "PID-15", CodesetType.PERSON_LANGUAGE);//getPID15PrimaryLanguage(map);
		String ethnicCd 	= map.get("PID-22");//getCodedEntity(map, "PID-22", CodesetType.PATIENT_ETHNICITY);//getPID22EthnicCd(map);
		
		String birthHospNm 		= map.get("PID-23");//getPID23BirthHostNm(map);
		String birthMultCode 	= map.get("PID-24");//getPID24BirthMultiple(map);
		String birthOrderNumber 	= map.get("PID-25");//getPID25BirthOrder(map);
		String deathDt 			= map.get("PID-29");
 		String deathInd 		= map.get("PID-30");
		
		String mogeStatusCode 	= map.get("PD1-16");//getPD1_16MogeCode(map);
		String publicityCode 	= map.get("PD1-11");
		String protectionCode 	= map.get("PD1-12");
		
		String pv1ClassCode 	= map.get("PV1-2");//getPV1Class(map);
		String pv1VFCCode 		= map.get("PV1-20");//getPatientVaccineFundEligCd(map);
		String pv1VFCEligDate 	= map.get("PV1-20-2");//getPatientVaccineFundEligEffDate(map);
		
		DqaPhoneNumber phone = getPatientPhone(map);
		
		patient.setPhone(phone);
		
		patient.setIdRegistry(childId);//PID-3-1 when PID-3-5 = "SR"
		patient.setIdMedicaid(medicaidId);//PID-3-1 when PID-3-5 = 'MA'
		patient.setIdSubmitter(patId);//PID-3-1 when PID-3-5 = 'MR'
		patient.setIdSsn(ssnTx);//PID-3-1 when PID-3-5 = 'SS'
		patient.setIdWic(wicId);//PID-3-1 when PID-3-5 =’WC'
		//TODO: Decide if I need birth county code here...  I think I should just get all the address repetitions...
		patient.setBirthDateString(birthDt);
		patient.setBirthPlace(birthHospNm);//PID-23
		patient.setBirthMultipleIndicator(birthMultCode);//PID-24
		patient.setBirthOrderNumber(birthOrderNumber);//PID-25
		patient.setBirthCounty(getBirthCounty(map));//PID-11-9 when PID-11-7 is BDL
		patient.setDeathDateString(deathDt);//PID-29
		patient.setDeathIndicator(deathInd);
		
		patient.setEthnicity(ethnicCd);//PID-22 first repetition...  CE not being used?
		patient.setNameFirst(legalFirstNm);//PID-5-2, first repetition 
		patient.setNameLast(legalLastNm);//PID-5-1, first repetition
		patient.setNameMiddle(legalMiddleNm);//PID-5-3, first repetition
		patient.setNameSuffix(legalSuffixNm);//PID-5-4, first repetition
		patient.setNameTypeCode(legalNmTypeCd);
		patient.setMotherMaidenName(momsMaidenNm);//PID-6
		patient.setRace(raceCd);//PID-10
		patient.setSexCode(sexCd);//PID-8
		patient.setPrimaryLanguage(language);//PID-15
		patient.setRegistryStatusCode(mogeStatusCode);;//PD1-16 Immunization Registry Status

		//PV1 values:
		patient.setFinancialEligibilityCode(pv1VFCCode);
		patient.setFinancialEligibilityDateString(pv1VFCEligDate);
		patient.setPatientClassCode(pv1ClassCode);
		
		//PD1 values: 
		patient.setProtectionCode(protectionCode);
		patient.setPublicityCode(publicityCode);
		
		return patient;
		
		//Transformations:
//		String messageAction = getVaccineAction(map);
//		patient.setCntyCd(cntyCd);//PID-11-9 in first repetition of PID-11
	}
	
	protected String getBirthCounty(HL7MessageMap map) {
		int fieldRep = map.findFieldRepWithValue("BDL",  "PID-11-7",  1);
		String birthCounty = null;
		if (fieldRep > 0) { 
			birthCounty = map.getAtIndex("PID-11-9", 1, fieldRep);
		}
		
		return birthCounty;
		
	}
	
	/**
	 * Values and Descriptions from Table 0441 - Immunization registry status (9/2014): 
	 * <ul>
	 * <li>A - Accepted
	 * <li>I - Inactive
	 * <li>L - Lost to follow up
	 * <li>M - Inactive-moved or gone elsewhere
	 * <li>P - Inactive-Permanently Inactive (do not reactivate or add new entries to this record)
	 * <li>U - Unknown
	 * 
	 * <br />
	 * Note: P generally indicates that the patient is deceased. 
	 * @param map
	 * @return
	 */
	private String getPD1_16MogeCode(HL7MessageMap map) {
		String mogeCode = map.get("PD1-16");
		return mogeCode;
	}
	

//	protected String getPID7BirthDt(HL7MessageMap map) {
//		String birthDt = map.get("PID-7");
//		return trimStringToLength(birthDt, 8);
//	}
//
//	protected CodedEntity getPID15PrimaryLanguage(HL7MessageMap map) {
//		return getCodedEntity(map, "PID-15", CodesetType.PERSON_LANGUAGE);
//	}
//	
//	protected CodedEntity getPID22EthnicCd(HL7MessageMap map) {
//		String ethnicCdField ="PID-22";
//		CodesetType type = CodesetType.PATIENT_ETHNICITY;
//		return getCodedEntity(map, ethnicCdField, type);
//	}
//	
//	protected String getPID24BirthMultiple(HL7MessageMap map) {
//		String value = map.get("PID-24");
//		return value;
//	}
//	
//	protected String getPID23BirthHostNm(HL7MessageMap map) {
//		String birthHospNm = map.get("PID-23");
//		return birthHospNm;
//	}
//
//	protected String getPID25BirthOrder(HL7MessageMap map) {
//		String value = map.get("PID-25");
//		return value;
//	}
//	
//	protected String getPID29DeathDt(HL7MessageMap map) {
//		String deathDt =  map.get("PID-29");
//		return trimStringToLength(deathDt, 8);
//	}
//	
//	protected String getPID30DeathInd(HL7MessageMap map) {
//		String deathDt =  map.get("PID-30");
//		return trimStringToLength(deathDt, 8);
//	}
//	
	
	protected DqaPhoneNumber getPatientPhone(HL7MessageMap map) {
		/*HL7Converter for interpreting the phone in the PID segment
		 *   protected String readPhone(String field, Map<Separator, Character> separators) {
			    String phone = "";
			    field = cleanRepeatsOnly(field, separators);
			    String[] comps = splitComponents(separators, field);
			    clean(comps, separators);
			    if (comps.length > 7 && comps[6].length() > 0) {
			      phone = "(" + comps[5] + ")" + comps[6];
			    } else {
			      phone = comps.length == 0 ? "" : comps[0];
			    }
			    return phone;
			  }
		 */
		//So.. if it has 6 and 7, use that.  otherwise, check the first ones.
		int pidIdx = map.getAbsoluteIndexForSegment("PID",  1);
		return hl7Util.getPhoneAt(map, "PID-13", pidIdx);
//		The first instance shall be primary according to implementaiton guide. 
	}
	
	protected PatientIdNumber getWCPatId(HL7MessageMap map) {
		return getPatientIdType("WC", map);
	}
	
	protected PatientIdNumber getMRPatId(HL7MessageMap map) {
		return getPatientIdType("MR", map);
	}
	
	protected PatientIdNumber getSSPatId(HL7MessageMap map) {
		return getPatientIdType("SS", map);
	}
	
	protected PatientIdNumber getMAPatId(HL7MessageMap map) {
		return getPatientIdType("MA", map);
	}
	
	protected PatientIdNumber getSRPatId(HL7MessageMap map) {
		return getPatientIdType("SR", map);
	}
	
	protected PatientIdNumber getPatientIdType(String type, HL7MessageMap map) {
		int i = map.findFieldRepWithValue(type, "PID-3-5", 1);
		
		if (i > 0) {
			Id id = hl7Util.getId(map, "PID-3-1", 1, CodesetType.PATIENT_ID, i);
			
			PatientIdNumber num = new PatientIdNumber(id, i);
			
			return num;
		}
		
		return new PatientIdNumber();
	}
	
	protected List<DqaPatientAddress> getPID_11PatientAddressList(HL7MessageMap map) {
		List<DqaPatientAddress> list = new ArrayList<DqaPatientAddress>();
		
		int fieldReps = map.getFieldRepCountFor("PID-11");
		
		for (int x = 1 ; x <= fieldReps; x++) {
			list.add(getPatientAddress(map, x));
		}
		return list;
	}
	
	protected DqaPatientAddress getPatientAddress(HL7MessageMap map, int fieldRep) {
		
		DqaAddress a = hl7Util.getAddressFromOrdinal(map, "PID-11", 1, fieldRep);
		DqaPatientAddress address = new DqaPatientAddress(a);
        
		return address;
	}
}
