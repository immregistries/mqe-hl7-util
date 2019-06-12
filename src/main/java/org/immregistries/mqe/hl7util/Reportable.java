package org.immregistries.mqe.hl7util;

import java.util.List;
import org.immregistries.mqe.hl7util.model.CodedWithExceptions;
import org.immregistries.mqe.hl7util.model.Hl7Location;

/**
 * The intention is to generalize the interface for the various types of things we report.
 *
 * @author Josh Hull
 */
public interface Reportable {
  SeverityLevel getSeverity();//expects I/W/E/A according to the IssueLevel.java class.

  CodedWithExceptions getHl7ErrorCode();//This is 0 for success, and there's a whole table for errors. General code

  List<Hl7Location> getHl7LocationList();

  String getReportedMessage(); // ERR-8

  String getDiagnosticMessage(); // ERR-7

  CodedWithExceptions getApplicationErrorCode(); // Detailed code of the issue, CDC code & MQE Code

  ReportableSource getSource(); // The system or process that created this reportable detection
}
