//package org.immregistries.mqe.core.model.transform;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.util.List;
//import org.immregistries.mqe.core.TestMessageGenerator;
//import org.immregistries.mqe.vxu.MqeMessageReceived;
//import org.immregistries.mqe.vxu.MqeVaccination;
//import org.immregistries.mqe.vxu.PatientImmunity;
//import org.immregistries.mqe.vxu.hl7.Observation;
//import org.immregistries.mqe.vxu.parse.HL7MessageParser;
//import org.junit.Test;
//
//public class MessageTransformerTest {
//
//	private static final String IMMUNITY_MSG =
//			 "MSH|^~\\&|||||20160413161526-0400||VXU^V04^VXU_V04|2bK5-B.07.14.1Nx|P|2.5.1|\r"
//			+"PID|||2bK5-B.07.14^^^AIRA-TEST^MR||Powell^Diarmid^T^^^^L||20030415|M||2106-3^White^HL70005|215 Armstrong Cir^^Brethren^MI^49619^USA^P||^PRN^PH^^^231^4238012|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
//			+"ORC|RE||N54R81.2^AIRA|\r"
//			+"RXA|0|1|20140420||998^No vaccine administered^CVX|999|||||||||||||||A|\r"
//			+"OBX|1|CE|59784-9^Disease with presumed immunity^LN|1|23511006^Meningococcal infectious disease (disorder)^SCT||||||F|\r"
//			+"OBX|2|CE|59784-9^Disease with presumed immunity^LN|2|4834000^Typhoid fever (disorder)^SCT||||||F|\r"
//			+"ORC|RE||N54R81.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
//			+"RXA|0|1|20160413||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||L0214MX||SKB^GlaxoSmithKline^MVX||||A|\r"
//			+"OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160413|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
//			+"OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
//			+"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
//			+"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160413||||||F|\r";
//
//	TestMessageGenerator genr = new TestMessageGenerator();
//	HL7MessageParser toObj = HL7MessageParser.INSTANCE;
//	MessageTransformer mt = MessageTransformer.INSTANCE;
//
//	@Test
//	public final void testObxList() {
//
//		MqeMessageReceived mr = toObj.extractMessageFromText(IMMUNITY_MSG);
//		mt.transform(mr);
//
//		List<PatientImmunity> immunities = mr.getPatient().getPatientImmunityList();
//		assertNotNull(immunities);
//		assertEquals("Should have two immunity", 2, immunities.size());
//		assertEquals("should be an immunity of type 23511006", "23511006", immunities.get(0).getImmunityCode());
//
//		List<MqeVaccination> vaccList = mr.getVaccinations();
//		assertNotNull(vaccList);
//		//it should have one vaccine for the 998 vacc.
//		//and then one for the 83 vaccination.
//		assertEquals("should have two vaccines", 2, vaccList.size());
//
//		MqeVaccination immVacc = vaccList.get(0);
//		MqeVaccination hepaVacc = vaccList.get(1);
//
//		assertEquals("First one should be 998", "998", immVacc.getAdminCvxCode());
//		assertEquals("Second one should be 83", "83", hepaVacc.getAdminCvxCode());
//
//		List<Observation> immObsList = immVacc.getObservations();
//		List<Observation> hepaObsList = immVacc.getObservations();
//
//		assertNotNull(immObsList);
//		assertEquals("imm obs list should have one entry", 2, immObsList.size());
//
//		int x = 1;
//
////		for (Observation o : immObsList) {
////			System.out.println("IMMUNITY OBSERVATION: " + x++);
////			System.out.println(o.getIdentifierCode());
////			System.out.println(o.getObservationDateString());
////			System.out.println(o.getObservationMethodCode());
////			System.out.println(o.getValue());
////			System.out.println(o.getValueTypeCode());
////			System.out.println(o.getSubId());
////		}
//
//		assertNotNull(hepaObsList);
//		assertEquals("imm obs list should have one entry", 2, hepaObsList.size());
//
//		x = 1;
//
////		for (Observation o : hepaObsList) {
////			System.out.println("IMMUNITY OBSERVATION: " + x++);
////			System.out.println(o.getIdentifierCode());
////			System.out.println(o.getObservationDateString());
////			System.out.println(o.getObservationMethodCode());
////			System.out.println(o.getValue());
////			System.out.println(o.getValueTypeCode());
////			System.out.println(o.getSubId());
////		}
//
//		assertEquals("imm obs entry should have identifier of ", "59784-9", immObsList.get(0).getIdentifierCode());
//
//	}
//
//
//}
