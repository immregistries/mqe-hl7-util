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
  private MessageGenerator mg = new MessageGenerator();
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
    assertEquals("should be... short", "PID|||||richard\r", hl7);

  }
}
