package org.immregistries.dqa.vxu.parse;

import java.util.List;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.hl7util.parser.MessageParserHL7;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.DqaMessageReceived;
import org.immregistries.dqa.vxu.DqaNextOfKin;
import org.immregistries.dqa.vxu.DqaPatient;
import org.immregistries.dqa.vxu.DqaVaccination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ul>
 * <li>This class will put data into the transfer message objects, given a message.
 * <li>Declaration:  This class will not get data from any external sources.  Any data transformations will happen in other classes, as augmentation
 * after this class is invoked.  This will give you ONLY what's in the message, and will not look up anything, or add it it. 
 * it WILL interpret the information that exists in the message so that the values are put into the appropriate places in the object model. Keep in mind
 * this strategic difference between transforming the data V.S. Interpreting the data.  
 * <br /><br />
 *  
 * Those objects can then be used to manipulate the MCIR data for the patient... 
 * so this was step 1.  Get the data out of the message into a holding place. 
 * Theoretically, the holding place could have been dropped right into the mcir objects
 * like CHILD, VACCINE, etc.'
 * This code was largely taken from  VaccinationUpdateToExtLoader.java and another class HL7Converter.java
 * 
 * THe portion that is basic data gathering is also pulled from SQL/PL packages as noted in the methods. 
 * 
 * TODO: still need to do the processing that gest done to populate the 
 * transfer data in the SQL/PL packages.  for these three in particular
 * p_populate_transfer_shot
 * p_populate_transfer_child
 * p_populate_transfer_resp_party
 * That should be accomplished in the next classes. 
 * 
 * 
 * I think the next step is to take the object, and then make a class to decide what to do with the information. 
 * 
 * SO - MAKE A NEW CLASS TO TAKE THIS INFO AND DO STUFF WITH IT. 
 * STUFF += update, add, delete from MCIR
 * STUFF += update VFC by calling the VFC stored PROC. 
 * 
 * Random thought:  this could be made more open-source friendly by using a value map...  just a map of <String,String>
 * you'd store it like this: <"patient.name.first", "George">...  but there'd have to be a standard reference model.  Like an ENUM. 
 * then you could just use the enum to map that junk: <PATIENT_FIRST_NAME, "George">
 *  
 * Then in the processor, you'd figure out what to do, and populate the right objects to do that, and execute. 
 * so you'd get the action code.  Then you'd pick all the pieces you need to do the person lookup, and find the MCIR person. 
 * Then you'd have the objects, and update that, and persist those changes.  Boom.  Awesome. 
 * Updates - just get the object from the DBB, and update it, and persist changes. 
 * Adds - create a new MCIR object, and persist it.  
 * With deletes, you'd do even less.  just find the vaccine, and delete it.
 * 
 * Right now it's all in TRANSFER_ objects, which may or may not be useful...  they're a part of the previous system. 
 * changing to the Map<MessagePartsEnum, String> would loosen it up a bit. 
 * 
 *  Could even validate off that in DQA. The enum could be used to map the HL7 parts to the business parts. 
 *  and then you could have references to put into your dbb of rules.  Yes.  this is good. 
 *  
 *  And the Enum could be generated from a database table of locations.  a location map so to speak.
 *    
 *  I guess the problem is the repeating entities.  HL7 attributes can repeat...  So you'd either have to have a nested model. Or just... go with objects. 
 *  And while i'm thinking about this...  why even do it?  what's wrong with an object model?  I guess it can get bloated. there's nothing documented about it. 
 *  Anyway...  I'll think on that.  There's nothing particularly hard about one or the other. 
 *  
 *   that part's really all about the message pre-processor. 
 *  
 * @author Josh Hull
 *
 */
public enum HL7MessageParser  {
	INSTANCE;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(HL7MessageParser.class);
	
	private MessageParserHL7 parser = new MessageParserHL7();
	
	private HL7MessageHeaderParser mshParser = HL7MessageHeaderParser.INSTANCE;
	private HL7PatientParser patientParser = HL7PatientParser.INSTANCE;
	private HL7NokParser nokParser = HL7NokParser.INSTANCE;
	private HL7VaccineParser vaccineParser = HL7VaccineParser.INSTANCE;
	
	
	
	/**
	 * Extracts all the values from the message into objects that can be used to 
	 * process the message. 
	 * @param map
	 * @return filled out MessageParts
	 */
	public DqaMessageReceived extractMessageFromText(String message) {
		HL7MessageMap map = parser.getMessagePartMap(message);
		DqaMessageReceived mr = extractFromMessage(map);
		return mr;
	}
	
	protected DqaMessageReceived extractFromMessage(HL7MessageMap map) {
		DqaMessageReceived container = new DqaMessageReceived();

		//Message Header
		DqaMessageHeader header = mshParser.getMessageHeader(map);
		container.setMessageHeader(header);
		
		//Child from message
		DqaPatient child = patientParser.getPatient(map);
		container.setPatient(child);
		
		//Shots from message
		List<DqaVaccination> shots = vaccineParser.getVaccinationList(map);
		container.setVaccinations(shots);
		
		//Resp Parties from message.
		List<DqaNextOfKin> respParties = nokParser.getNk1List(map);
		container.setNextOfKins(respParties);
		
		return container;
	}
	  
}
