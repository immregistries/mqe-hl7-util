package org.immregistries.dqa.vxu.hl7;

import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.immregistries.dqa.vxu.code.CodeReceived;

public class Id
{
  private String assigningAuthority = "";//new CodedEntity(CodesetType.ID_ASSIGNING_AUTHORITY);
  private CodeReceived codeReceived = null;
  private CodesetType tableType = null;
  private Name name = null;
  private String number = "";
  private String type = "";//new CodedEntity(CodesetType.ID_TYPE_CODE);
  
  public boolean isEmpty()
  {
    return number == null || number.isEmpty();
  }

  public Id(CodesetType tableType) {
    this.tableType = tableType;
  }

  public String getAssigningAuthority()
  {
    return assigningAuthority;
  }

  public String getAssigningAuthorityCode()
  {
    return assigningAuthority;
  }

  public CodeReceived getCodeReceived()
  {
    return codeReceived;
  }


  public CodesetType getTableType()
  {
    return tableType;
  }
  public Name getName()
  {
    if (name == null)
    {
      name = new Name();
    }
    return name;
  }
  public String getNumber()
  {
    return number;
  }
  public String getType()
  {
    return type;
  }
  public String getTypeCode()
  {
    return type;
  }
  public void setAssigningAuthorityCode(String assigningAuthorityCode)
  {
    this.assigningAuthority = assigningAuthorityCode;
  }
  public void setCodeReceived(CodeReceived codeReceived)
  {
    this.codeReceived = codeReceived;
  }
  public void setNumber(String number)
  {
    this.number = number;
  }
  
  public void setTypeCode(String typeCode)
  {
    this.type = typeCode;
  }
  
}
