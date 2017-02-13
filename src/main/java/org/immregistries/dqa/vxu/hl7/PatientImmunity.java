package org.immregistries.dqa.vxu.hl7;

public class PatientImmunity {
	
  private int immunityId = 0;
  private String immunity = "";//new CodedEntity(CodesetType.EVIDENCE_OF_IMMUNITY);
  
  public String getImmunityCode()
  {
    return immunity;
  }
  
  public void setImmunityCode(String immunityCode)
  {
    immunity = immunityCode;
  }
  
  public String getImmunity()
  {
    return immunity;
  }
  public int getImmunityId()
  {
    return immunityId;
  }
  public void setImmunityId(int immunityId)
  {
    this.immunityId = immunityId;
  }

}
