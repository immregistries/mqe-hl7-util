package org.immregistries.dqa.hl7util.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.hl7util.parser.MessageParser;
import org.immregistries.dqa.hl7util.parser.MessageParserHL7;
import org.junit.Before;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = ParserAppConfig.class)

public class HL7MessageMapTest {

//	@Autowired
	private MessageParser mpp = new MessageParserHL7();
	 
	private static final String MSH = "MSH|^~\\&|ECW|1337-44-01|MCIR|MDCH|20140619191115||VXU^V04|61731|P|2.3.1|||AL\r";
	private static final String PID =  "PID|||23456^^^^MR||FIRSTNAME^LASTNAME^^^^X~COOLNAME^AWESOMENAME2||20120604|M|||100 Main^^Lansing^MI^48912 1330^^~^^^^^US^BDL^^123456||5175555555\r";
	private static final String NK1 =  "NK1||NKLast^NKFirst|MTH^3^HL70063\r";
	private static final String ORC =  "ORC\r";
	private static final String RXA =  "RXA|0||20140614|20140614|83^Hepatitis A ped/adol^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
	private static final String RXA2 = "RXA|0|X|20140615|20140615|83^Awesome Immunization^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
	private static final String RXA3 = "RXA|0||20140616|20140616|83^IMPORTANT Immunization^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
	private static final String RXR =  "RXR|IM^Intramuscular^HL70162|RT^Right Thigh^HL70163\r";
	private static final String OBX =  "OBX|2|CE|64994-7^Vaccine funding program eligibility category^LN||V02^VFC Eligible - Medicaid/Medicare^HL70064||||||F|||20140614\r";

	private static final String EXAMPLE_VXU;
	
	private static final Map<String,String> SEG_MAP = new LinkedHashMap<String,String>();
	private static final List<String> SEG_LIST = new LinkedList<String>();
	static {
		
		SEG_MAP.put("MSH.1", MSH); //0
		SEG_MAP.put("PID.1", PID); //1
		SEG_MAP.put("NK1.1", NK1); //2
		SEG_MAP.put("ORC.1", ORC); //3
		SEG_MAP.put("RXA.1", RXA); //4
		SEG_MAP.put("OBX.1", OBX); //5
		SEG_MAP.put("ORC.2", ORC); //6
		SEG_MAP.put("RXA.2", RXA2);//7
		SEG_MAP.put("OBX.2", OBX); //8
		SEG_MAP.put("RXR.1", RXR); //9
		SEG_MAP.put("RXA.3", RXA3);//10
		SEG_MAP.put("OBX.3", OBX); //11
		
		StringBuffer sb = new StringBuffer();
		for (String seg : SEG_MAP.values()) {
			sb.append(seg);
		}
		
		for (String segName : SEG_MAP.keySet()) {
			SEG_LIST.add(segName.substring(0,3));
		}
		
		EXAMPLE_VXU = sb.toString();
	}

	public HL7MessageMap map;
	
	@Before
	public void setup() {
		map = mpp.getMessagePartMap(EXAMPLE_VXU);
	}
	
	@Test
	public void testFieldRepCounter() {
		//This should exist. 
		String field = "PID-5";
		int count = map.getFieldRepCountFor(field);
		assertEquals("should be 2", 2, count);
		
		field = "PID[1]-5";
		count = map.getFieldRepCountFor(field);
		assertEquals("should be 2", 2, count);
		
		//This doesn't exist.  Answer should be zero.
		field = "PID[2]-5";
		count = map.getFieldRepCountFor(field);
		assertEquals("should be 0", 0, count);
		
	}
	
	@Test
	public void testRandomThings() {
		String routeCode = map.getAtOrdinal("RXR-1", 1, 1);
		assertEquals("IM", routeCode);
	}

