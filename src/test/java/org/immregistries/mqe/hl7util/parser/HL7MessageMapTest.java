package org.immregistries.mqe.hl7util.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.immregistries.mqe.core.TestMessageGenerator;
import org.immregistries.mqe.hl7util.model.Hl7Location;
import org.junit.Before;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = ParserAppConfig.class)

public class HL7MessageMapTest {

  //	@Autowired
  private MessageParser mpp = new MessageParserHL7();

  private static final String MSH = "MSH|^~\\&|ECW|1337-44-01|MCIR|MDCH|20140619191115||VXU^V04|61731|P|2.3.1|||AL\r";
  private static final String PID = "PID|||23456^^^^MR||FIRSTNAME^LASTNAME^^^^X~COOLNAME^AWESOMENAME2||20120604|M|||100 Main^^Lansing^MI^48912 1330^^~^^^^^US^BDL^^123456||5175555555\r";
  private static final String NK1 = "NK1||NKLast^NKFirst|MTH^3^HL70063\r";
  private static final String ORC = "ORC\r";
  private static final String RXA = "RXA|0||20140614|20140614|83^Hepatitis A ped/adol^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
  private static final String RXA2 = "RXA|0|X|20140615|20140615|83^Awesome Immunization^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
  private static final String RXA3 = "RXA|0||20140616|20140616|83^IMPORTANT Immunization^CVX^90633^Hepatitis A ped/adol^CPT||||00^New immunization record^NIP001|Luginbill, David|1337-44-01^Sparrow Pediatrics (Lansing)||||J005080||MSD^Merck &Co.^MVX||||A|20140614\r";
  private static final String RXR = "RXR|IM^Intramuscular^HL70162|RT^Right Thigh^HL70163\r";
  private static final String OBX = "OBX|2|CE|64994-7^Vaccine funding program eligibility category^LN||V02^VFC Eligible - Medicaid/Medicare^HL70064||||||F|||20140614\r";

  private static final String EXAMPLE_VXU;

  private static final Map<String, String> SEG_MAP = new LinkedHashMap<String, String>();
  private static final List<String> SEG_LIST = new LinkedList<String>();

  static {

    SEG_MAP.put("MSH-1", MSH); //1
    SEG_MAP.put("PID-1", PID); //2
    SEG_MAP.put("NK1-1", NK1); //3
    SEG_MAP.put("ORC-1", ORC); //4
    SEG_MAP.put("RXA-1", RXA); //5
    SEG_MAP.put("OBX-1", OBX); //6
    SEG_MAP.put("ORC-2", ORC); //7
    SEG_MAP.put("RXA-2", RXA2);//8
    SEG_MAP.put("OBX-2", OBX); //9
    SEG_MAP.put("RXR-1", RXR); //10
    SEG_MAP.put("RXA-3", RXA3);//11
    SEG_MAP.put("OBX-3", OBX); //12

    StringBuffer sb = new StringBuffer();
    for (String seg : SEG_MAP.values()) {
      sb.append(seg);
    }

    for (String segName : SEG_MAP.keySet()) {
      SEG_LIST.add(segName.substring(0, 3));
    }

    EXAMPLE_VXU = sb.toString();
  }

  private static final String IMMUNITY_MSG =
            "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|20120701082240-0500||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^Jr^^^L~S^M^A^^^^A|Lam^Morgan^^^^^M|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^Apt 2^Warwick^MA^02452^USA^L^^MA0001~123 E Main Street^^Anytown^AZ^85203^MEX^M~^^^^^^BDL^^TY8888||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC|Hospital|Y|2||||20161010|Y\r"
          + "PD1|||Primary Facility^^^^^PF^^^^567||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^MD\r"
          + "RXA|0|1|20120814|20120815|33332-0010-01^Influenza, seasonal, injectable, preservative free^NDC|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1^^^^PRN|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"
          + "ORC|RE||IZ-783275^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^MD\r"
          + "RXA|0|1|20120814||03^MMR^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||01^^NIP001|||||||||||CP|A\r"
          + "ORC|RE||IZ-783276^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^MD\r"
          + "RXA|0|1|20120814||33332-0010-01^Influenza, seasonal, injectable, preservative free^NDC^09^^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1^^^^PRN|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"
          + "ORC|RE||IZ-783277^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^MD\r"
          + "RXA|0|1|20120814||09^^CVX^33332-0010-01^Influenza, seasonal, injectable, preservative free^NDC|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1^^^^PRN|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  private static final String EXTRA_PIPES_MSG =
      "MSH|^~\\&|Test EHR Application|||||";//pipes in MSH-5+

