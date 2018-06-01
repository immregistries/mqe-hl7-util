package org.immregistries.dqa.vxu.parse;

import static org.immregistries.dqa.vxu.VxuField.PATIENT_ADDRESS_CITY;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_ADDRESS_COUNTRY;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_ADDRESS_COUNTY;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_ADDRESS_STATE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_ADDRESS_STREET;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_ADDRESS_STREET2;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_ADDRESS_TYPE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_ADDRESS_ZIP;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_BIRTH_COUNTY;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_BIRTH_DATE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_BIRTH_INDICATOR;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_BIRTH_ORDER;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_BIRTH_PLACE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_CLASS;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_DEATH_DATE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_DEATH_INDICATOR;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_EMAIL;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_ETHNICITY;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GENDER;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_ADDRESS_CITY;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_ADDRESS_COUNTRY;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_ADDRESS_STATE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_ADDRESS_STREET;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_ADDRESS_STREET2;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_ADDRESS_ZIP;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_EMAIL;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_NAME_FIRST;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_NAME_LAST;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_NAME_MIDDLE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_PHONE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_PHONE_AREA_CODE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_PHONE_LOCAL_NUMBER;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_PHONE_TEL_EQUIP_CODE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_PHONE_TEL_USE_CODE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_GUARDIAN_RELATIONSHIP;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_MEDICAID_NUMBER;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_MOTHERS_MAIDEN_NAME;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_NAME_FIRST;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_NAME_LAST;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_NAME_MIDDLE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_NAME_SUFFIX;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_NAME_TYPE_CODE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_PHONE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_PHONE_AREA_CODE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_PHONE_LOCAL_NUMBER;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_PHONE_TEL_EQUIP_CODE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_PHONE_TEL_USE_CODE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_PRIMARY_FACILITY_ID;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_PRIMARY_LANGUAGE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_PROTECTION_INDICATOR;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_PUBLICITY_CODE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_RACE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_REGISTRY_ID;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_REGISTRY_STATUS;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_SSN;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_SUBMITTER_ID;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_SUBMITTER_ID_AUTHORITY;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_SUBMITTER_ID_TYPE_CODE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_VFC_EFFECTIVE_DATE;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_VFC_STATUS;
import static org.immregistries.dqa.vxu.VxuField.PATIENT_WIC_ID;

import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.code.NokRelationship;

/**
 * This takes the map built from a VXU message, and builds a patient object from it.
 */
public enum HL7PatientParser {

  INSTANCE;

