package org.immregistries.mqe.hl7util.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HL7QuickParser {
  INSTANCE;
  private final String msh7Regex = "^MSH(?:\\|[^|]*?){6}([^|]+)\\|";
  private final String msh4Regex = "^MSH(?:\\|[^|]*?){3}([^|]+)\\|";
  private Pattern msh7 = Pattern.compile(msh7Regex);
  private Pattern msh4 = Pattern.compile(msh4Regex);
  private static final String MSH_REGEX = "^\\s*MSH\\|\\^~\\\\&\\|.*";
  private static final String FHS_BHS_REGEX = "^\\s*(FHS|BHS)\\|.*";
  private static final String HL7_SEGMENT_REGEX = "^\\w\\w\\w\\|.*";

  public String getMsh7MessageDate(String message) {
    return getFirstMatch(msh7, message);
  }

  public String getMsh4Sender(String message) {
    return getFirstMatch(msh4, message);
  }

  String getFirstMatch(Pattern p, String message) {
    Matcher matcher = p.matcher(message);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return "";
  }
}
