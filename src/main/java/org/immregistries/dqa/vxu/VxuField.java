package org.immregistries.dqa.vxu;

import org.immregistries.dqa.codebase.client.reference.CodesetType;

public enum VxuField {
  /*
   * What do you think about the idea of tagging some of these bad boys as "Coded"
   */

  /*
   * What do you think about tagging the HL7 Location for these bad boys. Pull them from the MessageAttribute class.
   */

  //Fields: String fieldDesc, CodesetType typeOfValue, String hl7Field

  //general
  AUTHORIZATION("authorization", null, null),
  CONFIGURATION("configuration", null, null),
  PARSE("parse", null, null),
  PROCESSING("processing", null, null)

  //message header
  ,
  MESSAGE_ACCEPT_ACK_TYPE("accept ack type", CodesetType.ACKNOWLEDGEMENT_TYPE, "MSH-15"),
  MESSAGE_ALT_CHARACTER_SET("alt character set", null, null),
  MESSAGE_APP_ACK_TYPE("app ack type", CodesetType.ACKNOWLEDGEMENT_TYPE, "MSH-16"),
  MESSAGE_CHARACTER_SET("character set", null, null),
  MESSAGE_COUNTRY_CODE("country code", null, null),
  MESSAGE_ENCODING_CHARACTER("encoding character", null, null),
  MESSAGE_CONTROL_ID("message control id", null, "MSH-10"),
  MESSAGE_DATE("message date", null, "MSH-7"),
  MESSAGE_PROFILE_ID("message profile id", null, "MSH-21"),
  MESSAGE_TRIGGER("message trigger", null, "MSH-9.2"),
  MESSAGE_STRUCTURE("message trigger", null, "MSH-9.3"),
  MESSAGE_TYPE("message type", null, "MSH-9.1"),
  MESSAGE_PROCESSING_ID("processing id", null, "MSH-11"),
  MESSAGE_RECEIVING_APPLICATION("receiving application", null, "MSH-5"),
  MESSAGE_RECEIVING_FACILITY("receiving facility", null, "MSH-6"),
  MESSAGE_SEGMENT("segment", null, null),
  MESSAGE_SENDING_APPLICATION("sending application", null, "MSH-3"),
  MESSAGE_SENDING_FACILITY("sending facility", null, "MSH-4"),
  MESSAGE_VERSION("version", null, "MSH-12"),
  MESSAGE_SENDING_RESPONSIBLE_ORGANIZATION("sending responsible organization", null, "MSH-22")

  //Next of kin
  ,
  NEXT_OF_KIN_ADDRESS("address", null, "NK1-4"),
  NEXT_OF_KIN_ADDRESS_CITY("address city", null, "NK1-4.3"),
  NEXT_OF_KIN_ADDRESS_COUNTRY("address country", null, "NK1-4.6"),
  NEXT_OF_KIN_ADDRESS_COUNTY("address county", null, "NK1-4.9"),
  NEXT_OF_KIN_ADDRESS_STATE("address state", null, "NK1-4.4"),
  NEXT_OF_KIN_ADDRESS_STREET("address street", null, "NK1-4.1"),
  NEXT_OF_KIN_ADDRESS_STREET2("address street2", null, "NK1-4.2"),
  NEXT_OF_KIN_ADDRESS_TYPE("address type", CodesetType.ADDRESS_TYPE, "NK1-4.7"),
  NEXT_OF_KIN_ADDRESS_ZIP("address zip", null, "NK1-4.5"),
  NEXT_OF_KIN_EMAIL("email", null, "NK1-5.4"),
  NEXT_OF_KIN_NAME("name", null, "NK1-2"),
  NEXT_OF_KIN_NAME_FIRST("name first", null, "NK1-2.2"),
  NEXT_OF_KIN_NAME_LAST("name last", null, "NK1-2.1"),
  NEXT_OF_KIN_NAME_MIDDLE("name middle", null, "NK1-2.3"),
  NEXT_OF_KIN_NAME_SUFFIX("name suffix", null, "NK1-2.4"),
  NEXT_OF_KIN_PHONE("phone", null, "NK1-5"),
  NEXT_OF_KIN_PHONE_AREA_CODE("phone area code", null, "NK1-5.6"),
  NEXT_OF_KIN_PHONE_LOCAL_NUMBER("phone local number", null, "NK1-5.7"),
  NEXT_OF_KIN_PHONE_TEL_USE_CODE("phone tel use code", CodesetType.TELECOMMUNICATION_USE,
      "NK1-5.2"),
  NEXT_OF_KIN_PHONE_TEL_EQUIP_CODE("phone tel equip code", CodesetType.TELECOMMUNICATION_EQUIPMENT,
      "NK1-5.3"),
  NEXT_OF_KIN_RELATIONSHIP("relationship", CodesetType.PERSON_RELATIONSHIP, "NK1-3"),
  NEXT_OF_KIN_PRIMARY_LANGUAGE("primary language", CodesetType.PERSON_LANGUAGE, "NK1-20"),
  NEXT_OF_KIN_SSN("SSN", null, "NK1-33")

