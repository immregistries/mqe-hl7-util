package org.immregistries.mqe.vxu.parse;

import static org.junit.Assert.assertEquals;

import org.immregistries.mqe.hl7util.parser.HL7MessageMap;
import org.immregistries.mqe.hl7util.parser.MessageParserHL7;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.MqeAddress;
import org.immregistries.mqe.vxu.MqeVaccination;
import org.immregistries.mqe.vxu.VxuField;
import org.junit.Test;

public class HL7MessageParserTester {
  private MessageParserHL7 hl7Parser = new MessageParserHL7();
  private HL7MessageParser messageParser = HL7MessageParser.INSTANCE;

  private static final String IMMUNITY_MSG =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|20120701082240-0500||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^Jr^^^L~S^M^A^^^^A|Lam^Morgan^^^^^M|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|"
          + "32 Prescott Street Ave^Apt 2^Warwick^MA^02452^USA^L^^MA0001~123 E Main Street^^Anytown^AZ^85203^MEX^M~^^^^^^BDL^^TY8888||^PRN^PH^^^657^5558563||||||||"
          + "|2186-5^non Hispanic or Latino^CDCREC|Hospital|Y|2||||20161010|Y\r"
          + "PD1|||Primary Facility^^^^^PF^^^^567||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^MD\r"
          + "RXA|0|1|20120814|20120815|33332-0010-01^Influenza, seasonal, injectable, preservative free^NDC|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1^^^^PRN|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"
          + "ORC|RE||IZ-783275^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^MD\r"
          + "RXA|0|1|20120814||03^MMR^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||01^^NIP001|||||||||||CP|A\r"
          + "ORC|RE||IZ-783276^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^MD\r"
          + "RXA|0|1|20120814||33332-0010-01^Influenza, seasonal, injectable, preservative free^NDC^09^^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1^^^^PRN|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"
          + "ORC|RE||IZ-783277^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^MD\r"
          + "RXA|0|1|20120814||09^^CVX^33332-0010-01^Influenza, seasonal, injectable, preservative free^NDC|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1^^^^PRN|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  private HL7MessageMap map = hl7Parser.getMessagePartMap(IMMUNITY_MSG);

  @Test
  public void testMeta() {
    MqeMessageReceived mr = messageParser.extractFromMessage(map);
    MqePatient patient = mr.getPatient();

    assertEquals("Snow", patient.getMetaFieldInfo(VxuField.PATIENT_NAME_LAST).getValue());
    assertEquals("PID",
        patient.getMetaFieldInfo(VxuField.PATIENT_NAME_LAST).getHl7Location().getSegmentId());
    assertEquals(5,
        patient.getMetaFieldInfo(VxuField.PATIENT_NAME_LAST).getHl7Location().getFieldPosition());
    assertEquals(1, patient.getMetaFieldInfo(VxuField.PATIENT_NAME_LAST).getHl7Location()
        .getComponentNumber());
  }