  @Before
  public void setup() {

  }

  @Test
  public void testExtraPipes() {
    //we should have empty space values for fields with nothing in them... I think...
    HL7MessageMap xtrPipes = mpp.getMessagePartMap(EXTRA_PIPES_MSG);
    String expectedBlank = xtrPipes.getValue("MSH-5");
    assertEquals("Should not have something for MSH-5", null, expectedBlank);
  }

  @Test
  public void testFieldRepCounter() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    //This should exist.
    Hl7Location loc = new Hl7Location( "PID-5");
    int count = map.getFieldRepCountFor(loc);
    assertEquals("should be 2", 2, count);

    loc = new Hl7Location( "PID[1]-5");
    count = map.getFieldRepCountFor(loc);
    assertEquals("should be 2", 2, count);

    //This doesn't exist.  Answer should be zero.
    loc = new Hl7Location( "PID[2]-5");
    count = map.getFieldRepCountFor(loc);
    assertEquals("should be 0", 0, count);

  }

  @Test
  public void listToMap() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
//		This should return the first value at that address.  so it's implied that you want the first everything. This is a shortcut.
    String msh3 = map.getValue("MSH-3");
    assertEquals("msh3", "ECW", msh3);
//		Should be the same as the first MSH, third field, first repetition, first component, first sub-component: 
    String msh_1_3_111 = map.getValue("MSH[1]-3[1].1.1");
    assertEquals("MSH[1]-3[1].1.1 should be the same as MSH-3", "ECW", msh_1_3_111);
//		Should be able to get a very specific one too... As exact as this: 
//		This would be the third RXA segment, third field, second component, sixth sub-component.   
    String rxa_3_3_226 = map.getValue("RXA[3]-3[2].2.6");
    assertEquals(
        "RXA[3]-3[2].2.6 is the second repetition of field 3 in the third RXA... which is null, becuase repetition two doesn't exist.",
        null, rxa_3_3_226);
//		But how would i know there's a repetition of the field???  I don't know how.  
//		Maybe what I really want is to get a list of all the RXA[3].3.2-6 values...  
//		on a CVX/CPT entry, you might want to pick out the CVX and only use that???

//		Should be able to give just enough to get what I want:
//		second RXA segment, third field, third component, and either the value, or the first sub-component, 
//		which are arguably the same thing.   
    String rxa254 = map.getValue("RXA[2]-5.4");
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
  public void testGettingValues() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    String msh = map.getValue("MSH");
    assertEquals("MSH", "MSH", msh);

    String msh1 = map.getValue("MSH[1]");
    assertEquals("MSH[1]", "MSH", msh1);

    String msh1x = map.getValue("MSH", 1, 1);
    assertEquals("MSH", msh, msh1x);

    String rxa52 = map.getValue("RXA-5.2");
    assertEquals("RXA-5.2", "Hepatitis A ped/adol", rxa52);

    String rxa_2_52 = map.getValue("RXA[2]-5.2");
    assertEquals("RXA[2]-5.2", "Awesome Immunization", rxa_2_52);

    String rxa_2_52x = map.getValue("RXA-5.2", 8, 1);
    assertEquals("rxa_2_52x", rxa_2_52x, rxa_2_52);

    String pid5_2_2 = map.getValue("PID-5[2]-2");
    assertEquals("PID-5[2]-2", "AWESOMENAME2", pid5_2_2);

    String pid5_2_2x = map.getValue("PID-5.2", 2, 2);
    assertEquals("pid5_2_2x", pid5_2_2, pid5_2_2x);

    String pid5_2_1 = map.getValue("PID-5[2]-1");
    assertEquals("PID-5[2]-1", "COOLNAME", pid5_2_1);

    String pid55 = map.getValue("PID-55");
    assertEquals("undefined location PID-55", null, pid55);
  }

  public void testSegmentCount() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    int rxaCnt = map.getSegmentCount("RXA");
    assertEquals("Should be three RXA's", 3, rxaCnt);

    int orcCnt = map.getSegmentCount("ORC");
    assertEquals("Should be one ORC", 1, orcCnt);

    int mshCnt = map.getSegmentCount("MSH");
    assertEquals("Should be one MSH", 1, mshCnt);
  }

  @Test
  public void testCrazyAmpersand() {
//		RXA-16?
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    String rxa16 = map.getValue("RXA-17-2");
    assertEquals("RXA-17-2 - MSD^Merck &Co.^MVX", "Merck &Co.", rxa16);
  }

  @Test
  public void iterateThroughMessage() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    //can you go through the message in general???
    //I would need to be able to just get the absolute index of each segment, in order... or really just the segment names in order.
    //So really, just get the segment names in order that they appeared in the original message.
    List<String> list = map.getMessageSegments();

    assertEquals("The list should be the same length as the original.", SEG_LIST.size(),
        list != null ? list.size() : 0);
    assertEquals("the lists should match", SEG_LIST, list);