	@Test
	public void listToMap() {
//		This should return the first value at that address.  so it's implied that you want the first everything. This is a shortcut. 
		String msh3 = map.get("MSH-3");
		assertEquals("msh3", "ECW", msh3);
//		Should be the same as the first MSH, third field, first repetition, first component, first sub-component: 
		String msh_1_3_111 = map.get("MSH[0]-3~1-1-1");
		assertEquals("MSH[1]-3~1-1-1 should be the same as MSH-3", "ECW", msh_1_3_111);
//		Should be able to get a very specific one too... As exact as this: 
//		This would be the third RXA segment, third field, second component, sixth sub-component.   
		String rxa_3_3_226 = map.get("RXA[4]-3~2-2-6");
		assertEquals("RXA[4]-3~2-2-6 is the second repetition of field 3 in the third RXA... which is null, becuase repetition two doesn't exist.", null, rxa_3_3_226);
//		But how would i know there's a repetition of the field???  I don't know how.  
//		Maybe what I really want is to get a list of all the RXA[3]-3-2-6 values...  
//		on a CVX/CPT entry, you might want to pick out the CVX and only use that???
		
//		Should be able to give just enough to get what I want:
//		second RXA segment, third field, third component, and either the value, or the first sub-component, 
//		which are arguably the same thing.   
		String rxa254 = map.get("RXA[7]-5-4");
		assertEquals("rxa253", "90633", rxa254);
		
//		I also need to know how many segments there are...
		int rxaCnt = map.getSegmentCount("RXA");
		assertEquals("Should be three RXA's", 3, rxaCnt);
		
//		I want to know that so I can iterate over the segments properly...
//		So the root desire is to get a specific segment...  So I can get the specific RXA's stuff. 
		
//		So what I'm missing is a way to tell how many segments there are, and a way to index them
//		by the ordinal in the message reltive to that segment.  Third RXA instead of 8th segment...
//		Let's just make it happen		
		
//		Doesn't exist?  Should come back null. 
		
		System.out.println("PRINTING MAP");
		System.out.println(map.toString());
	}

	@Test
	public void testLocators() {
		
		String msh3 = map.fillInComponentAndSubComponent("MSH-3");
		assertEquals("msh3", "MSH-3-1-1", msh3);
		
		String msh31 = map.addFieldRepIfMIssing(msh3);
		assertEquals("msh3 plus field rep", "MSH-3~1-1-1", msh31);
		
		String msh301 = map.addFieldRepIfMIssing("MSH[0]-1");
		assertEquals("addFieldRepIfMIssing(\"MSH[0]-1\")", "MSH[0]-1~1", msh301);
		
		String msh311 = map.addSegmentIndexIfMissing(msh31);
		assertEquals("msh3 plus segIndex", "MSH[0]-3~1-1-1", msh311);
				
		String msh311x = map.makeLocatorString("MSH-3",  0,  1);
		assertEquals("msh3 both ways", msh311, msh311x);
		
		String rxa3  = map.addSegmentIndexIfMissing("RXA-3");
		assertEquals("RXA-3", "RXA[4]-3", rxa3);
		
	}
	
	@Test
	public void testGetFieldRep() {
		String locator = "MSH-3-1-1";
		int rep = map.getFieldRep(locator);
		assertEquals(locator, 1, rep);
		
		locator = "MSH-3~2-1-1";
		rep = map.getFieldRep(locator);
		assertEquals(locator, 2, rep);
		
		locator = "PID[2]-3~2-1-1";
		rep = map.getFieldRep(locator);
		assertEquals(locator, 2, rep);
		
		locator = "PID[2]-3-1-1";
		rep = map.getFieldRep(locator);
		assertEquals(locator, 1, rep);
		
		locator = "PID[2]-3";
		rep = map.getFieldRep(locator);
		assertEquals(locator, 1, rep);
		
		locator = "PID[2]";
		rep = map.getFieldRep(locator);
		assertEquals(locator, 1, rep);
	}
	
	@Test
	public void testGetFieldLocator() {
		String locator = "MSH-3-1-1";
		String loc = map.getFieldLocator(locator);
		assertEquals(locator, "MSH-3", loc);
		
		
		locator = "PID-3-1-1";
		loc = map.getFieldLocator(locator);
		assertEquals(locator, "PID-3", loc);
		
		locator = "PID[1]-1-1-1";
		loc = map.getFieldLocator(locator);
		assertEquals(locator, "PID[1]-1", loc);
		
		locator = "NK1-3";
		loc = map.getFieldLocator(locator);
		assertEquals(locator, "NK1-3", loc);
		
		locator = "NK1[2]-3~2-1-1";
		loc = map.getFieldLocator(locator);
		assertEquals(locator, "NK1[2]-3", loc);
	}
	
