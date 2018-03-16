package org.immregistries.dqa.hl7util.model;

import static org.junit.Assert.*;

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

}
