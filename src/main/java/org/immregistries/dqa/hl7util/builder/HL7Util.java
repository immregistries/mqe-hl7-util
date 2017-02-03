/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.immregistries.dqa.hl7util.builder;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.immregistries.dqa.hl7util.Reportable;
import org.immregistries.dqa.hl7util.SeverityLevel;
import org.immregistries.dqa.hl7util.hl7model.CodedWithExceptions;
import org.immregistries.dqa.hl7util.hl7model.ErrorLocation;

public class HL7Util
{
  public static final String MESSAGE_TYPE_VXU = "VXU";
  public static final String MESSAGE_TYPE_QBP = "QBP";

  public static final String ACK_ERROR = "AE";
  public static final String ACK_ACCEPT = "AA";
  public static final String ACK_REJECT = "AR";

  public static final String SEVERITY_ERROR = "E";
  public static final String SEVERITY_WARNING = "W";
  public static final String SEVERITY_INFORMATION = "I";

  public static final String PROCESSING_ID_DEBUGGING = "D";
  public static final String PROCESSING_ID_PRODUCTION = "P";
  public static final String PROCESSING_ID_TRAINING = "T";

  public static final String QUERY_RESULT_NO_MATCHES = "Z34";
  public static final String QUERY_RESULT_LIST_OF_CANDIDATES = "Z31";
  public static final String QUERY_RESULT_IMMUNIZATION_HISTORY = "Z32";

  public static final String QUERY_RESPONSE_TYPE = "RSP^K11^RSP_K11";

  public static final int BAR = 0;
  public static final int CAR = 1;
  public static final int TIL = 2;
  public static final int SLA = 3;
  public static final int AMP = 4;

  private static int ackCount = 1;

  public static synchronized int getNextAckCount()
  {
    if (ackCount == Integer.MAX_VALUE)
    {
      ackCount = 1;
    }
    return ackCount++;
  }

  public static boolean setupSeparators(String messageText, char[] separators)
  {
    if (messageText.startsWith("MSH") && messageText.length() > 10)
    {
      separators[BAR] = messageText.charAt(BAR + 3);
      separators[CAR] = messageText.charAt(CAR + 3);
      separators[TIL] = messageText.charAt(TIL + 3);
      separators[SLA] = messageText.charAt(SLA + 3);
      separators[AMP] = messageText.charAt(AMP + 3);
      return true;
    } else
    {
      setDefault(separators);
      return false;
    }
  }

  public static void setDefault(char[] separators)
  {
    separators[BAR] = '|';
    separators[CAR] = '^';
    separators[TIL] = '~';
    separators[SLA] = '\\';
    separators[AMP] = '&';
  }

  public static boolean checkSeparatorsAreValid(char[] separators)
  {
    boolean unique = true;
    // Make sure separators are unique for each other
    for (int i = 0; i < separators.length; i++)
    {
      for (int j = i + 1; j < separators.length; j++)
      {
        if (separators[i] == separators[j])
        {
          unique = false;
          break;
        }
      }
    }
    return unique;
  }

  public static String makeAckMessage(String ackType, String severityLevel, String message, AckData ackData, Reportable reportable)
  {
    StringBuilder ack = new StringBuilder();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
    String messageDate = sdf.format(new Date());
    // MSH
    ack.append("MSH|^~\\&");
    ack.append("|" + ackData.getSendingApplication()); // MSH-3 Sending
                                                       // Application
    ack.append("|" + ackData.getSendingFacility()); // MSH-4 Sending Facility
    ack.append("|" + ackData.getReceivingApplication()); // MSH-5 Receiving
    // Application
    ack.append("|" + ackData.getReceivingFacility()); // MSH-6 Receiving
                                                      // Facility
    ack.append("|" + messageDate); // MSH-7 Date/Time of Message
    ack.append("|"); // MSH-8 Security
    ack.append("|ACK"); // MSH-9
    // Message
    // Type
    ack.append("|" + messageDate + "." + getNextAckCount()); // MSH-10 Message
                                                             // Control ID
    ack.append("|P"); // MSH-11 Processing ID
    ack.append("|2.5.1"); // MSH-12 Version ID
    ack.append("|\r");
    // ack.append("SFT|" + SoftwareVersion.VENDOR + "|" +
    // SoftwareVersion.VERSION + "|" + SoftwareVersion.PRODUCT + "|" +
    // SoftwareVersion.BINARY_ID
    // + "|\r");
    ack.append("MSA|" + ackType + "|" + ackData.getProcessingControlId() + "|\r");

    makeERRSegment(ack, severityLevel, "", message, reportable);

    return ack.toString();

  }