	@Test
	public void testGettingValues() {
		String msh = map.get("MSH");
		assertEquals("MSH", "MSH", msh);
		
		String msh1 = map.get("MSH[0]");
		assertEquals("MSH[0]", "MSH", msh1);
		
		String msh1x = map.getAtIndex("MSH", 0, 1);
		assertEquals("MSH", msh, msh1x);
						
		String rxa52 = map.get("RXA-5-2");
		assertEquals("RXA-5-2", "Hepatitis A ped/adol", rxa52);
		
		String rxa_2_52 = map.get("RXA[7]-5-2");
		assertEquals("RXA[7]-5-2", "Awesome Immunization", rxa_2_52);
		
		String rxa_2_52x = map.getAtIndex("RXA-5-2", 7, 1);
		assertEquals("rxa_2_52x",rxa_2_52x, rxa_2_52);
		
		String pid5_2_2 = map.get("PID-5~2-2"); 
		assertEquals("PID-5~2-2", "AWESOMENAME2", pid5_2_2);
		
		String pid5_2_2x = map.getAtIndex("PID-5-2", 1, 2); 
		assertEquals("pid5_2_2x", pid5_2_2, pid5_2_2x);
		
		String pid5_2_1 = map.get("PID-5~2-1"); 
		assertEquals("PID-5~2-1", "COOLNAME", pid5_2_1);
		
		String pid55 = map.get("PID-55");
		assertEquals("undefined location PID-55", null, pid55);
	}
	
	@Test
	public void testSegmentNameParser() {
		String seg1 = "MSH-1";
		String seg2 = "RXA[2]-5-2";
		String seg3 = "RXA";
		
		assertEquals("MSH-1", "MSH", map.getSegmentNameFromLocator(seg1));
		assertEquals("RXA[2]-5-2", "RXA", map.getSegmentNameFromLocator(seg2));
		assertEquals("RXA", "RXA", map.getSegmentNameFromLocator(seg3));
		
	}
	
	public void testSegmentCount() {
		int rxaCnt = map.getSegmentCount("RXA");
		assertEquals("Should be three RXA's", 3, rxaCnt);
		
		int orcCnt = map.getSegmentCount("ORC");
		assertEquals("Should be one ORC", 1, orcCnt);
		
		int mshCnt = map.getSegmentCount("MSH");
		assertEquals("Should be one MSH", 1, mshCnt);
	}
	
	@Test
	public void testGetSegmentIndexFromLocation() {
		String loc = "RXR[1]-1~1-1-1";
		int idx = map.getSegmentIndexFromLocation(loc);
		assertEquals(loc, 1, idx);
	}
	

	@Test
	public void testCrazyAmpersand() {
//		RXA-16?
		String rxa16 = map.get("RXA-17-2");
		assertEquals("RXA-17-2 - MSD^Merck &Co.^MVX", "Merck &Co.", rxa16);
		
	}
	
	@Test
	public void iterateThroughMessage() {
		//can you go through the message in general???
		//I would need to be able to just get the absolute index of each segment, in order... or really just the segment names in order. 
		//So really, just get the segment names in order that they appeared in the original message. 
		List<String> list = map.getMessageSegments();
		
		assertEquals("The list should be the same length as the original." , SEG_LIST.size(), list != null ? list.size() : 0);
		assertEquals("the lists should match", SEG_LIST, list);
		
//		int idx = 1;
//		for (String seg : list) {
//			
//		}
	}
	
	@Test
	public void testAbsoluteGet() {
		String value = map.getAtIndex("OBX-5", 5, 1);
		assertEquals("vfc obx", "V02", value );
	}

	@Test
	public void testGetOrderedMessageSegments() {
		List<String> segs = map.getMessageSegments();
		
		//The arraylist is zero based...  hmm...  maybe
		//I should keep everything zero based. 
		assertEquals("4", "RXA", segs.get(4));
		assertEquals("7", "RXA", segs.get(7));
		assertEquals("10", "RXA", segs.get(10));
		
		assertEquals("0", "MSH", segs.get(0));
		
		assertEquals("3", "ORC", segs.get(3));
		assertEquals("6", "ORC", segs.get(6));
		
		for (String seg : segs) {
			System.out.println("+" + seg);
		}
	}
	
