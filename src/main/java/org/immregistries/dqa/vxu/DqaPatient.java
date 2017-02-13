package org.immregistries.dqa.vxu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.immregistries.dqa.vxu.hl7.Id;
import org.immregistries.dqa.vxu.hl7.Name;
import org.immregistries.dqa.vxu.hl7.OrganizationName;
import org.immregistries.dqa.vxu.hl7.PatientAddress;
import org.immregistries.dqa.vxu.hl7.PatientIdNumber;
import org.immregistries.dqa.vxu.hl7.PatientImmunity;
import org.immregistries.dqa.vxu.hl7.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DqaPatient {
	
private static final Logger LOGGER = LoggerFactory.getLogger(DqaPatient.class);

  private List<PatientAddress> patientAddressList = new ArrayList<PatientAddress>();
  
  private Name alias = new Name();
  
  private Date birthDate = null;
  private String birthDateString = "";
  
  private String birthMultipleInd = "";
  private String birthOrderNumber = "";//new String(CodesetType.BIRTH_ORDER);
  private String birthPlace = "";
  private String birthCounty = "";
 
  private Date deathDate = null;
  private String deathDateString;
  
  private String deathIndicator = "";
  
  private String ethnicity = "";//new String(CodesetType.PATIENT_ETHNICITY);
  private OrganizationName facility = new OrganizationName();
  
  private String financialEligibility = "";//new String(CodesetType.FINANCIAL_STATUS_CODE);
  private Date financialEligibilityDate = null;
  private String financialEligibilityDateString;
  
  private PatientIdNumber idMedicaid = new PatientIdNumber();
  private PatientIdNumber idRegistry = new PatientIdNumber();
  private PatientIdNumber idSsn = new PatientIdNumber();
  private PatientIdNumber idSubmitter = new PatientIdNumber();
  private PatientIdNumber idWic = new PatientIdNumber();
  
  
  private String motherMaidenName = "";
  private Name name = new Name();
  private long patientId = 0;
  private PhoneNumber phone = new PhoneNumber();
  private Id physician = new Id(CodesetType.PHYSICIAN_NUMBER);
  private String primaryLanguage = "";//new String(CodesetType.PERSON_LANGUAGE);
  private String protection = "";//new String(CodesetType.PATIENT_PROTECTION);
  private String publicity = "";//CodesetType.PATIENT_PUBLICITY);
  private String race = "";//new String(CodesetType.PATIENT_RACE);
  private String registryStatus = "";//new String(CodesetType.REGISTRY_STATUS);
  private String patientClass = "";//new String(CodesetType.PATIENT_CLASS);

  private String sex = "";//new String(CodesetType.PATIENT_SEX);
  private boolean isUnderAged = false;
  private boolean skipped = false;
  private List<PhoneNumber> patientPhoneList = new ArrayList<PhoneNumber>();
  private List<PatientImmunity> patientImmunityList = new ArrayList<PatientImmunity>();
  private Date systemCreationDate = null;

  //This comes out of the transform setp. The kin list will be interpreted, and one will be picked as the responsible party. 
  private DqaNextOfKin responsibleParty = null;
  
  public Date getSystemCreationDate()
  {
    return systemCreationDate;
  }

  public void setSystemCreationDate(Date systemCreationDate)
  {
    this.systemCreationDate = systemCreationDate;
  }

  public List<PatientImmunity> getPatientImmunityList()
  {
    return patientImmunityList;
  }
//
//  public List<PatientIdNumber> getPatientIdNumberList()
//  {
//    return patientIdNumberList;
//  }

  public List<PhoneNumber> getPatientPhoneList()
  {
    return patientPhoneList;
  }

  public List<PatientAddress> getPatientAddressList()
  {
    return patientAddressList;
  }

  public PatientAddress getAddress()
  {
    if (patientAddressList.size() > 0)
    {
      return patientAddressList.get(0);
    }
    return null;
  }

  public String getAddressCity()
  {
    return getAddress().getCity();
  }

  public String getAddressCountryCode()
  {
    return getAddress().getCountry();
  }

  public String getAddressCountyParishCode()
  {
    return getAddress().getCountyParishCode();
  }

  public String getAddressStateCode()
  {
    return getAddress().getStateCode();
  }

  public String getAddressStreet()
  {
    return getAddress().getStreet();
  }

  public String getAddressStreet2()
  {
    return getAddress().getStreet2();
  }

  public String getAddressTypeCode()
  {
    return getAddress().getTypeCode();
  }

  public String getAddressZip()
  {
    return getAddress().getZip();
  }

  public Name getAlias()
  {
    return alias;
  }

  public String getAliasFirst()
  {
    return alias.getFirst();
  }

  public String getAliasLast()
  {
    return alias.getLast();
  }

  public String getAliasMiddle()
  {
    return alias.getMiddle();
  }

  public String getAliasPrefix()
  {
    return alias.getPrefix();
  }

  public String getAliasSuffix()
  {
    return alias.getSuffix();
  }

  public String getAliasTypeCode()
  {
    return alias.getTypeCode();
  }

  public Date getBirthDate()
  {
    return birthDate;
  }

  public String getBirthMultipleInd()
  {
    return birthMultipleInd;
  }

  public String getBirthOrder()
  {
    return birthOrderNumber;
  }

  public String getBirthOrderNumber()
  {
    return birthOrderNumber;
  }

  public String getBirthPlace()
  {
    return birthPlace;
  }

  public Date getDeathDate()
  {
    return deathDate;
  }

  public String getDeathIndicator()
  {
    return deathIndicator;
  }

  public String getEthnicity()
  {
    return ethnicity;
  }
  
  public void setEthnicity(String eth) {
    this.ethnicity = eth;
  }


  public String getEthnicityCode()
  {
    return ethnicity;
  }

  public OrganizationName getFacility()
  {
    return facility;
  }

  public String getFacilityIdNumber()
  {
    return facility.getId();
  }

  public String getFacilityName()
  {
    return facility.getName();
  }

  public String getFinancialEligibility()
  {
    return financialEligibility;
  }

  public String getFinancialEligibilityCode()
  {
    return financialEligibility;
  }

  public PatientIdNumber getIdMedicaid()
  {
    return idMedicaid;
  }

  public String getIdMedicaidNumber()
  {
    return getIdMedicaid().getNumber();
  }

  public PatientIdNumber getIdRegistry()
  {
    return idRegistry;
  }

  public String getIdRegistryNumber()
  {
    return getIdRegistry().getNumber();
  }

  public PatientIdNumber getIdSsn()
  {
    return idSsn;
  }

  public String getIdSsnNumber()
  {
    return getIdSsn().getNumber();
  }

  public PatientIdNumber getIdSubmitter()
  {
    return idSubmitter;
  }

  public String getIdSubmitterAssigningAuthorityCode()
  {
    return getIdSubmitter().getAssigningAuthority();
  }

  public String getIdSubmitterNumber()
  {
    return getIdSubmitter().getNumber();
  }

  public String getIdSubmitterTypeCode()
  {
    return getIdSubmitter().getTypeCode();
  }

  public String getMotherMaidenName()
  {
    return motherMaidenName;
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

  public long getPatientId()
  {
    return patientId;
  }

  public PhoneNumber getPhone()
  {
    return phone;
  }

  public String getPhoneNumber()
  {
    return phone.getNumber();
  }

  public Id getPhysician()
  {
    return physician;
  }

  public String getPhysicianNameFirst()
  {
    return physician.getName().getFirst();
  }

  public String getPhysicianNameLast()
  {
    return physician.getName().getLast();
  }

  public String getPhysicianNumber()
  {
    return physician.getNumber();
  }

  public String getPrimaryLanguage()
  {
    return primaryLanguage;
  }

  public String getPrimaryLanguageCode()
  {
    return primaryLanguage;
  }

  public String getProtection()
  {
    return protection;
  }

  public String getProtectionCode()
  {
    return protection;
  }

  public String getPublicity()
  {
    return publicity;
  }

  public String getPublicityCode()
  {
    return publicity;
  }

  public String getRace()
  {
    return race;
  }
  
  public void setRace(String raceCe)
  {
    this.race = raceCe;
  }

  public String getRaceCode()
  {
    return race;
  }

  public String getRegistryStatus()
  {
    return registryStatus;
  }

  public String getRegistryStatusCode()
  {
    return registryStatus;
  }

  public String getSex()
  {
    return sex;
  }

  public String getSexCode()
  {
    return sex;
  }

  public boolean isSkipped()
  {
    return skipped;
  }

  public void setAliasFirst(String nameFirst)
  {
    alias.setFirst(nameFirst);
  }

  public void setAliasLast(String nameLast)
  {
    alias.setLast(nameLast);
  }

  public void setAliasMiddle(String nameMiddle)
  {
    alias.setMiddle(nameMiddle);
  }

  public void setAliasPrefix(String namePrefix)
  {
    alias.setPrefix(namePrefix);
  }

  public void setAliasSuffix(String nameSuffix)
  {
    alias.setSuffix(nameSuffix);
  }

  public void setAliasTypeCode(String nameTypeCode)
  {
    alias.setTypeCode(nameTypeCode);
  }

  public void setBirthDate(Date birthDate)
  {
    this.birthDate = birthDate;
  }

  public void setBirthMultipleIndicator(String birthMultiple)
  {
    this.birthMultipleInd = birthMultiple;
  }

  public void setBirthOrderNumber(String birthOrderCode)
  {
    this.birthOrderNumber = birthOrderCode;
  }

  public void setBirthPlace(String birthPlace)
  {
    this.birthPlace = birthPlace;
  }

  public void setDeathDate(Date deathDate)
  {
    this.deathDate = deathDate;
  }

  public void setDeathIndicator(String deathIndicator)
  {
    this.deathIndicator = deathIndicator;
  }

  public void setEthnicityCode(String ethnicityCode)
  {
    ethnicity = ethnicityCode;
  }

  public void setFacilityIdNumber(String facilityIdNumber)
  {
    facility.setId(facilityIdNumber);
  }

  public void setFacilityName(String facilityName)
  {
    facility.setName(facilityName);
  }

  public void setFinancialEligibilityCode(String financialEligibilityCode)
  {
    this.financialEligibility = financialEligibilityCode;
  }

  public void setIdMedicaidNumber(String idMedicaidNumber)
  {
    this.getIdMedicaid().setNumber(idMedicaidNumber);
  }

  public void setIdRegistryNumber(String idRegistryNumber)
  {
    this.getIdRegistry().setNumber(idRegistryNumber);
  }

  public void setIdSsnNumber(String idSsnNumber)
  {
    this.getIdSsn().setNumber(idSsnNumber);
  }

  public void setIdSubmitterAssigningAuthorityCode(String assigningAuthorityCode)
  {
    getIdSubmitter().setAssigningAuthorityCode(assigningAuthorityCode);
  }

  public void setIdSubmitterNumber(String number)
  {
    getIdSubmitter().setNumber(number);
  }

  public void setIdSubmitterTypeCode(String typeCode)
  {
    getIdSubmitter().setTypeCode(typeCode);
  }

  public void setMotherMaidenName(String motherMaidenName)
  {
    this.motherMaidenName = motherMaidenName;
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

  public void setPatientId(long patientId)
  {
    this.patientId = patientId;
  }

  public void setPhoneNumber(String phoneNumber)
  {
//	  LOGGER.info("setting phone number!");
	  if (phoneNumber.length() > 250) {
		  LOGGER.info("Trimming phone number!");
		  phone.setNumber(phoneNumber.substring(0,250));
	  } else {
		  phone.setNumber(phoneNumber);
	  }
    
  }
  
  public void setPhone(PhoneNumber phoneIn) {
	  this.phone = phoneIn;
  }

  public void setPhysicianNameFirst(String physicianNameFirst)
  {
    physician.getName().setFirst(physicianNameFirst);
  }

  public void setPhysicianNameLast(String physicianNameLast)
  {
    physician.getName().setLast(physicianNameLast);
  }

  public void setPhysicianNumber(String physicianNumber)
  {
    physician.setNumber(physicianNumber);
  }

  public void setPrimaryLanguageCode(String primaryLanguageCode)
  {
    primaryLanguage = primaryLanguageCode;
  }

  public void setProtectionCode(String protectionCode)
  {
    protection = protectionCode;
  }

  public void setPublicityCode(String publicityCode)
  {
    publicity = publicityCode;
  }

  public void setRaceCode(String raceCode)
  {
    race = raceCode;
  }

  public void setRegistryStatusCode(String registryStatusCode)
  {
    this.registryStatus = registryStatusCode;
  }

  public void setSexCode(String sexCode)
  {
    this.sex = sexCode;
  }

  public void setSkipped(boolean skipped)
  {
    this.skipped = skipped;
  }

  public String getPatientClassCode()
  {
    return patientClass;
  }

  public void setPatientClassCode(String code)
  {
    patientClass = code;
  }

  public String getPatientClass()
  {
    return patientClass;
  }

  public Date getFinancialEligibilityDate()
  {
    return financialEligibilityDate;
  }

  public void setFinancialEligibilityDate(Date financialEligibilityDate)
  {
    this.financialEligibilityDate = financialEligibilityDate;
  }

  public boolean isUnderAged()
  {
    return isUnderAged;
  }

  public void setUnderAged(boolean isUnderAged)
  {
    this.isUnderAged = isUnderAged;
  }

public String getBirthDateString() {
	return birthDateString;
}

public void setBirthDateString(String birthDateString) {
	this.birthDateString = birthDateString;
}

public void setPrimaryLanguage(String language) {
	this.primaryLanguage = language;
}

/**
 * @param idMedicaid the idMedicaid to set
 */
public void setIdMedicaid(PatientIdNumber idMedicaid) {
	this.idMedicaid = idMedicaid;
}

/**
 * @param idRegistry the idRegistry to set
 */
public void setIdRegistry(PatientIdNumber idRegistry) {
	this.idRegistry = idRegistry;
}

/**
 * @param idSsn the idSsn to set
 */
public void setIdSsn(PatientIdNumber idSsn) {
	this.idSsn = idSsn;
}

/**
 * @param idSubmitter the idSubmitter to set
 */
public void setIdSubmitter(PatientIdNumber idSubmitter) {
	this.idSubmitter = idSubmitter;
}

/**
 * @return the idWic
 */
public PatientIdNumber getIdWic() {
	return idWic;
}

/**
 * @param idWic the idWic to set
 */
public void setIdWic(PatientIdNumber idWic) {
	this.idWic = idWic;
}

public void setDeathDateString(String deathDt) {
	this.deathDateString = deathDt;
}


public String getDeathDateString() {
	return this.deathDateString;
	
}

/**
 * @return the financialEligibilityDateString
 */
public String getFinancialEligibilityDateString() {
	return financialEligibilityDateString;
}

/**
 * @param financialEligibilityDateString the financialEligibilityDateString to set
 */
public void setFinancialEligibilityDateString(
		String financialEligibilityDateString) {
	this.financialEligibilityDateString = financialEligibilityDateString;
}

public DqaNextOfKin getResponsibleParty() {
	return responsibleParty;
}

public String getBirthCounty() {
	return birthCounty;
}

public void setBirthCounty(String birthCounty) {
	this.birthCounty = birthCounty;
}

//public void setResponsibleParty(NextOfKin responsibleParty) {
//	this.responsibleParty = responsibleParty;
//}

}
