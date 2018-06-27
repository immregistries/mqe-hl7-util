package org.immregistries.mqe.hl7util.test;

import java.util.Random;
import org.immregistries.codebase.client.CodeMap;
import org.immregistries.codebase.client.generated.Codebase;
import org.immregistries.mqe.core.util.DateUtility;
import org.immregistries.mqe.vxu.MqeAddress;
import org.immregistries.mqe.vxu.MqeNextOfKin;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.MqeVaccination;
import org.immregistries.mqe.vxu.code.NokRelationship;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum VaccineGenerator {
  INSTANCE;

  CodeMap cb = new CodeMap();

  private static final Logger logger = LoggerFactory.getLogger(VaccineGenerator.class);
  public MqeVaccination getRandomVaccinesForAge(int age) {
    MqeVaccination v = new MqeVaccination();
    v.setIdSubmitter(generateRandomIdentifier());
    v.setActionCode("A");


    return v;
  }

  public String generateRandomIdentifier() {
    //generate a vaccine MRN:
    //use something from the timestamp plus "test"
    String msText = String.valueOf(new DateTime().getMillis());
    return msText.substring(msText.length() - 8, msText.length());
//    last 10 digits =
//    return msText.substring(0,msText.length() - 10);
  }
}
