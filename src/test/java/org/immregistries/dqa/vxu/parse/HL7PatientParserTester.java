package org.immregistries.dqa.vxu.parse;

import static org.junit.Assert.assertEquals;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.hl7util.parser.MessageParserHL7;
import org.immregistries.dqa.vxu.DqaPatient;
import org.junit.Test;

public class HL7PatientParserTester {
  private MessageParserHL7 rootParser = new MessageParserHL7();
  private HL7PatientParser pParser = HL7PatientParser.INSTANCE;

  private static final String IMMUNITY_MSG =
      /* 0 */ "MSH|^~\\&|||||20160413161526-0400||VXU^V04^VXU_V04|2bK5-B.07.14.1Nx|P|2.5.1|\r"
          /* 1 */ + "PID|||2bK5-B.07.14^^^AIRA-TEST^MR||Powell^Diarmid^T^^^^L||20030415|M||2106-3^White^HL70005|215 Armstrong Cir^^Brethren^MI^49619^USA^P~^^^^^^BDL^^123||^PRN^PH^^^231^4238012|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
          /* 2 */ + "ORC|RE||N54R81.2^AIRA|\r"
          /* 3 */ + "RXA|0|1|20140420||998^No vaccine administered^CVX|999|||||||||||||||A|\r"
          /* 4 */ + "OBX|1|CE|59784-9^Disease with presumed immunity^LN|1|23511006^Meningococcal infectious disease (disorder)^SCT||||||F|\r"
          /* 5 */ + "ORC|RE||N54R81.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          /* 6 */ + "RXA|0|1|20160413||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||L0214MX||SKB^GlaxoSmithKline^MVX||||A|\r"
          /* 7 */ + "RXR|IM^Intramuscular^HL70162|RT^Right Thigh^HL70163\r"
          /* 8 */ + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160413|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          /* 9 */ + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          /* 10 */ + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          /* 11 */ + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160413||||||F|\r";

  private HL7MessageMap map = rootParser.getMessagePartMap(IMMUNITY_MSG);

  @Test
  public void test4() {

    // This just validates that the message is correct for this test.
    int i = map.findFieldRepWithValue("BDL", "PID-11-7", 1);
    assertEquals("should be in second field rep", 2, i);

  }

  @Test
  public void testPidParse() {
    DqaPatient patient = HL7PatientParser.INSTANCE.getPatient(map);
    assertEquals("Powell", patient.getNameLast());
    assertEquals("Diarmid", patient.getNameFirst());
  }

}
