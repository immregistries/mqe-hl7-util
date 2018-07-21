package org.immregistries.mqe.hl7util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.immregistries.codebase.client.generated.Code;
import org.immregistries.mqe.vxu.MqeVaccination;
import org.joda.time.DateTime;
import org.junit.Test;

public class VaccineGeneratorTest {

  VaccineGenerator vg = VaccineGenerator.INSTANCE;

  @Test
  public void getRandomVaccinesForAge() {
    MqeVaccination mv = vg.makeRandomVaccineForAge(2, DateTime.now());
    assertNotNull(mv);
    assertTrue("SHould have a CVX", mv.getAdminCvxCode() != null);
  }

  @Test
  public void getCVXForAge() {
    for (int x = 1; x < 80; x++) {
      assertAgeCvx(x);
    }
  }

  private void assertAgeCvx(int age) {
    Code c = vg.getCVXForAge(5, new DateTime());
    if (c!=null) {
      System.out.println("Age Month: " + age + " cvx: " + c.getValue() + " label: " + c.getLabel() + " ---- use dates [" + c.getUseDate().getNotBefore() + " < admin < " + c.getUseDate().getNotAfter() + "]");
    }
    assertNotNull("awwwwwwwsnap. the cvx should not be null for a "+ age+ " month old", c);
  }

  @Test
  public void generateRandomIdentifier() {
    String id1 = vg.generateRandomIdentifier();
    String id2 = vg.generateRandomIdentifier();
    String id3 = vg.generateRandomIdentifier();
    String id4 = vg.generateRandomIdentifier();
    String id5 = vg.generateRandomIdentifier();

    System.out.println(id1);
    System.out.println(id2);
    System.out.println(id3);
    System.out.println(id4);
    System.out.println(id5);

    Set<String> s = new HashSet<>();
    s.add(id1);
    s.add(id2);
    s.add(id3);
    s.add(id4);
    s.add(id5);
    assertEquals("should have all five if there are no overlaps", 5, s.size());

  }
}
