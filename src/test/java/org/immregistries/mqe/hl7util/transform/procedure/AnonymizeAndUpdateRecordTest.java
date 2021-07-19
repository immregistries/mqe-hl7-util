package org.immregistries.mqe.hl7util.transform.procedure;

import org.immregistries.mqe.hl7util.transform.TransformRequest;
import org.immregistries.mqe.hl7util.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class AnonymizeAndUpdateRecordTest extends TestCase {

  private static final String MSH_ORIG = "MSH|^~\\&|||||20160805102500-0600||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r";
  private static final String MSH_NEW = "MSH|^~\\&|||||20161108102500-0600||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r";

  @Test
  public void test() {
    AnonymizeAndUpdateRecord.setAsOfDate("20161109");
    // Test reading MSH, basic tests
    test(MSH_ORIG, MSH_NEW);
    test("MSH|^~\\&|||||20160805||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test("MSH|^~\\&|||||20160805102500||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test("MSH|^~\\&|||||20160805102500-0600~Hello!||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500-0600~Hello!||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test("MSH|^~\\&|||||||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test("MSH|^~\\&|||||20160805102500-0600&||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500-0600&||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test("MSH|^~\\&|||||20160805102500-0600^||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500-0600^||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test("MSH|^~\\&|||||20160805102500-0600^~&||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500-0600^~&||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");

//    test(
//        MSH_ORIG
//            + "PID|1||1234^^^AIRA-TEST^MR||Pecos^Sawyer^K^^^^L|Marion^Valisa^^^^^M|20120725|F||2106-3^White^CDCREC|350 Greene Cir^^Little Lake^MI^49833^USA^P||^PRN^PH^^^906^3464569|||||||||2186-5^not Hispanic or Latino^CDCREC||N||||||N",
//        MSH_NEW
//            + "PID|1||1234^^^AIRA-TEST^MR||Pecos^Sawyer^K^^^^L|Marion^Valisa^^^^^M|20120725|F||2106-3^White^CDCREC|350 Greene Cir^^Little Lake^MI^49833^USA^P||^PRN^PH^^^906^3464569|||||||||2186-5^not Hispanic or Latino^CDCREC||N||||||N");

  }

  public void test(String om, String fm) {
    TransformRequest transformRequest = new TransformRequest(om);
    transformRequest.setTransformText(" run procedure ANONYMIZE_AND_UPDATE_RECORD");
    Transformer transformer = new Transformer();
    transformer.transform(transformRequest);
    assertEquals(fm, transformRequest.getResultText());
  }

}
