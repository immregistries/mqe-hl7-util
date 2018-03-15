package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.immregistries.dqa.hl7util.model.ErrorLocation;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.VxuField;
import org.immregistries.dqa.vxu.hl7.CodedEntity;
import org.immregistries.dqa.vxu.hl7.Observation;
import org.immregistries.dqa.vxu.hl7.OrganizationName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum HL7VaccinationParser {
  INSTANCE;

  private final MetaParser mp = MetaParser.INSTANCE;
  private static final Logger logger = LoggerFactory.getLogger(HL7VaccinationParser.class);


  /**
   * This builds a list of Vaccinations from the message map.
   * 
   * @param map
   * @return
   */
  public List<DqaVaccination> getVaccinationList(HL7MessageMap map) {
    // Start a list of shots
    List<DqaVaccination> shotList = new ArrayList<DqaVaccination>();

    // Get the list of segments so you know the total length.
    int segmentListSize = map.getMessageSegments().size();

    int startingPoint = map.getNextImmunizationStartingIndex(0);

    int positionId = 0;
    while (startingPoint < segmentListSize) {
      positionId++;
      int nextStartingPoint = map.getNextImmunizationStartingIndex(startingPoint);
      int finishPoint = nextStartingPoint - 1;

      DqaVaccination ts = getVaccination(map, startingPoint, finishPoint, positionId);
      shotList.add(ts);
      startingPoint = nextStartingPoint;

      // Theory: This will naturally finish the process to the end of the segments.
    }

    return shotList;
  }

  /**
   * @param map
   * @return
   */
  public DqaVaccination getVaccination(HL7MessageMap map, int vaccinationStartSegment,
      int vaccinationFinishSegment, int positionId) {

    DqaVaccination shot = new DqaVaccination();
    shot.setPositionId(positionId);

    // So my task here is to find the index of the:
    // ORC
    // RXA
    // RXR
    // OBX's
    // Maybe NTE segments that we ignore.
    List<String> segments = map.getMessageSegments();

    int orcIdx = -1;
    int rxaIdx = -1;
    int rxrIdx = -1;
    List<Integer> obxIdxList = new ArrayList<Integer>();

    // We're going to operate with segment indexes.
    for (int i = vaccinationStartSegment; i <= vaccinationFinishSegment; i++) {
      String segName = segments.get(i);
      switch (segName) {
        case "ORC":
          orcIdx = i;
          break;
        case "RXA":
          rxaIdx = i;
          break;
        case "RXR":
          rxrIdx = i;
          break;
        case "OBX":
          obxIdxList.add(i);
          break;
      }
    }

    if (orcIdx != -1) {
      shot.setField(mp.mapValue(orcIdx, VxuField.VACCINATION_ORDER_CONTROL_CODE, map));
      shot.setField(mp.mapValue(orcIdx, VxuField.VACCINATION_PLACER_ORDER_NUMBER, map));
      shot.setField(mp.mapValue(orcIdx, VxuField.VACCINATION_FILLER_ORDER_NUMBER, map));
    }

    shot.setField(mp.mapCodedValue(rxaIdx, VxuField.VACCINATION_CVX_CODE, map, new String[] {"CVX", "HL70292"}));
    shot.setField(mp.mapCodedValue(rxaIdx,VxuField.VACCINATION_CPT_CODE, map, new String[] {"CPT", "C4"}));
    shot.setField(mp.mapCodedValue(rxaIdx,VxuField.VACCINATION_NDC_CODE, map, new String[] {"NDC", "?"}));
    shot.setField(mp.mapValue(rxaIdx,VxuField.VACCINATION_ADMIN_DATE, map));
    shot.setField(mp.mapValue(rxaIdx,VxuField.VACCINATION_ADMIN_DATE_END, map));
    shot.setField(mp.mapValue(rxaIdx,VxuField.VACCINATION_ADMINISTERED_AMOUNT, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_ADMINISTERED_UNIT, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_INFORMATION_SOURCE, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_GIVEN_BY, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_FACILITY_ID, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_FACILITY_NAME, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_LOT_NUMBER, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_LOT_EXPIRATION_DATE, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_MANUFACTURER_CODE, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_REFUSAL_REASON, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_COMPLETION_STATUS, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_ACTION_CODE, map));
    shot.setField(mp.mapValue(rxaIdx, VxuField.VACCINATION_SYSTEM_ENTRY_TIME, map));

    if (rxrIdx != -1) {
      shot.setField(mp.mapValue(rxrIdx, VxuField.VACCINATION_BODY_ROUTE, map));
      shot.setField(mp.mapValue(rxrIdx, VxuField.VACCINATION_BODY_SITE, map));
    }

    logger.info("Segment ID's being used for this shot: ORC[" + orcIdx + "] RXA[" + rxaIdx
        + "] RXR[" + rxrIdx + "] OBX{" + obxIdxList + "}");



    for (Integer i : obxIdxList) {
      ErrorLocation errorLocation = new ErrorLocation("OBX-3");
      String value = mp.mapValue(i, map, errorLocation);
      if (value.equals("64994-7")) {
        shot.setField(mp.mapValue(i, VxuField.VACCINATION_FINANCIAL_ELIGIBILITY_CODE, map));
      }
    }



    List<Observation> observations = getObservations(map, obxIdxList);
    logger.info("Observation list: " + observations);
    shot.setObservations(observations);

    return shot;
  }


  /**
   * Question: Is this a transformation??? Should we be doing this in the transform layer? Picking
   * the financial eligibility out of the OBX segments?
   * 
   * @param map
   * @param associatedRxaSegId
   * @param obxIdxList
   * @return
   */
  protected String getShotVFCCode(HL7MessageMap map, int associatedRxaSegId,
      List<Integer> obxIdxList) {

    // Step 1: Look through the OBX list for a VFC code.
    // Assumption: the obxIdList is ordered, from smallest to largest.
    // Also - need to verify that it's an administered shot!

    /*
     * “administered” is derived from RXA-9. If the ID (first component) is ’00’, that means its
     * administered.
     */
    String vfcStatus = "V00";// "VFC eligibility not determined/unknown"

    // It stays unknown if it's a non-admin'd shot, or if it is not found in the OBX
    // segments of the message.

    String rxa9Val = map.getAtIndex("RXA-9", associatedRxaSegId, 1);
    logger.info("Administered value: " + rxa9Val + " for segment id: " + associatedRxaSegId);

    if ("00".equals(rxa9Val) && obxIdxList != null && obxIdxList.size() > 0) {
      // That means it's administered. You can then look at the OBX's to find
      // the VFC observation.
      int start = obxIdxList.get(0);
      int finish = obxIdxList.get(obxIdxList.size() - 1);
      // It's a VFC code if the OBX-3 value is "64994-7". SO look for that:
      List<Integer> vfcObxList = map.findAllSegmentRepsWithValuesWithinRange(
          new String[] {"64994-7"}, "OBX-3", start, finish, 1);
      logger.info("Observation segments: " + vfcObxList);

      if (vfcObxList != null && vfcObxList.size() > 0) {
        vfcStatus = map.getAtIndex("OBX-5", vfcObxList.get(0), 1);
      }

    }
    return vfcStatus;
  }

  private OrganizationName readOrganizationName(HL7MessageMap map, String field, int segIdx) {
    String id = map.getAtIndex(field + "-1", segIdx);
    String name = map.getAtIndex(field + "-4", segIdx);

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
   * 
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
   * 
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
   * Expects a segment index information from.
   * 
   * @param map
   * @param field
   * @param rxaIdx
   * @param fieldRep
   * @return
   */
  protected CodedEntity getCodedEntity(HL7MessageMap map, String field, int rxaIdx, int fieldRep) {
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
