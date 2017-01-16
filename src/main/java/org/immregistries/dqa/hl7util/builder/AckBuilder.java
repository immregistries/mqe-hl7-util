package org.immregistries.dqa.hl7util.builder;

import java.text.SimpleDateFormat;

/***
 * This is a place-holder for where the ack building code would land. 
 */
public enum AckBuilder {
	INSTANCE;
	//Took this SDF out of the previous code.  thought you might need it to format the dates that are in the ACK.  
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
	
	public String buildAckFrom(AckData ackDataIn) {
		//build a mighty-fine response here
		return "ACK";
	}
	
	
}
