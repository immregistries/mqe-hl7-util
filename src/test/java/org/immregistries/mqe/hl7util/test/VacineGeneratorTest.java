package org.immregistries.mqe.hl7util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VacineGeneratorTest {

  VaccineGenerator vg = VaccineGenerator.INSTANCE;

  @Test
  public void testIdGen() {
    System.out.println("-------------------------------------------------------------");
    String id1 = vg.generateRandomIdentifier();
    System.out.println(id1);
    System.out.println("-------------------------------------------------------------");
    String id2 = vg.generateRandomIdentifier();
    System.out.println(id2);
    System.out.println("-------------------------------------------------------------");
    assertNotEquals("Shouldn't be the same id", id1, id2);
    assertEquals("should be 8 digits", 12, id1.length());
  }

}
