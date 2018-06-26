package org.immregistries.mqe.hl7util.test;

import java.util.Random;
import org.immregistries.mqe.core.util.DateUtility;
import org.immregistries.mqe.vxu.MqeAddress;
import org.immregistries.mqe.vxu.MqeNextOfKin;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.code.NokRelationship;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonGenerator {

  private static final Logger logger = LoggerFactory.getLogger(PersonGenerator.class);
  private NameGenerator nameGenerator = new NameGenerator();
  private PlaceGenerator placeGenerator = new PlaceGenerator();
  private DateUtility datr = DateUtility.INSTANCE;
  public MqePatient getUniquePatient() {
    //need a month for the birth that's in the past.  and one for the immunization in the present.
    //lets just say birth is three months ago.
    DateTime dt = DateTime.now();
    dt = dt.minusMonths(3);
    int birthMonth = dt.getMonthOfYear();
    int birthYear = dt.getYear();

    final String firstName = nameGenerator.getRandomName();
    final String lastName = nameGenerator.getRandomName();
    final String middleName = nameGenerator.getRandomName();
    final String mothersName = nameGenerator.getRandomName();
    final String mothersMaiden = nameGenerator.getRandomName();
    final int hashName = firstName.hashCode();
    final String gender = new Random().nextBoolean() ? "M" : "F";
    MqeAddress addr = placeGenerator.getRandomAddress();

    MqePatient p = new MqePatient();
    p.setBirthDate(dt.toDate());
    p.setBirthDateString(datr.toDateString(dt));
    p.setNameFirst(firstName);
    p.setNameLast(lastName);
    p.setNameMiddle(middleName);
    p.setMotherMaidenName(mothersMaiden);
    p.getPatientAddressList().add(addr);
    p.setSexCode(gender);
    p.setIdSubmitterNumber(hashName+dt.toString());

    MqeNextOfKin n = new MqeNextOfKin();
    n.setAddress(addr);
    n.setNameFirst(mothersName);
    n.setNameLast(lastName);
    String[] codes = NokRelationship.getResponsibleCodes();
    int index = (int) (Math.random() * codes.length);
    n.setRelationshipCode(codes[index]);

    p.setResponsibleParty(n);

    return p;
  }
}
