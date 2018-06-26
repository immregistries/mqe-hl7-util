package org.immregistries.mqe.vxu;

import static org.immregistries.mqe.vxu.VxuObject.GENERAL;
import static org.immregistries.mqe.vxu.VxuObject.MESSAGE_HEADER;
import static org.immregistries.mqe.vxu.VxuObject.NEXT_OF_KIN;
import static org.immregistries.mqe.vxu.VxuObject.OBSERVATION;
import static org.immregistries.mqe.vxu.VxuObject.PATIENT;
import static org.immregistries.mqe.vxu.VxuObject.VACCINATION;

import org.immregistries.mqe.codebase.client.reference.CodesetType;

public enum VxuField {
  /*
   * What do you think about the idea of tagging some of these bad boys as "Coded"
   */

  /*
   * What do you think about tagging the HL7 Location for these bad boys. Pull them from the MessageAttribute class.
   */

  //Fields: String fieldDesc, CodesetType typeOfValue, String hl7Field

  //general
  AUTHORIZATION(GENERAL, "authorization", null, null),
  CONFIGURATION(GENERAL, "configuration", null, null),
  PARSE(GENERAL, "parse", null, null),
  PROCESSING(GENERAL, "processing", null, null),
  NONE(GENERAL, "object", null, null),

  //message header
  MESSAGE_ACCEPT_ACK_TYPE(MESSAGE_HEADER, "accept ack type", CodesetType.ACKNOWLEDGEMENT_TYPE, "MSH-15"),
  MESSAGE_ALT_CHARACTER_SET(MESSAGE_HEADER, "alt character set", null, null),
  MESSAGE_APP_ACK_TYPE(MESSAGE_HEADER, "app ack type", CodesetType.ACKNOWLEDGEMENT_TYPE, "MSH-16"),
  MESSAGE_CHARACTER_SET(MESSAGE_HEADER, "character set", null, null),
  MESSAGE_COUNTRY_CODE(MESSAGE_HEADER, "country code", null, null),
  MESSAGE_ENCODING_CHARACTER(MESSAGE_HEADER, "encoding character", null, null),
  MESSAGE_CONTROL_ID(MESSAGE_HEADER, "message control id", null, "MSH-10"),
  MESSAGE_DATE(MESSAGE_HEADER, "message date", null, "MSH-7"),
  MESSAGE_PROFILE_ID(MESSAGE_HEADER, "message profile id", null, "MSH-21"),
  MESSAGE_TRIGGER(MESSAGE_HEADER, "message trigger", null, "MSH-9.2"),
  MESSAGE_STRUCTURE(MESSAGE_HEADER, "message trigger", null, "MSH-9.3"),
  MESSAGE_TYPE(MESSAGE_HEADER, "message type", null, "MSH-9.1"),
  MESSAGE_PROCESSING_ID(MESSAGE_HEADER, "processing id", null, "MSH-11"),
  MESSAGE_RECEIVING_APPLICATION(MESSAGE_HEADER, "receiving application", null, "MSH-5"),
  MESSAGE_RECEIVING_FACILITY(MESSAGE_HEADER, "receiving facility", null, "MSH-6"),
  MESSAGE_SEGMENT(MESSAGE_HEADER, "segment", null, null),
  MESSAGE_SENDING_APPLICATION(MESSAGE_HEADER, "sending application", null, "MSH-3"),
  MESSAGE_SENDING_FACILITY(MESSAGE_HEADER, "sending facility", null, "MSH-4"),
  MESSAGE_VERSION(MESSAGE_HEADER, "version", null, "MSH-12"),
  MESSAGE_SENDING_RESPONSIBLE_ORGANIZATION(MESSAGE_HEADER, "sending responsible organization", null, "MSH-22")

