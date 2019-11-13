package org.immregistries.mqe.vxu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.mqe.hl7util.model.MetaFieldInfo;
import org.immregistries.mqe.util.validation.MqeValidatedObject;
import org.immregistries.mqe.vxu.hl7.Id;
import org.immregistries.mqe.vxu.hl7.Observation;
import org.immregistries.mqe.vxu.hl7.OrganizationName;

public class MqeVaccination extends MqeValidatedObject {
  @Override
  public TargetType getTargetType() { return TargetType.Vaccination;}


  public static final String ACTION_CODE_ADD = "A";
  public static final String ACTION_CODE_DELETE = "D";
  public static final String ACTION_CODE_UPDATE = "U";

  public static final String COMPLETION_COMPLETED = "CP";
  public static final String COMPLETION_NOT_ADMINISTERED = "NA";

  public static final String COMPLETION_PARTIALLY_ADMINISTERED = "PA";
  public static final String COMPLETION_REFUSED = "RE";
  public static final String INFO_SOURCE_ADMIN = "00";
  public static final String INFO_SOURCE_HIST = "01";

  private String action = "";// new CodedEntity(CodesetType.VACCINATION_ACTION_CODE);

  private List<String> adminCodeList = new ArrayList<String>();
  private String adminNdc = "";// new CodedEntity(CodesetType.VACCINATION_NDC_CODE);
  private String adminCpt = "";// new CodedEntity(CodesetType.VACCINATION_CPT_CODE);
  private String adminCvx = "";// new CodedEntity(CodesetType.VACCINATION_CVX_CODE);
  private String cvxDerived = "";// Derived in MessageTransformer.
  // private VaccineCvx vaccineCvx = null;
  // private VaccineCpt vaccineCpt = null;
  // private VaccineNdc vaccineNdc = null;

  private Date adminDate = null;
  private String adminDateString = "";
  private Date adminDateEnd = null;
  private String adminDateEndString;
  private String systemEntryDateString;
  private Date systemEntryDate = null;
  private boolean administered = false;
  private String amount = "";
  private String amountUnit = "";// new CodedEntity(CodesetType.ADMINISTRATION_UNIT);
  private String bodyRoute = "";// new CodedEntity(CodesetType.BODY_ROUTE);
  private String bodySite = "";// new CodedEntity(CodesetType.BODY_SITE);
  private String completion = "";// new CodedEntity(CodesetType.VACCINATION_COMPLETION);
  private String confidentiality = "";// new CodedEntity(CodesetType.VACCINATION_CONFIDENTIALITY);
  private Id enteredBy = new Id();
  private Date expirationDate = null;
  private String expirationDateString;
  private OrganizationName facility = new OrganizationName();
  private String facilityType = "";// new CodedEntity(CodesetType.FACILITY_TYPE);
  private String financialEligibility = "";// new CodedEntity(CodesetType.FINANCIAL_STATUS_CODE);
  private Id givenBy = new Id();
  private String idPlacer = "";
  private String idSubmitter = "";
  private String informationSource = "";// new
  // CodedEntity(CodesetType.VACCINATION_INFORMATION_SOURCE);
  private String lotNumber = "";
  private String manufacturer = "";// new CodedEntity(CodesetType.VACCINATION_MANUFACTURER_CODE);
  private List<Observation> observations = new ArrayList<Observation>();
  private String orderControl = "";// new CodedEntity(CodesetType.VACCINATION_ORDER_CONTROL_CODE);
  private Id orderedBy = new Id();
  private int positionSubId;
  private String product = "";// new CodedEntity(CodesetType.VACCINE_PRODUCT);
  private String refusal = "";// new CodedEntity(CodesetType.VACCINATION_REFUSAL);
  private long vaccinationId = 0l;
  private String fundingSource = "";// new CodedEntity(CodesetType.VACCINATION_FUNDING_SOURCE);
  private String refusalReason = "";
  private VaccinationVIS vaccinationVis = null;
  private String tradeName = "";// new CodedEntity(CodesetType.VACCINATION_TRADE_NAME);
  private String vaccineValidity = "";// new CodedEntity(CodesetType.VACCINATION_VALIDITY);
  private String ID;
  
  
  