  //observation
  ,
  OBSERVATION_VALUE_TYPE("value type", CodesetType.HL7_VALUE_TYPE, "OBX-2"),
  OBSERVATION_IDENTIFIER_CODE("identifier code", CodesetType.OBSERVATION_IDENTIFIER, "OBX-3"),
  OBSERVATION_IDENTIFIER_DESC("identifier description", CodesetType.OBSERVATION_IDENTIFIER,
      "OBX-3-2"),
  OBSERVATION_SUB_ID("observation sub id", CodesetType.OBSERVATION_IDENTIFIER, "OBX-4"),
  OBSERVATION_VALUE("value", null, "OBX-5"),
  OBSERVATION_VALUE_DESC("value description", null, "OBX-5-2"),
  OBSERVATION_DATE_TIME_OF_OBSERVATION("date time of observation", null, "OBX-14")

  //patient
  ,
  PATIENT_ADDRESS("address", null, "PID-11"),
  PATIENT_ADDRESS_CITY("address city", null, "PID-11.3"),
  PATIENT_ADDRESS_COUNTRY("address country", null, "PID-11.6"),
  PATIENT_ADDRESS_COUNTY("address county", null, "PID-11.9"),
  PATIENT_ADDRESS_STATE("address state", null, "PID-11.4"),
  PATIENT_ADDRESS_STREET("address street", null, "PID-11.1"),
  PATIENT_ADDRESS_STREET2("address street2", null, "PID-11.2"),
  PATIENT_ADDRESS_TYPE("address type", CodesetType.ADDRESS_TYPE, "PID-11.7"),
  PATIENT_ADDRESS_ZIP("address zip", null, "PID-11.5"),
  PATIENT_ALIAS("alias", null, "PID-5"),
  PATIENT_BIRTH_DATE("birth date", null, "PID-7"),
  PATIENT_BIRTH_INDICATOR("birth indicator", null, "PID-24"),
  PATIENT_BIRTH_ORDER("birth order", CodesetType.BIRTH_ORDER, "PID-25"),
  PATIENT_BIRTH_PLACE("birth place", null, "PID-23"),
  PATIENT_BIRTH_COUNTY("birth county", null, "PID-11.9"),
  PATIENT_BIRTH_REGISTRY_ID("birth registry id", null, "PID-3"),
  PATIENT_CLASS("class", CodesetType.PATIENT_CLASS, "PV1-2"),
  PATIENT_DEATH_DATE("death date", null, "PID-29"),
  PATIENT_DEATH_INDICATOR("death indicator", null, "PID-30"),
  PATIENT_EMAIL("email", null, "PID-13.4"),
  PATIENT_ETHNICITY("ethnicity", CodesetType.PATIENT_ETHNICITY, "PID-22"),
  PATIENT_GENDER("gender", CodesetType.PATIENT_SEX, "PID-8"),
  PATIENT_GUARDIAN("Guardian", null, "NK1"),
  PATIENT_GUARDIAN_ADDRESS("guardian address", null, "NK1-4"),
  PATIENT_GUARDIAN_ADDRESS_CITY("guardian address city", null, "NK1-4.3"),
  PATIENT_GUARDIAN_ADDRESS_STATE("guardian address state", null, "NK1-4.4"),
  PATIENT_GUARDIAN_ADDRESS_STREET("guardian address street", null, "NK1-4.1"),
  PATIENT_GUARDIAN_ADDRESS_STREET2("guardian address second street line", null, "NK1-4.2"),
  PATIENT_GUARDIAN_ADDRESS_ZIP("guardian address zip", null, "NK1-4.5"),
  PATIENT_GUARDIAN_ADDRESS_COUNTRY("guardian address zip", null, "NK1-4.6"),
  PATIENT_GUARDIAN_ADDRESS_COUNTY("address county", null, "NK1-4.9"),
  PATIENT_GUARDIAN_NAME("guardian name", null, "NK1-2"),
  PATIENT_GUARDIAN_NAME_FIRST("guardian name first", null, "NK1-2.2"),
  PATIENT_GUARDIAN_NAME_MIDDLE("name middle", null, "NK1-2.3"),
  PATIENT_GUARDIAN_NAME_LAST("guardian name last", null, "NK1-2.1"),
  PATIENT_GUARDIAN_RESPONSIBLE_PARTY("guardian responsible party", null, "NK1"),
  PATIENT_GUARDIAN_PHONE("guardian phone", null, "NK1-5"),
  PATIENT_GUARDIAN_EMAIL("email", null, "NK1-5.4"),
  PATIENT_GUARDIAN_PHONE_AREA_CODE("phone area code", null, "NK1-5.6"),
  PATIENT_GUARDIAN_PHONE_LOCAL_NUMBER("phone local number", null, "NK1-5.7"),
  PATIENT_GUARDIAN_PHONE_TEL_USE_CODE("phone tel use code", CodesetType.TELECOMMUNICATION_USE,
      "NK1-5.2"),
  PATIENT_GUARDIAN_PHONE_TEL_EQUIP_CODE("phone tel equip code",
      CodesetType.TELECOMMUNICATION_EQUIPMENT, "NK1-5.3"),
  PATIENT_GUARDIAN_PRIMARY_LANGUAGE("primary language", CodesetType.PERSON_LANGUAGE, "NK1-20"),
  PATIENT_GUARDIAN_RELATIONSHIP("guardian relationship", null, "NK1-3"),