//		int idx = 1;
//		for (String seg : list) {
//			
//		}
  }

  @Test
  public void testLineGet() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    String value = map.getValue("OBX-5", 6, 1);
    assertEquals("vfc obx", "V02", value);
  }

  @Test
  public void testGetOrderedMessageSegments() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
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
  public void testIndexing() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    int line = 5;//lines are one based.
    int sequence = 1;
    //should get 1 as the relative index of absolute index 5.
    int calculateSequence = map.getSequenceFromLine(line);
    //should get 4 as the absolute index of relative index 1.
    int calculateLine = map.getLineFromSequence("RXA", sequence);
    assertEquals("sequence from line", sequence, calculateSequence);
    assertEquals("line from sequence", line, calculateLine);
  }

  @Test
  public void testThisToo() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    HL7MessageMap bigMap = mpp.getMessagePartMap(IMMUNITY_MSG);
    int seq = map.getSequenceFromLine("RXA", 5);
    assertEquals(1,seq);

    seq = bigMap.getSequenceFromLine("RXA", 13);
    assertEquals(2,seq);

  }


  @Test
  public void testSegmentIndex() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    //First test getting it from the segment.
    int line = map.getLineFromSequence("OBX", 3);
    assertEquals("Third OBX is in line ", 12, line);

    line = map.getLineFromSequence("OBX", 2);
    assertEquals("second OBX is in in line ", 9, line);

    line = map.getLineFromSequence("RXA", 1);
    assertEquals("first RXA ", 5, line);

    line = map.getLineFromSequence("OBX-3", 3);
    assertEquals("Third OBX is in line ", 12, line);

    line = map.getLineFromSequence("OBX-3", 2);
    assertEquals("second OBX is in line ", 9, line);

    line = map.getLineFromSequence("OBX-3.1", 2);
    assertEquals("second OBX is in line ", 9, line);

    line = map.getLineFromSequence("OBX-3.1", 2);
    assertEquals("second OBX is in line ", 9, line);

  }
//
//  @Test
//  public void testFindAllSegmentRepsWithValues() {
//    //Only the second RXA has an RXA-2 value.
//    List<Integer> resultList = map
//        .findAllIndexesForSegmentWithValues(new String[]{"X", "Y"}, "RXA-2");
//    assertEquals("Should have one entry in the list", 1, resultList.size());
//    System.out.println("resultList: " + resultList);
//    assertTrue("Indexes should hold the value 2. ", resultList.contains(new Integer(2)));
//
//    resultList = map.findAllIndexesForSegmentWithValues(new String[]{"64994-7"}, "OBX-3");
//    assertEquals("OBX-3 64994-7", 3, resultList.size());
//
//    resultList.removeAll(Arrays.asList(new Integer[]{1, 2, 3}));
//    assertEquals("the result list should have 1,2,3", 0, resultList.size());
//
//    resultList = map.findAllSegmentRepsWithValuesWithinRange(new String[]{"X"}, "RXA-2", 3, 3, 1);
//    assertEquals("shouldn't find any", 0, resultList.size());
//
//    resultList = map.findAllSegmentRepsWithValuesWithinRange(new String[]{"X"}, "RXA-2", 2, 3, 1);
//    assertTrue("Should have 2", resultList.contains(new Integer(2)));
//    assertEquals("Should only have one entry", 1, resultList.size());
//
//    resultList = map.findAllSegmentRepsWithValuesWithinRange(new String[]{"X"}, "RXA-2", 1, 2, 1);
//    assertTrue("Should have 2 (starting with 1)", resultList.contains(new Integer(2)));
//    assertEquals("Should only have one entry (starting with 1)", 1, resultList.size());
//
//    resultList = map.findAllSegmentRepsWithValuesWithinRange(new String[]{"X"}, "RXA-2", 2, 4, 1);
//    assertTrue("Should have 2 (2-4)", resultList.contains(new Integer(2)));
//    assertEquals("Should only have one entry (2-4)", 1, resultList.size());
//
//  }

  @Test
  public void testGetSegmentName() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    String seg = map.getSegIdAtLine(5);
    assertEquals("rxa at 5", "RXA", seg);

    seg = map.getSegIdAtLine(6);
    assertEquals("OBX at 6", "OBX", seg);

    seg = map.getSegIdAtLine(1);
    assertEquals("MSH at 1", "MSH", seg);

    seg = map.getSegIdAtLine(3);
    assertEquals("Nk1 at 3", "NK1", seg);
  }

  @Test
  public void testLastSegRxaStartingPoint() {
    HL7MessageMap hmm = mpp.getMessagePartMap(TestMessageGenerator.LAST_SEG_RXA);
    int first = hmm.getImmunizationBoundaryEnd(0);
    assertEquals("First", 4, first);
    int second = hmm.getImmunizationBoundaryEnd(5);
    assertEquals("Second", 11, second);

    int third = hmm.getImmunizationBoundaryEnd(12);
    assertEquals("third", 13, third);

  }

  @Test
  public void testGetNextVaccinationStartingPoint() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
