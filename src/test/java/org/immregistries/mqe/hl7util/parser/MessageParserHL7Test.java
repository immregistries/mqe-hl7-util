package org.immregistries.mqe.hl7util.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.immregistries.mqe.hl7util.parser.model.HL7MessagePart;
import org.immregistries.mqe.hl7util.parser.profile.generator.FieldComplexity;
import org.junit.Test;

public class MessageParserHL7Test {

	private MessageParserHL7 mpp = new MessageParserHL7();
	 
	private static final String MSH = "MSH|^~\\&|ECW|1337-44-01|MCIR|MDCH|20140619191115||VXU^V04|61731|P|2.3.1|||AL\r";
	private static final String MSH_CRAZY = "MSH%$~\\&%ECW%1337-44-01%MCIR%MDCH%20140619191115%%VXU$V04%61731%P%2.3.1%%%AL\r";
	private static final String PID =  "PID|||23456^^^^MR||FIRSTNAME^LASTNAME^^^^X~COOLNAME^AWESOMENAME2||20120604|M|||100 Main^^Lansing^MI^48912 1330^^||5175555555\r";
	private static final String NK1 =  "NK1||NKLast^NKFirst|^3^HL70063\r";
	private static final String ORC =  "ORC\r";
	private static final String RXA =  "RXA|0||20140614|20140614|83^Hepatitis A ped/adol^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
	private static final String RXR =  "RXR|IM^Intramuscular^HL70162|RT^Right Thigh^HL70163\r";
	private static final String OBX =  "OBX|2|CE|64994-7^Vaccine funding program eligibility category^LN||V02^VFC Eligible - Medicaid/Medicare^HL70064||||||F|||20140614\r";

	private static final String EXAMPLE_VXU;
	public static String getExampleVXU() {
		return EXAMPLE_VXU;
	}
	
	private static final Map<String,String> SEG_MAP = new LinkedHashMap<String,String>();
	private static final List<String> SEG_LIST = new LinkedList<String>();
	static {
		SEG_MAP.put("MSH.1", MSH);
		SEG_MAP.put("PID.1", PID);
		SEG_MAP.put("NK1.1", NK1);
		SEG_MAP.put("ORC.1", ORC);
		SEG_MAP.put("RXA.1", RXA);
		SEG_MAP.put("RXA.2", RXA);
		SEG_MAP.put("RXR.1", RXR);
		SEG_MAP.put("RXA.3", RXA);
		SEG_MAP.put("OBX.1", OBX);
		
		StringBuffer sb = new StringBuffer();
		for (String seg : SEG_MAP.values()) {
			sb.append(seg);
		}
		
		for (String segName : SEG_MAP.keySet()) {
			SEG_LIST.add(segName.substring(0,3));
		}
		
		EXAMPLE_VXU = sb.toString();
	}
	
	private static final String PV1_WIERD_START = 
			"PV1|1|R|^^^1203-67-41^^^WMFMFAC^^AMB^WMFMBLD||||66344362664^Yoldlittle^Ladywho^^MD^^^^WMFM_MI_NPI^P^^^NPI|4412455562335^Yoldlittle^Ladywho^^MD^^^^WMFM_MI_SPI^P^^^CD:B4332244~B23344223^Yoldlittle^Ladywho^^MD^^^^WMFM_MI_DEA^P^^^DOCDEA~AC063882^Yoldlittle^Ladywho^^MD^^^^WMFM_MI_LICNBR^P^^^LICENSENBR~44233442^Yoldlittle^Ladywho^^MD^^^^WMFM_MI_NPI^P^^^NPI~4283^Yoldlittle^Ladywho^^MD^^^^CD:5252334^P^^^EXTID~Conner^Yoldlittle^Ladywho^^MD^^^^WMFM_M\r";
	private static final String PV1_WIERD_CONTINUATION = 
			"I_PRSNL^P^^^EXTID~366436344^Yoldlittle^Ladywho^^MD^^^^CD:36634455^P^^^EXTID~MICACW1^Yoldlittle^Ladywho^^MD^^^^WMFM_MI_HP_PRSNL^P^^^EXTID||CD:534434455||||||||O||^19960426|||||||||||||||||||WMFMFAC||D|||20130510215832|20130511120406\r";
	
