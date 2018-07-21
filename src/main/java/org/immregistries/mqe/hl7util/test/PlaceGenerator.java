package org.immregistries.mqe.hl7util.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.immregistries.mqe.vxu.MqeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaceGenerator {

  private static final Logger logger = LoggerFactory.getLogger(PlaceGenerator.class);
  private NameGenerator ng = NameGenerator.INSTANCE;

  public MqeAddress getRandomAddress() {
      String streetNumber = String.valueOf(new Integer((int) (Math.random() * 9999)));
      String street = ng.getRandomName();
      String primaryStreet = streetNumber + " " + street + " Ave";
      CityGenerator cg = CityGenerator.getRandomForState("MI");
      MqeAddress addr = new MqeAddress();
      addr.setStreet(primaryStreet);
      addr.setCity(cg.city);
      addr.setStateCode(cg.state);
      addr.setZip(cg.zip);
      return addr;
  }
}