  public MqeVaccination(String iD) {
	super();
	ID = iD;
  }
  
  public MqeVaccination() {
	super();
		
  }

private List<String> vaccineGroupsDerived = new ArrayList<String>();

  public String getCvxDerived() {
    return cvxDerived;
  }

  public void setCvxDerived(String cvxDerived) {
    this.cvxDerived = cvxDerived;
  }

  public List<String> getVaccineGroupsDerived() {
    return vaccineGroupsDerived;
  }

public void setVaccineGroupsDerived(List<String> vaccineGroupsDerived) {
    this.vaccineGroupsDerived = vaccineGroupsDerived;
  }

  public String getFacilityType() {
    return facilityType;
  }

  public String getFacilityTypeCode() {
    return facilityType;
  }

  public void setFacilityTypeCode(String facilityTypeCode) {
    this.facilityType = facilityTypeCode;
  }

  public String getTradeName() {
    return tradeName;
  }

  public String getTradeNameCode() {
    return tradeName;
  }

  public void setTradeNameCode(String tradeNameCode) {
    this.tradeName = tradeNameCode;
  }

  public String getVaccineValidity() {
    return vaccineValidity;
  }

  public String getVaccineValidityCode() {
    return vaccineValidity;
  }

  public void setVaccineValidityCode(String vaccineValidityCode) {
    this.vaccineValidity = vaccineValidityCode;
  }

  public VaccinationVIS getVaccinationVis() {
    return vaccinationVis;
  }

  public void setVaccinationVis(VaccinationVIS vis) {
    this.vaccinationVis = vis;
  }

  public String getRefusalReason() {
    return refusalReason;
  }

  public void setRefusalReason(String refusalReason) {
    this.refusalReason = refusalReason;
  }

  public String getFundingSourceCode() {
    return fundingSource;
  }

  public void setFundingSourceCode(String fundingSourceCode) {
    this.fundingSource = fundingSourceCode;
  }

  public String getAction() {
    return action;
  }

  public String getActionCode() {
    return action;
  }

  public String getAdminCptCode() {
    return adminCpt;
  }

  public String getAdminCvxCode() {
    return adminCvx;
  }

  // public static final String ACTION_CODE_ADD = "A";
  // public static final String ACTION_CODE_DELETE = "D";
  // public static final String ACTION_CODE_UPDATE = "U";
  //
  // public static final String INFO_SOURCE_ADMIN = "00";
  // public static final String INFO_SOURCE_HIST = "01";

  public Date getAdminDate() {
    return adminDate;
  }

  public Date getAdminDateEnd() {
    return adminDateEnd;
  }

  public String getAmount() {
    return amount;
  }

  public String getAmountUnit() {
    return amountUnit;
  }

  public String getAmountUnitCode() {
    return amountUnit;
  }

  public String getBodyRoute() {
    return this.bodyRoute;
  }

  public String getBodyRouteCode() {
    return this.bodyRoute;
  }

  public String getBodySite() {
    return this.bodySite;
  }

  public String getBodySiteCode() {
    return this.bodySite;
  }

  public String getCompletion() {
    return completion;
  }

  public String getCompletionCode() {
    return completion;
  }

  public String getConfidentialityCode() {
    return confidentiality;
  }

  public Id getEnteredBy() {
    return enteredBy;
  }

  public String getEnteredByNameFirst() {
    return enteredBy.getName().getFirst();
  }

  public String getEnteredByNameLast() {
    return enteredBy.getName().getLast();
  }

