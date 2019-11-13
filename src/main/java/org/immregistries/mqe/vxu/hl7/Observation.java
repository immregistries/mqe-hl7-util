package org.immregistries.mqe.vxu.hl7;

import org.immregistries.mqe.hl7util.model.MetaFieldInfo;
import org.immregistries.mqe.util.validation.MqeValidatedObject;
import org.immregistries.mqe.vxu.MetaFieldInfoHolder;
import org.immregistries.mqe.vxu.TargetType;

public class Observation extends MqeValidatedObject {
  @Override
  public TargetType getTargetType() { return TargetType.Observation; }
  private String observationIdentifier = "";//new CodedEntity(CodesetType.OBSERVATION_IDENTIFIER);
  private String observationIdentifierDescription = "";
  private String observationValue = "";
  private String observationValueDesc = "";
  private String observationDateString;
  private String observationSubId = "";
  private String valueType = "";//new CodedEntity(CodesetType.HL7_VALUE_TYPE);
  private String observationMethodCode = "";//this could be at the immunization level, or patient level
  //example: VXC40^Eligibility captured at the immunization level^CDCPHINVS
  //example of patient level???

  public String getSubId() {
    return observationSubId;
  }

  public void setSubId(String subId) {
    this.observationSubId = subId;
  }

  public String getIdentifierCode() {
    return this.observationIdentifier;
  }

  public String getValue() {
    return observationValue;
  }

  public String getValueTypeCode() {
    return valueType;
  }

  public void setIdentifierCode(String observationIdentifierCode) {
    this.observationIdentifier = observationIdentifierCode;
  }

  public void setValue(String observationValue) {
    this.observationValue = observationValue;
  }

  public void setValueTypeCode(String valueTypeCode) {
    this.valueType = valueTypeCode;
  }

  public String getObservationMethodCode() {
    return observationMethodCode;
  }

  public void setObservationMethodCode(String observationMethodCode) {
    this.observationMethodCode = observationMethodCode;
  }

  public String getObservationDateString() {
    return observationDateString;
  }

  public void setObservationDateString(String observationDateString) {
    this.observationDateString = observationDateString;
  }

  public String getObservationValueDesc() {
    return observationValueDesc;
  }

  public void setObservationValueDesc(String observationValueDesc) {
    this.observationValueDesc = observationValueDesc;
  }

  public String getObservationIdentifierDescription() {
    return observationIdentifierDescription;
  }

  public void setObservationIdentifierDescription(
      String observationIdentifierDescription) {
    this.observationIdentifierDescription = observationIdentifierDescription;
  }

  @Override
  public TargetType getTargetType() {
    return TargetType.Observation;
  }

  @Override
  protected void setFieldFromMetaFieldInfo(MetaFieldInfo metaFieldInfo) {
    String value = metaFieldInfo.getValue();
    switch (metaFieldInfo.getVxuField()) {
      case OBSERVATION_DATE_TIME_OF_OBSERVATION:
        this.observationDateString = value;
        break;
      case OBSERVATION_VALUE_TYPE:
        this.valueType = value;
        break;
      case OBSERVATION_VALUE_DESC:
        this.observationValueDesc = value;
        break;
      case OBSERVATION_VALUE:
        this.observationValue = value;
        break;
      case OBSERVATION_IDENTIFIER_CODE:
        this.observationIdentifier = value;
        break;
      case OBSERVATION_IDENTIFIER_DESC:
        this.observationValueDesc = value;
        break;
      case OBSERVATION_SUB_ID:
        this.observationSubId = value;
        break;
    }
  }

  @Override
  public String toString() {
    return "Observation{" +
        "observationIdentifier='" + observationIdentifier + '\'' +
        ", observationIdentifierDescription='" + observationIdentifierDescription + '\'' +
        ", observationValue='" + observationValue + '\'' +
        ", observationValueDesc='" + observationValueDesc + '\'' +
        ", observationDateString='" + observationDateString + '\'' +
        ", observationSubId='" + observationSubId + '\'' +
        ", valueType='" + valueType + '\'' +
        ", observationMethodCode='" + observationMethodCode + '\'' +
        '}';
  }
}
