package org.immregistries.dqa.hl7util.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class Hl7LocationTest {

  @Test
  public void test() {

    assertEquals(2, new Hl7Location("PID[3]-1[2].1.1").getFieldRepetition());
    assertEquals(2, new Hl7Location("PID[2]-1.1.1").getSegmentSequence());
    assertEquals(3, new Hl7Location("PID-1[3].1.1").getFieldRepetition());
    assertEquals(1, new Hl7Location("PID-1.1").getFieldRepetition());

    assertEquals("PID", new Hl7Location("PID").getSegmentId());
    assertEquals("PID", new Hl7Location("PID-").getSegmentId());
    assertEquals("PID", new Hl7Location("PID-1").getSegmentId());
    assertEquals("PID", new Hl7Location("PID-1.2").getSegmentId());
    assertEquals(1, new Hl7Location("PID-1").getFieldPosition());
    assertEquals(1, new Hl7Location("PID-1.2").getFieldPosition());
    assertEquals(2, new Hl7Location("PID-1.2").getComponentNumber());
    assertEquals(1, new Hl7Location("PID[1]-1[1].1.1").getFieldRepetition());
    assertEquals(1, new Hl7Location("PID[1]-1.1.1").getFieldRepetition());
    assertEquals(1, new Hl7Location("PID-1[1].1.1").getFieldRepetition());
    assertEquals(1, new Hl7Location("PID-1.1").getFieldRepetition());
    assertEquals(1, new Hl7Location("PID-1").getFieldRepetition());
    assertEquals(1, new Hl7Location("PID-1").getComponentNumber());

  }

  @Test
  public void testNotEqual() {
    runLessThanSet("RXA-5.2.1");
    runLessThanSet("RXA[5]-5.2.1");
    runLessThanSet("RXA-5[1].2.1");
    runLessThanSet("RXA-5");
  }

  private void runLessThanSet(String location) {
    Hl7Location one = new Hl7Location(location);
    Hl7Location two = new Hl7Location(location);
    two.setSegmentSequence(9);
    assertNotEquals(location + "should NOT be the same", one, two);
    assertEquals(location + "compare should be -1", -1, one.compareTo(two));
  }


  @Test
  public void testLocators() {
    Hl7Location loc = new Hl7Location("MSH-3");
    assertEquals("msh3", "MSH[1]-3[1].1.1", loc.toString());
    loc = new Hl7Location("MSH[0]-1");
    assertEquals("addFieldRepIfMIssing(\"MSH[0]-1\")", "MSH[0]-1[1].1.1", loc.toString());
  }

  @Test
  public void testGetSegmentSequenceFromLocation() {
    Hl7Location loc = new Hl7Location("RXR[1]-1[1].1.1");
    assertEquals("Location: " + loc.toString(), 1, loc.getSegmentSequence());
  }

  @Test
  public void testEqualComparison() {
    runEqualsSet("RXA-5.2.1");
    runEqualsSet("RXA[5]-5.2.1");
    runEqualsSet("RXA-5[1].2.1");
    runEqualsSet("RXA-5");
  }

  private void runEqualsSet(String location) {
    Hl7Location one = new Hl7Location(location);
    Hl7Location two = new Hl7Location(location);
    assertEquals(location + "should be the same", one, two);
    assertEquals(location + "compare should be zero", 0, one.compareTo(two));
    Hl7Location three = new Hl7Location(location);
    three.setLine(5);
    assertEquals(location + "compare should be zero", 0, three.compareTo(two));
  }

  @Test
  public void testHl7Format() {
    assertEquals("PID[2]-1", new Hl7Location("PID[2]-1.1.1").getAbbreviated());
    assertEquals("PID-1", new Hl7Location("PID[1]-1.1.1").getAbbreviated());
    assertEquals("MSH-1", new Hl7Location("MSH-1.1.1").getAbbreviated());
    assertEquals("PD1-1", new Hl7Location("PD1-1.1.1").getAbbreviated());
    assertEquals("RXA[2]-1", new Hl7Location("RXA[2]-1.1.1").getAbbreviated());
    assertEquals("ORC[1]-1", new Hl7Location("ORC[1]-1.1.1").getAbbreviated());
    assertEquals("ORC[2]-1", new Hl7Location("ORC[2]-1.1.1").getAbbreviated());
    assertEquals("RXR[1]-1", new Hl7Location("RXR[1]-1.1.1").getAbbreviated());
    assertEquals("RXR[2]-1", new Hl7Location("RXR[2]-1.1.1").getAbbreviated());
  }

  @Test
  public void testExpectSingle() {
    assertEquals(true, new Hl7Location("PID-1").expectOneSegmentId());
    assertEquals(false, new Hl7Location("RXA-1").expectOneSegmentId());
  }

  @Test
  public void testOrigFormat() {
    Hl7Location loc = new Hl7Location("PID[1]-5~1-1-1");
    Hl7Location loc2 = new Hl7Location("PID-5~2-1-1");
    assertEquals("Should be first field repetition", 1, loc.getFieldRepetition());
    assertEquals("Should be second field repetition", 2, loc2.getFieldRepetition());
  }

  @Test
  public void testGetFieldRepFromLoc() {
    String locator = "RXA[1]-5[3].1.1";
    Hl7Location loc = new Hl7Location(locator);
    assertEquals(locator, 3, loc.getFieldRepetition());

    locator = "RXA[1]-5[5].1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, 5, loc.getFieldRepetition());

    locator = "RXA-5[9].1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, 9, loc.getFieldRepetition());

    locator = "RXA-5[0].1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, 0, loc.getFieldRepetition());

    locator = "RXA-5";
    loc = new Hl7Location(locator);
    assertEquals(locator, 1, loc.getFieldRepetition());

    locator = "RXA";
    loc = new Hl7Location(locator);
    assertEquals(locator, 1, loc.getFieldRepetition());

  }


  @Test
  public void testGetFieldRep() {
    String locator = "MSH-3.1.1";
    Hl7Location loc = new Hl7Location(locator);
    assertEquals(locator, 1, loc.getFieldRepetition());

    locator = "MSH-3[2].1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, 2, loc.getFieldRepetition());

    locator = "PID[2]-3[2].1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, 2, loc.getFieldRepetition());

    locator = "PID[2].3.1-1";
    loc = new Hl7Location(locator);
    assertEquals(locator, 1, loc.getFieldRepetition());

    locator = "PID[2]-3";
    loc = new Hl7Location(locator);
    assertEquals(locator, 1, loc.getFieldRepetition());

    locator = "PID[2]";
    loc = new Hl7Location(locator);
    assertEquals(locator, 1, loc.getFieldRepetition());
  }

  @Test
  public void fullFormat() {
    Hl7Location loc = new Hl7Location("PID-1.1.1", 2, 1);
    assertEquals("PID[1]-1[1].1.1", loc.toString());
    assertEquals("PID[1]-1[1].1.1", new Hl7Location("PID[1]-1[1].1.1", 1).toString());
    assertEquals("MSH-1", new Hl7Location("MSH-1.1.1").getAbbreviated());
    assertEquals("PD1-1", new Hl7Location("PD1-1.1.1").getAbbreviated());
    assertEquals("RXA[2]-1", new Hl7Location("RXA[2]-1.1.1").getAbbreviated());
    assertEquals("ORC[1]-1", new Hl7Location("ORC[1]-1.1.1").getAbbreviated());
    assertEquals("ORC[2]-1", new Hl7Location("ORC[2]-1.1.1").getAbbreviated());
    assertEquals("RXR[1]-1", new Hl7Location("RXR[1]-1.1.1").getAbbreviated());
    assertEquals("RXR[2]-1", new Hl7Location("RXR[2]-1.1.1").getAbbreviated());
  }


  @Test
  public void testGetFieldLocator() {
    String locator = "MSH-3.1-1";
    Hl7Location loc = new Hl7Location(locator);
    assertEquals(locator, "MSH-3", loc.getFieldLoc());

    locator = "PID-3.1-1";
    loc = new Hl7Location(locator);
    assertEquals(locator, "PID-3", loc.getFieldLoc());

    locator = "PID[1]-1.1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, "PID-1", loc.getFieldLoc());

    locator = "NK1-3";
    loc = new Hl7Location(locator);
    assertEquals(locator, "NK1-3", loc.getFieldLoc());

    locator = "NK1[2]-3[2].1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, "NK1[2]-3", loc.getFieldLoc());
  }


  @Test
  public void testGetFieldLocFromLoc() {
    String locator = "RXA[1]-5[3].1.1";
    Hl7Location loc = new Hl7Location(locator);
    assertEquals(locator, "RXA-5", loc.getFieldLoc());

    locator = "RXA[1]-5[5].1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, "RXA-5", loc.getFieldLoc());

    locator = "RXA-5[9].1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, "RXA-5", loc.getFieldLoc());

    locator = "RXA-5[0].1.1";
    loc = new Hl7Location(locator);
    assertEquals(locator, "RXA-5", loc.getFieldLoc());

    locator = "RXA-5";
    loc = new Hl7Location(locator);
    assertEquals(locator, locator, loc.getFieldLoc());

    locator = "RXA";
    loc = new Hl7Location(locator);
    assertEquals(locator, "RXA", loc.getFieldLoc());

    locator = null;
    loc = new Hl7Location(locator);
    assertEquals(locator, "", loc.getFieldLoc());

  }


  @Test
  public void generateLocatorForIndexTest() {
    Hl7Location locator = new Hl7Location("OBX-3");
    locator.setFieldRepetition(2);
    assertEquals("OBX[1]-3[2].1.1", locator.toString());

    locator = new Hl7Location("OBX-3.1");
    locator.setFieldRepetition(2);
    assertEquals("OBX[1]-3[2].1.1", locator.toString());

    locator = new Hl7Location("OBX-3.1");
    locator.setFieldRepetition(2);
    assertEquals("OBX[1]-3[2].1.1", locator.toString());

    locator = new Hl7Location("OBX-3.2.4");
    locator.setFieldRepetition(9);
    assertEquals("OBX[1]-3[9].2.4", locator.toString());

    locator = new Hl7Location("OBX-3.2.4");
    locator.setFieldRepetition(9);
    assertEquals("OBX[1]-3[9].2.4", locator.toString());

    locator = new Hl7Location("OBX-3.2.4");
    locator.setFieldRepetition(3);
    assertEquals("OBX[1]-3[3].2.4", locator.toString());

    locator = new Hl7Location("OBX-3.2.4", 5, 11);
    locator.setFieldRepetition(9);
    assertEquals("OBX[11]-3[9].2.4", locator.toString());

    locator = new Hl7Location("OBX-3.2.4", 5, 9);
    locator.setFieldRepetition(3);
    assertEquals("OBX[9]-3[3].2.4", locator.toString());
  }


}