  public String getEnteredByNumber() {
    return enteredBy.getNumber();
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public OrganizationName getFacility() {
    return facility;
  }

  public void setFacility(OrganizationName on) {
    this.facility = on;
  }


  public String getFacilityIdNumber() {
    return facility.getIdNumber();
  }

  public String getFacilityName() {
    return facility.getName();
  }

  public String getFinancialEligibilityCode() {
    return financialEligibility;
  }

  public Id getGivenBy() {
    return givenBy;
  }

  public String getGivenByNameFirst() {
    return givenBy.getName().getFirst();
  }

  public String getGivenByNameLast() {
    return givenBy.getName().getLast();
  }

  public String getGivenByNumber() {
    return givenBy.getNumber();
  }

  public String getIdPlacer() {
    return idPlacer;
  }

  public String getIdSubmitter() {
    return idSubmitter;
  }

  public String getInformationSource() {
    return informationSource;
  }

  public void setInformationSource(String ce) {
    this.informationSource = ce;
  }

  public String getInformationSourceCode() {
    return informationSource;
  }

  public String getLotNumber() {
    return lotNumber;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public String getManufacturerCode() {
    return manufacturer;
  }

  public List<Observation> getObservations() {
    return observations;
  }

  public String getOrderControl() {
    return orderControl;
  }

  public void setOrderControl(String ce) {
    this.orderControl = ce;
  }

  public String getOrderControlCode() {
    return orderControl;
  }

  public Id getOrderedBy() {
    return orderedBy;
  }

  public String getOrderedByNameFirst() {
    return orderedBy.getName().getFirst();
  }

  public String getOrderedByNameLast() {
    return orderedBy.getName().getLast();
  }

  public String getOrderedByNumber() {
    return orderedBy.getNumber();
  }

  public String getProduct() {
    return this.product;
  }

  public String getRefusal() {
    return refusal;
  }

  public String getRefusalCode() {
    return refusal;
  }

  public Date getSystemEntryDate() {
    return systemEntryDate;
  }

  public long getVaccinationId() {
    return vaccinationId;
  }

  public boolean isActionAdd() {
    return ACTION_CODE_ADD.equals(action);
  }

  public boolean isActionDelete() {
    return ACTION_CODE_DELETE.equals(action);
  }

  public boolean isActionUpdate() {
    return ACTION_CODE_UPDATE.equals(action);
  }

  public boolean isAdministered() {
    return administered;
  }

  public boolean isCompletionCompleted() {
    return COMPLETION_COMPLETED.equals(completion);
  }

  public boolean isCompletionNotAdministered() {
    return COMPLETION_NOT_ADMINISTERED.equals(completion);
  }

  public boolean isCompletionPartiallyAdministered() {
    return COMPLETION_PARTIALLY_ADMINISTERED.equals(completion);
  }

  public boolean isCompletionCompletedOrPartiallyAdministered() {
    return COMPLETION_COMPLETED.equals(completion)
        || COMPLETION_PARTIALLY_ADMINISTERED.equals(completion);
  }

  public boolean isCompletionRefused() {
    return COMPLETION_REFUSED.equals(completion);
  }

  public void setActionCode(String actionCode) {
    this.action = actionCode;
  }

  public void setAdminCptCode(String adminCptCode) {
    this.adminCpt = adminCptCode;
  }

  public void setAdminNdcCode(String code) {
    this.adminNdc = code;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public void setAdminCvxCode(String adminCvxCode) {
    this.adminCvx = adminCvxCode;
  }

  public void setAdminDate(Date adminDate) {
    this.adminDate = adminDate;
  }

  public void setAdminDateEnd(Date adminDateEnd) {
    this.adminDateEnd = adminDateEnd;
  }

  public void setAdministered(boolean administered) {
    this.administered = administered;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public void setAmountUnitCode(String amountUnitCode) {
    amountUnit = amountUnitCode;
  }

  public void setBodyRouteCode(String bodyRouteCode) {
    this.bodyRoute = bodyRouteCode;
  }

  public void setBodySiteCode(String bodySiteCode) {
    this.bodySite = bodySiteCode;
  }

  public void setCompletionCode(String completionCode) {
    this.completion = completionCode;
  }

  public void setConfidentialityCode(String confidentialityCode) {
    confidentiality = confidentialityCode;
  }

  public void setEnteredByNameFirst(String enteredByNameFirst) {
    enteredBy.getName().setFirst(enteredByNameFirst);
  }

  public void setEnteredByNameLast(String enteredByNameLast) {
    enteredBy.getName().setLast(enteredByNameLast);
  }

  public void setEnteredByNumber(String enteredByNumber) {
    enteredBy.setNumber(enteredByNumber);
  }

  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }

  public void setFacilityIdNumber(String facilityIdNumber) {
    facility.setId(facilityIdNumber);
  }

  public void setFacilityName(String facilityName) {
    facility.setName(facilityName);
  }

  public void setFinancialEligibilityCode(String financialEligibilityCode) {
    this.financialEligibility = financialEligibilityCode;
  }

  public void setGivenByNameFirst(String givenByNameFirst) {
    givenBy.getName().setFirst(givenByNameFirst);
  }

  public void setGivenByNameLast(String givenByNameLast) {
    givenBy.getName().setLast(givenByNameLast);
  }

  public void setGivenByNumber(String givenByNumber) {
    givenBy.setNumber(givenByNumber);
  }

  public void setIdPlacer(String idPlacer) {
    this.idPlacer = idPlacer;
  }

  public void setIdSubmitter(String idSubmitter) {
    this.idSubmitter = idSubmitter;
  }

  public void setInformationSourceCode(String informationSourceCode) {
    informationSource = informationSourceCode;
  }

  public void setLotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
  }

  public void setManufacturerCode(String manufacturerCode) {
    manufacturer = manufacturerCode;
  }

  public void setObservations(List<Observation> observations) {
    this.observations = observations;
  }

  public void setOrderControlCode(String code) {
    orderControl = code;
  }

  public void setOrderedByNameFirst(String orderedByNameFirst) {
    orderedBy.getName().setFirst(orderedByNameFirst);
  }

  public void setOrderedByNameLast(String orderedByNameLast) {
    orderedBy.getName().setLast(orderedByNameLast);
  }

  public void setOrderedByNumber(String orderedByNumber) {
    orderedBy.setNumber(orderedByNumber);
  }

  public void setRefusalCode(String refusalCode) {
    refusal = refusalCode;
  }

  public void setSystemEntryDate(Date systemEntryDate) {
    this.systemEntryDate = systemEntryDate;
  }

  public void setVaccinationId(long vaccinationId) {
    this.vaccinationId = vaccinationId;
  }

  // public void setVaccineCvx(VaccineCvx vaccineCvx)
  // {
  // this.vaccineCvx = vaccineCvx;
  // }

  public String getAdminDateString() {
    return adminDateString;
  }

  public void setAdminDateString(String adminDateString) {
    this.adminDateString = adminDateString;
  }

  /**
   * @return the adminNdc
   */
  public String getAdminNdc() {
    return adminNdc;
  }

  /// **
  // * @return the vaccineCpt
  // */
  // public VaccineCpt getVaccineCpt() {
  // return vaccineCpt;
  // }
  //
  /// **
  // * @param vaccineCpt the vaccineCpt to set
  // */
  // public void setVaccineCpt(VaccineCpt vaccineCpt) {
  // this.vaccineCpt = vaccineCpt;
  // }

  /**
   * @return the adminCodeList
   */
  public List<String> getAdminCodeList() {
    return adminCodeList;
  }

  /**
   * @param adminCodeList the adminCodeList to set
   */
  public void setAdminCodeList(List<String> adminCodeList) {
    this.adminCodeList = adminCodeList;
  }

  /**
   * @return the expirationDateString
   */
  public String getExpirationDateString() {
    return expirationDateString;
  }

  /**
   * @param expirationDateString the expirationDateString to set
   */
  public void setExpirationDateString(String expirationDateString) {
    this.expirationDateString = expirationDateString;
  }

  /**
   * @return the systemEntryDateString
   */
  public String getSystemEntryDateString() {
    return systemEntryDateString;
  }

  /**
   * @param systemEntryDateString the systemEntryDateString to set
   */
  public void setSystemEntryDateString(String systemEntryDateString) {
    this.systemEntryDateString = systemEntryDateString;
  }

  /**
   * @return the adminDateEndString
   */
  public String getAdminDateEndString() {
    return adminDateEndString;
  }

  /**
   * @param adminDateEndString the adminDateEndString to set
   */
  public void setAdminDateEndString(String adminDateEndString) {
    this.adminDateEndString = adminDateEndString;
  }

  /**
   * @return the positionSubId
   */
  public int getPositionSubId() {
    return positionSubId;
  }

  /**
   * @param positionSubId the positionSubId to set
   */
  public void setPositionSubId(int positionSubId) {
    this.positionSubId = positionSubId;
  }

  @Override
  public String toString() {
    return "\nMqeVaccination----------------------------------------------{" +
        "\n  action='" + action + '\'' +
        "\n, adminCodeList=" + adminCodeList +
        "\n, adminNdc='" + adminNdc + '\'' +
        "\n, adminCpt='" + adminCpt + '\'' +
        "\n, adminCvx='" + adminCvx + '\'' +
        "\n, cvxDerived='" + cvxDerived + '\'' +
        "\n, adminDate=" + adminDate +
        "\n, adminDateString='" + adminDateString + '\'' +
        "\n, adminDateEnd=" + adminDateEnd +
        "\n, adminDateEndString='" + adminDateEndString + '\'' +
        "\n, systemEntryDateString='" + systemEntryDateString + '\'' +
        "\n, systemEntryDate=" + systemEntryDate +
        "\n, administered=" + administered +
        "\n, amount='" + amount + '\'' +
        "\n, amountUnit='" + amountUnit + '\'' +
        "\n, bodyRoute='" + bodyRoute + '\'' +
        "\n, bodySite='" + bodySite + '\'' +
        "\n, completion='" + completion + '\'' +
        "\n, confidentiality='" + confidentiality + '\'' +
        "\n, enteredBy=" + enteredBy +
        "\n, expirationDate=" + expirationDate +
        "\n, expirationDateString='" + expirationDateString + '\'' +
        "\n, facility=" + facility +
        "\n, facilityType='" + facilityType + '\'' +
        "\n, financialEligibility='" + financialEligibility + '\'' +
        "\n, givenBy=" + givenBy +
        "\n, idPlacer='" + idPlacer + '\'' +
        "\n, idSubmitter='" + idSubmitter + '\'' +
        "\n, informationSource='" + informationSource + '\'' +
        "\n, lotNumber='" + lotNumber + '\'' +
        "\n, manufacturer='" + manufacturer + '\'' +
        "\n, observations=" + observations +
        "\n, orderControl='" + orderControl + '\'' +
        "\n, orderedBy=" + orderedBy +
        "\n, positionSubId=" + positionSubId +
        "\n, product='" + product + '\'' +
        "\n, refusal='" + refusal + '\'' +
        "\n, vaccinationId=" + vaccinationId +
        "\n, fundingSource='" + fundingSource + '\'' +
        "\n, refusalReason='" + refusalReason + '\'' +
        "\n, vaccinationVis=" + vaccinationVis +
        "\n, tradeName='" + tradeName + '\'' +
        "\n, vaccineValidity='" + vaccineValidity + '\'' +
        "\n, vaccineGroupsDerived=" + vaccineGroupsDerived +
        '}';
  }

  @Override
  protected void setFieldFromMetaFieldInfo(MetaFieldInfo metaFieldInfo) {
    String value = metaFieldInfo.getValue();
    if (StringUtils.isNotBlank(value)) {
      switch (metaFieldInfo.getVxuField()) {
        case VACCINATION_ACTION_CODE:
          action = value;
          break;
        case VACCINATION_ADMINISTERED_AMOUNT:
          amount = value;
          break;
        case VACCINATION_ADMINISTERED_UNIT:
          amountUnit = value;
          break;
        case VACCINATION_ADMIN_CODE:
          break;
        case VACCINATION_ADMIN_CODE_TABLE:
          break;
        case VACCINATION_ADMIN_DATE:
          adminDateString = value;
          break;
        case VACCINATION_ADMIN_DATE_END:
          adminDateEndString = value;
          break;
        case VACCINATION_BODY_ROUTE:
          bodyRoute = value;
          break;
        case VACCINATION_BODY_SITE:
          bodySite = value;
          break;
        case VACCINATION_COMPLETION_STATUS:
          completion = value;
          break;
        case VACCINATION_CONFIDENTIALITY_CODE:
          confidentiality = value;
          break;
        case VACCINATION_CPT_CODE:
          adminCpt = value;
          break;
        case VACCINATION_CVX_CODE:
          adminCvx = value;
          break;
        case VACCINATION_CVX_CODE_AND_CPT_CODE:
          break;
        case VACCINATION_FACILITY_ID:
          facility.setId(value);
          break;
        case VACCINATION_FACILITY_NAME:
          facility.setName(value);
          break;
        case VACCINATION_FACILITY_TYPE:
          break;
        case VACCINATION_FILLER_ORDER_NUMBER:
          idSubmitter = value;
          break;
        case VACCINATION_FINANCIAL_ELIGIBILITY_CODE:
          financialEligibility = value;
          break;
        case VACCINATION_GIVEN_BY:
          givenBy.setNumber(value);
          break;
        case VACCINATION_ID:
          break;
        case VACCINATION_ID_OF_RECEIVER:
          idPlacer = value;
          break;
        case VACCINATION_ID_OF_SENDER:
          idSubmitter = value;
          break;
        case VACCINATION_INFORMATION_SOURCE:
          informationSource = value;
          break;
        case VACCINATION_LOT_EXPIRATION_DATE:
          expirationDateString = value;
          break;
        case VACCINATION_LOT_NUMBER:
          lotNumber = value;
          break;
        case VACCINATION_MANUFACTURER_CODE:
          manufacturer = value;
          break;
        case VACCINATION_ORDERED_BY:
          orderedBy.setNumber(value);
          break;
        case VACCINATION_ORDER_CONTROL_CODE:
          orderControl = value;
          break;
        case VACCINATION_ORDER_FACILITY_ID:
          facility.setId(value);
          break;
        case VACCINATION_ORDER_FACILITY_NAME:
          facility.setName(value);
          break;
        case VACCINATION_PLACER_ORDER_NUMBER:
          idPlacer = value;
          break;
        case VACCINATION_PRODUCT:
          product = value;
          break;
        case VACCINATION_RECORDED_BY:
          break;
        case VACCINATION_REFUSAL_REASON:
          refusalReason = value;
          break;
        case VACCINATION_SYSTEM_ENTRY_TIME:
          systemEntryDateString = value;
          break;
        case VACCINATION_TRADE_NAME:
          tradeName = value;
          break;
        case VACCINATION_TRADE_NAME_AND_MANUFACTURER:
          break;
        case VACCINATION_TRADE_NAME_AND_VACCINE:
          break;
        case VACCINATION_VALIDITY_CODE:
          vaccineValidity = value;
          break;
        case VACCINATION_VIS:
          break;
        case VACCINATION_VIS_CVX_CODE:
          vaccinationVis.setCvxCode(value);
          break;
        case VACCINATION_VIS_DELIVERY_DATE:
          break;
        case VACCINATION_VIS_DOCUMENT_TYPE:
          vaccinationVis.setDocumentCode(value);
          break;
        case VACCINATION_VIS_PRESENTED_DATE:
          vaccinationVis.setPresentedDateString(value);
          break;
        case VACCINATION_VIS_PUBLISHED_DATE:
          vaccinationVis.setPublishedDateString(value);
          break;
        case VACCINATION_VIS_VERSION_DATE:
          break;
        case VACCINATION_NDC_CODE:
          adminNdc = value;
          break;
        case VACCINATION_FUNDING_SOURCE_CODE:
          fundingSource = value;
          break;
        default:
          break;

      }
    }



  }

  // public VaccineNdc getVaccineNdc() {
  // return vaccineNdc;
  // }
  //
  // public void setVaccineNdc(VaccineNdc vaccineNdc) {
  // this.vaccineNdc = vaccineNdc;
  // }


}