  PATIENT_IMMUNITY_CODE("immunity code", CodesetType.EVIDENCE_OF_IMMUNITY, null),
  PATIENT_IMMUNIZATION_REGISTRY_STATUS("immunization registry status", null, "PD1-16"),
  PATIENT_MEDICAID_NUMBER("Medicaid number", null, "PID-3"),
  PATIENT_NAME_MIDDLE("middle name", null, "PID-5.3"),
  PATIENT_MOTHERS_MAIDEN_NAME("mother's maiden name", null, "PID-6.1"),
  PATIENT_NAME("name", null, "PID-5"),
  PATIENT_NAME_FIRST("name first", null, "PID-5.2"),
  PATIENT_NAME_LAST("name last", null, "PID-5.1"),
  PATIENT_NAME_SUFFIX("name suffix", null, "PID-5.4"),
  PATIENT_NAME_TYPE_CODE("name type code", CodesetType.PERSON_NAME_TYPE, "PID-5.7"),
  PATIENT_PHONE("phone", null, "PID-13"),
  PATIENT_PHONE_AREA_CODE("phone area code", null, "PID-13.6"),
  PATIENT_PHONE_LOCAL_NUMBER("phone local number", null, "PID-13.7"),
  PATIENT_PHONE_TEL_USE_CODE("phone tel use code", CodesetType.TELECOMMUNICATION_USE, "PID-13.2"),
  PATIENT_PHONE_TEL_EQUIP_CODE("phone tel equip code", CodesetType.TELECOMMUNICATION_EQUIPMENT,
      "PID-13.3"),
  PATIENT_PRIMARY_FACILITY_ID("primary facility id", null, "PD1-3.3"),
  PATIENT_PRIMARY_FACILITY_NAME("primary facility name", null, "PD1-3.1"),
  PATIENT_PRIMARY_LANGUAGE("primary language", CodesetType.PERSON_LANGUAGE, "PID-15"),
  PATIENT_PRIMARY_PHYSICIAN_ID("primary physician id", CodesetType.PHYSICIAN_NUMBER, "PD1-4.1"),
  PATIENT_PRIMARY_PHYSICIAN_NAME("primary physician name", null, "PD1-4.2"),
  PATIENT_PROTECTION_INDICATOR("protection indicator", CodesetType.PATIENT_PROTECTION, "PD1-12"),
  PATIENT_PUBLICITY_CODE("publicity code", CodesetType.PATIENT_PUBLICITY, "PD1-11"),
  PATIENT_RACE("race", CodesetType.PATIENT_RACE, "PID-10"),
  PATIENT_REGISTRY_ID("registry id", null, "PID-3"),
  PATIENT_REGISTRY_STATUS("registry status", null, "PD1-16"),
  PATIENT_SSN("SSN", null, "PID-3"),
  PATIENT_SUBMITTER_ID("submitter id", null, "PID-3"),
  PATIENT_SUBMITTER_ID_AUTHORITY("submitter id authority", null, "PID-3.4"),
  PATIENT_SUBMITTER_ID_TYPE_CODE("submitter id type code", null, "PID-3.5"),
  PATIENT_SYSTEM_CREATION_DATE("system creation date", null, null),
  PATIENT_VFC_EFFECTIVE_DATE("VFC effective date", null, "PV1-20.2"),
  PATIENT_VFC_STATUS("VFC status", CodesetType.FINANCIAL_STATUS_CODE, "PV1-20.1"),
  PATIENT_WIC_ID("WIC id", null, "PID-3")

