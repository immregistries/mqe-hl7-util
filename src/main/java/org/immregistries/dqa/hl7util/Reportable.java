package org.immregistries.dqa.hl7util;

/**
 * The intention is to generalize 
 * the interface for the various types of things we report. 
 * @author Josh Hull
 */
public interface Reportable {
	String getSeverity();//expects I/W/E/A according to the IssueLevel.java class. 
	int getHl7ErrorCode();//This is 0 for success, and there's a whole table for errors. 
	String getHl7Location();
	String getReportedMessage();
}
