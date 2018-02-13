package org.immregistries.dqa.vxu.parse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.hl7.CodedEntity;
import org.immregistries.dqa.vxu.hl7.Observation;
import org.immregistries.dqa.vxu.hl7.OrganizationName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public enum HL7VaccineParser {
	INSTANCE;

	private static final Logger logger = LoggerFactory.getLogger(HL7VaccineParser.class);
	
	private HL7ParsingUtil hl7Util = HL7ParsingUtil.INSTANCE;

	/**
	 * This builds a list of Vaccinations from the message map.
	 * @param map
	 * @return
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
		
		for (CodedEntity code : vxList) {
			    if ("CVX"		.equals(code.getTable()) 
	    		||  "CVX"		.equals(code.getAltTable()) 
			    ||  "HL70292"	.equals(code.getTable())
			    ||  "HL70292"	.equals(code.getAltTable()))
			    {
			      shot.setAdminCvxCode(code.getCode());
			    }
			    else if ("CPT"		.equals(code.getTable()) 
	    		||  "CPT"		.equals(code.getAltTable()) 
	    		||  "C4"	.equals(code.getTable())
	    		||  "C4"	.equals(code.getAltTable()))
			    {
			      shot.setAdminCptCode(code.getCode());
			    }
			    else if ("NDC"		.equals(code.getTable()) 
	    		||  "NDC"		.equals(code.getAltTable()) 
	    		||  "?"	.equals(code.getTable())
	    		||  "?"	.equals(code.getAltTable()))
			    {
			      shot.setAdminNdcCode(code.getCode());
			    }
		}

		String messageRxaCnt= map.getAtIndex("RXA-1", rxaIdx);
		String subMsgRxaCnt = map.getAtIndex("RXA-2", rxaIdx);
		String shotDt 		= map.getAtIndex("RXA-3", rxaIdx);//getShotDt(map, rxaIdx);
		String shotDtEnd 	= map.getAtIndex("RXA-4", rxaIdx);//getShotDt(map, rxaIdx);
		String doseVl 		= map.getAtIndex("RXA-6", rxaIdx);//getAdministeredAmount(map, rxaIdx);
		String doseVlUnit	= map.getAtIndex("RXA-7", rxaIdx);
		String source 		= map.getAtIndex("RXA-9", rxaIdx);//getCodedEntity(map, "RXA-9",  rxaIdx, CodesetType.VACCINATION_INFORMATION_SOURCE);
		String adminProviderId = map.getAtIndex("RXA-10-1", rxaIdx);//getProviderId(map, rxaIdx);
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

		shot.setGivenByNumber(adminProviderId);
		
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
		
		shot.setOrderControlCode(orderId);
		shot.setIdPlacer(idPlacer);
		shot.setIdSubmitter(shotMrn);
		
		shot.setFinancialEligibilityCode(vfcCode);

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

	/**
	 * Expects a relative index. 
	 * @param map
	 * @param rxaIdx
	 * @return
	 */
	protected List<CodedEntity> getVaccineCodes(HL7MessageMap map, int rxaIdx) {
		List<CodedEntity> vaxList = new ArrayList<>();
		
		int fieldCount = map.getFieldRepCountFor("RXA-5", rxaIdx);
		for (int x = 1; x <= fieldCount; x++) {
			CodedEntity vxuCode = getCodedEntity(map, "RXA-5", rxaIdx, x);
			vaxList.add(vxuCode);
		}
		
		return vaxList;
	}

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
	 * Gets the observation at the index specified.
	 * @param map
	 * @param idx this is the absolute index in the message, of the OBX segment. 
	 * @return
	 */
	protected Observation getObservation(HL7MessageMap map, Integer idx) {
		Observation o = new Observation();
		
		String valueTypeCode  = map.getAtIndex("OBX-2", idx, 1);
		o.setValueTypeCode(valueTypeCode);
		
		String identifier     = map.getAtIndex("OBX-3", idx, 1);
		o.setIdentifierCode(identifier);

		String identifierDesc = map.getAtIndex("OBX-3-2", idx, 1);
		o.setObservationIdentifierDescription(identifierDesc);

		String subId          = map.getAtIndex("OBX-4", idx, 1);
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
	  * Expects a segment index  
	  * information from. 
	  * @param map
	  * @param field
	  * @param rxaIdx
	  * @param fieldRep
	  * @return
	  */
	  protected CodedEntity getCodedEntity(HL7MessageMap map, String field, int rxaIdx, int fieldRep)
	  {
		logger.info("Getting coded entity at : " + field + " seg abs idx: " + rxaIdx);
		CodedEntity ce = new CodedEntity();
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
