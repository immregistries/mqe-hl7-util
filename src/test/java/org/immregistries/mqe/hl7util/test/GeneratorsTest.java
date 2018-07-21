package org.immregistries.mqe.hl7util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.immregistries.mqe.hl7util.model.Hl7Location;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.parse.HL7MessageParser;
import org.junit.Test;

public class GeneratorsTest {

  private final PersonGenerator pg = PersonGenerator.INSTANCE;
  private final MessageGenerator mg = MessageGenerator.INSTANCE;
  private final HL7MessageParser mp = HL7MessageParser.INSTANCE;
  @Test
  public void printFromMessageText() {
//      1. get a message:
    String message = mg.getUniqueMessage();
    MqeMessageReceived mr = mp.extractMessageFromText(message);
    System.out.println("-------THIS IS THE MODEL----------");
    System.out.println(mr);
    System.out.println("-------    END MODEL    ----------");


  }

  @Test
  public void testPersonGen() {
      MqePatient p1 = pg.generateTestPatient();
      MqePatient p2 = pg.generateTestPatient();
    System.out.println("-------------------------------------------------------------");
    System.out.println(p1);
    System.out.println("-------------------------------------------------------------");
    System.out.println(p2);
    System.out.println("-------------------------------------------------------------");
    assertNotEquals("Shouldn't be the same person", p1.getName(), p2);
  }

  @Test
  public void testCity() {
    assertState("MI");
    assertState("SD");
    assertState("WI");
    assertState("MN");
    assertNullForState(null);
    assertNullForState("");
    assertNullForState("FL");
  }

  @Test
  public void testSameState() {
    String state = "MI";
    CityGenerator g1 = CityGenerator.getRandomForState(state);
    System.out.println(g1);
    CityGenerator g2 = CityGenerator.getRandomForState(state);
    System.out.println(g2);
    assertNotEquals("Shouldn't be the same", g1, g2);
  }


  @Test
  public void testMultipleRandom() {
    CityGenerator g1 = CityGenerator.getRandom();
    System.out.println(g1);
    CityGenerator g2 = CityGenerator.getRandom();
    System.out.println(g2);
    assertNotEquals("Shouldn't be the same", g1, g2);
  }

  private void assertNullForState(String state) {
    CityGenerator g = CityGenerator.getRandomForState(state);
    System.out.println(state + " - " + g);
    assertNull("Should be a "+null+" zip code", g);
  }

  private void assertState(String state) {
    CityGenerator g = CityGenerator.getRandomForState(state);
    System.out.println(state + " - " + g);
    assertEquals("Should be a "+state+" zip code", state, g.state);
  }
}
