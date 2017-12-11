package org.immregistries.dqa.vxu;

import org.immregistries.dqa.vxu.hl7.Id;
import org.immregistries.dqa.vxu.hl7.Observation;
import org.immregistries.dqa.vxu.hl7.OrganizationName;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DqaRefusal {
  public static final String ACTION_CODE_ADD = "A";
  public static final String ACTION_CODE_DELETE = "D";
  public static final String ACTION_CODE_UPDATE = "U";
  public static final String INFO_SOURCE_ADMIN = "00";
  public static final String INFO_SOURCE_HIST = "01";
  private String action = "";//new CodedEntity(CodesetType.VACCINATION_ACTION_CODE);
  private DateTime systemEntryDate = null;
  private String confidentiality = "";//new CodedEntity(CodesetType.VACCINATION_CONFIDENTIALITY);
  private Id enteredBy = new Id();
  private Date expirationDate = null;
  private String expirationDateString;
  private OrganizationName facility = new OrganizationName();
  private String facilityType = "";//new CodedEntity(CodesetType.FACILITY_TYPE);
  private String idPlacer = "";
  private String idSubmitter = "";
  private String informationSource = "";//new CodedEntity(CodesetType.VACCINATION_INFORMATION_SOURCE);
  private String manufacturer = "";//new CodedEntity(CodesetType.VACCINATION_MANUFACTURER_CODE);
  private List<Observation> observations = new ArrayList<Observation>();
  private String orderControl = "";//new CodedEntity(CodesetType.VACCINATION_ORDER_CONTROL_CODE);
  private Id orderedBy = new Id();
  private String refusalReason = "";//new CodedEntity(CodesetType.VACCINATION_REFUSAL);
  private String cvxRefused = "";
  private List<String> vaccineGroupsDerived = new ArrayList<String>();


}