	private static final String WIERD_LINE_BREAK = 
			MSH + PID + PV1_WIERD_START + PV1_WIERD_CONTINUATION + NK1 + ORC + RXA + RXR + OBX;

	@Test
	public void testGetMSH1Chars() {
		
		assertEquals("Component Separator", "^", mpp.componentSeparator);
		assertEquals("subComponentSeparator", "&", mpp.subComponentSeparator);
		assertEquals("repetitionSeparator", "~", mpp.repetitionSeparator);
		assertEquals("escapeChar", "\\", mpp.escapeChar);
		
		MessageParserHL7 hnp = new MessageParserHL7();
		hnp.setDelimitersFromMessage(MSH_CRAZY);
		
		assertEquals("fieldSeparator", "%", hnp.fieldSeparator);
		assertEquals("componentSeparator", "$", hnp.componentSeparator);
		assertEquals("subComponentSeparator", "&", hnp.subComponentSeparator);
		assertEquals("repetitionSeparator", "~", hnp.repetitionSeparator);
		assertEquals("escapeChar", "\\", hnp.escapeChar);
		
	}
	
	@Test
	public void testSplitCrazySegment() {
		
		mpp.setDelimitersFromMessage(MSH_CRAZY);
		String[] fieldArray = mpp.splitFields(MSH_CRAZY.replace("\r", ""));
		
		assertEquals("MSH-0", "MSH", fieldArray[0]);
		assertEquals("MSH-1", "%", fieldArray[1]);
		assertEquals("MSH-2", "$~\\&", fieldArray[2]);
		assertEquals("MSH-3", "ECW", fieldArray[3]);
		assertEquals("MSH-4", "1337-44-01", fieldArray[4]);
		assertEquals("MSH-5", "MCIR", fieldArray[5]);
		assertEquals("MSH-6", "MDCH", fieldArray[6]);
		assertEquals("MSH-7", "20140619191115", fieldArray[7]);
		assertEquals("MSH-8", "", fieldArray[8]);
		assertEquals("MSH-9", "VXU$V04", fieldArray[9]);
		assertEquals("MSH-10", "61731", fieldArray[10]);
		assertEquals("MSH-11", "P", fieldArray[11]);
		assertEquals("MSH-12", "2.3.1", fieldArray[12]);
		assertEquals("MSH-13", "", fieldArray[13]);
		assertEquals("MSH-14", "", fieldArray[14]);
		assertEquals("MSH-15", "AL", fieldArray[15]);
	}

	@Test
	public void testSplitSegments() {
		String[] segmentArray = mpp.splitSegments(EXAMPLE_VXU);
		assertEquals("Number of segments",  this.SEG_LIST.size(), segmentArray.length);
	}
	
	@Test
	public void testSplitWierdSegments() {
		String[] segmentArray = mpp.splitSegments(WIERD_LINE_BREAK);
		assertEquals("Should be 5 segments", 8, segmentArray.length);
	}
	
	@Test
	public void testIsProperlyFormedSegment() {
		boolean nopeShouldBeFalse = mpp.isProperlyFormedSegment(PV1_WIERD_CONTINUATION);
		assertFalse("That is not a good segment", nopeShouldBeFalse);
	}
	
	@Test
	public void testCleanSegmentBreaks() {
		mpp.setDelimitersFromMessage(WIERD_LINE_BREAK);
		String[] basicWierdness ={PV1_WIERD_START, PV1_WIERD_CONTINUATION};
		String[] intoOne = mpp.cleanSegmentBreaks(basicWierdness);
		assertEquals("Should be down to one String", 1, intoOne.length);
		assertEquals("Should be down to one String", PV1_WIERD_START + PV1_WIERD_CONTINUATION, intoOne[0]);
		
		String[] someWierdness = mpp.splitSegmentsByLineBreak(WIERD_LINE_BREAK);
		assertEquals("Starting with 9 segments", 9, someWierdness.length);
		
		String[] gettingBetter = mpp.cleanSegmentBreaks(someWierdness);
		
		if (gettingBetter.length != 8) {
			for ( String s : gettingBetter) {
				System.out.println(s);
			}
		}

		assertEquals("Should be down to 8 segments after the cleansing", 8, gettingBetter.length);
		
	}
	
