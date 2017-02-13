package org.immregistries.dqa.vxu.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.hl7util.parser.MessageParserHL7;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.immregistries.dqa.vxu.hl7.CodedEntity;
import org.immregistries.dqa.vxu.hl7.Observation;
import org.immregistries.dqa.vxu.parse.HL7VaccineParser;
import org.junit.Test;

public class HL7VaccineParserTester {
	private MessageParserHL7 rootParser = new MessageParserHL7();
	private HL7VaccineParser vParser = HL7VaccineParser.INSTANCE;
	
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
		assertEquals("shouldn't have rxr info - route", null, immVac.getBodyRoute());
		assertEquals("shouldn't have rxr info - site", null, immVac.getBodySite());
		//Second one should.
		DqaVaccination hepAVac = vList.get(1);
		assertEquals("should have rxr info - route", "IM", hepAVac.getBodyRoute());
		assertEquals("should have rxr info - site", "RT", hepAVac.getBodySite());
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

}
