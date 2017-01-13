package org.immregistries.dqa.hl7util.builder;

/**
 * The intention is to generalize 
 * the interface for the various types of things we report. 
 * 
 * @author Josh Hull
 */
public interface Reportable {
	String getLevel();//expects I/W/E/A according to the IssueLevel.java class. 
	String getMessage();
}
