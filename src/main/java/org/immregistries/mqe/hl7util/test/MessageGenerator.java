package org.immregistries.mqe.hl7util.test;

import java.util.Date;
import java.util.List;
import java.util.Random;
import org.immregistries.mqe.core.util.DateUtility;
import org.immregistries.mqe.vxu.MqeMessageHeader;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeVaccination;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum MessageGenerator {
  INSTANCE;
  private static final Logger logger = LoggerFactory.getLogger(MessageGenerator.class);

  private PersonGenerator pg = PersonGenerator.INSTANCE;
  private DateUtility datr = DateUtility.INSTANCE;
  //this needs:
  //current year, current month, unique number, first name, birth year, birth month,
  //current year, current month, current year, current month.
  private NameGenerator ng = NameGenerator.INSTANCE;
  private VaccineGenerator vg = VaccineGenerator.INSTANCE;

  public MqeMessageReceived generateTestMessage() {
    MqeMessageReceived mr = new MqeMessageReceived();
    MqeMessageHeader mh = mr.getMessageHeader();
    mh.setMessageDate(new Date());
    mh.setSendingFacility("MQE");
    mh.setMessageControl(String.valueOf(DateTime.now().getMillis()));
    mh.setMessageStructure("VXU_V04");
    mh.setMessageTrigger("V04");
    mh.setMessageType("VXU");
    mh.setProcessingStatusCode("T");
    mh.setReceivingApplication("MQE");
    mh.setReceivingFacility("HL7-UTIL");
    mh.setMessageVersion("2.5.1");

    mr.setPatient(pg.generateTestPatient());

    List<MqeVaccination> mvList = mr.getVaccinations();
    Date birthDate = mr.getPatient().getBirthDate();
    int monthsOld = datr.monthsBetween(birthDate, new Date());
    mvList.add(vg.makeRandomVaccineForAge(monthsOld, DateTime.now()));
    mvList.add(vg.makeRandomVaccineForAge(monthsOld, DateTime.now()));
    mvList.add(vg.makeRandomVaccineForAge(monthsOld, DateTime.now()));
    mvList.add(vg.makeRandomVaccineForAge(monthsOld, DateTime.now()));

    return mr;
  }

  public String getUniqueMessage() {
    DateTime dtCurrent = DateTime.now();
    final int currentYear = dtCurrent.getYear();
    final int currentMonth = dtCurrent.getMonthOfYear();
    //For the unique patient id:
    final int julianDay = dtCurrent.getDayOfYear();

    //need a month for the birth that's in the past.  and one for the immunization in the present.
    //lets just say birth is three months ago.
    DateTime dt = DateTime.now();
    dt = dt.minusMonths(3);
    int birthMonth = dt.getMonthOfYear();
    int birthYear = dt.getYear();

    final String firstName = ng.getRandomName();
    final String lastName = ng.getRandomName();
    final String mothersName = ng.getRandomName();
    final String mothersMaiden = ng.getRandomName();
    final int hashName = firstName.hashCode();

    final String gender = new Random().nextBoolean() ? "M" : "F";

    String testMessageFormat1 =
              "MSH|^~\\&||1255-60-20|MCIR|MDCH|%s||VXU^V04^VXU_V04|3WzJ-A.01.01.2aF|T|2.5.1"
            + "\rPID|||3WzJ-A.01.01%s^^^AIRA-TEST^MR||%s^%s^^^^^L|%s^%s|%d%02d01|%s||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|417 Bridge St NW^^Grand Rapids^MI^49544^USA^P||^PRN^PH^^^616^9245843|||||||||2135-2^Hispanic or Latino^HL70005|Birth Place PID-23"
            + "\rPD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20170301|20170301"
            + "\rNK1|1|%s^%s^^^^^L|MTH^Mother^HL70063|81 Page Pl^^GR^MI^49544^USA^P|^PRN^PH^^^616^9245843"
            + "\rORC|RE||V51L2.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L"
            + "\rRXA|0|1|%d%02d01||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y5841RR||MSD^Merck and Co^MVX||||A"
            + "\rRXR|SC^^HL70162|RA^^HL70163"
            + "\rOBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20170301|||VXC40^Eligibility captured at the immunization level^CDCPHINVS"
            + "\rOBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F"
            + "\rOBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F"
            + "\rOBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20170301||||||F"
            + "\rORC|RE||BQ61L11032.2^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L"
//            + "\rRXA|0|1|%d%02d01||00005-0100-01^Trumenba^NDC|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||N3783EO||PFR^Pfizer, Inc^MVX|||CP|A";
            + "\rRXA|0|1|%d%02d01||66019-0302-10^FluMist Quadrivalent^NDC|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||N3783EO||MED^^MVX|||CP|A";

    String today = datr.toTzString(new DateTime());

    return String.format(testMessageFormat1,
        today,
        (hashName + julianDay),
        lastName, firstName,
        mothersMaiden, mothersName,
        birthYear, birthMonth,
        gender,
        lastName, mothersName,
        currentYear, currentMonth,
        currentYear, currentMonth);
  }
}
