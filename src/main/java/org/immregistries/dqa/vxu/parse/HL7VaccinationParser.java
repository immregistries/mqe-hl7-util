package org.immregistries.dqa.vxu.parse;

import static org.immregistries.dqa.vxu.VxuField.OBSERVATION_DATE_TIME_OF_OBSERVATION;
import static org.immregistries.dqa.vxu.VxuField.OBSERVATION_IDENTIFIER_CODE;
import static org.immregistries.dqa.vxu.VxuField.OBSERVATION_IDENTIFIER_DESC;
import static org.immregistries.dqa.vxu.VxuField.OBSERVATION_SUB_ID;
import static org.immregistries.dqa.vxu.VxuField.OBSERVATION_VALUE;
import static org.immregistries.dqa.vxu.VxuField.OBSERVATION_VALUE_DESC;
import static org.immregistries.dqa.vxu.VxuField.OBSERVATION_VALUE_TYPE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_ACTION_CODE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_ADMINISTERED_AMOUNT;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_ADMINISTERED_UNIT;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_ADMIN_CODE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_ADMIN_DATE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_ADMIN_DATE_END;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_BODY_ROUTE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_BODY_SITE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_COMPLETION_STATUS;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_CPT_CODE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_CVX_CODE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_FACILITY_ID;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_FACILITY_NAME;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_FILLER_ORDER_NUMBER;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_FINANCIAL_ELIGIBILITY_CODE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_GIVEN_BY;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_INFORMATION_SOURCE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_LOT_EXPIRATION_DATE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_LOT_NUMBER;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_MANUFACTURER_CODE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_NDC_CODE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_ORDER_CONTROL_CODE;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_PLACER_ORDER_NUMBER;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_REFUSAL_REASON;
import static org.immregistries.dqa.vxu.VxuField.VACCINATION_SYSTEM_ENTRY_TIME;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.immregistries.dqa.hl7util.model.Hl7Location;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.hl7.CodedEntity;
import org.immregistries.dqa.vxu.hl7.Observation;
import org.immregistries.dqa.vxu.hl7.OrganizationName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum HL7VaccinationParser {
  INSTANCE;

  private static final Logger logger = LoggerFactory.getLogger(HL7VaccinationParser.class);


  /**
   * This builds a list of Vaccinations from the message map.
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
      int finishPoint = map.getNextImmunizationStartingIndex(startingPoint) - 1;
      DqaVaccination ts = getVaccination(map, startingPoint, finishPoint, positionId);
      shotList.add(ts);
      startingPoint = finishPoint + 1;

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

    MetaParser mp = new MetaParser(map);
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
      shot.setFields(mp.mapValues(orcIdx,
          VACCINATION_ORDER_CONTROL_CODE,
          VACCINATION_PLACER_ORDER_NUMBER,
          VACCINATION_FILLER_ORDER_NUMBER
      ));
    }

    shot.setField(mp.mapCodedValue(rxaIdx, VACCINATION_CVX_CODE, "CVX", "HL70292"));
    shot.setField(mp.mapCodedValue(rxaIdx, VACCINATION_CPT_CODE, "CPT", "C4"));
    shot.setField(mp.mapCodedValue(rxaIdx, VACCINATION_NDC_CODE, "NDC", "?"));

    shot.setFields(mp.mapValues(rxaIdx,
        VACCINATION_ADMIN_DATE
        , VACCINATION_ADMIN_DATE_END
        , VACCINATION_ADMINISTERED_AMOUNT
        , VACCINATION_ADMINISTERED_UNIT
        , VACCINATION_INFORMATION_SOURCE
        , VACCINATION_GIVEN_BY
        , VACCINATION_FACILITY_ID
        , VACCINATION_FACILITY_NAME
        , VACCINATION_LOT_NUMBER
        , VACCINATION_LOT_EXPIRATION_DATE
        , VACCINATION_MANUFACTURER_CODE
        , VACCINATION_REFUSAL_REASON
        , VACCINATION_COMPLETION_STATUS
        , VACCINATION_ACTION_CODE
        , VACCINATION_ADMIN_CODE
        , VACCINATION_SYSTEM_ENTRY_TIME
    ));

    if (rxrIdx != -1) {
      shot.setFields(mp.mapValues(rxrIdx,
          VACCINATION_BODY_ROUTE,
          VACCINATION_BODY_SITE));
    }

    logger.info("Segment ID's being used for this shot: ORC[" + orcIdx + "] RXA[" + rxaIdx
        + "] RXR[" + rxrIdx + "] OBX{" + obxIdxList + "}");

    for (Integer i : obxIdxList) {
      Hl7Location hl7Location = new Hl7Location("OBX-3");
      String value = mp.getValue(hl7Location);
      if (value != null && value.equals("64994-7")) {
        shot.setFields(mp.mapValues(i, VACCINATION_FINANCIAL_ELIGIBILITY_CODE));
      }
    }

    List<Observation> observations = getObservations(mp, obxIdxList);
    logger.info("Observation list: " + observations);
    shot.setObservations(observations);

    return shot;
  }

  // TODO: I think most of the rest of this can be deleted. but we need to modify the tests to make
  // sure.

  /**
   * Question: Is this a transformation??? Should we be doing this in the transform layer? Picking
   * the financial eligibility out of the OBX segments?
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
          new String[]{"64994-7"}, "OBX-3", start, finish, 1);
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
  private List<Observation> getObservations(MetaParser mp, List<Integer> obxIdxList) {
    logger.info("OBXidList: " + obxIdxList);
    List<Observation> list = new ArrayList<Observation>();

    for (Integer i : obxIdxList) {
      Observation o = getObservation(mp, i);
      logger.info("OBX: " + ReflectionToStringBuilder.toString(o));
      list.add(o);
    }

    return list;
  }

  /**
   * Gets the observation at the index specified.
   *
   * @param idx this is the absolute index in the message, of the OBX segment.
   */
  protected Observation getObservation(MetaParser mp, Integer idx) {
    Observation o = new Observation();
    o.setFields(mp.mapValues(idx,
        OBSERVATION_VALUE_TYPE
        , OBSERVATION_VALUE_DESC
        , OBSERVATION_VALUE
        , OBSERVATION_SUB_ID
        , OBSERVATION_IDENTIFIER_DESC
        , OBSERVATION_IDENTIFIER_CODE
        , OBSERVATION_DATE_TIME_OF_OBSERVATION
    ));
    return o;
  }

  /**
   * Expects a segment index information from.
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