  //Next of kin
  ,
  NEXT_OF_KIN_ADDRESS(NEXT_OF_KIN, "address", null, "NK1-4"),
  NEXT_OF_KIN_ADDRESS_CITY(NEXT_OF_KIN, "address city", null, "NK1-4.3"),
  NEXT_OF_KIN_ADDRESS_COUNTRY(NEXT_OF_KIN, "address country", null, "NK1-4.6"),
  NEXT_OF_KIN_ADDRESS_COUNTY(NEXT_OF_KIN, "address county", null, "NK1-4.9"),
  NEXT_OF_KIN_ADDRESS_STATE(NEXT_OF_KIN, "address state", null, "NK1-4.4"),
  NEXT_OF_KIN_ADDRESS_STREET(NEXT_OF_KIN, "address street", null, "NK1-4.1"),
  NEXT_OF_KIN_ADDRESS_STREET2(NEXT_OF_KIN, "address street2", null, "NK1-4.2"),
  NEXT_OF_KIN_ADDRESS_TYPE(NEXT_OF_KIN, "address type", CodesetType.ADDRESS_TYPE, "NK1-4.7"),
  NEXT_OF_KIN_ADDRESS_ZIP(NEXT_OF_KIN, "address zip", null, "NK1-4.5"),
  NEXT_OF_KIN_EMAIL(NEXT_OF_KIN, "email", null, "NK1-5.4"),
  NEXT_OF_KIN_NAME(NEXT_OF_KIN, "name", null, "NK1-2"),
  NEXT_OF_KIN_NAME_FIRST(NEXT_OF_KIN, "name first", null, "NK1-2.2"),
  NEXT_OF_KIN_NAME_LAST(NEXT_OF_KIN, "name last", null, "NK1-2.1"),
  NEXT_OF_KIN_NAME_MIDDLE(NEXT_OF_KIN, "name middle", null, "NK1-2.3"),
  NEXT_OF_KIN_NAME_SUFFIX(NEXT_OF_KIN, "name suffix", null, "NK1-2.4"),
  NEXT_OF_KIN_PHONE(NEXT_OF_KIN, "phone", null, "NK1-5"),
  NEXT_OF_KIN_PHONE_AREA_CODE(NEXT_OF_KIN, "phone area code", null, "NK1-5.6"),
  NEXT_OF_KIN_PHONE_LOCAL_NUMBER(NEXT_OF_KIN, "phone local number", null, "NK1-5.7"),
  NEXT_OF_KIN_PHONE_TEL_USE_CODE(NEXT_OF_KIN, "phone tel use code", CodesetType.TELECOMMUNICATION_USE,
      "NK1-5.2"),
  NEXT_OF_KIN_PHONE_TEL_EQUIP_CODE(NEXT_OF_KIN, "phone tel equip code", CodesetType.TELECOMMUNICATION_EQUIPMENT,
      "NK1-5.3"),
  NEXT_OF_KIN_RELATIONSHIP(NEXT_OF_KIN, "relationship", CodesetType.PERSON_RELATIONSHIP, "NK1-3"),
  NEXT_OF_KIN_PRIMARY_LANGUAGE(NEXT_OF_KIN, "primary language", CodesetType.PERSON_LANGUAGE, "NK1-20"),
  NEXT_OF_KIN_SSN(NEXT_OF_KIN, "SSN", null, "NK1-33")

  //observation
  ,
  OBSERVATION_VALUE_TYPE(OBSERVATION, "value type", CodesetType.HL7_VALUE_TYPE, "OBX-2"),
  OBSERVATION_IDENTIFIER_CODE(OBSERVATION, "identifier code", CodesetType.OBSERVATION_IDENTIFIER, "OBX-3"),
  OBSERVATION_IDENTIFIER_DESC(OBSERVATION, "identifier description", CodesetType.OBSERVATION_IDENTIFIER, "OBX-3-2"),
  OBSERVATION_SUB_ID(OBSERVATION, "observation sub id", CodesetType.OBSERVATION_IDENTIFIER, "OBX-4"),
  OBSERVATION_VALUE(OBSERVATION, "value", null, "OBX-5"),
  OBSERVATION_VALUE_DESC(OBSERVATION, "value description", null, "OBX-5-2"),
  OBSERVATION_DATE_TIME_OF_OBSERVATION(OBSERVATION, "date time of observation", null, "OBX-14")