	@Test
	public void testRelativeAndAbsoluteIndexing() {
		int absolute = 4;//Absolute indexes are zero based. 
		int relative = 1;
		//should get 1 as the relative index of absolute index 5. 
		int calculateRelative = map.getSegmentOrdinalFromAbsoluteIndex(absolute);
		
		//should get 4 as the absolute index of relative index 1. 
		int calculateAbsolute  = map.getAbsoluteIndexForLocationOrdinal("RXA", relative);
		
		assertEquals("Relative from absolute", relative, calculateRelative);
		assertEquals("Absolute from relative", absolute, calculateAbsolute);
	}
	
	
	@Test
	public void testAbsoluteSegmentIndex() {
		//First test getting it from the segment. 
		int absolute = map.getAbsoluteIndexForSegment("OBX", 3);
		assertEquals("Third OBX is in absolute index of...", 11, absolute);

		absolute = map.getAbsoluteIndexForSegment("OBX", 2);
		assertEquals("second OBX is in absolute index of...", 8, absolute);
		
		absolute = map.getAbsoluteIndexForSegment("RXA",  1);
		assertEquals("first RXA..", 4, absolute);
		
		//From locator. 
		absolute = map.getAbsoluteIndexForLocationOrdinal("OBX-3", 3);
		assertEquals("Third OBX is in absolute index of...", 11, absolute);

		absolute = map.getAbsoluteIndexForLocationOrdinal("OBX-3", 2);
		assertEquals("second OBX is in absolute index of...", 8, absolute);
		
		absolute = map.getAbsoluteIndexForLocationOrdinal("RXA-5",  1);
		absolute = map.getAbsoluteIndexForSegment("RXA",  1);
			
	}
	
	@Test
	public void testFindAllSegmentRepsWithValues() {
		//Only the second RXA has an RXA-2 value. 
		List<Integer> resultList = map.findAllIndexesForSegmentWithValues(new String[]{"X",  "Y"}, "RXA-2", 1);
		assertEquals("Should have one entry in the list", 1, resultList.size());
		System.out.println("resultList: " + resultList);
		assertTrue("Indexes should hold the value 2. ", resultList.contains(new Integer(2)));

		resultList = map.findAllIndexesForSegmentWithValues(new String[]{"64994-7"}, "OBX-3", 1);
		assertEquals("OBX-3 64994-7", 3, resultList.size());
		
		resultList.removeAll(Arrays.asList(new Integer[] {1,2,3}));
		assertEquals("the result list should have 1,2,3", 0, resultList.size());

		resultList = map.findAllSegmentRepsWithValuesWithinRange(new String[] {"X"},  "RXA-2",  3, 3, 1);
		assertEquals("shouldn't find any", 0, resultList.size());
		
		resultList = map.findAllSegmentRepsWithValuesWithinRange(new String[] {"X"},  "RXA-2",  2, 3, 1);
		assertTrue("Should have 2", resultList.contains(new Integer(2)));
		assertEquals("Should only have one entry", 1, resultList.size());
		
		resultList = map.findAllSegmentRepsWithValuesWithinRange(new String[] {"X"},  "RXA-2",  1, 2, 1);
		assertTrue("Should have 2 (starting with 1)", resultList.contains(new Integer(2)));
		assertEquals("Should only have one entry (starting with 1)", 1, resultList.size());
		
		resultList = map.findAllSegmentRepsWithValuesWithinRange(new String[] {"X"},  "RXA-2",  2, 4, 1);
		assertTrue("Should have 2 (2-4)", resultList.contains(new Integer(2)));
		assertEquals("Should only have one entry (2-4)", 1, resultList.size());
		
	}
	
	@Test
	public void testGetSegmentName() {
		String seg = map.getSegmentAtAbsoluteIndex(5);
		assertEquals("rxa at 5", "RXA", seg);
		
		seg = map.getSegmentAtAbsoluteIndex(6);
		assertEquals("OBX at 6", "OBX", seg);
		
		seg = map.getSegmentAtAbsoluteIndex(1);
		assertEquals("MSH at 1", "MSH", seg);
		
		seg = map.getSegmentAtAbsoluteIndex(3);
		assertEquals("Nk1 at 3", "NK1", seg);
	}
	
	@Test
	public void testGetNextVaccinationStartingPoint() {
		int start = map.getNextImmunizationStartingIndex(0);
		assertEquals("First", 3, start);
		
		int next = map.getNextImmunizationStartingIndex(start);
		assertEquals("second", 6, next);
		
		int third = map.getNextImmunizationStartingIndex(next);
		assertEquals("third", 10 , third);
		
		int fourth = map.getNextImmunizationStartingIndex(third);
		assertEquals("fourth", 12 , fourth);
		
	}
	
