package org.immregistries.dqa.hl7util.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HL7QuickParser {
  INSTANCE;
  private final String msh7Regex = "^MSH(?:\\|[^|]*?){6}([^|]+)\\|";
  private Pattern msh7 = Pattern.compile(msh7Regex);
  private static final String MSH_REGEX = "^\\s*MSH\\|\\^~\\\\&\\|.*";
  private static final String FHS_BHS_REGEX = "^\\s*(FHS|BHS)\\|.*";
  private static final String HL7_SEGMENT_REGEX = "^\\w\\w\\w\\|.*";

  public String getMsh7MessageDate(String message) {
    Matcher matcher = msh7.matcher(message);
    if (matcher.find()) {
      String msgDate = matcher.group(1);
      return msgDate;
    }
    return "";
  }

}
