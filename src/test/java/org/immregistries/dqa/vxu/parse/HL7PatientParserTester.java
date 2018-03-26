package org.immregistries.dqa.vxu.parse;

import static org.junit.Assert.assertEquals;

import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.hl7util.parser.MessageParserHL7;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.DqaPatientAddress;
import org.immregistries.dqa.vxu.VxuField;
import org.junit.Test;

import java.util.List;

public class HL7PatientParserTester {
  private MessageParserHL7 rootParser = new MessageParserHL7();
  private HL7PatientParser pParser = HL7PatientParser.INSTANCE;

  private static final String IMMUNITY_MSG =
          /* 0 */ "MSH|^~\\&|||||20160413161526-0400||VXU^V04^VXU_V04|2bK5-B.07.14.1Nx|P|2.5.1|\r"
          /* 1 */ + "PID|||2bK5-B.07.14^^^AIRA-TEST^MR||Powell^Diarmid^T^^^^L||20030415|M||2106-3^White^HL70005|215 Armstrong Cir^^Brethren^MI^49619^USA^P~216 Armstrong Cir^^Brethren^MI^49619^USA^P~^^^^^^BDL^^123||^PRN^PH^^^231^4238012|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
          /* 2 */ + "NK1|0|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          /* 2 */ + "NK1|1|Lam^xxxxx^^^^^L|FTH^Father^HL70063|99 Prescott Street Ave^^Warwicks^MI^48864^USA^L|^PRN^PH^^^657^5558563\r"
          /* 3 */ + "ORC|RE||N54R81.2^AIRA|\r"
          /* 4 */ + "RXA|0|1|20140420||998^No vaccine administered^CVX|999|||||||||||||||A|\r"
          /* 5 */ + "OBX|1|CE|59784-9^Disease with presumed immunity^LN|1|23511006^Meningococcal infectious disease (disorder)^SCT||||||F|\r"
          /* 6 */ + "ORC|RE||N54R81.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          /* 7 */ + "RXA|0|1|20160413||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||L0214MX||SKB^GlaxoSmithKline^MVX||||A|\r"
          /* 8 */ + "RXR|IM^Intramuscular^HL70162|RT^Right Thigh^HL70163\r"
          /* 9 */ + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160413|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          /* 10 */ + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          /* 11 */ + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          /* 12 */ + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160413||||||F|\r";

  private HL7MessageMap map = rootParser.getMessagePartMap(IMMUNITY_MSG);

  @Test
  public void test4() {

    // This just validates that the message is correct for this test.
    int i = map.findFieldRepWithValue("BDL", "PID-11-7", 1);
    assertEquals("should be in second field rep", 3, i);

  }

  @Test
  public void testAddressParse() {
    DqaPatient patient = pParser.getPatient(map);
    List<DqaPatientAddress> pa = patient.getPatientAddressList();
    assertEquals(3,pa.size());
  }

  @Test
  public void testPidParse() {
    DqaPatient patient = pParser.getPatient(map);
    assertEquals("Powell", patient.getNameLast());
    assertEquals("Diarmid", patient.getNameFirst());
  }


  @Test
  public void testPatientGuardian() {
    DqaPatient patient = pParser.getPatient(map);
    assertEquals("MTH", patient.getResponsibleParty().getRelationshipCode());
    assertEquals("Morgan", patient.getResponsibleParty().getNameFirst());
    assertEquals("Lam", patient.getResponsibleParty().getNameLast());
    assertEquals("32 Prescott Street Ave", patient.getResponsibleParty().getAddress().getStreet());
    assertEquals("Warwick", patient.getResponsibleParty().getAddress().getCity());
    assertEquals("MA", patient.getResponsibleParty().getAddress().getStateCode());
    assertEquals("02452", patient.getResponsibleParty().getAddress().getZip());
    assertEquals("USA", patient.getResponsibleParty().getAddress().getCountry());
  }


  @Test
  public void testPatientGuardianMeta() {
    DqaPatient patient = pParser.getPatient(map);
    MetaFieldInfo mfi = patient.getMetaFieldInfo(VxuField.PATIENT_GUARDIAN_NAME_FIRST);
    assertEquals("Morgan", mfi.getValue());
    assertEquals("NK1[2]-2~1-2-1", mfi.getHl7Location().getMessageMapLocator());
    assertEquals(1, mfi.getHl7Location().getSegmentSequence());
    mfi = patient.getMetaFieldInfo(VxuField.PATIENT_GUARDIAN_RELATIONSHIP);
    assertEquals("MTH", mfi.getValue());
    assertEquals("NK1[2]-3~1-1-1", mfi.getHl7Location().getMessageMapLocator());
    assertEquals(1, mfi.getHl7Location().getSegmentSequence());
    mfi = patient.getMetaFieldInfo(VxuField.PATIENT_GUARDIAN_NAME_LAST);
    assertEquals("Lam", mfi.getValue());
    assertEquals("NK1[2]-2~1-1-1", mfi.getHl7Location().getMessageMapLocator());
    assertEquals(1, mfi.getHl7Location().getSegmentSequence());
    mfi = patient.getMetaFieldInfo(VxuField.PATIENT_GUARDIAN_ADDRESS_STREET);
    assertEquals("32 Prescott Street Ave", mfi.getValue());
    assertEquals("NK1[2]-4~1-1-1", mfi.getHl7Location().getMessageMapLocator());
    assertEquals(1, mfi.getHl7Location().getSegmentSequence());
    mfi = patient.getMetaFieldInfo(VxuField.PATIENT_GUARDIAN_ADDRESS_CITY);
    assertEquals("Warwick", mfi.getValue());
    assertEquals("NK1[2]-4~1-3-1", mfi.getHl7Location().getMessageMapLocator());
    assertEquals(1, mfi.getHl7Location().getSegmentSequence());
    mfi = patient.getMetaFieldInfo(VxuField.PATIENT_GUARDIAN_ADDRESS_STATE);
    assertEquals("MA", mfi.getValue());
    assertEquals("NK1[2]-4~1-4-1", mfi.getHl7Location().getMessageMapLocator());
    assertEquals(1, mfi.getHl7Location().getSegmentSequence());
    mfi = patient.getMetaFieldInfo(VxuField.PATIENT_GUARDIAN_ADDRESS_ZIP);
    assertEquals("02452", mfi.getValue());
    assertEquals("NK1[2]-4~1-5-1", mfi.getHl7Location().getMessageMapLocator());
    assertEquals(1, mfi.getHl7Location().getSegmentSequence());
    mfi = patient.getMetaFieldInfo(VxuField.PATIENT_GUARDIAN_ADDRESS_COUNTRY);
    assertEquals("USA", mfi.getValue());
    assertEquals("NK1[2]-4~1-6-1", mfi.getHl7Location().getMessageMapLocator());
    assertEquals(1, mfi.getHl7Location().getSegmentSequence());
  }

}
