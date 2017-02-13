package org.immregistries.dqa.vxu.parse;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaMessageHeader;

public enum HL7MessageHeaderParser {
	INSTANCE;
	
	/**
	 * Reads the MSH fields and builds a Message Header object from them. 
	 * @param map
	 * @return
	 */
	public DqaMessageHeader getMessageHeader(HL7MessageMap map) {
		
	    String sendingApp 		= map.get("MSH-3");//getMSH3SendingApplication(map);
	    String sendingFacility 	= map.get("MSH-4");//getMSH4SendingFacility(map);
	    String receivingApp 	= map.get("MSH-5");//getMSH5ReceivingApplication(map);
	    String recevingFacility = map.get("MSH-6");//getMSH6ReceivingFacility(map);
	    String sentDate 		= map.get("MSH-7");//getMSH7DateTime(map);
	    String messageType 		= map.get("MSH-9-1");//getMSH9_1MessageType(map);
	    String messageTrigger 	= map.get("MSH-9-2");//getMSH9_2MessageTrigger(map);
	    String messageStructure = map.get("MSH-9-3");//getMSH9_3MessageStructure(map);
	    String messageControlId = map.get("MSH-10");//getMSH10MessageControlId(map);
	    String processingId 	= map.get("MSH-11");//getMSH11ProcessingId(map);
	    String versionId 		= map.get("MSH-12");//getMSH12VersionId(map);
	    String ackTypeAcceptCode= map.get("MSH-15");//getMSH15AcceptCode(map);
	    String messageAckType 	= map.get("MSH-16");//getMSH16ApplicationAcknowledgementType(map);
	    String countryCode 		= map.get("MSH-17");//getMSH17CountryCode(map);
	    String charSetCode 		= map.get("MSH-18");//getMSH18CharacterSet(map);
	    String charSetAltCode 	= map.get("MSH-20");//getMSH20CharacterSetAltCode(map);
	    String messageProfile 	= map.get("MSH-21");//getMSH21MessageProfileIdentifier(map);
	    
	    DqaMessageHeader h = new DqaMessageHeader();
	    h.setSendingApplication(sendingApp);
	    h.setSendingFacility(sendingFacility);
	    h.setReceivingApplication(receivingApp);
	    h.setReceivingFacility(recevingFacility);
	    h.setMessageDateString(sentDate);
	    h.setMessageType(messageType);
	    h.setMessageTrigger(messageTrigger);
	    h.setMessageStructure(messageStructure);
	    h.setMessageControl(messageControlId);
	    h.setProcessingIdCode(processingId);
	    h.setMessageVersion(versionId);
	    h.setAckTypeAcceptCode(ackTypeAcceptCode);
	    h.setAckTypeApplicationCode(messageAckType);
	    h.setCountryCode(countryCode);
	    h.setCharacterSetCode(charSetCode);
	    h.setCharacterSetAltCode(charSetAltCode);
	    h.setMessageProfile(messageProfile);
	    
	    return h;
	}
}
