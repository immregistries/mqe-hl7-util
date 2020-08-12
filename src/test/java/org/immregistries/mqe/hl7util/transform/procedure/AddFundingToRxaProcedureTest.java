package org.immregistries.mqe.hl7util.transform.procedure;

import org.immregistries.mqe.hl7util.transform.TransformRequest;
import org.immregistries.mqe.hl7util.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class AddFundingToRxaProcedureTest extends TestCase {

  private static final String ELIGIBILITY_ALL =
      ProcedureFactory.ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA;

  private static final String SOURCE_ALL = ProcedureFactory.ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA;

  private static final String SOURCE_ADMIN =
      ProcedureFactory.ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA;

  private static final String ELIGIBILITY_ADMIN =
      ProcedureFactory.ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA;

  private static final String TEST1_ORIGINAL =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
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

  private static final String TEST1_FINAL =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
          + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"
          + "OBX|5|CE|30963-3^Vaccine Funding Source^LN|5|VXC50^Public^CDCPHINVS||||||F|\r";

  private static final String TEST2_ORIGINAL =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
          + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|2|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|3|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  private static final String TEST2A_FINAL =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
          + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|2|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|3|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"
          + "OBX|4|CE|64994-7^Vaccine funding program eligibility category^LN|4|V01^Not VFC eligible^HL70064||||||F|||20150817|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r";

  private static final String TEST2B_FINAL =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
          + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|2|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|3|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"
          + "OBX|4|CE|64994-7^Vaccine funding program eligibility category^LN|4|V01^Not VFC eligible^HL70064||||||F|||20150817|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|5|CE|30963-3^Vaccine Funding Source^LN|5|VXC50^Public^CDCPHINVS||||||F|\r";

  private static final String TEST3_ORIGINAL =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
          + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||01^Historical^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|30956-7^vaccine type^LN|1|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|2|TS|29768-9^Date vaccine information statement published^LN|1|20120702||||||F\r"
          + "OBX|3|TS|29769-7^Date vaccine information statement presented^LN|1|20120814||||||F\r";

  private static final String TEST3A_FINAL =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
          + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||01^Historical^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|30956-7^vaccine type^LN|1|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|2|TS|29768-9^Date vaccine information statement published^LN|1|20120702||||||F\r"
          + "OBX|3|TS|29769-7^Date vaccine information statement presented^LN|1|20120814||||||F\r"
          + "OBX|4|CE|64994-7^Vaccine funding program eligibility category^LN|4|V01^Not VFC eligible^HL70064||||||F|||20150817|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r";

  private static final String TEST3B_FINAL =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
          + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||01^Historical^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|30956-7^vaccine type^LN|1|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|2|TS|29768-9^Date vaccine information statement published^LN|1|20120702||||||F\r"
          + "OBX|3|TS|29769-7^Date vaccine information statement presented^LN|1|20120814||||||F\r"
          + "OBX|4|CE|30963-3^Vaccine Funding Source^LN|4|VXC50^Public^CDCPHINVS||||||F|\r";

  private static final String TEST4_ORIGINAL =
      "MSH|^~\\&|||||20200803140619-0400||VXU^V04^VXU_V04|E99Y1.2sI|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1||P87C4^^^AIRA-TEST^MR||ElkAIRA^CarlyAIRA^Jackie^^^^L|McCullochAIRA^FaithAIRA^^^^^M|20180603|F||2106-3^White^CDCREC|1181 Nuth Cir^^Bloomingdale^MI^49026^USA^P||^PRN^PH^^^269^3834724|||||||||2186-5^not Hispanic or Latino^CDCREC\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20200803|20200803\r"
          + "NK1|1|ElkAIRA^McCullochAIRA^^^^^L|MTH^Mother^HL70063|1181 Nuth Cir^^Bloomingdale^MI^49026^USA^P|^PRN^PH^^^269^3834724\r"
          + "ORC|RE||BP87C4.1^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1\r"
          + "RXA|0|1|20200603||21^Varicella^CVX|999|||01^Historical information - source unspecified^NIP001|||||||||||CP|A\r"
          + "ORC|RE||BP87C4.2^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
          + "RXA|0|1|20200803||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||N3783EO||MSD^Merck and Co^MVX|||CP|A\r"
          + "RXR|C38299^Subcutaneous^NCIT|RA^^HL70163\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20200803|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20200803||||||F\r";
  private static final String TEST4_FINAL = TEST4_ORIGINAL
      + "OBX|5|CE|30963-3^Vaccine Funding Source^LN|5|VXC50^Public^CDCPHINVS||||||F|\r";

  @Test
  public void test() {
    test(TEST1_ORIGINAL, TEST1_FINAL, SOURCE_ALL);
    test(TEST1_ORIGINAL, TEST1_FINAL, SOURCE_ADMIN);

    // nothing should happen to a fully populated message
    test(TEST1_FINAL, TEST1_FINAL, SOURCE_ALL);
    test(TEST1_FINAL, TEST1_FINAL, SOURCE_ADMIN);
    test(TEST1_FINAL, TEST1_FINAL, ELIGIBILITY_ALL);
    test(TEST1_FINAL, TEST1_FINAL, ELIGIBILITY_ADMIN);

    test(TEST2_ORIGINAL, TEST2A_FINAL, ELIGIBILITY_ALL);
    test(TEST2_ORIGINAL, TEST2A_FINAL, ELIGIBILITY_ADMIN);
    test(TEST2A_FINAL, TEST2B_FINAL, SOURCE_ALL);
    test(TEST2A_FINAL, TEST2B_FINAL, SOURCE_ADMIN);

    // no change for historical
    test(TEST3_ORIGINAL, TEST3_ORIGINAL, SOURCE_ADMIN);
    test(TEST3_ORIGINAL, TEST3_ORIGINAL, ELIGIBILITY_ADMIN);

    // change historical
    test(TEST3_ORIGINAL, TEST3A_FINAL, ELIGIBILITY_ALL);
    test(TEST3_ORIGINAL, TEST3B_FINAL, SOURCE_ALL);

    // multiple immunizations
    test(TEST4_ORIGINAL, TEST4_FINAL, SOURCE_ADMIN);

  }

  public void test(String om, String fm, String procedure) {
    TransformRequest transformRequest = new TransformRequest(om);
    transformRequest.setTransformText(" run procedure " + procedure);
    Transformer transformer = new Transformer();
    transformer.transform(transformRequest);
    assertEquals(fm, transformRequest.getResultText());
  }
}
