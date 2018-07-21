package org.immregistries.mqe.vxu.parse;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.immregistries.mqe.hl7util.model.MetaFieldInfo;
import org.immregistries.mqe.hl7util.parser.HL7MessageMap;
import org.immregistries.mqe.hl7util.parser.MessageParserHL7;
import org.immregistries.mqe.vxu.MqeNextOfKin;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.VxuField;
import org.junit.Test;

public class HL7NokParserTester {

  private MessageParserHL7 mapParser = new MessageParserHL7();
  private HL7NokParser nokParser = HL7NokParser.INSTANCE;

  private static final String NOKS_MSG =
      //@formatter:off
          /* 1 */   "MSH|^~\\&|||||20160413161526-0400||VXU^V04^VXU_V04|2bK5-B.07.14.1Nx|P|2.5.1|\r"
          /* 2 */ + "PID|||1^^^AIRA-TEST^MR||Lam^aaaaa^T^^^^L||20030415|M||2106-3^White^HL70005|215 Armstrong Cir^^Brethren^MI^49619^USA^P[215] Armstrong Cir^^Brethren^MI^49619^USA^P~^^^^^^BDL^^123||^PRN^PH^^^111^5554444|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
          /* 3 */ + "NK1|0|Lam^yyyyy^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^111^5553333\r"
          /* 4 */ + "NK1|1|Lam^xxxxx^^^^^L|FTH^Father^HL70063|99 Prescott Street Ave^^Warwicks^MI^48864^USA^L|^PRN^PH^^^111^5552222\r"
          /* 5 */ + "NK1|2|Lam^sssss^^^^^L|GRP^Grand Parent^HL70063|11 Prescott Street Ave^^Warwicks^MI^48864^USA^L|^PRN^PH^^^111^5551111\r"
;
  //@formatter:on
  private HL7MessageMap map = mapParser.getMessagePartMap(NOKS_MSG);
  private List<MqeNextOfKin> noks = nokParser.getNk1List(map);

  @Test
  public void testAllNoksAreFound() {
    int numNoks = noks.size();
    assertEquals(numNoks, 3);
  }

  @Test
  public void testRp() {
    MqePatient dp = HL7PatientParser.INSTANCE.getPatient(map);
    MqeNextOfKin nok = dp.getResponsibleParty();
    assertEquals("MTH", nok.getRelationshipCode());
    assertEquals("yyyyy", nok.getNameFirst());
    assertEquals("Lam", nok.getNameLast());
    assertEquals("32 Prescott Street Ave", nok.getAddress().getStreet());
    assertEquals("Warwick", nok.getAddress().getCity());
    assertEquals("MA", nok.getAddress().getStateCode());
    assertEquals("02452", nok.getAddress().getZip());
    assertEquals("USA", nok.getAddress().getCountryCode());
    assertEquals("(111)555-3333", nok.getPhoneNumber());
  }

  @Test
  public void testNk1() {
    MqeNextOfKin nok = noks.get(0);
    assertEquals("MTH", nok.getRelationshipCode());
    assertEquals("yyyyy", nok.getNameFirst());
    assertEquals("Lam", nok.getNameLast());
    assertEquals("32 Prescott Street Ave", nok.getAddress().getStreet());
    assertEquals("Warwick", nok.getAddress().getCity());
    assertEquals("MA", nok.getAddress().getStateCode());
    assertEquals("02452", nok.getAddress().getZip());
    assertEquals("USA", nok.getAddress().getCountryCode());
    assertEquals("(111)555-3333", nok.getPhoneNumber());
  }