  @Test
  public void testPatient() {
    MqeMessageReceived mr = messageParser.extractFromMessage(map);
    MqePatient patient = mr.getPatient();
    assertEquals(3, patient.getPatientAddressList().size());

    {
      MqeAddress a1 = patient.getPatientAddress();
      assertEquals("32 Prescott Street Ave", a1.getStreet());
      assertEquals("Apt 2", a1.getStreet2());
      assertEquals("Warwick", a1.getCity());
      assertEquals("MA", a1.getStateCode());
      assertEquals("02452", a1.getZip());
      assertEquals("USA", a1.getCountryCode());
      assertEquals("MA0001", a1.getCountyParishCode());
    }
    {
      MqeAddress a2 = patient.getPatientAddressList().get(1);
      assertEquals("123 E Main Street", a2.getStreet());
      assertEquals("", a2.getStreet2());
      assertEquals("Anytown", a2.getCity());
      assertEquals("AZ", a2.getStateCode());
      assertEquals("85203", a2.getZip());
      assertEquals("MEX", a2.getCountryCode());
      assertEquals("", a2.getCountyParishCode());
    }
    
//    assertEquals("S", patient.getAlias().getLast());
//    assertEquals("M", patient.getAlias().getFirst());
//    assertEquals("A", patient.getAlias().getMiddle());
    assertEquals("20070706", patient.getBirthDateString());
    assertEquals("Y", patient.getBirthMultipleInd());
    assertEquals("2", patient.getBirthOrder());
    assertEquals("Hospital", patient.getBirthPlace());
    assertEquals("TY8888", patient.getBirthCounty());
    assertEquals("20161010", patient.getDeathDateString());
    assertEquals("Y", patient.getDeathIndicator());
    assertEquals("2186-5", patient.getEthnicity());
    // private OrganizationName facility = new OrganizationName();
//    assertEquals("567", patient.getFacility().getId());
//    assertEquals("Primary Facility", patient.getFacility().getName());
    // private String financialEligibility = "";// new String(CodesetType.FINANCIAL_STATUS_CODE);
//    assertEquals("", patient.getFinancialEligibility());
//    assertEquals("", patient.getIdMedicaid().getFormattedNumber());
//    assertEquals("", patient.getIdRegistry().getFormattedNumber());
//    assertEquals("", patient.getIdSsn().getFormattedNumber());
    assertEquals("D26376273", patient.getIdSubmitter().getNumber());
//    assertEquals("", patient.getIdWic().getFormattedNumber());
    assertEquals("Lam", patient.getMotherMaidenName());
    assertEquals("Snow", patient.getNameLast());
    assertEquals("Madelynn", patient.getNameFirst());
    assertEquals("Ainsley", patient.getNameMiddle());
    assertEquals("5558563", patient.getPhone().getLocalNumber());
    assertEquals("657", patient.getPhone().getAreaCode());
    assertEquals("", patient.getPrimaryLanguage());
    assertEquals("", patient.getProtection());
    assertEquals("", patient.getPublicity());
    assertEquals("2076-8", patient.getRace());
    assertEquals("", patient.getRegistryStatus());
    assertEquals("", patient.getPatientClass());
    assertEquals("F", patient.getSex());
    assertEquals("Jr", patient.getNameSuffix());
  }

  @Test
  public void testVaccination() {
    MqeMessageReceived mr = messageParser.extractFromMessage(map);
    assertEquals(4, mr.getVaccinations().size());
    {
      MqeVaccination v1 = mr.getVaccinations().get(0);
      assertEquals("20120814", v1.getAdminDateString());
      assertEquals("20120815", v1.getAdminDateEndString());
      assertEquals("33332-0010-01", v1.getAdminNdc());
      assertEquals("", v1.getAdminCvxCode());
      assertEquals("", v1.getAdminCptCode());
      assertEquals("A", v1.getAction());
      assertEquals("0.5", v1.getAmount());
      assertEquals("mL", v1.getAmountUnit());
      assertEquals("C28161", v1.getBodyRoute());
      assertEquals("LD", v1.getBodySite());
      assertEquals("CP", v1.getCompletion());
      assertEquals("", v1.getConfidentialityCode());
      assertEquals("", v1.getCvxDerived());
      assertEquals("X68", v1.getFacilityIdNumber());
      assertEquals("X68", v1.getFacilityName());
      assertEquals("", v1.getFacilityType());
      assertEquals("", v1.getFundingSourceCode());
      assertEquals("", v1.getGivenByNameFirst());
      assertEquals("", v1.getGivenByNameLast());
      assertEquals("7832-1", v1.getGivenByNumber());
      assertEquals("", v1.getIdPlacer());
      assertEquals("IZ-783274", v1.getIdSubmitter());
      assertEquals("00", v1.getInformationSource());
      assertEquals("Z0860BB", v1.getLotNumber());
      assertEquals("CSL", v1.getManufacturer());
      assertEquals("RE", v1.getOrderControl());
      assertEquals("", v1.getOrderedByNameFirst());
      assertEquals("", v1.getOrderedByNameLast());
      assertEquals("", v1.getOrderedByNumber());
      assertEquals("", v1.getRefusal());
      assertEquals("", v1.getRefusalReason());
      assertEquals("", v1.getVaccineValidity());
    }
    {
      MqeVaccination v2 = mr.getVaccinations().get(1);
      assertEquals("", v2.getAdminNdc());
      assertEquals("03", v2.getAdminCvxCode());
      assertEquals("", v2.getAdminCptCode());
    }
    {
      MqeVaccination v3 = mr.getVaccinations().get(2);
      assertEquals("33332-0010-01", v3.getAdminNdc());
      assertEquals("09", v3.getAdminCvxCode());
      assertEquals("", v3.getAdminCptCode());
    }
    {
      MqeVaccination v4 = mr.getVaccinations().get(3);
      assertEquals("33332-0010-01", v4.getAdminNdc());
      assertEquals("09", v4.getAdminCvxCode());
      assertEquals("", v4.getAdminCptCode());
    }

  }


}