  public DqaPatient getPatient(HL7MessageMap map) {
    MetaParser mp = new MetaParser(map);
    int line = map.getLineForSegmentName("PID");
    DqaPatient patient = new DqaPatient();

//    List<String> keyList = new ArrayList<>(map.getLocationValueMap().keySet());
//    Collections.sort(keyList);

    patient.setFields(
        //TODO: this needs to build an address, and then set the address to the patient.
        //OTHERWISE it won't save more than one in the map.
        mp.mapAllRepetitions(line,
            PATIENT_ADDRESS_STREET,
            PATIENT_ADDRESS_STREET2,
            PATIENT_ADDRESS_CITY,
            PATIENT_ADDRESS_STATE,
            PATIENT_ADDRESS_ZIP,
            PATIENT_ADDRESS_TYPE,
            PATIENT_ADDRESS_COUNTRY,
            PATIENT_ADDRESS_COUNTY));

    patient.setFields(mp.mapValues(line,
        PATIENT_PHONE
        , PATIENT_PHONE_AREA_CODE
        , PATIENT_PHONE_LOCAL_NUMBER
        , PATIENT_PHONE_TEL_EQUIP_CODE
        , PATIENT_PHONE_TEL_USE_CODE
        , PATIENT_NAME_LAST
        , PATIENT_NAME_FIRST
        , PATIENT_NAME_MIDDLE
        , PATIENT_NAME_SUFFIX
        , PATIENT_BIRTH_DATE
        , PATIENT_BIRTH_PLACE
        , PATIENT_BIRTH_INDICATOR
        , PATIENT_BIRTH_ORDER
        , PATIENT_DEATH_DATE
        , PATIENT_DEATH_INDICATOR
        , PATIENT_ETHNICITY
        , PATIENT_PRIMARY_FACILITY_ID
        , PATIENT_NAME_MIDDLE
        , PATIENT_NAME_LAST
        , PATIENT_NAME_FIRST
        , PATIENT_NAME_SUFFIX
        , PATIENT_NAME_TYPE_CODE
        , PATIENT_MOTHERS_MAIDEN_NAME
        , PATIENT_RACE
        , PATIENT_GENDER
        , PATIENT_PRIMARY_LANGUAGE
        , PATIENT_REGISTRY_STATUS
        , PATIENT_VFC_STATUS
        , PATIENT_VFC_STATUS
        , PATIENT_VFC_EFFECTIVE_DATE
        , PATIENT_CLASS
        , PATIENT_PROTECTION_INDICATOR
        , PATIENT_PUBLICITY_CODE));

    patient.setField(mp.mapFieldWhere(line, PATIENT_EMAIL, "PID-13.2", "NET"));
    patient.setField(mp.mapFieldWhere(line, PATIENT_REGISTRY_ID, "PID-3.5", "SR"));
    patient.setField(mp.mapFieldWhere(line, PATIENT_MEDICAID_NUMBER, "PID-3.5", "MA"));

    MetaFieldInfo mfi = mp.mapFieldWhere(line, PATIENT_SUBMITTER_ID, "PID-3.5", "MR");
    if (mfi != null) {//This means it found MR!
      patient.setField(mfi);
      patient.setField(mp.mapFieldWhere(line, PATIENT_SUBMITTER_ID_AUTHORITY, "PID-3.5", "MR"));
      patient.setField(mp.mapFieldWhere(line, PATIENT_SUBMITTER_ID_TYPE_CODE, "PID-3.5", "MR"));
    } else {
      patient.setField(mp.mapFieldWhere(line, PATIENT_SUBMITTER_ID, "PID-3.5", "PI"));
      patient.setField(mp.mapFieldWhere(line, PATIENT_SUBMITTER_ID_AUTHORITY, "PID-3.5", "PI"));
      patient.setField(mp.mapFieldWhere(line, PATIENT_SUBMITTER_ID_TYPE_CODE, "PID-3.5", "PI"));
    }

    patient.setField(mp.mapFieldWhere(line, PATIENT_SSN, "PID-3.5", "SS"));
    patient.setField(mp.mapFieldWhere(line, PATIENT_WIC_ID, "PID-3.5", "WC"));
    patient.setField(mp.mapFieldWhere(line, PATIENT_BIRTH_COUNTY, "PID-11.7", "BDL"));

    int responsibleLine = map
        .findFirstSegmentWhereFieldHas("NK1-3.1", NokRelationship.getResponsibleCodes());
    patient.setFields(mp.mapValues(responsibleLine
        , PATIENT_GUARDIAN_NAME_FIRST
        , PATIENT_GUARDIAN_NAME_MIDDLE
        , PATIENT_GUARDIAN_NAME_LAST
        , PATIENT_GUARDIAN_ADDRESS_CITY
        , PATIENT_GUARDIAN_ADDRESS_STATE
        , PATIENT_GUARDIAN_ADDRESS_STREET
        , PATIENT_GUARDIAN_ADDRESS_STREET2
        , PATIENT_GUARDIAN_ADDRESS_ZIP
        , PATIENT_GUARDIAN_ADDRESS_COUNTRY
        , PATIENT_GUARDIAN_RELATIONSHIP
        , PATIENT_GUARDIAN_PHONE
        , PATIENT_GUARDIAN_EMAIL
        , PATIENT_GUARDIAN_PHONE_AREA_CODE
        , PATIENT_GUARDIAN_PHONE_LOCAL_NUMBER
        , PATIENT_GUARDIAN_PHONE_TEL_USE_CODE
        , PATIENT_GUARDIAN_PHONE_TEL_EQUIP_CODE
    ));

    return patient;
  }

}
