package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.hl7.CodedEntity;
import org.immregistries.dqa.vxu.hl7.Observation;
import org.immregistries.dqa.vxu.hl7.OrganizationName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum HL7VaccineParser {
	INSTANCE;
	
	protected static final String OBX3_DISEASE_WITH_PRESUMED_IMMUNITY = "59784-9";
	protected static final String OBX3_DISEASE_WITH_SEROLOGICAL_EVIDENCE_OF_IMMUNITY = "75505-8";
	private static final String VACCINATION_ACTION_ADD = "A";
	
	private static final Logger logger = LoggerFactory.getLogger(HL7VaccineParser.class);
	
	private HL7Util hl7Util = HL7Util.INSTANCE;
	
	//For example, the second RXA has an associated OBX, and RXR. 
	//So I should be able to fairly simply find the indexes which are greater 
	//than the current, and less than the next. 

	//The goal is to find all the segments between this and the next RXA segment. 
	//The idea is to get an arraylist of the segments...  in order... 
	//then I could just look for the index of the next RXA, and loop through 
	//the stuff between here and there. 
	
	//So the core problem is getting the segment names between here and there. 
	//once I know the segment name, and segment index, 
	//I can grab the relevant data from it.  
	
	//The rules according to the MCIR guide are that the ORC comes first
	//Then the RXA, and RXR (recommended, but apparently optional), 
	//And the OBX, which is required if RXA-9 = '00'.  The NTE segment is ingnored. 
	
	//So, The ORC should come before each RXA. 
	
//	So Maybe I could figure out a profile, and then grab it...  
//	the profile would be: 
//		check for something that preferably starts with ORC, and if not that, then RXA. 
//			Then it contains everything until the next match of ORC or RXA
//			Get that from the map object... it'd have to be a list of segment and index...
//			Maybe it can be a list of segments...
//	
			
	//So the algorithm goes thusly: 
		//Loop the list.  find the first ORC or RXA.  At the next RXA or ORC, 
		//send the last indexes into the method to build a TRANSFER_SHOT record. 
	/* (non-Javadoc)
	 * @see gov.mi.mdch.xfer.rewrite.TransferMessageService#getAllTransferShots(gov.mi.mdch.immunization.message.HL7MessageMap)
	 */
	public List<DqaVaccination> getVaccinationList(HL7MessageMap map) {
		//Start a list of shots
		List<DqaVaccination> shotList = new ArrayList<DqaVaccination>();
		
		//Get the list of segments so you know the total length. 
		int segmentListSize = map.getMessageSegments().size();
		
		int startingPoint = map.getNextImmunizationStartingIndex(0);

		while (startingPoint < segmentListSize) {
			int nextStartingPoint = map.getNextImmunizationStartingIndex(startingPoint);
			int finishPoint = nextStartingPoint - 1;
			DqaVaccination ts = getVaccination(map, startingPoint, finishPoint);
			shotList.add(ts);
			startingPoint = nextStartingPoint;
			//Theory: This will naturally finish the process to the end of the segments.
		}
		
		return shotList;
	}
	
	/**
	 * this is going to the be the most complicated process.  There are dependent segments
	 * and you need to read those into the Trasnfer_Shot object to create a whole shot record.
	 * 
	 * <p>Notice, a lot of the HL7 locations were represented in HL7Converter.java in this method: 
	 * 
	 * <code>
	<br />private void readRXA(String[] fields, VaccinationUpdateMessage vum, Vaccination vaccination, Map<Separator, Character> separators) {
	<br />vum.addDebugOutput("[RXA] Perscription Administration");
	<br />readRXA3AdminDate(fields, vum, vaccination, separators);
    <br />readRXA5Vaccination(fields, vum, vaccination, separators);
    <br />readRXA6AdministeredAmount(fields, vum, vaccination, separators);
    <br />readRXA9AdminNotes(fields, vum, vaccination, separators);
    <br />readRXA10Provider(fields, vum, vaccination, separators);
    <br />readRXA11AdministeredAtLocation(fields, vum, vaccination, separators);
    <br />readRXA15LotNumber(fields, vum, vaccination, separators);
    <br />readRXA16LotExpirationDate(fields, vum, vaccination, separators);
    <br />readRXA17Manufacturer(fields, vum, vaccination, separators);
    <br />readRXA18RefusalReason(fields, vum, vaccination, separators);
    <br />readRXA20CompletionStatus(fields, vum, vaccination, separators);
    <br />readRXA22EntryDateTime(fields, vum, vaccination, separators);
    <br />readRXA21ActionCode(fields, vum, vaccination, separators);
    <br />readRXA22EntryDateTime(fields, vum, vaccination, separators);
  <br />}
  </code>
	 * @param map
	 * @return
	 */
	public DqaVaccination getVaccination(HL7MessageMap map, int vaccinationStartSegment, int vaccinationFinishSegment) {
		
		DqaVaccination shot = new DqaVaccination();
		
		//So my task here is to find the index of the:
		//ORC
		//RXA
		//RXR
		//OBX's
		//Maybe NTE segments that we ignore. 
		List<String> segments = map.getMessageSegments();

		int orcIdx = -1;
		int rxaIdx = -1;
		int rxrIdx = -1;
		List<Integer> obxIdxList = new ArrayList<Integer>();
		
		//We're going to operate with segment indexes.  
		for (int i = vaccinationStartSegment; i <= vaccinationFinishSegment; i++) {
			String segName = segments.get(i);
			switch (segName) {
				case "ORC": orcIdx = i; break;
				case "RXA": rxaIdx = i; break;
				case "RXR": rxrIdx = i; break;
				case "OBX": obxIdxList.add(i); break;
			}
		} 
		
		logger.info("Segment ID's being used for this shot: ORC[" + orcIdx + "] RXA[" + rxaIdx + "] RXR[" + rxrIdx + "] OBX{" + obxIdxList + "}");

		//Find the vaccine code. This is kind of a big deal.
		List<CodedEntity> vxList = getVaccineCodes(map, rxaIdx);
		
		boolean codeFound = false;
		
		for (CodedEntity code : vxList) {
			    if ("CVX"		.equals(code.getTable()) 
	    		||  "CVX"		.equals(code.getAltTable()) 
			    ||  "HL70292"	.equals(code.getTable())
			    ||  "HL70292"	.equals(code.getAltTable()))
			    {
			      shot.setAdminCvxCode(code.getCode());
//			      shot.getAdminCvx().setText(code.getText());
//			      shot.getAdminCvx().setTable(code.getTable());
			      codeFound = true;
			    }
			    else if ("CPT"		.equals(code.getTable()) 
	    		||  "CPT"		.equals(code.getAltTable()) 
	    		||  "C4"	.equals(code.getTable())
	    		||  "C4"	.equals(code.getAltTable()))
			    {
			      shot.setAdminCptCode(code.getCode());
//			      shot.getAdminCpt().setText(code.getText());
//			      shot.getAdminCpt().setTable(code.getTable());
			      codeFound = true;
			    }
			    else if ("NDC"		.equals(code.getTable()) 
	    		||  "NDC"		.equals(code.getAltTable()) 
	    		||  "?"	.equals(code.getTable())
	    		||  "?"	.equals(code.getAltTable()))
			    {
			      shot.setAdminNdcCode(code.getCode());
//			      shot.getAdminNdc().setText(code.getText());
//			      shot.getAdminNdc().setTable(code.getTable());
			      codeFound = true;
			    }
		}
		
//		String facilityCd   = map.get("RXA-");//getMSH4SendingFacility(map); This is a transformation...
		
		String messageRxaCnt= map.getAtIndex("RXA-1", rxaIdx);
		String subMsgRxaCnt = map.getAtIndex("RXA-2", rxaIdx);
		String shotDt 		= map.getAtIndex("RXA-3", rxaIdx);//getShotDt(map, rxaIdx);
		String shotDtEnd 	= map.getAtIndex("RXA-4", rxaIdx);//getShotDt(map, rxaIdx);
		String doseVl 		= map.getAtIndex("RXA-6", rxaIdx);//getAdministeredAmount(map, rxaIdx);
		String doseVlUnit	= map.getAtIndex("RXA-7", rxaIdx);
		String source 		= map.getAtIndex("RXA-9", rxaIdx);//getCodedEntity(map, "RXA-9",  rxaIdx, CodesetType.VACCINATION_INFORMATION_SOURCE);
//		String sourceId 	= map.getAtIndex("RXA-10-1", rxaIdx);//getProviderId(map, rxaIdx);
		OrganizationName org= readOrganizationName(map, "RXA-11", rxaIdx);
		String doseLotTx 	= map.getAtIndex("RXA-15", rxaIdx);//getVaccineDoseLot(map, rxaIdx);
		String doseExpDtStr = map.getAtIndex("RXA-16", rxaIdx);
		String mfrCd 		= map.getAtIndex("RXA-17", rxaIdx);//getVaccineMfrCode(map, rxaIdx);
		String nonAdmCd 	= map.getAtIndex("RXA-18", rxaIdx);//getNonAdminCd(map, rxaIdx);
		String completionCode=map.getAtIndex("RXA-20", rxaIdx);
		String actionCd 	= map.getAtIndex("RXA-21", rxaIdx);//getVaccineAction(map);
		String systemEntryDt= map.getAtIndex("RXA-22", rxaIdx);//getVaccineAction(map);
		
		String routeCd 		= map.getAtIndex("RXR-1", rxrIdx);//getRouteCd(map, rxrIdx);
		String bodySiteCd 	= map.getAtIndex("RXR-2", rxrIdx);//getBodySiteCd(map, rxrIdx);
		
		String orderId      = map.getAtIndex("ORC-1", orcIdx);//getOrderControlId(map, orcIdx);
		String idPlacer     = map.getAtIndex("ORC-2", orcIdx);//getPlacerOrderNumber(map, orcIdx);
		String shotMrn 		= map.getAtIndex("ORC-3", orcIdx);//getVaccineMRN(map, orcIdx);//This comes from ORC-3!
		
		//Hmm.... this is a lot of transformation here...
		String vfcCode 		= getShotVFCCode(map, rxaIdx, obxIdxList);
		
		if (StringUtils.isNumeric(messageRxaCnt)) {
			shot.setPositionId(Integer.parseInt(messageRxaCnt));
		}
		if (StringUtils.isNumeric(subMsgRxaCnt)) {
			shot.setPositionSubId(Integer.parseInt(subMsgRxaCnt));
		}
		
		shot.setAdminDateString(shotDt);
		shot.setAdminDateEndString(shotDtEnd);
		shot.setAmount(doseVl);
		shot.setAmountUnitCode(doseVlUnit);
		shot.setInformationSource(source);
		shot.setFacility(org);
		shot.setLotNumber(doseLotTx);
		shot.setExpirationDateString(doseExpDtStr);
		
		shot.setManufacturerCode(mfrCd);
		shot.setRefusalCode(nonAdmCd);
		
		shot.setBodyRouteCode(routeCd);
		shot.setBodySiteCode(bodySiteCd);
		
		shot.setCompletionCode(completionCode);
		shot.setActionCode(actionCd);
		shot.setSystemEntryDateString(systemEntryDt);

		List<Observation> observations = getObservations(map, obxIdxList);
		logger.info("Observation list: " + observations);
		shot.setObservations(observations);
		
//		ts.setFacilityName(facilityName);
//		shot.setFacilityIdNumber(facilityCd);
//		ts.setFacilityTypeCode(facilityTypeCode);
		
		shot.setOrderControlCode(orderId);
		shot.setIdPlacer(idPlacer);
		shot.setIdSubmitter(shotMrn);
		
		shot.setFinancialEligibilityCode(vfcCode);
//		ts.setTreatmentMappingCode(treatmentMappingCode);

		return shot;
	}
	

	/**
	 * Question:  Is this a transformation???
	 * Should we be doing this in the transform layer? Picking the financial eligibility out of the OBX segments?
	 * @param map
	 * @param associatedRxaSegId
	 * @param obxIdxList
	 * @return
	 */
	protected String getShotVFCCode(HL7MessageMap map, int associatedRxaSegId, List<Integer> obxIdxList) {
		
		//Step 1: Look through the OBX list for a VFC code. 
		//Assumption:  the obxIdList is ordered, from smallest to largest. 
		//Also - need to verify that it's an administered shot!  

		/*
		 * “administered” is derived from RXA-9.  
		 * If the ID (first component) is ’00’, 
		 * that means its administered. 
		 */
		String vfcStatus = "V00";//"VFC eligibility not determined/unknown"
		
		//It stays unknown if it's a non-admin'd shot, or if it is not found in the OBX
		//segments of the message. 
		
		String rxa9Val = map.getAtIndex("RXA-9", associatedRxaSegId, 1);
		logger.info("Administered value: " + rxa9Val + " for segment id: " + associatedRxaSegId);
		
		if ("00".equals(rxa9Val) && obxIdxList != null && obxIdxList.size() > 0) {
			//That means it's administered.  You can then look at the OBX's to find 
			//the VFC observation. 
			int start = obxIdxList.get(0);
			int finish = obxIdxList.get(obxIdxList.size() - 1);
			//It's a VFC code if the OBX-3 value is "64994-7".  SO look for that: 
			List<Integer> vfcObxList = map.findAllSegmentRepsWithValuesWithinRange(new String[] {"64994-7"},"OBX-3",start,finish,1);
			logger.info("Observation segments: " + vfcObxList);
			
			if (vfcObxList != null && vfcObxList.size() > 0) { 
				vfcStatus = map.getAtIndex("OBX-5", vfcObxList.get(0), 1);
			} 
			
		}
		
		return vfcStatus;
		
	}
	

	  private OrganizationName readOrganizationName(HL7MessageMap map, String field, int segIdx) {
	    String id =  map.getAtIndex(field + "-1",  segIdx);
	    String name =  map.getAtIndex(field + "-4",  segIdx);
	    
	    OrganizationName on = new OrganizationName();
	    if (StringUtils.isBlank(name)) {
	    	on.setId(id);
	    } else {
	    	on.setId(name);
	    	on.setName(name);
	    }
	    return on;
	  }
	  

	
	protected void HL7ConverterNotes() {
		/*
		 * the method "cleanAndReadField" essentially does the following: 
		 * Remove field repetitions
		 * Only take the first component, and sub-component. 
		 * Replace escape characters. 
		 */
	}
	
	protected String getNonAdminCd(HL7MessageMap map, int segIdx) {
		String nonAdminCd = map.getAtIndex("RXA-18", segIdx, 1);
		return nonAdminCd;
	}
	
	protected String getRefusalReason(HL7MessageMap map, int segIdx) {
		/*
		 * HL7Converter.java
		 *  protected void readRXA18RefusalReason(String[] fields, VaccinationUpdateMessage vum, Vaccination vaccination, Map<Separator, Character> separators) {
			    if (fields.length <= 18) {
			      return;
			    }
			    vaccination.setRefusalReason(cleanAndReadField(fields[18], separators));
			    vum.addDebugOutput(" + RXA-18 refusal reason        " + vaccination.getRefusalReason());
			  }
  
  			VaccinationUpdateToExtLoader.java
  			
  			ext.setNonAdmCode(chop(Table.SUBSTANCE_REFUSAL.mapToMcirValue(vaccination.getRefusalReason()), 2));
		 */
		
		String nonAdminCd = getNonAdminCd(map, segIdx);
		return nonAdminCd;
	}
	
	protected String getPatientVaccineFundEligCd(HL7MessageMap map) {
		/*
		 * From HL7Converter.java: 
		 * PV1-20

			 privatevoid readPV1(String[] fields, VaccinationUpdateMessage vum, Patient patient, Map<Separator, Character> separators) {
			    if (fields.length <= 20) {
			      return;
			    }
			    String field = cleanRepeatsOnly(fields[20], separators);
			    String[] comps = splitComponents(separators, field);
			    clean(comps, separators);
			    if (comps.length > 0) {
			      patient.getVfc().setStatus(comps[0]);
			      vum.addDebugOutput(" + PV1-20 vfc status            " + patient.getVfc().getStatus());
			      if (comps.length >= 2) {
			        patient.getVfc().setEffectiveDate(comps[1]);
			        vum.addDebugOutput(" + PV1-20 vfc effective date    " + patient.getVfc().getEffectiveDateString());
			      }
			    }
			
			  }
  
  From VaccineUpdateToExtLoader.java: 
			  //ext.setVaccineFundEligCode(chop(vaccination.getVfc().getStatus(), 1)); Bug# 8612
			ext.setVaccineFundEligCode(chop(Table.VFC_STATUS.mapToMcirValue(vaccination.getVfc().getStatus()), 1));
		 * 
		 * 
		 */
		
		String messageValue = map.get("PV1-20");
		return messageValue;
	}
	
	protected String getPatientVaccineFundEligEffDate(HL7MessageMap map) {
		String effDate = map.get("PV1-20-2");
		return effDate;
	}
	
	protected String getPV1Class(HL7MessageMap map) {
		String effDate = map.get("PV1-2");
		return effDate;
	}
	
	protected String getVaccineMRN(HL7MessageMap map, int orcIdx) {
		/*
		 * From HL7Converter.java: 
		 * ORC-3

			vaccination.setVaccinationIdSender(cleanAndReadField(fields[3], separators));
    		vum.addDebugOutput(" + ORC-3  vacc id sender      " + vaccination.getVaccinationIdSender());
    
    	From VaccineUpdateToExtLoader.java: 
    		ext.setVaccinationMrn(chop(vaccination.getVaccinationIdSender(),199));
		 */
		
		String messageVal = map.getAtIndex("ORC-3", orcIdx, 1);
		return chop(messageVal, 199);
		
	}
	
	protected String getOrderControlId(HL7MessageMap map, int orcIdx) {
		String controlId = map.getAtIndex("ORC-1", orcIdx, 1);
		return controlId;
	}
	
	
	protected String getPlacerOrderNumber(HL7MessageMap map, int orcIdx) {
		String controlId = map.getAtIndex("ORC-2", orcIdx, 1);
		return controlId;
	}
	
	
	
//	TODO: Make sure we really don't need this here. 
//	protected long getSiteId() {
/*
 * Site_id is not on the HL7 Message...  
 * It's looked up from the Transfer Interface PIN, 
 * which is gotten from the message_interface
 * 
 * Site_id is not set onto the EXT_TRANSFER_DATA record in VaccinationUpdateToExtLoader
 * 
 * In the stored procedure; 
 * 
 * 	NVL(ext_transfer_rec.site_id,l_site_id)

 	SELECT     Site_id
   	INTO       l_site_id 
   	FROM       Transfer_Job
   	WHERE      request_status_id = p_request_id;
   
    This will be added in a layer where we're augmenting/transforming data.  We can probably get the site_id from the PIN in the message. 
    	
 */
//		return ConvertSiteId("0");
//	}
	
	protected Long ConvertSiteId(String siteId) {

//		CREATE OR REPLACE FUNCTION CONVERT_SITE_ID( fi_site_id in VARCHAR2 )
//		  RETURN NUMBER
//		IS
//
//		l_site_id       VARCHAR2(12);
//		retval NUMBER;
//
//		BEGIN

		try {
	//		  l_site_id := NVL(fi_site_id,'X');
	//		  IF l_site_id = 'X' THEN
	//		    retval := NULL;
			if (siteId == null) {
				return null;
			}
	//		  ELSIF (SUBSTR(UPPER(l_site_id),1,2)='U0') THEN
	//		    l_site_id := 'U9' || SUBSTR(l_site_id,3);
	//		    retval := TO_NUMBER(SUBSTR(l_site_id,2));
			else if ("U0".equalsIgnoreCase(siteId.substring(0, 2))) {
				return Long.valueOf("9" + siteId.substring(3));//How do you cast this to a number???
			}
	//		  ELSIF l_site_id = 'MCIR' THEN
	//		    l_site_id := 'U90000000000';
	//		    retval := TO_NUMBER(SUBSTR(l_site_id,2));
			else if ("MCIR".equals(siteId)) {
				return new Long(90000000000L);
			}
	//		  ELSIF (SUBSTR(UPPER(l_site_id),1,1)='U') THEN
	//		    retval := TO_NUMBER(SUBSTR(l_site_id,2));
			else if ("U".equals(siteId.substring(0, 1))) {
				return new Long(siteId.substring(2));
			}
	//		  ELSE
	//		    retval := TO_NUMBER(l_site_id);
				return new Long(siteId);
//			  END IF;
			//
//					  return retval;
		} catch (NumberFormatException e) {
//			EXCEPTION
//			  WHEN OTHERS THEN
//			     DBMS_OUTPUT.PUT_LINE(l_site_id || ':' ||SQLERRM);
//			     RAISE;
			throw new RuntimeException("Error Converting Site Id",  e);
			//TODO: put some attention to this...  Need to come up with a more unified exception handling strategy...  This layer needs to have it's own exception probably...
		}

//
//		EXCEPTION
//		  WHEN OTHERS THEN
//		     DBMS_OUTPUT.PUT_LINE(l_site_id || ':' ||SQLERRM);
//		     RAISE;
//		END CONVERT_SITE_ID;

	}
	
	protected String getRouteCd(HL7MessageMap map, int segIdx) {
		/*HL7Converter.java
		 * RXR-1

			privatevoid readRXR(String[] fields, VaccinationUpdateMessage vum, Vaccination vaccination, Map<Separator, Character> separators) {
			    if (fields.length <= 1) {
			      return;
			    }
			    vaccination.setBodyRoute(cleanAndReadField(fields[1], separators));
			    if (fields.length <= 2) {
			      return;
			    }
			    vaccination.setBodySite(cleanAndReadField(fields[2], separators));
			  }
  
  		VaccinationUpdateToExtLoader.java
  			chop(Table.BODY_ROUTE.mapToMcirValue(vaccination.getBodyRoute()), 1)
		 */
		String routeCode = map.getAtIndex("RXR-1", segIdx, 1);
		
		return routeCode;
	}
	
	protected String getBodySiteCd(HL7MessageMap map, int segIdx) {
		
		/*
		 * HL7Converter.java
		 * RXR-2

				privatevoid readRXR(String[] fields, VaccinationUpdateMessage vum, Vaccination vaccination, Map<Separator, Character> separators) {
				    if (fields.length <= 1) {
				      return;
				    }
				    vaccination.setBodyRoute(cleanAndReadField(fields[1], separators));
				    if (fields.length <= 2) {
				      return;
				    }
				    vaccination.setBodySite(cleanAndReadField(fields[2], separators));
				  }
  
  		VaccinationUpdateToExtLoader.java
    		chop(Table.BODY_SITE.mapToMcirValue(vaccination.getBodySite()), 1)
		 */
		
		String siteCd = map.getAtIndex("RXR-2", segIdx, 1);
		
		return siteCd;
		
	}
	
	protected String getShotDt(HL7MessageMap map, int segIdx) {
		/*
		 * HL7Converter.java: 
		 * RXA-3

			 protected void readRXA3AdminDate(String[] fields, VaccinationUpdateMessage vum, Vaccination vaccination, Map<Separator, Character> separators) {
			    if (fields.length <= 3) {
			      return;
			    }
			    String date = cleanAndReadField(fields[3], separators);
			    vaccination.setAdministeredDateString(date.length() > 8 ? date.substring(0, 8) : date);
			    vum.addDebugOutput(" + RXA-3  admin date            " + vaccination.getAdministeredDateString());
			    if (fields.length <= 4) {
			      return;
			    }
			    vaccination.setAdministeredDateEndString(cleanAndReadField(fields[4], separators));
			    vum.addDebugOutput(" + RXA-4  admin date end        " + vaccination.getAdministeredDateEndString());
			  }
			  
		  VaccinationUpdateToExtLoader.java
		  dateNormalizer(vaccination.getAdministeredDateString())
		  
		  
		 * 
		 */
		
		String adminsteredDateString = map.getAtIndex("RXA-3", segIdx, 1);
		String normalizedDate = hl7Util.dateNormalizer(adminsteredDateString);
		return normalizedDate;
	}

	
	protected String getProviderId(HL7MessageMap map, int segIdx) {
/*HL7Converter.java
 *   protected void readRXA10Provider(String[] fields, VaccinationUpdateMessage vum, Vaccination vaccination, Map<Separator, Character> separators) {
    if (fields.length <= 10) {
	      return;
	    }
	    readXcn(fields[10], vaccination.getPersonThatVaccinated(), separators);
	    vum.addDebugOutput("RXA-10 person that vaccinated   " + vaccination.getPersonThatVaccinated());
	  }
	  
	    protected void readXcn(String field, Person person, Map<Separator, Character> separators) {
		    field = cleanRepeatsOnly(field, separators);
		    String[] comps = splitComponents(separators, field);
		    clean(comps, separators);
		    if (comps.length > 0)
		    {
		      person.setId(comps[0]);
		      if (comps.length > 1) {
		        person.setNameLast(comps[1]);
		        if (comps.length > 2) {
		          person.setNameFirst(comps[2]);
		        }
		      }
		    }
		  }
		  
		  And from VaccinationUpdateToExtLoader.java
		  ext.setInitials(chop(vaccination.getPersonThatVaccinated().getId(), 3));
		  
		  So the ID is getting set from RXA-10-1, trimmed to 3
		  
		  that doesn't make sense.  most of those are numbers that are around four to six digits long. 
 */
		String providerId = map.getAtIndex("RXA-10-1", segIdx, 1);
		return chop(providerId, 3);
	}
	
	
	/**
	 * Expects a relative index. 
	 * @param map
	 * @param segIdx
	 * @return
	 */
	protected List<CodedEntity> getVaccineCodes(HL7MessageMap map, int rxaIdx) {
		List<CodedEntity> vaxList = new ArrayList<>();
		
		int fieldCount = map.getFieldRepCountFor("RXA-5", rxaIdx);
		for (int x = 1; x <= fieldCount; x++) {
			CodedEntity vxuCode = getCodedEntity(map, "RXA-5", rxaIdx, CodesetType.VACCINATION_CVX_CODE, x);
			vaxList.add(vxuCode);
		}
		
		return vaxList;
	}
//
//	/**
//	 * The CVX is expected in the RXA-5-1 (code type specified in RXA-5-3)
//	 * <p>However, it may be coded as the alternative coding system if the provider sends an NDC code, or if they mixed up the CVX/CPT alternate/primary.
//	 * <p>This was previously done in HL7Converter.java in the method readRXA5Vaccination
//	 * @param map
//	 * @param segIdx
//	 * @return
//	 */
//	protected String getCVX(HL7MessageMap map, int segIdx) {
//		//CVX should be the primary identifier.   
//		int cvxRep = map.findFieldRepWithValue("CVX", "RXA-5-3", segIdx);
//		String cvxVal = null;
//		if (cvxRep > 0) {
//			cvxVal = map.getAtIndex("RXA-5-1", segIdx, cvxRep);
//		} else {
//			//If it's in the Alternate identifier spot, get it from there...  
//			//This isn't a good thing though.  Hopefully they just mixed CVX/CPT in the primary/alternate spots.
//			cvxRep = map.findFieldRepWithValue("CVX", "RXA-5-6", segIdx);
//			if (cvxRep > 0) {
//				cvxVal = map.getAtIndex("RXA-5-4", segIdx, cvxRep);
//			}
//		}
//		return cvxVal;
//	}
//	
//	/**
//	 * The CVX is expected in the RXA-5-1 (code type specified in RXA-5-3)
//	 * <p>However, it may be coded as the alternative coding system if the provider sends an NDC code, or if they mixed up the CVX/CPT alternate/primary.
//	 * <p>This was previously done in HL7Converter.java in the method readRXA5Vaccination
//	 * @param map
//	 * @param segIdx
//	 * @return
//	 */
//	protected String getNDC(HL7MessageMap map, int segIdx) {
//		//CVX should be the primary identifier.   
//		int ndcRep = map.findFieldRepWithValue("NDC", "RXA-5-3", segIdx);
//		String ndcVal = null;
//		if (ndcRep > 0) {
//			ndcVal = map.getAtIndex("RXA-5-1", segIdx, ndcRep);
//		} else {
//			//If it's in the Alternate identifier spot, get it from there...  
//			//This isn't a good thing though.  Hopefully they just mixed CVX/CPT in the primary/alternate spots.
//			ndcRep = map.findFieldRepWithValue("NDC", "RXA-5-6", segIdx);
//			if (ndcRep > 0) {
//				ndcVal = map.getAtIndex("RXA-5-4", segIdx, ndcRep);
//			}
//		}
//		return ndcVal;
//	}
//	
//	
//	/**
//	 * The CPT may be provided as an alternate identifier in RXA-5. 
//	 * We allow for them to put it in the primary slot, but that is not a well formed message. 
//	 * @param map
//	 * @param segIdx
//	 * @return
//	 */
//	protected String getCPT(HL7MessageMap map, int segIdx) {
//		int cptRep = map.findFieldRepWithValue("CPT", "RXA-5-6", segIdx);
//		String cptVal = null;
//		
//		if (cptRep > 0) {
//			cptVal = map.getAtIndex("RXA-5-4", segIdx, cptRep);
//		} else {
//			//If it's in the Primary identifier spot, get it from there...  
//			//This isn't a good thing though.  Hopefully they just mixed CVX/CPT in the primary/alternate spots.
//			cptRep = map.findFieldRepWithValue("CPT", "RXA-5-3", segIdx);
//			if (cptRep > 0) {
//				cptVal = map.getAtIndex("RXA-5-1", segIdx, cptRep);
//			}
//		}
//		return cptVal;
//	}
	
//	/**
//	 * This is takedn from HL7Converter.java in the method "guessAndSetVaccinationCode". 
//	 * <p>It's basically checking the values in RXA-5-1 and RXA-5-4, and deciding if it's 
//	 * a CPT code by whether or not it has five digits and starts with 90. 
//	 * <p>If the value does look like a CPT, it should be taken as one. 
//	 * <p>This is slightly different than the HL7Converter.java code, because in that case, it only called this
//	 * if neither a CPT or a CVX was found. 
//	 * @param map
//	 * @param segIdx
//	 */
//	protected String guessCptFromRxa5(HL7MessageMap map, int segIdx) {
//		String rxa5primary 		= map.get("RXA-5-1", segIdx, 1);
//		
//		if (looksLikeACptCode(rxa5primary)) {
//			return rxa5primary;
//		} else {
//			String rxa5alternate 	= map.get("RXA-5-4", segIdx, 1);
//			if (looksLikeACptCode(rxa5alternate)) {
//				return rxa5alternate;
//			}
//		}
//		
//		return null;
//	}
	
	/**
	 * This is takedn from HL7Converter.java in the method "guessAndSetVaccinationCode". 
	 * <p>It's basically checking the values in RXA-5-1 and RXA-5-4, and deciding if it's 
	 * a CPT code by whether or not it has five digits and starts with 90. 
	 * <p>If the value does look like a CPT, it's ignored for this method. We want stuff that doesn't look like a CPT code. 
	 * <p>This is the opposite of the guessCpt method.  
	 * <p>This is slightly different than the HL7Converter.java code, because in that case, it only called this
	 * if neither a CPT or a CVX was found. 
	 * @param map
	 * @param segIdx
	 */
	//TODO:  Now we can just use the code type.  Don't guess anymore.  it should say if it's a CVX/NDC/CPT
//	protected String guessCvxFromRxa5(HL7MessageMap map, int segIdx) {
//		String rxa5primary 		= map.get("RXA-5-1", segIdx, 1);
//		
//		if (!looksLikeACptCode(rxa5primary)) {
//			return rxa5primary;
//		} else {
//			String rxa5alternate 	= map.get("RXA-5-4", segIdx, 1);
//			if (!looksLikeACptCode(rxa5alternate)) {
//				return rxa5alternate;
//			}
//		}
//		
//		return null;
//	}
//	
//	protected boolean looksLikeACptCode(String string) {
//		return string != null && string.length() == 5 && string.startsWith("90");
//	}
	

	/**
	 * This is very simple: 
	 * <p>vaccination.setManufacturerCode(cleanAndReadField(fields[17], separators));
	 * <p>This means we were getting it from RXA-17-1, and ignoring the rest. 
	 * @param map
	 * @param segIdx
	 * @return
	 */
	protected String getVaccineMfrCode(HL7MessageMap map, int segIdx) {
		String mfrCode = map.getAtIndex("RXA-17", segIdx, 1);
		//Should we check to make sure the coding system is specified as "MVX"?
		return mfrCode;
	}
	
//	/**
//	 * This method finds an OBX that is tagged as an observation of immunity.  
//	 * <p>Then it gets the value from that OBX and returns it. 
//	 * <p>if it doesn't find an OBX with one of the immunity codes, it returns null;
//	 * @param map
//	 * @param obxIdxList
//	 * @return String
//	 */
//	private String getImmunityObservationCode(HL7MessageMap map,
//			List<Integer> obxIdxList) {
//		String immunityCd = null;
//		
//		int immunityObsSeg = getImmunityObxSegmentIndex(map, obxIdxList);
//		
//		if (immunityObsSeg > 0) {
//			immunityCd = map.getAtIndex("OBX-5", immunityObsSeg, 1);
//		} 
//		
//		return immunityCd;
//	}
	
	/**
	 * Getting the observation segments for this rxa...
	 */
	private List<Observation> getObservations(HL7MessageMap map, List<Integer> obxIdxList) {
		logger.info("OBXidList: " + obxIdxList);
		List<Observation> list = new ArrayList<Observation>();
		
		for (Integer i : obxIdxList) {
			Observation o = getObservation(map, i); 
			logger.info("OBX: " + ReflectionToStringBuilder.toString(o));
			list.add(o);
		}
		
		return list;
	}
	
	/**
	 *  The previous version of this: 
		<code>private void populateOBX(MessageReceived message)
			  {
			    Observation obs = new Observation();
			    vaccination.getObservations().add(obs);
			    readCodeEntity(2, obs.getValueType());
			    readCodeEntity(3, obs.getObservationIdentifier());
			    obs.setObservationSubId(getValue(4));
			    obs.setObservationValue(getValue(5));
			  }
		</code>
	 * @param map
	 * @param idx this is the absolute index in the message, of the OBX segment. 
	 * @return
	 */
	protected Observation getObservation(HL7MessageMap map, Integer idx) {
		Observation o = new Observation();
		
		String valueTypeCode = map.getAtIndex("OBX-2", idx, 1);
		o.setValueTypeCode(valueTypeCode);
		
		String identifier = map.getAtIndex("OBX-3", idx, 1);
		o.setIdentifierCode(identifier);
		String identifierDesc = map.getAtIndex("OBX-3-2", idx, 1);
		o.setObservationIdentifierDescription(identifierDesc);
		
		
		String subId = map.getAtIndex("OBX-4", idx, 1);
		o.setSubId(subId);
		
		String observationValue = map.getAtIndex("OBX-5", idx, 1);
		o.setValue(observationValue);
		String observationValueDesc = map.getAtIndex("OBX-5-2", idx, 1);
		o.setObservationValueDesc(observationValueDesc);
		
		String observationDate = map.getAtIndex("OBX-14", idx, 1);
		o.setObservationDateString(observationDate);

		return o;
	}
	
	/**
	 * THe code in this method is derived from several other methods and classes: 
	 * <br /><br />
	 * <code>
	 * protected void readOBX(String[] fields, VaccinationUpdateMessage vum, Observation observation, Map<Separator, Character> separators) {
		    vum.addDebugOutput("[OBX] Observation Segment");
		    if (fields.length <= 3) {
		      return;
		    }
		    observation.setIdentifierCode(cleanAndReadField(fields[3], separators));
		    vum.addDebugOutput(" + OBX-3 observation identifier " + observation.getIdentifierCode());
		    if (fields.length <= 5) {
		      return;
		    }
		    observation.setValue(cleanAndReadField(fields[5], separators));
		    vum.addDebugOutput(" + OBX-5 observation value      " + observation.getValue());
		  }
  
		String obx3Code = vaccination.getObservation().getIdentifierCode();
	    if (OBX3_DISEASE_WITH_PRESUMED_IMMUNITY.equals(obx3Code) || OBX3_DISEASE_WITH_SEROLOGICAL_EVIDENCE_OF_IMMUNITY.equals(obx3Code) ) {
	    
		    String obx5Code = vaccination.getObservation().getValue();
		    ext.setHl7ImmunityCd(chop(obx5Code, 50));
		    //Assuming if an OBX-3 indicates documented immunity, the RXA-18 refusal reason won't be filled out, so we'll hard code it here.
		    ext.setNonAdmCode(chop(Table.SUBSTANCE_REFUSAL.mapToMcirValue("42"), 2));
		   
		    if ("998".equals(vaccination.getVaccinationCode().getCvx())) {
		    	//998^No vaccine administered^CVX <- that's the CE that gets coded in RXA-5 for an evidence of immunity message.
		    	String vaccine4code = getVaccineCdForImmunity(obx5Code);
		    	if (vaccine4code != null) {
		    		ext.setVaccine4Code(vaccine4code);
		    	}
		    }
	    } 
	    
	    	static String getVaccineCdForImmunity(String immunityCd) {
				ConverterHelper helper = ConverterHelper.getInstance();
				ImmunityAntigen ia;
				try {
					ia = helper.getImmunityAntigenByImmunityCd(immunityCd, false);
					if (ia != null) {
						return ia.getVaccineCd();
					}
				} catch (MessageException | DAOSysException e) {
		
				}
				return null;
			}
	   </code>
	 * @param map
	 * @param relativeObxList
	 * @return
	 */
//	protected int getImmunityObxSegmentIndex(HL7MessageMap map, List<Integer> relativeObxList) {
//		
//		//TODO: check RXA-20 for "NA" non-administered. 
//		
//		
//		//assumes the Obx list is ordered...  
//		int first = relativeObxList.get(0);
//		int last = relativeObxList.get(relativeObxList.size() - 1);
//		
//		List<Integer> immunitySegIdxs = map.findAllSegmentRepsWithValuesWithinRange(new String[]{OBX3_DISEASE_WITH_PRESUMED_IMMUNITY, OBX3_DISEASE_WITH_SEROLOGICAL_EVIDENCE_OF_IMMUNITY},
//				"OBX-3", 
//				first, last, 1);
//		
//		if (immunitySegIdxs.size() > 0) {
//			//Should only be one...  and only the first one is meaningful. 
//			return immunitySegIdxs.get(0);
//		}
//		return -1;
//	}
	
//	protected String getAdministeredAmount(HL7MessageMap map, int segIdx) {
//		/*  HL7Converter.java
//		 *  protected void readRXA6AdministeredAmount(String[] fields, VaccinationUpdateMessage vum, Vaccination vaccination, Map<Separator, Character> separators) {
//			    if (fields.length <= 6) {
//			      return;
//			    }
//			    String amount = "";
//			    amount = cleanAndReadField(fields[6], separators);
//			    vum.addDebugOutput("RXA-6  amount                   " + amount);
//			    if (fields.length > 7) {
//			      amount = amount + cleanAndReadField(fields[7], separators);
//			      vum.addDebugOutput("RXA-7  amount + units           " + amount);
//			    }
//			    vaccination.setAdministeredAmount(amount);
//			  }
//			  
//		  	VaccinationUpdateToExtLoader.java
//			  ext.setDoseVl(fixAdministeredAmount(vaccination.getAdministeredAmount()));
//			  
//			static String fixAdministeredAmount(String amount) {
//			    if (!amount.equals("")) {
//			      amount = amount.trim().toLowerCase();
//			      if (amount.endsWith("ml") || amount.endsWith("cc")) {
//			        amount = amount.substring(0, amount.length() - 2).trim();
//			      }
//			      try {
//			        if (!amount.equals("")) {
//			          amount = String.valueOf(Double.parseDouble(amount));
//			        }
//			      } catch (NumberFormatException nfe) {
//			        amount = "";
//			      }
//			    }
//			    return amount;
//			  }
//  
//			And then in the Stored Procs: 
//			
//			common_validations.f_masked_val('DOSE_VL',ext_transfer_rec.dose_vl)
//			
//			But this validation should probably be done in a separate process... I think so. yes. 
//		 */
//		
////		So what happens in the old process is
////		get the amount and units, 
////		put them together, 
////		then get them, 
////		take them apart, 
////		validate that the amount is a number, 
////		and then use that value. 
//		
//		String amount = map.getAtIndex("RXA-6", segIdx, 1);
////		String units = map.get("RXA-7", segIdx, 1);
//		try {
//	        if (!StringUtils.isBlank(amount)) {
//	          amount = String.valueOf(Double.parseDouble(amount));
//	        }
//	    } catch (NumberFormatException nfe) {
//	        amount = "";
//	    }
//		return amount;
//	}
	
	
	
//	protected String getVaccineDoseLot(HL7MessageMap map, int segIdx) {
//		
//		/*From HL7Converter.java
//		  
//		      protected void readRXA15LotNumber(String[] fields, VaccinationUpdateMessage vum, Vaccination vaccination, Map<Separator, Character> separators) {
//			    if (fields.length <= 15) {
//			      return;
//			    }
//			    vaccination.setLotNumber(cleanAndReadField(fields[15], separators));
//			    vum.addDebugOutput(" + RXA-15 lot number            " + vaccination.getFacility());
//			  }
//			  
//		   From VaccinationToExtLoader.java
//		   		ext.setDoseLot(chop(vaccination.getLotNumber(), 20));
//		 */
//		String lot = map.getAtIndex("RXA-15", segIdx, 1);
//		return chop(lot, 40);
//	}
	
//TODO: make sure to trim everything.  go back to the VaccinationToExtLoader and make sure to trim the same way.  
	protected static String chop(String value, int length) {
		if (value == null) {
			return "";
		}
		if (value.length() <= length) {
			return value;
		}
		return value.substring(0, length);
	}
	
	
	
	


//	protected int getRespPartyNK1Index(HL7MessageMap map) {
//		/*VaccinationUpdatetoEXtLoader.java - check out how they're deciding which NK1 to use.  That's what we're doing here. 
//		  protected static NextOfKin findResponsibleParty(VaccinationUpdateMessage message, Patient patient) {
//			    NextOfKin responsibleParty = null;
//			    for (NextOfKin nok : message.getNextOfKins()) {
//			      if (nok.getRelationship().equals("") || nok.isRelationshipFather() || nok.isRelationshipGuardian() || nok.isRelationshipParent() || nok.isRelationshipMother()
//			          || (nok.isRelationshipSelf() && patient.isChild())) {
//			        responsibleParty = nok;
//			        break;
//			      }
//			    }
//			    return responsibleParty;
//			  }
//		 */
//		
//		//Find the first responsible party that's of relationship type:
//		// father, guardian, parent, mother
//		// So step 1... find the first one of those things in the NK1 list...
//		List<Integer> respParties = map.findAllIndexesForSegmentWithValues(new String[] {"MTH","FTH","PAR","GRD"},"NK1-3", 1);
//
//		if (respParties == null || respParties.size() < 1) {
//			return map.findSegmentIndexWithValue("SEL", "NK1-3", 1); 
//		}
//		
//		//Return the first match.  In the old code there was no order with which it was determined. 
//		return respParties.get(0);
//	}
	
//	protected boolean isChild(HL7MessageMap map) {
//		//this was hard coded to true...  with a note to change it.  check out VaccinationUpdateToExtLoader.findAddress and Patient.isChild
//		//TODO: check the age.  Greater than 17 = not child...
//		return true;
//	}
	
	
//	protected String trimStringToLength(String s, int length) {
//		if (s != null && s.length() > length) {
//			return s.substring(0, length);
//		}
//		return s;
//	}


//	/**
//	 * The second part of this is certainly a transform step...
//	 * @param map
//	 * @return
//	 */
//	protected String getVaccineAction(HL7MessageMap map) {
//		String actionCode = map.get("RXA-21");
//	    if (StringUtils.isBlank(actionCode)) {
//	    	actionCode = VACCINATION_ACTION_ADD;
//		} 
//		return actionCode;
//	}

//	 protected CodedEntity getCodedEntity(HL7MessageMap map, String field, CodesetType type) {
//		 //I'm not sure this works right...  need to check. With a unit test.   Please.  Now.  
//		 return getCodedEntity(map, field, 1, type, 1);
//	  }
	 
	  
	 /**
	  * Expects a segment index  
	  * information from. 
	  * @param map
	  * @param field
	  * @param rxaIdx
	  * @param type
	  * @param fieldRep
	  * @return
	  */
	  protected CodedEntity getCodedEntity(HL7MessageMap map, String field, int rxaIdx, CodesetType type, int fieldRep)
	  {
		logger.info("Getting coded entity at : " + field + " seg abs idx: " + rxaIdx);
		CodedEntity ce = new CodedEntity(type);
		String code = map.getAtIndex(field + "-1", rxaIdx, fieldRep);
		logger.info(field + "-1" + " = " + code);
	    ce.setCode(code);
	    ce.setText(map.getAtIndex(field + "-2", rxaIdx, fieldRep));
	    ce.setTable(map.getAtIndex(field + "-3", rxaIdx, fieldRep));
	    ce.setAltCode(map.getAtIndex(field + "-4", rxaIdx, fieldRep));
	    ce.setAltText(map.getAtIndex(field + "-5", rxaIdx, fieldRep));
	    ce.setAltTable(map.getAtIndex(field + "-6", rxaIdx, fieldRep));
	    logger.info(ce.toString());
	    return ce;
	  }
}
