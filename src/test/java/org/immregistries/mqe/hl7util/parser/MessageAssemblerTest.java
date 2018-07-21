package org.immregistries.mqe.hl7util.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.immregistries.mqe.hl7util.model.Hl7Location;
import org.immregistries.mqe.hl7util.test.MessageGenerator;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = ParserAppConfig.class)

public class MessageAssemblerTest {

  //	@Autowired
  private MessageParser mpp = new MessageParserHL7();
  private MessageGenerator mg = MessageGenerator.INSTANCE;
  public HL7MessageMap map;

  @Test
  public void testNameChanges() {
    String message = mg.getUniqueMessage();
    //let's map it...
    HL7MessageMap map = mpp.getMessagePartMap(message);
    map.put(new Hl7Location("PID-5-1"), "bob");
    String newName = map.getValue("PID-5-1");
    assertEquals("Should be bob", "bob", newName);
  }


  @Test
  public void testEmptyMapBuilder() {
    HL7MessageMap map = new HL7MessageMap();
    map.put(new Hl7Location("PID-5-1"), "richard");
    map.put(new Hl7Location("PID[1]-5-1"), "bob");
    map.put(new Hl7Location("PID[2]-5-1"), "george");
    map.put(new Hl7Location("PID[3]-5-1"), "phillip");
    String newName = map.getValue("PID-5-1");
    assertEquals("Should be bob", "bob", newName);
  }

  @Test
  public void reassembleTest() {
    HL7MessageMap map = new HL7MessageMap();
    map.put(new Hl7Location("PID-5.1"), "richard");
    String hl7 = map.reassemble();
    assertEquals("should be... short", "PID|||||richard", hl7);
  }

  @Test
  public void fullReassembly() {
    String message = mg.getUniqueMessage();
//    System.out.println("Starting:");
//    System.out.println(message);
    //let's map it...
    HL7MessageMap map = mpp.getMessagePartMap(message);
    String hl7 = map.reassemble();
//    System.out.println("ENDING: ");
//    System.out.println(hl7);
    assertEquals("no modifications.  should be equal", message, hl7);
  }

  @Test
  public void padSeparatorsTest() {
    HL7MessageMap map = new HL7MessageMap();
    StringBuilder sb = map.padSeparators(new StringBuilder("MSH|^&~\\|MCIR|MCIR|1|2||"), '|', 8);
    System.out.println(sb);

    sb = map.padSeparators(new StringBuilder("PID|1|2|3|4|5|6|7"), '|', 8);
    System.out.println(sb);

    sb = map.padSeparators(new StringBuilder("ORC|1|2|3|4|"), '|', 5);
    System.out.println(sb);
  }
}