	@Test
	public void testGetSegmentName() {
		String[] segmentArray = mpp.splitSegments(EXAMPLE_VXU);
		
		String setNm = mpp.getSegmentName(segmentArray[0]);
		assertEquals("MSH name",  "MSH", setNm);

		setNm = mpp.getSegmentName(segmentArray[1]);
		assertEquals("PID name",  "PID", setNm);
		
		setNm = mpp.getSegmentName(segmentArray[2]);
		assertEquals("NK1 name",  "NK1", setNm);

		setNm = mpp.getSegmentName(segmentArray[4]);
		assertEquals("RXA name",  "RXA", setNm);
		
	}
	
	@Test
	public void testSplitFields() {
		String[] segmentArray = mpp.splitSegments(EXAMPLE_VXU);
		
		String[] fieldArray = mpp.splitFields(segmentArray[SEG_LIST.indexOf("MSH")]);
		
		assertEquals("MSH-0", "MSH", fieldArray[0]);
		assertEquals("MSH-1", "|", fieldArray[1]);
		assertEquals("MSH-2", "^~\\&", fieldArray[2]);
		assertEquals("MSH-3", "ECW", fieldArray[3]);
		assertEquals("MSH-4", "1337-44-01", fieldArray[4]);
		assertEquals("MSH-5", "MCIR", fieldArray[5]);
		assertEquals("MSH-6", "MDCH", fieldArray[6]);
		assertEquals("MSH-7", "20140619191115", fieldArray[7]);
		assertEquals("MSH-8", "", fieldArray[8]);
		assertEquals("MSH-9", "VXU^V04", fieldArray[9]);
		assertEquals("MSH-10", "61731", fieldArray[10]);
		assertEquals("MSH-11", "P", fieldArray[11]);
		assertEquals("MSH-12", "2.3.1", fieldArray[12]);
		assertEquals("MSH-13", "", fieldArray[13]);
		assertEquals("MSH-14", "", fieldArray[14]);
		assertEquals("MSH-15", "AL", fieldArray[15]);
		
		fieldArray = mpp.splitFields(segmentArray[SEG_LIST.indexOf("PID")]);
		assertEquals("PID-0", "PID", fieldArray[0]);
		assertEquals("PID-1", "", fieldArray[1]);
		assertEquals("PID-2", "", fieldArray[2]);
		assertEquals("PID-3", "23456^^^^MR", fieldArray[3]);
		assertEquals("PID-4", "", fieldArray[4]);
		assertEquals("PID-5", "FIRSTNAME^LASTNAME^^^^X~COOLNAME^AWESOMENAME2", fieldArray[5]);
		assertEquals("PID-6", "", fieldArray[6]);
		assertEquals("PID-7", "20120604", fieldArray[7]);
		assertEquals("PID-8", "M", fieldArray[8]);
		assertEquals("PID-9", "", fieldArray[9]);
		assertEquals("PID-10", "", fieldArray[10]);
		assertEquals("PID-11", "100 Main^^Lansing^MI^48912 1330^^", fieldArray[11]);
		assertEquals("PID-12", "", fieldArray[12]);
		assertEquals("PID-13", "5175555555", fieldArray[13]);
		
		fieldArray = mpp.splitFields(segmentArray[SEG_LIST.indexOf("NK1")]);
		assertEquals("NK1-0", "NK1", fieldArray[0]);
		assertEquals("NK1-1", "", fieldArray[1]);
		assertEquals("NK1-2", "NKLast^NKFirst", fieldArray[2]);
		assertEquals("NK1-3", "^3^HL70063", fieldArray[3]);
		
		fieldArray = mpp.splitFields(segmentArray[SEG_LIST.indexOf("ORC")]);
		assertEquals("ORC-0", "ORC", fieldArray[0]);
		
		fieldArray = mpp.splitFields(segmentArray[SEG_LIST.indexOf("RXA")]);
		assertEquals("RXA-0", "RXA", fieldArray[0]);
		assertEquals("RXA-1", "0", fieldArray[1]);
		assertEquals("RXA-2", "", fieldArray[2]);
		assertEquals("RXA-3", "20140614", fieldArray[3]);
		assertEquals("RXA-4", "20140614", fieldArray[4]);
		assertEquals("RXA-5", "83^Hepatitis A ped/adol^CVX^90633^Hepatitis A ped/adol^CPT", fieldArray[5]);
		assertEquals("RXA-6", "", fieldArray[6]);
		assertEquals("RXA-7", "", fieldArray[7]);
		assertEquals("RXA-8", "", fieldArray[8]);
		assertEquals("RXA-9", "00^New immunization record^NIP001", fieldArray[9]);
		assertEquals("RXA-10", "Luginbill, David", fieldArray[10]);
		assertEquals("RXA-11", "1337-44-01^Sparrow Pediatrics (Lansing)", fieldArray[11]);
		assertEquals("RXA-12", "", fieldArray[12]);
		assertEquals("RXA-13", "", fieldArray[13]);
		assertEquals("RXA-14", "", fieldArray[14]);
		assertEquals("RXA-15", "J005080", fieldArray[15]);
		assertEquals("RXA-16", "", fieldArray[16]);
		assertEquals("RXA-17", "MSD^Merck &Co.^MVX", fieldArray[17]);
		assertEquals("RXA-18", "", fieldArray[18]);
		assertEquals("RXA-19", "", fieldArray[19]);
		assertEquals("RXA-20", "", fieldArray[20]);
		assertEquals("RXA-21", "A", fieldArray[21]);
		assertEquals("RXA-22", "20140614", fieldArray[22]);
		
		fieldArray = mpp.splitFields(segmentArray[SEG_LIST.indexOf("RXR")]);
		assertEquals("RXR-0", "RXR", fieldArray[0]);
		assertEquals("RXR-1", "IM^Intramuscular^HL70162", fieldArray[1]);
		assertEquals("RXR-2", "RT^Right Thigh^HL70163", fieldArray[2]);
		
		
		fieldArray = mpp.splitFields(segmentArray[SEG_LIST.indexOf("OBX")]);
		assertEquals("OBX-0", "OBX", fieldArray[0]);
		assertEquals("OBX-1", "2", fieldArray[1]);
		assertEquals("OBX-2", "CE", fieldArray[2]);
		assertEquals("OBX-3", "64994-7^Vaccine funding program eligibility category^LN", fieldArray[3]);
		assertEquals("OBX-4", "", fieldArray[4]);
		assertEquals("OBX-5", "V02^VFC Eligible - Medicaid/Medicare^HL70064", fieldArray[5]);
		assertEquals("OBX-6", "", fieldArray[6]);
		assertEquals("OBX-7", "", fieldArray[7]);
		assertEquals("OBX-8", "", fieldArray[8]);
		assertEquals("OBX-9", "", fieldArray[9]);
		assertEquals("OBX-10", "", fieldArray[10]);
		assertEquals("OBX-11", "F", fieldArray[11]);
		assertEquals("OBX-12", "", fieldArray[12]);
		assertEquals("OBX-13", "", fieldArray[13]);
		assertEquals("OBX-14", "20140614", fieldArray[14]);
		
//		//For generating tests, use this, and then validate the values with the message you're testing. 
//		System.out.println("Segment array length: " + segmentArray.length);
//		for (String seg : segmentArray) {
//			String[] fieldArrayX = mpp.splitFields(seg);
//			String segNm = mpp.getSegmentName(seg);
//			for (int x = 0; x < fieldArrayX.length; x++) {
//				System.out.println("assertEquals(\"" + segNm + "-" + x + "\", \"" + fieldArrayX[x] + "\", fieldArray["+ x + "]);");
//			}
//		}
		
	}
	
