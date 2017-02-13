package org.immregistries.dqa.vxu;

import org.immregistries.dqa.vxu.code.NokRelationship;
import org.immregistries.dqa.vxu.hl7.Address;
import org.immregistries.dqa.vxu.hl7.Name;
import org.immregistries.dqa.vxu.hl7.PhoneNumber;

public class DqaNextOfKin {
  
  private Address address = new Address();
  private Name name = new Name();
  private long nextOfKinId;
  private PhoneNumber phone = new PhoneNumber();
  private int positionId = 0;
  private String relationship = "";//new CodedEntity(CodesetType.PERSON_RELATIONSHIP);
  private boolean skipped = false;
  private String primaryLanguageCode;
  
  public Address getAddress()
  {
    return address;
  }


  public void setAddress(Address a) {
	  this.address = a;
  }
  
  public Name getName()
  {
    return name;
  }

  public String getNameFirst()
  {
    return name.getFirst();
  }

  public String getNameLast()
  {
    return name.getLast();
  }

  public String getNameMiddle()
  {
    return name.getMiddle();
  }

  public String getNamePrefix()
  {
    return name.getPrefix();
  }

  public String getNameSuffix()
  {
    return name.getSuffix();
  }

  public String getNameTypeCode()
  {
    return name.getTypeCode();
  }

  public long getNextOfKinId()
  {
    return nextOfKinId;
  }

  public PhoneNumber getPhone()
  {
    return phone;
  }

  public String getPhoneNumber()
  {
    return phone.getNumber();
  }

  public int getPositionId()
  {
    return positionId;
  }

  public String getRelationship()
  {
    return relationship;
  }

  public String getRelationshipCode()
  {
    return relationship;
  }

  public boolean isSkipped()
  {
    return skipped;
  }

  public void setNameFirst(String nameFirst)
  {
    name.setFirst(nameFirst);
  }

  public void setNameLast(String nameLast)
  {
    name.setLast(nameLast);
  }

  public void setNameMiddle(String nameMiddle)
  {
    name.setMiddle(nameMiddle);
  }

  public void setNamePrefix(String namePrefix)
  {
    name.setPrefix(namePrefix);
  }

  public void setNameSuffix(String nameSuffix)
  {
    name.setSuffix(nameSuffix);
  }

  public void setNameTypeCode(String nameTypeCode)
  {
    name.setTypeCode(nameTypeCode);
  }

  public void setNextOfKinId(long nextOfKinId)
  {
    this.nextOfKinId = nextOfKinId;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    phone.setNumber(phoneNumber);
  }
  
  public void setPhone(PhoneNumber phoneIn) {
	  this.phone = phoneIn;
  }

  public void setPositionId(int positionId)
  {
    this.positionId = positionId;
  }

  public void setRelationshipCode(String relationshipCode)
  {
    relationship = relationshipCode;
  }

  public void setSkipped(boolean skipped)
  {
    this.skipped = skipped;
  }

/**
 * @return the primaryLanguageCode
 */
public String getPrimaryLanguageCode() {
	return primaryLanguageCode;
}

/**
 * @param primaryLanguageCode the primaryLanguageCode to set
 */
public void setPrimaryLanguageCode(String primaryLanguageCode) {
	this.primaryLanguageCode = primaryLanguageCode;
}

public NokRelationship getNokRelationship() {
	return NokRelationship.get(this.getRelationshipCode());
}

public boolean isResponsibleRelationship() {
	return getNokRelationship().isResponsibleRelationship();
}


public boolean isChildRelationship() {
	return getNokRelationship().isChildRelationship();
}

}
