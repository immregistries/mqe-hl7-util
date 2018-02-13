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
 * <li>This class will parse a VXU message and put it into objects that represent the business concepts.

 * <li>Declaration:  This class will not get data from any external sources.  Any data transformations will happen in other classes, as augmentation
 * after this class is invoked.  This will give you ONLY what's in the message, and will not look up anything, or add it it.
 * it WILL interpret the information that exists in the message so that the values are put into the appropriate places in the object model. Keep in mind
 * this strategic difference between transforming the data V.S. Interpreting the data.
 * <br /><br />
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
	 * @param message VXU text
	 * @return a set of objects representing the business concepts
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
