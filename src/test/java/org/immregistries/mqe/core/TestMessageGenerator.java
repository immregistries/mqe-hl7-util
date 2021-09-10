package org.immregistries.mqe.core;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestMessageGenerator {
	
	public static final String MSH = "MSH|^~\\&|ECW|1337-44-01|MCIR|MDCH|20140619191115||VXU^V04|61731|P|2.3.1|||AL\r";
	public static final String PID =  "PID|||23456^^^^MR||LASTNAME^FIRSTNAME^LEGALMIDDLE^LEGALSUFFIX^^X~COOLNAME^AWESOMENAME2|MOMSMAIDEN|20120604|M||RACECD|100 Main^^Lansing^MI^48912-1330^US^^^60~^^^^^US^BDL^^123456||^PRN^PH^^^517^555-5555~(517)555-5555^P^H||en|||||||22|23||||||FIELD29-----||||||\r";
	public static final String PID2 = "PID|||00000^^^^MR~111111^^^^WC~22222^^^^SS~33333^^^^MA~44444^^^^SR||LEGALLAST^^^^^X~COOLNAME^AWESOMENAME2||20120605|F|||100 Main^^Lansing^MI^48912-1330^US^~^^^^^US^BDL^^123456||(517)555-2222^P^H~^PRN^PH^^^517^555-5555||en||||\r";
	public static final String NK1 =  "NK1||NKLast^NKFirst|MTH^3^HL70063|100 Main^^Lansing^MI^48912 1330^US^|5175555555|||||||||||||||20||||||||||30|||33^^^^SS||||37|\r";
	public static final String ORC =  "ORC|||123456|\r";
	public static final String RXA =  "RXA|0||20140614|20140614|83^Hepatitis A ped/adol^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
	public static final String RXA2 = "RXA|0|X|20140615|20140615|87^Awesome Immunization^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
	public static final String RXA3 = "RXA|0||20140616|20140616|89^IMPORTANT Immunization^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
	public static final String RXR =  "RXR|IM^Intramuscular^HL70162|RT^Right Thigh^HL70163\r";
	public static final String OBX =  "OBX|2|CE|64994-7^Vaccine funding program eligibility category^LN||V02^VFC Eligible - Medicaid/Medicare^HL70064||||||F|||20140614\r";

	public static String EXAMPLE_VXU;
	
	public static String EXAMPLE_VXU_II = (MSH + PID2 + NK1 + ORC + RXA + RXR + OBX);
	
	private static final Map<String,String> SEG_MAP = new LinkedHashMap<String,String>();
	private static final List<String> SEG_LIST = new LinkedList<String>();
	
	public static final String AIRA_MSH = "MSH|^~\\&|||||20160518151704-0400||VXU^V04^VXU_V04|5B6-B.10.05.1fa|P|2.5.1|\r";
	public static final String AIRA_PID = "PID|||5B6-B.10.05^^^AIRA-TEST^MR||Latimer^Rocco^Janus^^^^L|Monona^Ushma|20120524|M||2106-3^White^HL70005|266 Lynch St^^Caspian^MI^49915^USA^P||^PRN^PH^^^906^3997846|||||||||2186-5^not Hispanic or Latino^HL70005|\r";
	public static final String AIRA_PD1 = "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20160518|20160518|\r";
	public static final String AIRA_NK1 = "NK1|1|Monona^Ushma^^^^^L|FTH^Father^HL70063|266 Lynch St^^Caspian^MI^49915^USA^P|^PRN^PH^^^906^3997846|\r";
	public static final String AIRA_ORC = "ORC|RE||E47Q115.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r";
	public static final String AIRA_RXA = "RXA|0|1|20160518||03^MMR^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||U1747GW||MSD^Merck and Co^MVX||||A|\r";
	public static final String AIRA_RXR = "RXR|SC^^HL70162|LA^^HL70163|\r";
	public static final String AIRA_OBX1 = "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20160518|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r";
	public static final String AIRA_OBX2 = "OBX|2|CE|30956-7^Vaccine Type^LN|2|03^MMR^CVX||||||F|\r";
	public static final String AIRA_OBX3 = "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120420||||||F|\r";
	public static final String AIRA_OBX4 = "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160518||||||F|\r";

	public static final String AIRA_TEST_MSG = AIRA_MSH + AIRA_PID + AIRA_PD1 + AIRA_NK1 + AIRA_ORC + AIRA_RXA + AIRA_RXR + AIRA_OBX1 + AIRA_OBX2 + AIRA_OBX3 + AIRA_OBX4;

	public static final String IMMUNITY_MSG_PID = "PID|||2bK5-B.07.14^^^AIRA-TEST^MR||Powell^Diarmid^T^^^^L||20030415|M||2106-3^White^HL70005|215 Armstrong Cir^^Brethren^MI^49619^USA^P||^PRN^PH^^^231^4238012|||||||||2186-5^not Hispanic or Latino^HL70005|\r";
	private static final String IMMUNITY_MSG = 
			 "MSH|^~\\&|||||20160413161526-0400||VXU^V04^VXU_V04|2bK5-B.07.14.1Nx|P|2.5.1|\r"
			+ IMMUNITY_MSG_PID
			+"ORC|RE||N54R81.2^AIRA|\r"
			+"RXA|0|1|20140420||998^No vaccine administered^CVX|999|||||||||||||||A|\r"
			+"OBX|1|CE|59784-9^Disease with presumed immunity^LN|1|23511006^Meningococcal infectious disease (disorder)^SCT||||||F|\r"
			+"ORC|RE||N54R81.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
			+"RXA|0|1|20160413||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||L0214MX||SKB^GlaxoSmithKline^MVX||||A|\r"
			+"OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160413|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
			+"OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
			+"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
			+"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160413||||||F|\r";

	public static final String LAST_SEG_RXA_PID = /*2*/	"\nPID|||3WzJ-A.01.01-1797334094^^^AIRA-TEST^MR||Orna^Tahira^^^^^L|Aemilius^Reynolds|20180301|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|81 Page Pl^^GR^MI^49544^USA^P||^PRN^PH^^^616^9245843|||||||||2135-2^Hispanic or Latino^HL70005|Birth Place PID-23|||";
	public static final String LAST_SEG_RXA =
    /*1*/     "MSH|^~\\&||1255-60-20|MCIR|MDCH|201860301131228-0500||VXU^V04^VXU_V04|3WzJ-A.01.01.2aF|T|2.5.1|"
    /*2*/	+ LAST_SEG_RXA_PID
    /*3*/	+ "\nPD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20170301|20170301|"
    /*4*/	+ "\nNK1|1|Orna^Reynolds^^^^^L|MTH^Mother^HL70063|81 Page Pl^^GR^MI^49544^USA^P|^PRN^PH^^^616^9245843|"
    /*5*/	+ "\nORC|RE||V51L2.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|"
    /*6*/	+ "\nRXA|0|1|20180601||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y5841RR||MSD^Merck and Co^MVX||||A|"
    /*7*/	+ "\nRXR|SC^^HL70162|RA^^HL70163|"
    /*8*/	+ "\nOBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20170301|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|"
    /*9*/	+ "\nOBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|"
    /*10*/+ "\nOBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|"
    /*11*/+ "\nOBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20170301||||||F|"
    /*12*/+ "\nORC|RE||BQ61L11032.2^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L"
    /*13*/+ "\nRXA|0|1|20180601||66019-0302-10^FluMistQuadrivalent^NDC|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||N3783EO||MED^^MVX|||CP|A"
			;

	static
	{
		SEG_MAP.put("MSH.1", MSH);
		SEG_MAP.put("PID.1", PID);
		SEG_MAP.put("NK1.1", NK1);
		SEG_MAP.put("ORC.1", ORC);
		SEG_MAP.put("RXA.1", RXA);
		SEG_MAP.put("OBX.1", OBX);
		SEG_MAP.put("ORC.2", ORC);
		SEG_MAP.put("RXA.2", RXA2);
		SEG_MAP.put("OBX.2", OBX);
		SEG_MAP.put("RXR.1", RXR);
		SEG_MAP.put("RXA.3", RXA3);
		SEG_MAP.put("OBX.3", OBX);
		
		StringBuffer sb = new StringBuffer();
		for (String seg : SEG_MAP.values()) {
			sb.append(seg);
		}
		
		for (String segName : SEG_MAP.keySet()) {
			SEG_LIST.add(segName.substring(0,3));
		}
		
		EXAMPLE_VXU = sb.toString();
		
	}

	public String getExampleVXU_1() {
		return EXAMPLE_VXU;
	}
	
	public String getExampleVXU_2() {
		return EXAMPLE_VXU_II;
	}
	
	public String getImmunityMessage() {
		return IMMUNITY_MSG;
	}
	
	public Map<String,String> getSegmentMap1() {
		return SEG_MAP;
	}
	
	public List<String> getSegmentList1() {
		return SEG_LIST;
	}
	
	public String getAiraTestMsg() {
		return AIRA_TEST_MSG;
	}
}