  //patient
  ,
  PATIENT_ADDRESS(PATIENT, "address", null, "PID-11"),
  PATIENT_ADDRESS_CITY(PATIENT, "address city", null, "PID-11.3"),
  PATIENT_ADDRESS_COUNTRY(PATIENT, "address country", null, "PID-11.6"),
  PATIENT_ADDRESS_COUNTY(PATIENT, "address county", null, "PID-11.9"),
  PATIENT_ADDRESS_STATE(PATIENT, "address state", null, "PID-11.4"),
  PATIENT_ADDRESS_STREET(PATIENT, "address street", null, "PID-11.1"),
  PATIENT_ADDRESS_STREET2(PATIENT, "address street2", null, "PID-11.2"),
  PATIENT_ADDRESS_TYPE(PATIENT, "address type", CodesetType.ADDRESS_TYPE, "PID-11.7"),
  PATIENT_ADDRESS_ZIP(PATIENT, "address zip", null, "PID-11.5"),
  PATIENT_ALIAS(PATIENT, "alias", null, "PID-5"),
  PATIENT_BIRTH_DATE(PATIENT, "birth date", null, "PID-7"),
  PATIENT_BIRTH_INDICATOR(PATIENT, "birth indicator", null, "PID-24"),
  PATIENT_BIRTH_ORDER(PATIENT, "birth order", CodesetType.BIRTH_ORDER, "PID-25"),
  PATIENT_BIRTH_PLACE(PATIENT, "birth place", null, "PID-23"),
  PATIENT_BIRTH_COUNTY(PATIENT, "birth county", null, "PID-11.9"),
  PATIENT_BIRTH_REGISTRY_ID(PATIENT, "birth registry id", null, "PID-3"),
  PATIENT_CLASS(PATIENT, "class", CodesetType.PATIENT_CLASS, "PV1-2"),
  PATIENT_DEATH_DATE(PATIENT, "death date", null, "PID-29"),
  PATIENT_DEATH_INDICATOR(PATIENT, "death indicator", null, "PID-30"),
  PATIENT_EMAIL(PATIENT, "email", null, "PID-13.4"),
  PATIENT_ETHNICITY(PATIENT, "ethnicity", CodesetType.PATIENT_ETHNICITY, "PID-22"),
  PATIENT_GENDER(PATIENT, "gender", CodesetType.PATIENT_SEX, "PID-8"),
  PATIENT_GUARDIAN(PATIENT, "Guardian", null, "NK1"),
  PATIENT_GUARDIAN_ADDRESS(PATIENT, "guardian address", null, "NK1-4"),
  PATIENT_GUARDIAN_ADDRESS_CITY(PATIENT, "guardian address city", null, "NK1-4.3"),
  PATIENT_GUARDIAN_ADDRESS_STATE(PATIENT, "guardian address state", null, "NK1-4.4"),
  PATIENT_GUARDIAN_ADDRESS_STREET(PATIENT, "guardian address street", null, "NK1-4.1"),
  PATIENT_GUARDIAN_ADDRESS_STREET2(PATIENT, "guardian address second street line", null, "NK1-4.2"),
  PATIENT_GUARDIAN_ADDRESS_ZIP(PATIENT, "guardian address zip", null, "NK1-4.5"),
  PATIENT_GUARDIAN_ADDRESS_COUNTRY(PATIENT, "guardian address zip", null, "NK1-4.6"),
  PATIENT_GUARDIAN_ADDRESS_COUNTY(PATIENT, "address county", null, "NK1-4.9"),
  PATIENT_GUARDIAN_NAME(PATIENT, "guardian name", null, "NK1-2"),
  PATIENT_GUARDIAN_NAME_FIRST(PATIENT, "guardian name first", null, "NK1-2.2"),
  PATIENT_GUARDIAN_NAME_MIDDLE(PATIENT, "name middle", null, "NK1-2.3"),
  PATIENT_GUARDIAN_NAME_LAST(PATIENT, "guardian name last", null, "NK1-2.1"),
  PATIENT_GUARDIAN_RESPONSIBLE_PARTY(PATIENT, "guardian responsible party", null, "NK1"),
  PATIENT_GUARDIAN_PHONE(PATIENT, "guardian phone", null, "NK1-5"),
  PATIENT_GUARDIAN_EMAIL(PATIENT, "email", null, "NK1-5.4"),
  PATIENT_GUARDIAN_PHONE_AREA_CODE(PATIENT, "phone area code", null, "NK1-5.6"),
  PATIENT_GUARDIAN_PHONE_LOCAL_NUMBER(PATIENT, "phone local number", null, "NK1-5.7"),
  PATIENT_GUARDIAN_PHONE_TEL_USE_CODE(PATIENT, "phone tel use code", CodesetType.TELECOMMUNICATION_USE,
      "NK1-5.2"),
  PATIENT_GUARDIAN_PHONE_TEL_EQUIP_CODE(PATIENT, "phone tel equip code",
      CodesetType.TELECOMMUNICATION_EQUIPMENT, "NK1-5.3"),
  PATIENT_GUARDIAN_PRIMARY_LANGUAGE(PATIENT, "primary language", CodesetType.PERSON_LANGUAGE, "NK1-20"),
  PATIENT_GUARDIAN_RELATIONSHIP(PATIENT, "guardian relationship", null, "NK1-3"),

