package org.immregistries.mqe.hl7util.transform.procedure;

import org.immregistries.mqe.hl7util.transform.TransformRequest;
import org.immregistries.mqe.hl7util.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class RemoveVaccinationGroupsProcedureTest extends TestCase {

  private static final String TEST1_ORIGINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-013.00|P|2.5.1|||AL|ER\r"
      + "PID|1||MR-67323^^^NIST MPI^MR||Fleming^Chad^^^^^L||20100830|M\r" + "ORC|RE||9999^CDC\r"
      + "RXA|0|1|20120815||03^MMR^CVX|999||||||||||||00^Parental Refusal^NIP002||RE\r";

  private static final String TEST1_FINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-013.00|P|2.5.1|||AL|ER\r"
      + "PID|1||MR-67323^^^NIST MPI^MR||Fleming^Chad^^^^^L||20100830|M\r";

  private static final String TEST2_ORIGINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
      + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
      + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
      + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
      + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
      + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
      + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  private static final String TEST2_FINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
      + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
      + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
      + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
      + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
      + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
      + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  private static final String TEST3_ORIGINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
      + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
      + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
      + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
      + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
      + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
      + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r" + "ORC|RE||9999^CDC\r"
      + "RXA|0|1|20120815||03^MMR^CVX|999||||||||||||00^Parental Refusal^NIP002||RE\r";

  private static final String TEST3_FINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
      + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
      + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
      + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
      + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
      + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
      + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  private static final String TEST4_ORIGINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
      + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
      + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
      + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r" + "ORC|RE||9999^CDC\r"
      + "RXA|0|1|20120815||03^MMR^CVX|999||||||||||||00^Parental Refusal^NIP002||RE\r"
      + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
      + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
      + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  private static final String TEST4_FINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
      + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
      + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
      + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
      + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
      + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
      + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  private static final String TEST5_ORIGINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
      + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
      + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
      + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r" + "ORC|RE||9999^CDC\r"
      + "RXA|0|1|20120815||03^MMR^CVX|999||||||||||||00^Parental Refusal^NIP002||RE\r"
      + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
      + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
      + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r" + "ORC|RE||9999^CDC\r"
      + "RXA|0|1|20120815||03^MMR^CVX|999||||||||||||00^Parental Refusal^NIP002||RE\r";

  private static final String TEST5_FINAL = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
      + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
      + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
      + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
      + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
      + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
      + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  @Test
  public void test() {
    test(TEST1_ORIGINAL, TEST1_FINAL);
    test(TEST2_ORIGINAL, TEST2_FINAL);
    test(TEST3_ORIGINAL, TEST3_FINAL);
    test(TEST4_ORIGINAL, TEST4_FINAL);
    test(TEST5_ORIGINAL, TEST5_FINAL);
  }

  public void test(String om, String fm) {
    TransformRequest transformRequest = new TransformRequest(om);
    transformRequest.setTransformText(" run procedure Remove_Vaccination_Groups where RXA-20 equals 'RE'");
    Transformer transformer = new Transformer();
    transformer.transform(transformRequest);
    assertEquals(fm, transformRequest.getResultText());
  }
}
