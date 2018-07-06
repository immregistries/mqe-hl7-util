package org.immregistries.mqe.hl7util.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.immregistries.codebase.client.CodeMap;
import org.immregistries.codebase.client.CodeMapBuilder;
import org.immregistries.codebase.client.generated.Code;
import org.immregistries.codebase.client.generated.UseAge;
import org.immregistries.codebase.client.reference.CodesetType;
import org.immregistries.mqe.core.util.DateUtility;
import org.immregistries.mqe.vxu.MqeVaccination;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum VaccineGenerator {
  INSTANCE;

  CodeMap cb = CodeMapBuilder.INSTANCE.getDefaultCodeMap();

  private static final Logger logger = LoggerFactory.getLogger(VaccineGenerator.class);

  public MqeVaccination makeRandomVaccineForAge(int age, DateTime messageDate) {
    MqeVaccination v = new MqeVaccination();
    v.setIdSubmitter(generateRandomIdentifier());//This is ORC-3, FILLER_ORDER_NUMBER
    v.setActionCode("A");
    v.setAdminDate(messageDate.minusDays(1).toDate());
    v.setAdministered(true);
    v.setCompletionCode("CP");
    v.setLotNumber("XJSLDFJ");
    Code cvx = getCVXForAge(age, messageDate);
    v.setAdminCvxCode(cvx.getValue());

    return v;
  }

  private DateUtility datr = DateUtility.INSTANCE;

  public Code getCVXForAge(int months, DateTime adminDate) {
    Collection<Code> codes = cb.getCodesForTable(CodesetType.VACCINATION_CVX_CODE);
    int random = (int) (Math.random() * 5); //(probably want at most five vaccines);
    List<Code> rc = new ArrayList<>();
    for (Code c : codes) {
      UseAge a = c.getUseAge();

      if (c.getUseDate() != null) {
        DateTime notBeforeDate = datr.parseDateTime(c.getUseDate().getNotBefore());
        DateTime notAfterDate = datr.parseDateTime(c.getUseDate().getNotAfter());
        if (notBeforeDate != null && adminDate.isBefore(notBeforeDate)) {
          continue;
        }
        if (notAfterDate != null && adminDate.isAfter(notAfterDate)) {
          continue;
        }
      }

      int afterMonth = a.getNotBeforeMonth();
      int beforeMonth = a.getNotAfterMonth();
      if (months > afterMonth && months < beforeMonth
          && !"998".equalsIgnoreCase(c.getValue())
          && !"999".equalsIgnoreCase(c.getValue())) {
        rc.add(c);
      }
    }
    //if there's no matches, just use a random one.
    if (rc.size() == 0)

    {
      rc.addAll(codes);
    }

    return rc.get((int) (Math.random() * rc.size()));
  }

  public String generateRandomIdentifier() {
    //generate a vaccine MRN:
    try {
      Thread.sleep(1);//This is so that it's not so fast that it doesn't show a new millisecond
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    String msText = String.valueOf(new DateTime().getMillis());
    return "TEST" + msText.substring(msText.length() - 8, msText.length());
//    last 10 digits =
//    return msText.substring(0,msText.length() - 10);
  }
}
