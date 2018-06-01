package org.immregistries.dqa.vxu.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.hl7util.parser.MessageParserHL7;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaNextOfKin;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.VxuField;
import org.junit.Test;

public class HL7MshParserTester {

  private HL7MessageHeaderParser mshParser = HL7MessageHeaderParser.INSTANCE;
  private MessageParserHL7 mapParser = new MessageParserHL7();
  private HL7MessageMap map = mapParser.getMessagePartMap(EXAMPLE_MSG);
  private static final String EXAMPLE_MSG =
      //@formatter:off
          /* 1 */   "MSH|^~\\&||1337-44-01|||20160413161526-0400||VXU^V04^VXU_V04|2bK5-B.07.14.1Nx|P|2.5.1|\r"
          /* 2 */ + "PID|||1^^^AIRA-TEST^MR||Lam^aaaaa^T^^^^L||20030415|M||2106-3^White^HL70005|215 Armstrong Cir^^Brethren^MI^49619^USA^P[215] Armstrong Cir^^Brethren^MI^49619^USA^P~^^^^^^BDL^^123||^PRN^PH^^^111^5554444|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
          /* 3 */ + "NK1|0|Lam^yyyyy^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^111^5553333\r"
          /* 4 */ + "NK1|1|Lam^xxxxx^^^^^L|FTH^Father^HL70063|99 Prescott Street Ave^^Warwicks^MI^48864^USA^L|^PRN^PH^^^111^5552222\r"
          /* 5 */ + "NK1|2|Lam^sssss^^^^^L|GRP^Grand Parent^HL70063|11 Prescott Street Ave^^Warwicks^MI^48864^USA^L|^PRN^PH^^^111^5551111\r"
;
  //@formatter:on
  @Test
  public void mshHasExpectedParts() {
    DqaMessageHeader msh = this.mshParser.getMessageHeader(map);
    assertNotNull(msh);
    assertEquals("date", "20160413161526-0400", msh.getMessageDateString());
    assertEquals("message identifier", "2bK5-B.07.14.1Nx", msh.getMessageControl());
    assertEquals("pin", "1337-44-01", msh.getSendingFacility());
  }

}

