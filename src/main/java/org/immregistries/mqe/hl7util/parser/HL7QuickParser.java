package org.immregistries.mqe.hl7util.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HL7QuickParser {
  INSTANCE;
  private final String msh7Regex = "^MSH(?:\\|[^|]*?){6}([^|]+)\\|";
  private final String msh10Regex = "^MSH(?:\\|[^|]*?){9}([^|]+)\\|";
  private final String msh4Regex = "^MSH(?:\\|[^|]*?){3}([^|]+)\\|";
  private final String pid_regex = "^(PID\\|.*\\|)";
//  private static final String MSH_REGEX = "^\\s*MSH\\|\\^~\\\\&\\|.*";
//  private static final String FHS_BHS_REGEX = "^\\s*(FHS|BHS)\\|.*";
//  private static final String HL7_SEGMENT_REGEX = "^\\w\\w\\w\\|.*";
  private Pattern pidPattern = Pattern.compile(pid_regex, Pattern.MULTILINE);
  private Pattern msh7Pattern = Pattern.compile(msh7Regex);
  private Pattern msh10Pattern = Pattern.compile(msh10Regex);
  private Pattern msh4Pattern = Pattern.compile(msh4Regex);

  public String getMsh7MessageDate(String message) {
    return getFirstMatch(msh7Pattern, message);
  }

  public String getMsh10ControlId(String message) {
    return getFirstMatch(msh10Pattern, message);
  }

  public String getPidSegment(String message) {
    return getFirstMatch(pidPattern, message);
  }

  public String getMsh4Sender(String message) {
    return getFirstMatch(msh4Pattern, message);
  }

  private String getFirstMatch(Pattern p, String message) {
    Matcher matcher = p.matcher(message);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return "";
  }
}
