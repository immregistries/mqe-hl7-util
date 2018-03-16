package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.VxuField;

/**
 * This takes the map built from a VXU message, and builds a patient object from it.
 */
public enum HL7PatientParser{

  INSTANCE;

  public DqaPatient getPatient(HL7MessageMap map) {
    MetaParser mp = new MetaParser(map);
    mp.setAbsoluteSegmentIndex(map.getIndexForSegmentName("PID"));
      
    DqaPatient patient = new DqaPatient();
    
    List<String> keyList = new ArrayList<>(map.getLocationValueMap().keySet());
    Collections.sort(keyList);
    
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_STREET));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_STREET2));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_CITY));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_STATE));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_ZIP));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_TYPE));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_COUNTRY));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_COUNTY));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE_AREA_CODE));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE_LOCAL_NUMBER));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE_TEL_EQUIP_CODE));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE_TEL_USE_CODE));
    patient.setField(mp.mapValue(VxuField.PATIENT_EMAIL, "PID-13.2", "NET"));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_LAST));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_FIRST));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_MIDDLE));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_SUFFIX));
    patient.setField(mp.mapValue(VxuField.PATIENT_REGISTRY_ID, "PID-3.5", "SR"));
    patient.setField(mp.mapValue(VxuField.PATIENT_MEDICAID_NUMBER, "PID-3.5", "MA"));

    MetaFieldInfo mfi = mp.mapValue(VxuField.PATIENT_SUBMITTER_ID, "PID-3.5", "MR");
    if (mfi != null) {//This means it found MR!
      patient.setField(mfi);
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID_AUTHORITY, "PID-3.5", "MR"));
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID_TYPE_CODE, "PID-3.5", "MR"));
    } else {
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID, "PID-3.5", "PI"));
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID_AUTHORITY, "PID-3.5", "PI"));
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID_TYPE_CODE, "PID-3.5", "PI"));
    }
    patient.setField(mp.mapValue(VxuField.PATIENT_SSN, "PID-3.5", "SS"));
    patient.setField(mp.mapValue(VxuField.PATIENT_WIC_ID, "PID-3.5", "WC"));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_DATE));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_PLACE));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_INDICATOR));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_ORDER));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_COUNTY, "PID-11.7", "BDL"));
    patient.setField(mp.mapValue(VxuField.PATIENT_DEATH_DATE));
    patient.setField(mp.mapValue(VxuField.PATIENT_DEATH_INDICATOR));
    patient.setField(mp.mapValue(VxuField.PATIENT_ETHNICITY));
    patient.setField(mp.mapValue(VxuField.PATIENT_PRIMARY_FACILITY_ID));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_MIDDLE));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_LAST));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_FIRST));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_SUFFIX));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_TYPE_CODE));
    patient.setField(mp.mapValue(VxuField.PATIENT_MOTHERS_MAIDEN_NAME));
    patient.setField(mp.mapValue(VxuField.PATIENT_RACE));
    patient.setField(mp.mapValue(VxuField.PATIENT_GENDER));
    patient.setField(mp.mapValue(VxuField.PATIENT_PRIMARY_LANGUAGE));
    patient.setField(mp.mapValue(VxuField.PATIENT_REGISTRY_STATUS));
    patient.setField(mp.mapValue(VxuField.PATIENT_VFC_STATUS));
    patient.setField(mp.mapValue(VxuField.PATIENT_VFC_STATUS));
    patient.setField(mp.mapValue(VxuField.PATIENT_VFC_EFFECTIVE_DATE));
    patient.setField(mp.mapValue(VxuField.PATIENT_CLASS));
    patient.setField(mp.mapValue(VxuField.PATIENT_PROTECTION_INDICATOR));
    patient.setField(mp.mapValue(VxuField.PATIENT_PUBLICITY_CODE));
    return patient;
  }


}