  public static void appendErrorCode(StringBuilder ack, CodedWithExceptions cwe)
  {
    if (cwe != null)
    {
      if (cwe.hasIdentifier() && (cwe.getText() == null || cwe.getText().equals("")))
      {
        cwe.setNameOfCodingSystem("HL70357");
        if (cwe.getIdentifier().equals("0"))
        {
          cwe.setText("Message accepted");
        } else if (cwe.getIdentifier().equals("100"))
        {
          cwe.setText("Segment sequence error");
        } else if (cwe.getIdentifier().equals("101"))
        {
          cwe.setText("Required field missing");
        } else if (cwe.getIdentifier().equals("102"))
        {
          cwe.setText("Data type error");
        } else if (cwe.getIdentifier().equals("103"))
        {
          cwe.setText("Table value not found");
        } else if (cwe.getIdentifier().equals("200"))
        {
          cwe.setText("Unsupported message type");
        } else if (cwe.getIdentifier().equals("201"))
        {
          cwe.setText("Unsupported event code");
        } else if (cwe.getIdentifier().equals("202"))
        {
          cwe.setText("Unsupported processing ID");
        } else if (cwe.getIdentifier().equals("203"))
        {
          cwe.setText("Unsupported version ID");
        } else if (cwe.getIdentifier().equals("204"))
        {
          cwe.setText("Unknown key identifier");
        } else if (cwe.getIdentifier().equals("205"))
        {
          cwe.setText("Duplicate key identifier");
        } else if (cwe.getIdentifier().equals("206"))
        {
          cwe.setText("Application record locked");
        } else if (cwe.getIdentifier().equals("207"))
        {
          cwe.setText("Application internal error");
        }
      }
    }
    printCodedWithExceptions(ack, cwe);
  }

  public static void makeERRSegment(StringBuilder ack, Reportable reportable)
  {
    CodedWithExceptions hl7ErrorCode = reportable.getHl7ErrorCode();

    ack.append("ERR||");
    ack.append(printErr3(reportable));
    // 2 Error Location
    ack.append("|");
    // 3 HL7 Error Code
    if (hl7ErrorCode == null)
    {
      hl7ErrorCode = new CodedWithExceptions();
      hl7ErrorCode.setIdentifier("0");
    }
    HL7Util.appendErrorCode(ack, reportable.getHl7ErrorCode());
    ack.append("|");
    // 4 Severity
    SeverityLevel level = reportable.getSeverity();
    ack.append(level != null ? level.getCode() : "E");
    
    ack.append("|");
    // 5 Application Error Code
    appendAppErrorCode(ack, reportable);
    ack.append("|");
    // 6 Application Error Parameter
    ack.append("|");
    // 7 Diagnostic Information
    // ack.append(escapeHL7Chars(reportable.getReportedMessage()));
    // 8 User Message
    ack.append("|");
    ack.append(escapeHL7Chars(reportable.getReportedMessage()));
    ack.append("|\r");
  }

  public static void appendAppErrorCode(StringBuilder ack, Reportable reportable)
  {
    if (reportable != null)
    {
      CodedWithExceptions cwe = reportable.getApplicationErrorCode();
      if (cwe != null)
      {
        if (cwe.hasIdentifier() && (cwe.getText() == null || cwe.getText().equals("")))
        {
          cwe.setNameOfCodingSystem("HL70533");
          if (cwe.getIdentifier().equals("1"))
          {
            cwe.setText("Illogical Date error");
          } else if (cwe.getIdentifier().equals("2"))
          {
            cwe.setText("Invalid Date");
          } else if (cwe.getIdentifier().equals("3"))
          {
            cwe.setText("Illogical Value error");
          } else if (cwe.getIdentifier().equals("4"))
          {
            cwe.setText("Invalid value");
          } else if (cwe.getIdentifier().equals("5"))
          {
            cwe.setText("Table value not found");
          } else if (cwe.getIdentifier().equals("6"))
          {
            cwe.setText("Required observation missing");
          } else if (cwe.getIdentifier().equals("7"))
          {
            cwe.setText("Required data missing");
          } else if (cwe.getIdentifier().equals("8"))
          {
            cwe.setText("Data was ignored");
          }
        }
      }
      printCodedWithExceptions(ack, cwe);
    }

  }