//    SEG_MAP.put("MSH-1", MSH); //1
//    SEG_MAP.put("PID-1", PID); //2
//    SEG_MAP.put("NK1-1", NK1); //3
//    SEG_MAP.put("ORC-1", ORC); //4
//    SEG_MAP.put("RXA-1", RXA); //5
//    SEG_MAP.put("OBX-1", OBX); //6
//    SEG_MAP.put("ORC-2", ORC); //7
//    SEG_MAP.put("RXA-2", RXA2);//8
//    SEG_MAP.put("OBX-2", OBX); //9
//    SEG_MAP.put("RXR-1", RXR); //10
//    SEG_MAP.put("RXA-3", RXA3);//11
//    SEG_MAP.put("OBX-3", OBX); //12

    int start = map.getImmunizationBoundaryEnd(0);
    assertEquals("First", 3, start);

    int next = map.getImmunizationBoundaryEnd(4);
    assertEquals("second", 6, next);

    int third = map.getImmunizationBoundaryEnd(7);
    assertEquals("third", 10, third);

    int fourth = map.getImmunizationBoundaryEnd(11);
    assertEquals("third", 12, fourth);

  }

  @Test
  public void testFindFieldRepWithValue() {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    String loc = "PID-5.1";
    String expected = "FIRSTNAME";
    int rep = map.findFieldRepWithValue(expected, loc, 1);
    assertEquals(loc +" "+ expected, 1, rep);

    loc = "RXA-5.3";
    expected = "CVX";
    rep = map.findFieldRepWithValue(expected, loc, 1);
    assertEquals(loc + expected, 1, rep);

    loc = "PID-11-7";
    expected = "BDL";
    rep = map.findFieldRepWithValue(expected, loc, 1);
    assertEquals(loc + expected, 2, rep);

    loc = "PID-5.1";
    expected = "COOLNAME";
    rep = map.findFieldRepWithValue(expected, loc, 1);
    assertEquals(loc + expected, 2, rep);

    loc = "PID-5.1";
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

    loc = "PID-5.1";
    expected = null;
    rep = map.findFieldRepWithValue(expected, loc, 1);
    assertEquals(loc + expected, 0, rep);

  }

  @Test
  public void testIndexTheFieldRep() {
    this.checkReps("PID[1]-5" , 2);
    this.checkReps("RXR[1]-1" , 1);
    this.checkReps("RXR[99]-1", null);
  }

  private void checkReps(String field, Integer repsIN) {
    HL7MessageMap map = mpp.getMessagePartMap(EXAMPLE_VXU);
    Map<String, Integer> fieldRepMap = map.fieldRepetitions;
    Hl7Location l = new Hl7Location(field);
    String fieldLoc = l.getFieldLoc();
    Integer reps = fieldRepMap.get(fieldLoc);
    assertEquals(field, repsIN, reps);
  }
}
