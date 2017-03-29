package org.immregistries.dqa.hl7util.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ErrorLocationTest {

  @Test
  public void test() {
    assertEquals("PID", new ErrorLocation("PID").getSegmentId());
    assertEquals("PID", new ErrorLocation("PID-").getSegmentId());
    assertEquals("PID", new ErrorLocation("PID-1").getSegmentId());
    assertEquals("PID", new ErrorLocation("PID-1.2").getSegmentId());
    assertEquals(1, new ErrorLocation("PID-1").getFieldPosition());
    assertEquals(1, new ErrorLocation("PID-1.2").getFieldPosition());
    assertEquals(2, new ErrorLocation("PID-1.2").getComponentNumber());
    assertEquals(0, new ErrorLocation("PID-1").getComponentNumber());
  }

}
