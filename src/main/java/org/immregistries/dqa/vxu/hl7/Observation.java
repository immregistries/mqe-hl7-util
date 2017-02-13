package org.immregistries.dqa.vxu.hl7;


public class Observation {
  
  private String observationIdentifier = "";//new CodedEntity(CodesetType.OBSERVATION_IDENTIFIER);
  private String observationValue = "";
  private String observationDateString;
  private String observationSubId = "";
  private String valueType = "";//new CodedEntity(CodesetType.HL7_VALUE_TYPE);
  private String observationMethodCode ="";//this could be at the immunization level, or patient level
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

   
}
