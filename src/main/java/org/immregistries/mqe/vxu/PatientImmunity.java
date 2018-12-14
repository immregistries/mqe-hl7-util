package org.immregistries.mqe.vxu;

public class PatientImmunity {

  private String code = "";
  private String type = "";
//new CodedEntity(CodesetType.EVIDENCE_OF_IMMUNITY);

  public String getCode() {
	  return code;
  }
  public void setCode(String code) {
	  this.code = code;
  }
  public String getType() {
	  return type;
  }
  public void setType(String type) {
	  this.type = type;
  }

}
