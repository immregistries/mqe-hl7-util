package org.immregistries.dqa.vxu.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.hl7util.parser.MessageParserHL7;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.hl7.CodedEntity;
import org.immregistries.dqa.vxu.hl7.Observation;
import org.immregistries.dqa.vxu.parse.HL7VaccinationParser;
import org.junit.Test;

public class HL7VaccineParserTester {
	private MessageParserHL7 rootParser = new MessageParserHL7();
	private HL7VaccinationParser vParser = HL7VaccinationParser.INSTANCE;
	
	private static final String IMMUNITY_MSG = 
	/* 0*/		 "MSH|^~\\&|||||20160413161526-0400||VXU^V04^VXU_V04|2bK5-B.07.14.1Nx|P|2.5.1|\r"
	/* 1*/		+"PID|||2bK5-B.07.14^^^AIRA-TEST^MR||Powell^Diarmid^T^^^^L||20030415|M||2106-3^White^HL70005|215 Armstrong Cir^^Brethren^MI^49619^USA^P||^PRN^PH^^^231^4238012|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
	/* 2*/		+"ORC|RE||N54R81.2^AIRA|\r"
	/* 3*/		+"RXA|0|1|20140420||998^No vaccine administered^CVX|999|||||||||||||||A|\r"
	/* 4*/		+"OBX|1|CE|59784-9^Disease with presumed immunity^LN|1|23511006^Meningococcal infectious disease (disorder)^SCT||||||F|\r"
	/* 5*/		+"ORC|RE||N54R81.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
	/* 6*/		+"RXA|0|1|20160413||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||L0214MX||SKB^GlaxoSmithKline^MVX||||A|\r"
	/* 7*/		+"RXR|IM^Intramuscular^HL70162|RT^Right Thigh^HL70163\r"
	/* 8*/		+"OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160413|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
	/* 9*/		+"OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
	/*10*/		+"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
	/*11*/		+"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160413||||||F|\r";
	
	private static String VFC_BREAKER = 
			"MSH|^~\\&|||||20170427122558-0600||VXU^V04^VXU_V04|2jCa-L.IZ-AD-2|D|2.5.1|||ER|AL|||||Z22^CDCPHINVS||\r"
           +"PID|1||U37X24^^^AIRA-TEST^MR||Story^Archie^A^^^^L|Brennan^Rayna|20161019|M||2028-9^Asian^CDCREC|278 Shelby Cir^^Wyoming^MI^49418^USA^P||^PRN^PH^^^616^8248132~^NET^^Elise.Wong@isp.com|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|1|||||N\r"
           +"PD1|||||||||||02^Reminder/Recall - any method^HL70215|N|20150624|||A|20170427|20170427\r"
           +"ORC|RE|AU37X24.1^NIST-AA-IZ-2|BU37X24.1^AIRA|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||654^Thomas^Wilma^Elizabeth^^^^^NIST-PI-1^L^^^MD|||||NISTEHRFAC^NISTEHRFacility^HL70362\r"
           +"RXA|0|1|20170217||20^DTaP^CVX|999|mL^mL^UCUM||01^Historical^NIP001|7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|^^^NIST-Clinic-1||||315841|20151216|PMC^Sanofi Pasteur^MVX|||CP|A\r"
           +"RXR|OTH^Intramuscular^NCIT|RD^Right Deltoid^HL70163\r"
           +"OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|PHC70^Private^CDCPHINVS||||||F|||20150624\r"
           +"OBX|2|CE|64994-7^Vaccine Funding Program Eligibility^LN|2|V01^Not VFC Eligible^HL70064||||||F|||20150624|||VXC40^per immunization^CDCPHINVS\r"
           +"OBX|3|CE|69764-9^Document Type^LN|3|253088698300028811150224^Tetanus/Diphtheria (Td) VIS^cdcgs1vis||||||F|||20150624\r"
           +"OBX|4|DT|29769-7^Date Vis Presented^LN|3|20150624||||||F|||20150624\r"
           +"ORC|RE||BU37X24.2^AIRA|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|||||||NISTEHRFAC^NISTEHRFacility^HL70362\r"
           +"RXA|0|1|20170217||20^DTaP^CVX|999|||01^Historical^NIP001|||||||||||CP|A\r"
           +"ORC|RE||BU37X24.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^L^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|||||NISTEHRFAC^NISTEHRFacility^HL70362\r"
           +"RXA|0|1|20170427||49^Hib^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||U5086LL||MSD^Merck and Co^MVX|||CP|A\r";
	
	private HL7MessageMap map = rootParser.getMessagePartMap(IMMUNITY_MSG);
	

	@Test
	public void test4() {
		List<DqaVaccination> vaccList = vParser.getVaccinationList(map);
		assertNotNull(vaccList);
		//it should have one vaccine for the 998 vacc. 
		//and then one for the 
		assertEquals("should have two vaccines", 2, vaccList.size());
		assertEquals("Second one should be 83", "83", vaccList.get(1).getAdminCvxCode());
		assertEquals("First one should be 998", "998", vaccList.get(0).getAdminCvxCode());
	}
	
	@Test
	public void rxrTest() {
		//testing the site and route
		List<DqaVaccination> vList = vParser.getVaccinationList(map);
		assertNotNull(vList);
		assertEquals("Should have two", 2, vList.size());
		//first one should not have RXR info.  
		DqaVaccination immVac = vList.get(0);
		assertEquals("1 shouldn't have rxr info - route", "", immVac.getBodyRoute());
		assertEquals("1 shouldn't have rxr info - site", "", immVac.getBodySite());
		//Second one should.
		DqaVaccination hepAVac = vList.get(1);
		assertEquals("2 should have rxr info - route", "IM", hepAVac.getBodyRoute());
		assertEquals("2 should have rxr info - site", "RT", hepAVac.getBodySite());
	}
	
	@Test
	public void test5() {
		List<CodedEntity> cvxList = vParser.getVaccineCodes(map, 3);
		assertNotNull(cvxList);
		assertEquals("Should have one vaccine code", 1, cvxList.size());
		assertEquals("first item in the list should be 998...", "998", cvxList.get(0).getCode());
	}
	
	@Test 
	public void testObservationGetter() {
		//the first observation is the fourth segment in this message.  (zero based)
		Observation o = vParser.getObservation(map, 4);
		assertNotNull(o);
			
		assertEquals("identifier", "59784-9", o.getIdentifierCode());
		assertEquals("value", "23511006", o.getValue());
		
		o = vParser.getObservation(map, 8);
		assertNotNull(o);
		assertEquals("identifier", "64994-7", o.getIdentifierCode());
		assertEquals("value", "V02", o.getValue());
		
		o = vParser.getObservation(map, 9);
		assertNotNull(o);
		assertEquals("identifier", "30956-7", o.getIdentifierCode());
		assertEquals("value", "85", o.getValue());

		o = vParser.getObservation(map, 10);
		assertNotNull(o);
		assertEquals("identifier", "29768-9", o.getIdentifierCode());
		assertEquals("value", "20111025", o.getValue());
		
		o = vParser.getObservation(map, 11);
		assertNotNull(o);
		assertEquals("identifier", "29769-7", o.getIdentifierCode());
		assertEquals("value", "20160413", o.getValue());
	}
	
	@Test
	public void testVfcProblems() {
		String message = VFC_BREAKER;
		HL7MessageMap messageMap = rootParser.getMessagePartMap(message);
		List<Integer> obxIdxList = new ArrayList<Integer>(); 
		String vfc = vParser.getShotVFCCode(messageMap, 13, obxIdxList);
		assertEquals("Should not find a vfc code",  "V00", vfc);
	}

}
