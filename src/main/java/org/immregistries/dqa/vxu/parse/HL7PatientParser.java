package org.immregistries.dqa.vxu.parse;

import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.VxuField;

/**
 * This takes the map built from a VXU message, and builds a patient object from it.
 */
public enum HL7PatientParser{

  INSTANCE;
  private MetaParser mp = MetaParser.INSTANCE;
  
  public DqaPatient getPatient(HL7MessageMap map) {
    
    DqaPatient patient = new DqaPatient();
    
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_STREET, map));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_STREET2, map));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_CITY, map));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_STATE, map));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_ZIP, map));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_TYPE, map));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_COUNTRY, map));
    patient.setFields(mp.mapAllRepetitions(VxuField.PATIENT_ADDRESS_COUNTY, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE_AREA_CODE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE_LOCAL_NUMBER, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE_TEL_EQUIP_CODE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_PHONE_TEL_USE_CODE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_EMAIL, map, "PID-13.2", "NET"));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_LAST, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_FIRST, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_MIDDLE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_SUFFIX, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_REGISTRY_ID, map, "PID-3.5", "SR"));
    patient.setField(mp.mapValue(VxuField.PATIENT_MEDICAID_NUMBER, map, "PID-3.5", "MA"));

    MetaFieldInfo mfi = mp.mapValue(VxuField.PATIENT_SUBMITTER_ID, map, "PID-3.5", "MR");
    if (mfi != null) {//This means it found MR!
      patient.setField(mfi);
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID_AUTHORITY, map, "PID-3.5", "MR"));
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID_TYPE_CODE, map, "PID-3.5", "MR"));
    } else {
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID, map, "PID-3.5", "PI"));
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID_AUTHORITY, map, "PID-3.5", "PI"));
      patient.setField(mp.mapValue(VxuField.PATIENT_SUBMITTER_ID_TYPE_CODE, map, "PID-3.5", "PI"));
    }
    patient.setField(mp.mapValue(VxuField.PATIENT_SSN, map, "PID-3.5", "SS"));
    patient.setField(mp.mapValue(VxuField.PATIENT_WIC_ID, map, "PID-3.5", "WC"));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_DATE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_PLACE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_INDICATOR, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_ORDER, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_BIRTH_COUNTY, map, "PID-11.7", "BDL"));
    patient.setField(mp.mapValue(VxuField.PATIENT_DEATH_DATE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_DEATH_INDICATOR, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_ETHNICITY, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_MIDDLE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_LAST, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_FIRST, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_SUFFIX, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_NAME_TYPE_CODE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_MOTHERS_MAIDEN_NAME, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_RACE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_GENDER, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_PRIMARY_LANGUAGE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_REGISTRY_STATUS, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_VFC_STATUS, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_VFC_STATUS, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_VFC_EFFECTIVE_DATE, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_CLASS, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_PROTECTION_INDICATOR, map));
    patient.setField(mp.mapValue(VxuField.PATIENT_PUBLICITY_CODE, map));
    return patient;
  }


}