  private static void printCodedWithExceptions(StringBuilder ack, CodedWithExceptions cwe)
  {
    if (cwe != null)
    {
      if (cwe.hasIdentifier())
      {
        ack.append(cwe.getIdentifier());
        ack.append("^");
        ack.append(cwe.getText());
        ack.append("^");
        ack.append(cwe.getNameOfCodingSystem());
        if (cwe.hasAlternateIdentifier())
        {
          ack.append("^");
        }
      }
      if (cwe.hasAlternateIdentifier())
      {
        ack.append(cwe.getAlternateIdentifier());
        ack.append("^");
        ack.append(cwe.getAlternateText());
        ack.append("^");
        ack.append(cwe.getNameOfAlternateCodingSystem());
      }
    }
  }

  private static String printErr3(Reportable reportable)
  {
	StringBuilder ack = new StringBuilder();
    boolean repeating = false;
    if (reportable.getHl7LocationList() != null)
    {
      for (ErrorLocation errorLocation : reportable.getHl7LocationList())
      {
        if (errorLocation.hasSegmentId())
        {
          if (repeating)
          {
            ack.append("~");
          }
          repeating = true;
          ack.append(errorLocation.getSegmentId());
          ack.append("^");
          if (errorLocation.getSegmentSequence() == 0)
          {
            ack.append(1);
          } else
          {
            ack.append(errorLocation.getSegmentSequence());
          }

          if (errorLocation.hasFieldPosition())
          {
            ack.append("^");
            ack.append(errorLocation.getFieldPosition());
            ack.append("^");
            if (errorLocation.getFieldRepetition() == 0)
            {
              ack.append(1);
            } else
            {
              ack.append(errorLocation.getFieldRepetition());
            }
            if (errorLocation.hasComponentNumber())
            {
              ack.append("^");
              ack.append(errorLocation.getComponentNumber());
              if (errorLocation.hasSubComponentNumber())
              {
                ack.append("^");
                ack.append(errorLocation.getSubComponentNumber());
              }
            }
          }
        }
      }
    }
    return ack.toString();
  }

  public static void makeERRSegment(StringBuilder ack, String severity, String hl7ErrorCode, String textMessage, Reportable reportable)
  {

    if (severity.equals("E") && StringUtils.isBlank(hl7ErrorCode))
    {
      hl7ErrorCode = "102";
    }
    ack.append("ERR||");
    // 2 Error Location
    ack.append(reportable.getHl7LocationList()!= null && reportable.getHl7LocationList().size() > 0 ? reportable.getHl7LocationList().get(0) : "");
    ack.append("|");
    // 3 HL7 Error Code
    HL7Util.appendErrorCode(ack, reportable.getHl7ErrorCode());
    ack.append("|");
    // 4 Severity
    ack.append(severity);
    ack.append("|");
    // 5 Application Error Code
    HL7Util.appendAppErrorCode(ack, reportable);
    ack.append("|");
    // 6 Application Error Parameter
    ack.append("|");
    // 7 Diagnostic Information
    ack.append("|");
    // 8 User Message
    ack.append(escapeHL7Chars(textMessage));
    ack.append("|\r");

  }

  public static String escapeHL7Chars(String s)
  {
    if (s == null)
    {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (char c : s.toCharArray())
    {
      if (c >= ' ')
      {
        switch (c) {
        case '~':
          sb.append("\\R\\");
          break;
        case '\\':
          sb.append("\\E\\");
          break;
        case '|':
          sb.append("\\F\\");
          break;
        case '^':
          sb.append("\\S\\");
          break;
        case '&':
          sb.append("\\T\\");
          break;
        default:
          sb.append(c);
        }
      }
    }
    return sb.toString();
  }

}
