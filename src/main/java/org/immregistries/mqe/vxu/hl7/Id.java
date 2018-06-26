package org.immregistries.mqe.vxu.hl7;

public class Id {

  private String assigningAuthority = "";//new CodedEntity(CodesetType.ID_ASSIGNING_AUTHORITY);
  private Name name = null;
  private String number = "";
  private String type = "";//new CodedEntity(CodesetType.ID_TYPE_CODE);

  public boolean isEmpty() {
    return number == null || number.isEmpty();
  }

  public String getAssigningAuthority() {
    return assigningAuthority;
  }

  public String getAssigningAuthorityCode() {
    return assigningAuthority;
  }

  public Name getName() {
    if (name == null) {
      name = new Name();
    }
    return name;
  }

  public String getNumber() {
    return number;
  }

  public String getType() {
    return type;
  }

  public String getTypeCode() {
    return type;
  }

  public void setAssigningAuthorityCode(String assigningAuthorityCode) {
    this.assigningAuthority = assigningAuthorityCode;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public void setTypeCode(String typeCode) {
    this.type = typeCode;
  }

  @Override
  public String toString() {
    return "Id{" +
        "assigningAuthority='" + assigningAuthority + '\'' +
        ", name=" + name +
        ", number='" + number + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
