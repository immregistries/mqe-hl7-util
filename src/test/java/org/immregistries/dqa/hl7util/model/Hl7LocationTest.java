package org.immregistries.dqa.hl7util.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Hl7LocationTest {

  @Test
  public void test() {
    assertEquals("PID", new Hl7Location("PID").getSegmentId());
    assertEquals("PID", new Hl7Location("PID-").getSegmentId());
    assertEquals("PID", new Hl7Location("PID-1").getSegmentId());
    assertEquals("PID", new Hl7Location("PID-1.2").getSegmentId());
    assertEquals(1, new Hl7Location("PID-1").getFieldPosition());
    assertEquals(1, new Hl7Location("PID-1.2").getFieldPosition());
    assertEquals(2, new Hl7Location("PID-1.2").getComponentNumber());
    assertEquals(0, new Hl7Location("PID-1").getComponentNumber());
  }

  @Test
  public void testSegmentSequence() {
    assertEquals("PID[2]-1", new Hl7Location("PID[2]-1.1.1").toString());
    assertEquals("PID-1", new Hl7Location("PID[1]-1.1.1").toString());
    assertEquals("MSH-1", new Hl7Location("MSH-1.1.1").toString());
    assertEquals("PD1-1", new Hl7Location("PD1-1.1.1").toString());
    assertEquals("RXA[2]-1", new Hl7Location("RXA[2]-1.1.1").toString());
    assertEquals("ORC[1]-1", new Hl7Location("ORC[1]-1.1.1").toString());
    assertEquals("ORC[2]-1", new Hl7Location("ORC[2]-1.1.1").toString());
    assertEquals("RXR[1]-1", new Hl7Location("RXR[1]-1.1.1").toString());
    assertEquals("RXR[2]-1", new Hl7Location("RXR[2]-1.1.1").toString());
  }

  @Test
  public void testExpectSingle() {
    assertEquals(true, new Hl7Location("PID-1").expectOneSegmentId());
    assertEquals(false, new Hl7Location("RXA-1").expectOneSegmentId());
  }


}