  PATIENT_IMMUNITY_CODE(PATIENT, "immunity code", CodesetType.EVIDENCE_OF_IMMUNITY, null),
  PATIENT_IMMUNIZATION_REGISTRY_STATUS(PATIENT, "immunization registry status", null, "PD1-16"),
  PATIENT_MEDICAID_NUMBER(PATIENT, "Medicaid number", null, "PID-3"),
  PATIENT_NAME_MIDDLE(PATIENT, "middle name", null, "PID-5.3"),
  PATIENT_MOTHERS_MAIDEN_NAME(PATIENT, "mother's maiden name", null, "PID-6.1"),
  PATIENT_NAME(PATIENT, "name", null, "PID-5"),
  PATIENT_NAME_FIRST(PATIENT, "name first", null, "PID-5.2"),
  PATIENT_NAME_LAST(PATIENT, "name last", null, "PID-5.1"),
  PATIENT_NAME_SUFFIX(PATIENT, "name suffix", null, "PID-5.4"),
  PATIENT_NAME_TYPE_CODE(PATIENT, "name type code", CodesetType.PERSON_NAME_TYPE, "PID-5.7"),
  PATIENT_PHONE(PATIENT, "phone", null, "PID-13"),
  PATIENT_PHONE_AREA_CODE(PATIENT, "phone area code", null, "PID-13.6"),
  PATIENT_PHONE_LOCAL_NUMBER(PATIENT, "phone local number", null, "PID-13.7"),
  PATIENT_PHONE_TEL_USE_CODE(PATIENT, "phone tel use code", CodesetType.TELECOMMUNICATION_USE, "PID-13.2"),
  PATIENT_PHONE_TEL_EQUIP_CODE(PATIENT, "phone tel equip code", CodesetType.TELECOMMUNICATION_EQUIPMENT,
      "PID-13.3"),
  PATIENT_PRIMARY_FACILITY_ID(PATIENT, "primary facility id", null, "PD1-3.3"),
  PATIENT_PRIMARY_FACILITY_NAME(PATIENT, "primary facility name", null, "PD1-3.1"),
  PATIENT_PRIMARY_LANGUAGE(PATIENT, "primary language", CodesetType.PERSON_LANGUAGE, "PID-15"),
  PATIENT_PRIMARY_PHYSICIAN_ID(PATIENT, "primary physician id", CodesetType.PHYSICIAN_NUMBER, "PD1-4.1"),
  PATIENT_PRIMARY_PHYSICIAN_NAME(PATIENT, "primary physician name", null, "PD1-4.2"),
  PATIENT_PROTECTION_INDICATOR(PATIENT, "protection indicator", CodesetType.PATIENT_PROTECTION, "PD1-12"),
  PATIENT_PUBLICITY_CODE(PATIENT, "publicity code", CodesetType.PATIENT_PUBLICITY, "PD1-11"),
  PATIENT_RACE(PATIENT, "race", CodesetType.PATIENT_RACE, "PID-10"),
  PATIENT_REGISTRY_ID(PATIENT, "registry id", null, "PID-3"),
  PATIENT_REGISTRY_STATUS(PATIENT, "registry status", null, "PD1-16"),
  PATIENT_SSN(PATIENT, "SSN", null, "PID-3"),
  PATIENT_SUBMITTER_ID(PATIENT, "submitter id", null, "PID-3"),
  PATIENT_SUBMITTER_ID_AUTHORITY(PATIENT, "submitter id authority", null, "PID-3.4"),
  PATIENT_SUBMITTER_ID_TYPE_CODE(PATIENT, "submitter id type code", null, "PID-3.5"),
  PATIENT_VFC_EFFECTIVE_DATE(PATIENT, "VFC effective date", null, "PV1-20.2"),
  PATIENT_VFC_STATUS(PATIENT, "VFC status", CodesetType.FINANCIAL_STATUS_CODE, "PV1-20.1"),
  PATIENT_WIC_ID(PATIENT, "WIC id", null, "PID-3")