  //vaccination
  ,
  VACCINATION_ACTION_CODE("action code", CodesetType.VACCINATION_ACTION_CODE, "RXA-21"),
  VACCINATION_ADMIN_CODE("admin code", CodesetType.VACCINATION_CVX_CODE, "RXA-5"),
  VACCINATION_ADMIN_CODE_TABLE("admin code table", null, "RXA-5"),
  VACCINATION_ADMIN_DATE("admin date", null, "RXA-3"),
  VACCINATION_ADMIN_DATE_END("admin date end", null, "RXA-4"),
  VACCINATION_ADMINISTERED_AMOUNT("administered amount", null, "RXA-6"),
  VACCINATION_ADMINISTERED_UNIT("administered unit", CodesetType.ADMINISTRATION_UNIT, "RXA-7"),
  VACCINATION_BODY_ROUTE("body route", CodesetType.BODY_ROUTE, "RXR-1"),
  VACCINATION_BODY_SITE("body site", CodesetType.BODY_SITE, "RXR-2"),
  VACCINATION_COMPLETION_STATUS("completion status", CodesetType.VACCINATION_COMPLETION, "RXA-20"),
  VACCINATION_CONFIDENTIALITY_CODE("confidentiality code", CodesetType.VACCINATION_CONFIDENTIALITY,
      "ORC-28"),
  VACCINATION_CPT_CODE("CPT code", CodesetType.VACCINATION_CPT_CODE, "RXA-5"),
  VACCINATION_CVX_CODE("CVX code", CodesetType.VACCINATION_CVX_CODE, "RXA-5"),
  VACCINATION_NDC_CODE("NDC code", CodesetType.VACCINATION_NDC_CODE, "RXA-5"),
  VACCINATION_CVX_CODE_AND_CPT_CODE("CVX code and CPT code", null, "RXA-5"),
  VACCINATION_FACILITY_ID("facility id", null, "RXA-11.4"),
  VACCINATION_FACILITY_NAME("facility name", null, "RXA-11.4"),
  VACCINATION_FACILITY_TYPE("facility type", null, null),
  VACCINATION_FILLER_ORDER_NUMBER("filler order number", null, "ORC-3"),
  VACCINATION_FINANCIAL_ELIGIBILITY_CODE("financial eligibility code",
      CodesetType.FINANCIAL_STATUS_CODE, "OBX-5"),
  VACCINATION_GIVEN_BY("given by", null, "RXA-10"),
  VACCINATION_ID("id", null, "ORC-3"),
  VACCINATION_ID_OF_RECEIVER("id of receiver", null, "ORC-2"),
  VACCINATION_ID_OF_SENDER("id of sender", null, "ORC-3"),
  VACCINATION_INFORMATION_SOURCE("information source", CodesetType.VACCINATION_INFORMATION_SOURCE,
      "RXA-9"),
  VACCINATION_VIS("VIS", null, "RXA-9"),
  VACCINATION_VIS_VERSION_DATE("VIS Version Date", null, null),
  VACCINATION_VIS_DELIVERY_DATE("VIS Delivery Date", null, null),
  VACCINATION_VIS_CVX_CODE("VIS CVX Code", CodesetType.VACCINATION_VIS_CVX_CODE, null),
  VACCINATION_VIS_DOCUMENT_TYPE("VIS document type", null, null),
  VACCINATION_VIS_PUBLISHED_DATE("VIS published date", null, null),
  VACCINATION_VIS_PRESENTED_DATE("VIS presented date", null, null),
  VACCINATION_LOT_EXPIRATION_DATE("lot expiration date", null, "RXA-16"),
  VACCINATION_LOT_NUMBER("lot number", null, "RXA-15"),
  VACCINATION_MANUFACTURER_CODE("manufacturer code", CodesetType.VACCINATION_MANUFACTURER_CODE,
      "RXA-17"),
  VACCINATION_ORDER_CONTROL_CODE("order control code", null, "ORC-1"),
  VACCINATION_ORDER_FACILITY_ID("order facility id", null, "ORC-21"),
  VACCINATION_ORDER_FACILITY_NAME("order facility name", null, "ORC-21"),
  VACCINATION_ORDERED_BY("ordered by", null, "XCN-12"),
  VACCINATION_PLACER_ORDER_NUMBER("placer order number", null, "ORC-2"),
  VACCINATION_PRODUCT("product", CodesetType.VACCINE_PRODUCT, "RXA-5"),
  VACCINATION_RECORDED_BY("recorded by", null, "ORC-10"),
  VACCINATION_REFUSAL_REASON("refusal reason", CodesetType.VACCINATION_REFUSAL, "RXA-18"),
  VACCINATION_SYSTEM_ENTRY_TIME("system entry time", null, "RXA-22"),
  VACCINATION_TRADE_NAME("trade name", null, null),
  VACCINATION_TRADE_NAME_AND_VACCINE("trade name and vaccine", null, null),
  VACCINATION_TRADE_NAME_AND_MANUFACTURER("trade name and manufacturer", null, null),
  VACCINATION_VALIDITY_CODE("validity code", null, null),
  NONE("object", null, null);

  private final String fieldDescription;
  private final CodesetType valueType;
  private final String hl7Field;

  VxuField(String fieldDesc, CodesetType typeOfValue, String hl7Field) {
    this.fieldDescription = fieldDesc;
    this.valueType = typeOfValue;
    this.hl7Field = hl7Field;
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

}