	@Test
	public void testSplitParts() {
		String[] subs = mpp.splitParts(1, "VXU^V04");
		assertEquals("Should be split in two", 2, subs.length);
		
		String[] components = mpp.splitParts(1,"V02^VFC Eligible - Medicaid/Medicare^HL70064");
		assertEquals("Component 1", "V02", components[0]);
		assertEquals("Component 2", "VFC Eligible - Medicaid/Medicare", components[1]);
		assertEquals("Component 3", "HL70064", components[2]);

		//This is analgous to something like the RXA-11 component 2, which is an HD datatype and has three possible sub-components. 
		String[] subComponents = mpp.splitParts(2,"Bedford Medical Arts&1300-87-99");
		assertEquals("Component 1", "Bedford Medical Arts", subComponents[0]);
		assertEquals("Component 2", "1300-87-99", subComponents[1]);
		
		//This is from NK1-33 (CX) 4 assigning authority (HD).  The first position of an HD element is Namespace ID, and the second is Universal ID. 
		subComponents = mpp.splitParts(2,"&EHR");
		assertEquals("Component 1", "", subComponents[0]);
		assertEquals("Component 2", "EHR", subComponents[1]);
//		you would represent this with NK1-33-4-2...

		
	}
	
	@Test
	public void testSplitRepetitions() {
		//This is a CX datatype repeated.  Note:  A repetition could be present at any level of sub-component. 
		//This is from a PID-3 field:
		String[] repetitions = mpp.splitRepetitions("M7777^^^^EPI~7777^^^MMPC^MMPC~7777.6^^^MMPC^MMPCOLD~543210^^^SHCPI^CPI~543210^^^SHMRN^MR");
		assertEquals("repetition 1", "M7777^^^^EPI", repetitions[0]);
		assertEquals("repetition 2", "7777^^^MMPC^MMPC", repetitions[1]);
		assertEquals("repetition 3", "7777.6^^^MMPC^MMPCOLD", repetitions[2]);
		assertEquals("repetition 4", "543210^^^SHCPI^CPI", repetitions[3]);
		assertEquals("repetition 5", "543210^^^SHMRN^MR", repetitions[4]);

		//This condition should never happen because you cannot have a repetition separator for a sub-component.  The only repetitions should be at the field level, not component level or sub-component.  
		repetitions = mpp.splitRepetitions("&EHR~234&MHA~&EMR");
		assertEquals("repetition 1", "&EHR", repetitions[0]);
		assertEquals("repetition 2", "234&MHA", repetitions[1]);
		assertEquals("repetition 3", "&EMR", repetitions[2]);
	}
	@Test
	public void testRepetitions() {
		String fieldWithRepetitions = "WEB^SPUD~WEB^CHAROLOTTE";
		HL7MessagePart loc = new HL7MessagePart(1, "PID", 5);
		loc.setValue(fieldWithRepetitions);
		List<HL7MessagePart> map = mpp.mapField(loc);
		
		assertEquals("Should get four total locations including two repetitions", 4, map.size());
		
		int numRep1Locs=0;
		int numRep2Locs=0;
		
		for (HL7MessagePart hl7 : map) {
			numRep1Locs += hl7.getFieldRepetition() == 1 ? 1 : 0;
			numRep2Locs += hl7.getFieldRepetition() == 2 ? 1 : 0;
		}
		assertEquals("Should have two locations for repetition 1", 2, numRep1Locs);
		assertEquals("Should have two locations for repetition 2", 2, numRep2Locs);
	}
	