  @Test
  public void testNk2() {
    MqeNextOfKin nok = noks.get(1);
    assertEquals("FTH", nok.getRelationshipCode());
    assertEquals("xxxxx", nok.getNameFirst());
    assertEquals("Lam", nok.getNameLast());
    assertEquals("99 Prescott Street Ave", nok.getAddress().getStreet());
    assertEquals("Warwicks", nok.getAddress().getCity());
    assertEquals("MI", nok.getAddress().getStateCode());
    assertEquals("48864", nok.getAddress().getZip());
    assertEquals("USA", nok.getAddress().getCountryCode());
    assertEquals("(111)555-2222", nok.getPhoneNumber());
  }


  @Test
  public void testNk3() {
    MqeNextOfKin nok = noks.get(2);
    assertEquals("GRP", nok.getRelationshipCode());
    assertEquals("sssss", nok.getNameFirst());
    assertEquals("Lam", nok.getNameLast());
    assertEquals("11 Prescott Street Ave", nok.getAddress().getStreet());
    assertEquals("Warwicks", nok.getAddress().getCity());
    assertEquals("MI", nok.getAddress().getStateCode());
    assertEquals("48864", nok.getAddress().getZip());
    assertEquals("USA", nok.getAddress().getCountryCode());
    assertEquals("(111)555-1111", nok.getPhoneNumber());
  }


  @Test
  public void testPatientGuardianMeta() {
    MqeNextOfKin nok = noks.get(2);
    MetaFieldInfo mfi = nok.getMetaFieldInfo(VxuField.NEXT_OF_KIN_NAME_FIRST);
    assertEquals("sssss", mfi.getValue());
    assertEquals("NK1[3]-2[1].2.1", mfi.getHl7Location().toString());
    assertEquals(3, mfi.getHl7Location().getSegmentSequence());
    mfi = nok.getMetaFieldInfo(VxuField.NEXT_OF_KIN_RELATIONSHIP);
    assertEquals("GRP", mfi.getValue());
    assertEquals("NK1[3]-3[1].1.1", mfi.getHl7Location().toString());
    assertEquals(3, mfi.getHl7Location().getSegmentSequence());
    mfi = nok.getMetaFieldInfo(VxuField.NEXT_OF_KIN_NAME_LAST);
    assertEquals("Lam", mfi.getValue());
    assertEquals("NK1[3]-2[1].1.1", mfi.getHl7Location().toString());
    assertEquals(3, mfi.getHl7Location().getSegmentSequence());
    mfi = nok.getMetaFieldInfo(VxuField.NEXT_OF_KIN_ADDRESS_STREET);
    assertEquals("11 Prescott Street Ave", mfi.getValue());
    assertEquals("NK1[3]-4[1].1.1", mfi.getHl7Location().toString());
    assertEquals(3, mfi.getHl7Location().getSegmentSequence());
    mfi = nok.getMetaFieldInfo(VxuField.NEXT_OF_KIN_ADDRESS_CITY);
    assertEquals("Warwicks", mfi.getValue());
    assertEquals("NK1[3]-4[1].3.1", mfi.getHl7Location().toString());
    assertEquals(3, mfi.getHl7Location().getSegmentSequence());
    mfi = nok.getMetaFieldInfo(VxuField.NEXT_OF_KIN_ADDRESS_STATE);
    assertEquals("MI", mfi.getValue());
    assertEquals("NK1[3]-4[1].4.1", mfi.getHl7Location().toString());
    assertEquals(3, mfi.getHl7Location().getSegmentSequence());
    mfi = nok.getMetaFieldInfo(VxuField.NEXT_OF_KIN_ADDRESS_ZIP);
    assertEquals("48864", mfi.getValue());
    assertEquals("NK1[3]-4[1].5.1", mfi.getHl7Location().toString());
    assertEquals(3, mfi.getHl7Location().getSegmentSequence());
    mfi = nok.getMetaFieldInfo(VxuField.NEXT_OF_KIN_ADDRESS_COUNTRY);
    assertEquals("USA", mfi.getValue());
    assertEquals("NK1[3]-4[1].6.1", mfi.getHl7Location().toString());
    assertEquals(3, mfi.getHl7Location().getSegmentSequence());
  }
}
