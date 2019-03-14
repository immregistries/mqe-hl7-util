package org.immregistries.mqe.hl7util.model;

import static org.junit.Assert.assertEquals;

import org.immregistries.mqe.vxu.MqePhoneNumber;
import org.junit.Test;

public class MqePhoneNumberTest {

  @Test
  public void test() {
    MqePhoneNumber mn = new MqePhoneNumber();
    String messy = "(517) 616-1182";
    String expected="5176161182";
    String onlyDigits = mn.onlyDigits(messy);
    assertEquals(expected, onlyDigits);
    messy = "517-616-1182";
    expected="5176161182";
    onlyDigits = mn.onlyDigits(messy);
    assertEquals(expected, onlyDigits);
  }


  @Test
  public void test2() {
    String messy = "(517) 616-1182";
    MqePhoneNumber mn = new MqePhoneNumber(messy);
    String expectedArea="517";
    String expectedLocal="6161182";
    assertEquals(expectedArea, mn.getAreaCode());
    assertEquals(expectedLocal, mn.getLocalNumber());
  }


  @Test
  public void test3() {
    String messy = "(517) 616-1182";
    String expectedArea="517";
    String expectedLocal="6161182";
    MqePhoneNumber m = new MqePhoneNumber();
    assertEquals("Area Code", expectedArea, m.getAreaCodeFrom(messy));
    assertEquals("local number", expectedLocal, m.getLocalNumberFrom(messy));
  }
}