	@Test
	public void generateInserts() {
		
		List<HL7MessagePart> map = mpp.getMessagePartList(EXAMPLE_VXU);
		
		assertNotNull("results should not be null", map);
//		String insert = "insert into mcir_trunk.message_eav (MESSAGE_ID, LOCATION_CD, SEGMENT_INDEX, FIELD_REPETITION_NUM, VALUE_TEXT) values (%s,'%s', %s,%s,'%s');";		
		int fieldParts = map.size();
//		System.out.println("FieldParts#" + fieldParts);
//		System.out.println("set scan off;");
//		for (MessageEAV loc : map) {
//			String generatedInsert =
//					String.format(insert, 1, loc.getLocationCd(), loc.getSegmentIndex() , loc.getFieldRepetition(), loc.getValue()); 
//			System.out.println(generatedInsert);
//		}
		
		if (map.size() != 179) {
			System.out.println(map);
		}
		
		assertEquals("Map size should be 179.  There is an extra & in a field that shoudl be tagged as simple. That segment is repeated three times.  So if it's not coming through right, you'll see 180...", 179, fieldParts);
	}
	
	//start with flat notation
	@Test
	public void testMapSegment() {
		String segment = "MSH|^~\\&|ECW|1337-44-01|MCIR|MDCH|20140619191115||VXU^V04|61731|P|2.3.1|||AL";
		List<HL7MessagePart> dataList = mpp.mapSegment(segment, 0, 1);
		assertEquals("Message Should have 17 fields including the segment name and field separator", 17, dataList.size());//since there's no repetitions, it's just a straight calculation.
	}
	
