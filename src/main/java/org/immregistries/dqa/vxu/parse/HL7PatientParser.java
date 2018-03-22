package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaPatient;

import static org.immregistries.dqa.vxu.VxuField.*;

import org.immregistries.dqa.vxu.code.NokRelationship;

/**
 * This takes the map built from a VXU message, and builds a patient object from it.
 */
public enum HL7PatientParser {

    INSTANCE;

    public DqaPatient getPatient(HL7MessageMap map) {
        MetaParser mp = new MetaParser(map);
        int index = map.getIndexForSegmentName("PID");
        DqaPatient patient = new DqaPatient();

        List<String> keyList = new ArrayList<>(map.getLocationValueMap().keySet());
        Collections.sort(keyList);

        patient.setFields(
            //TODO: this needs to build an address, and then set the address to the patient.
            //OTHERWISE it won't save more than one in the map.
            mp.mapAllRepetitions(index,
                PATIENT_ADDRESS_STREET,
                PATIENT_ADDRESS_STREET2,
                PATIENT_ADDRESS_CITY,
                PATIENT_ADDRESS_STATE,
                PATIENT_ADDRESS_ZIP,
                PATIENT_ADDRESS_TYPE,
                PATIENT_ADDRESS_COUNTRY,
                PATIENT_ADDRESS_COUNTY));

        patient.setFields(mp.mapValues(index,
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

        patient.setField(mp.mapFieldWhere(index, PATIENT_EMAIL, "PID-13.2", "NET"));
        patient.setField(mp.mapFieldWhere(index, PATIENT_REGISTRY_ID, "PID-3.5", "SR"));
        patient.setField(mp.mapFieldWhere(index, PATIENT_MEDICAID_NUMBER, "PID-3.5", "MA"));


        MetaFieldInfo mfi = mp.mapFieldWhere(index, PATIENT_SUBMITTER_ID, "PID-3.5", "MR");
        if (mfi != null) {//This means it found MR!
            patient.setField(mfi);
            patient.setField(mp.mapFieldWhere(index, PATIENT_SUBMITTER_ID_AUTHORITY, "PID-3.5", "MR"));
            patient.setField(mp.mapFieldWhere(index, PATIENT_SUBMITTER_ID_TYPE_CODE, "PID-3.5", "MR"));
        } else {
            patient.setField(mp.mapFieldWhere(index, PATIENT_SUBMITTER_ID, "PID-3.5", "PI"));
            patient.setField(mp.mapFieldWhere(index, PATIENT_SUBMITTER_ID_AUTHORITY, "PID-3.5", "PI"));
            patient.setField(mp.mapFieldWhere(index, PATIENT_SUBMITTER_ID_TYPE_CODE, "PID-3.5", "PI"));
        }

        patient.setField(mp.mapFieldWhere(index, PATIENT_SSN, "PID-3.5", "SS"));
        patient.setField(mp.mapFieldWhere(index, PATIENT_WIC_ID, "PID-3.5", "WC"));
        patient.setField(mp.mapFieldWhere(index, PATIENT_BIRTH_COUNTY, "PID-11.7", "BDL"));

        patient.setFields(mp.mapFirstWhereSegmentHas("NK1-3.1", NokRelationship.getResponsibleCodes()
              , PATIENT_GUARDIAN_NAME_FIRST
              , PATIENT_GUARDIAN_NAME_LAST
              , PATIENT_GUARDIAN_ADDRESS_CITY
              , PATIENT_GUARDIAN_ADDRESS_STATE
              , PATIENT_GUARDIAN_ADDRESS_STREET
              , PATIENT_GUARDIAN_ADDRESS_ZIP
              , PATIENT_GUARDIAN_ADDRESS_COUNTRY
              , PATIENT_GUARDIAN_ADDRESS
              , PATIENT_GUARDIAN_RELATIONSHIP
              , PATIENT_GUARDIAN_PHONE
        ));

        return patient;
    }

}