  //vaccination
  ,
  VACCINATION_ACTION_CODE(VACCINATION, "action code", CodesetType.VACCINATION_ACTION_CODE, "RXA-21"),
  VACCINATION_ADMIN_CODE(VACCINATION, "admin code", CodesetType.VACCINATION_CVX_CODE, "RXA-5"),
  VACCINATION_ADMIN_CODE_TABLE(VACCINATION, "admin code table", null, "RXA-5"),
  VACCINATION_ADMIN_DATE(VACCINATION, "admin date", null, "RXA-3"),
  VACCINATION_ADMIN_DATE_END(VACCINATION, "admin date end", null, "RXA-4"),
  VACCINATION_ADMINISTERED_AMOUNT(VACCINATION, "administered amount", null, "RXA-6"),
  VACCINATION_ADMINISTERED_UNIT(VACCINATION, "administered unit", CodesetType.ADMINISTRATION_UNIT, "RXA-7"),
  VACCINATION_BODY_ROUTE(VACCINATION, "body route", CodesetType.BODY_ROUTE, "RXR-1"),
  VACCINATION_BODY_SITE(VACCINATION, "body site", CodesetType.BODY_SITE, "RXR-2"),
  VACCINATION_COMPLETION_STATUS(VACCINATION, "completion status", CodesetType.VACCINATION_COMPLETION, "RXA-20"),
  VACCINATION_CONFIDENTIALITY_CODE(VACCINATION, "confidentiality code", CodesetType.VACCINATION_CONFIDENTIALITY,
      "ORC-28"),
  VACCINATION_CPT_CODE(VACCINATION, "CPT code", CodesetType.VACCINATION_CPT_CODE, "RXA-5"),
  VACCINATION_CVX_CODE(VACCINATION, "CVX code", CodesetType.VACCINATION_CVX_CODE, "RXA-5"),
  VACCINATION_NDC_CODE(VACCINATION, "NDC code", CodesetType.VACCINATION_NDC_CODE, "RXA-5"),
  VACCINATION_CVX_CODE_AND_CPT_CODE(VACCINATION, "CVX code and CPT code", null, "RXA-5"),
  VACCINATION_FACILITY_ID(VACCINATION, "facility id", null, "RXA-11.4"),
  VACCINATION_FACILITY_NAME(VACCINATION, "facility name", null, "RXA-11.4"),
  VACCINATION_FACILITY_TYPE(VACCINATION, "facility type", null, null),
  VACCINATION_FILLER_ORDER_NUMBER(VACCINATION, "filler order number", null, "ORC-3"),
  VACCINATION_FINANCIAL_ELIGIBILITY_CODE(VACCINATION, "financial eligibility code",
      CodesetType.FINANCIAL_STATUS_CODE, "OBX-5"),
  VACCINATION_GIVEN_BY(VACCINATION, "given by", null, "RXA-10"),
  VACCINATION_ID(VACCINATION, "id", null, "ORC-3"),
  VACCINATION_ID_OF_RECEIVER(VACCINATION, "id of receiver", null, "ORC-2"),
  VACCINATION_ID_OF_SENDER(VACCINATION, "id of sender", null, "ORC-3"),
  VACCINATION_INFORMATION_SOURCE(VACCINATION, "information source", CodesetType.VACCINATION_INFORMATION_SOURCE,
      "RXA-9"),
  VACCINATION_VIS(VACCINATION, "VIS", null, "RXA-9"),
  VACCINATION_VIS_VERSION_DATE(VACCINATION, "VIS Version Date", null, null),
  VACCINATION_VIS_DELIVERY_DATE(VACCINATION, "VIS Delivery Date", null, null),
  VACCINATION_VIS_CVX_CODE(VACCINATION, "VIS CVX Code", CodesetType.VACCINATION_VIS_CVX_CODE, null),
  VACCINATION_VIS_DOCUMENT_TYPE(VACCINATION, "VIS document type", null, null),
  VACCINATION_VIS_PUBLISHED_DATE(VACCINATION, "VIS published date", null, null),
  VACCINATION_VIS_PRESENTED_DATE(VACCINATION, "VIS presented date", null, null),
  VACCINATION_LOT_EXPIRATION_DATE(VACCINATION, "lot expiration date", null, "RXA-16"),
  VACCINATION_LOT_NUMBER(VACCINATION, "lot number", null, "RXA-15"),
  VACCINATION_MANUFACTURER_CODE(VACCINATION, "manufacturer code", CodesetType.VACCINATION_MANUFACTURER_CODE,
      "RXA-17"),
  VACCINATION_ORDER_CONTROL_CODE(VACCINATION, "order control code", null, "ORC-1"),
  VACCINATION_ORDER_FACILITY_ID(VACCINATION, "order facility id", null, "ORC-21"),
  VACCINATION_ORDER_FACILITY_NAME(VACCINATION, "order facility name", null, "ORC-21"),
  VACCINATION_ORDERED_BY(VACCINATION, "ordered by", null, "XCN-12"),
  VACCINATION_PLACER_ORDER_NUMBER(VACCINATION, "placer order number", null, "ORC-2"),
  VACCINATION_PRODUCT(VACCINATION, "product", CodesetType.VACCINE_PRODUCT, "RXA-5"),
  VACCINATION_RECORDED_BY(VACCINATION, "recorded by", null, "ORC-10"),
  VACCINATION_REFUSAL_REASON(VACCINATION, "refusal reason", CodesetType.VACCINATION_REFUSAL, "RXA-18"),
  VACCINATION_SYSTEM_ENTRY_TIME(VACCINATION, "system entry time", null, "RXA-22"),
  VACCINATION_TRADE_NAME(VACCINATION, "trade name", null, null),
  VACCINATION_TRADE_NAME_AND_VACCINE(VACCINATION, "trade name and vaccine", null, null),
  VACCINATION_TRADE_NAME_AND_MANUFACTURER(VACCINATION, "trade name and manufacturer", null, null),
  VACCINATION_VALIDITY_CODE(VACCINATION, "validity code", null, null),

  ;

  private final String fieldDescription;
  private final CodesetType valueType;
  private final String hl7Field;
  private final VxuObject object;

  VxuField(VxuObject object, String fieldDesc, CodesetType typeOfValue, String hl7Field) {
    this.object = object;
    this.fieldDescription = fieldDesc;
    this.valueType = typeOfValue;
    this.hl7Field = hl7Field;
  }

  public static VxuField getByType(CodesetType t) {
    if (t == null) {
      return NONE;
    }

    for (VxuField f : VxuField.values()) {
      if (t == f.getCodesetType()) {
        return f;
      }
    }
    return NONE;
  }

  public static VxuField getByName(String name) {
    if (name != null) {
      for (VxuField f : VxuField.values()) {
        if (name.equals(f.toString())) {
          return f;
        }
      }
    }
    return NONE;
  }

  public String getFieldDescription() {
    return fieldDescription;
  }

  public CodesetType getCodesetType() {
    return valueType;
  }

  public String getHl7Locator() {
    return hl7Field;
  }

  public VxuObject getObject() {
    return this.object;
  }

}