	@Test
	public void testMapMessage() {
		long start = Calendar.getInstance().getTimeInMillis();
		List<HL7MessagePart> map = mpp.getMessagePartList(EXAMPLE_VXU);
		long stop = Calendar.getInstance().getTimeInMillis();
		
		assertNotNull("results should not be null", map);
		
//		int fieldParts = map.size();
//		System.out.println("FieldParts#" + fieldParts);
//		for (MessageEAV loc : map) {
//			System.out.println("(" + loc.getSegmentIndex() + ") " + loc.getLocationCd() + " ~ " + loc.getFieldRepetition() + " > " + loc.getValue());
//		}
//		
//		System.out.println("Elapsed time for mapping a message: " + (stop - start) + " milliseconds");
	}

	@Test
	public void testHasSeparators() {
		String field = "This has no sub components";
		boolean hasSeparators = mpp.hasSeparators(field);
		assertEquals(field, false, hasSeparators);
		
		field = "This has~sub components";
		hasSeparators = mpp.hasSeparators(field);
		assertEquals(field, true, hasSeparators);
		
		field = "This has^sub components";
		hasSeparators = mpp.hasSeparators(field);
		assertEquals(field, true, hasSeparators);

		field = "This has&sub components";
		hasSeparators = mpp.hasSeparators(field);
		assertEquals(field, true, hasSeparators);

		field = "M7777^^^^EPI~7777^^^MMPC^MMPC~7777.6^^^MMPC^MMPCOLD~543210^^^SHCPI^CPI~543210^^^SHMRN^MR";
		hasSeparators = mpp.hasSeparators(field);
		assertEquals(field, true, hasSeparators);
		
		field = "VXU^V04";
		hasSeparators = mpp.hasSeparators(field);
		assertEquals(field, true, hasSeparators);
		
		field = "hello^this^is^complex";
		hasSeparators = mpp.hasSeparators(field);
		assertEquals(field, true, hasSeparators);
				
	}
	
	@Test
	public void testGetComplexityFromValue() {
		String fieldVal = "This has^sub components";
		FieldComplexity complexity = mpp.determineComplexityFromValue(fieldVal);
		assertEquals(fieldVal, FieldComplexity.COMPLEX, complexity);
		
		fieldVal = "V02^VFC Eligible - Medicaid/Medicare^HL70064";
		complexity = mpp.determineComplexityFromValue(fieldVal);
		assertEquals(fieldVal, FieldComplexity.COMPLEX, complexity);
		
		fieldVal = "This is not complex at all!";
		complexity = mpp.determineComplexityFromValue(fieldVal);
		assertEquals(fieldVal, FieldComplexity.SIMPLE, complexity);
		
	}
	
	@Test
	public void testLocatorNameLength() {
		//There shouldn't be any locator longer than 20: 
		List<HL7MessagePart> list = mpp.getMessagePartList(EXAMPLE_VXU);
		for (HL7MessagePart part : list) {
			assertTrue("no loc code should be longer than 20.  This one:[" + part.getLocationCd() + "]", 20 > part.getLocationCd().length());
		}
	}

	@Test
	public void testSegSeqIndex() {
		List<HL7MessagePart> list = mpp.getMessagePartList(EXAMPLE_VXU);
		for (HL7MessagePart p : list) {
			String seg = p.getLocationCd().substring(0,3);
			switch (seg) {
				case "MSH":
					assertSeqIdx(1,1,p);
					break;
				case "PID":
					assertSeqIdx(2,1,p);
					break;
				case "NK1":
					assertSeqIdx(3,1,p);
					break;
        case "RXA":
          assertTrue("RXA idx should be one of 4,5,7 " + p, Arrays.asList(5,6,8).contains(p.getSegmentIndex()));
          assertTrue("RXA seq should be one of 1,2,3 " + p, Arrays.asList(1,2,3).contains(p.getSegmentSeq()));
          break;
        case "OBX":
          assertSeqIdx(9,1,p);
          break;
        case "RXR":
          assertSeqIdx(7,1,p);
          break;
			}
		}
	}

	public void assertSeqIdx(int line, int seq, HL7MessagePart p) {
		assertEquals("Expected seg idx for: " + p, line, p.getSegmentIndex());
		assertEquals("Expected seg seq for: " + p, seq, p.getSegmentSeq());
	}
}