	@Test
	public void testFindFieldRepWithValue() {
		

		String loc = "PID-5-1";
		String expected = "FIRSTNAME";
		int rep = map.findFieldRepWithValue(expected, loc, 1);
		assertEquals(loc + expected, 1, rep);
		
		loc = "RXA-5-3";
		expected = "CVX";
		rep = map.findFieldRepWithValue(expected, loc, 1);
		assertEquals(loc + expected, 1, rep);

		loc = "PID-11-7";
		expected = "BDL";
		rep = map.findFieldRepWithValue(expected, loc, 1);
		assertEquals(loc + expected, 2, rep);
		
		loc = "PID-5-1";
		expected = "COOLNAME";
		rep = map.findFieldRepWithValue(expected, loc, 1);
		assertEquals(loc + expected, 2, rep);
		
		loc = "PID-5-1";
		expected = "NOTAVALUE";
		rep = map.findFieldRepWithValue(expected, loc, 1);
		assertEquals(loc + expected, 0, rep);
		
		loc = "NOT A LOC";
		expected = "NOTAVALUE";
		rep = map.findFieldRepWithValue(expected, loc, 1);
		assertEquals(loc + expected, 0, rep);
		
		loc = null;
		expected = "NOTAVALUE";
		rep = map.findFieldRepWithValue(expected, loc, 1);
		assertEquals(loc + expected, 0, rep);
		
		loc = "PID-5-1";
		expected = null;
		rep = map.findFieldRepWithValue(expected, loc, 1);
		assertEquals(loc + expected, 0, rep);
		
	}
	
	@Test
	public void testIndexTheFieldRep() {
		
		Map<String, Integer> fieldRepMap = map.fieldRepetitions;
		
		String field = "PID[1]-5";
		Integer reps = fieldRepMap.get(field);
		assertEquals(field, new Integer(2), reps);
		
		field = "RXR[9]-1";
		reps = fieldRepMap.get(field);
		assertEquals(field, new Integer(1), reps);
		
		field = "RXR[99]-1";
		reps = fieldRepMap.get(field);
		assertEquals(field, null, reps);
	}
	
	@Test
	public void testGetFieldRepFromLoc() {
		String loc = "RXA[1]-5~3-1-1";
		int rep = map.getFieldRep(loc);
		assertEquals(loc, 3, rep);
		
		loc = "RXA[1]-5~5-1-1";
		rep = map.getFieldRep(loc);
		assertEquals(loc, 5, rep);
		
		loc = "RXA-5~9-1-1";
		rep = map.getFieldRep(loc);
		assertEquals(loc, 9, rep);

		loc = "RXA-5~0-1-1";
		rep = map.getFieldRep(loc);
		assertEquals(loc, 0, rep);
		
		loc = "RXA-5";
		rep = map.getFieldRep(loc);
		assertEquals(loc, 1, rep);
		
		loc = "RXA";
		rep = map.getFieldRep(loc);
		assertEquals(loc, 1, rep);
		
	}
	
	@Test
	public void testGetFieldLocFromLoc() {
		String loc = "RXA[1]-5~3-1-1";
		String fieldLoc = map.getFieldLocator(loc);
		assertEquals(loc, "RXA[1]-5", fieldLoc);
		
		loc = "RXA[1]-5~5-1-1";
		fieldLoc = map.getFieldLocator(loc);
		assertEquals(loc, "RXA[1]-5", fieldLoc);
		
		loc = "RXA-5~9-1-1";
		fieldLoc = map.getFieldLocator(loc);
		assertEquals(loc, "RXA-5", fieldLoc);

		loc = "RXA-5~0-1-1";
		fieldLoc = map.getFieldLocator(loc);
		assertEquals(loc, "RXA-5", fieldLoc);
		
		loc = "RXA-5";
		fieldLoc = map.getFieldLocator(loc);
		assertEquals(loc, loc, fieldLoc);
		
		loc = "RXA";
		fieldLoc = map.getFieldLocator(loc);
		assertEquals(loc, "RXA", fieldLoc);
		
		loc = null;
		fieldLoc = map.getFieldLocator(loc);
		assertEquals(loc, null, fieldLoc);
		
	}
}
