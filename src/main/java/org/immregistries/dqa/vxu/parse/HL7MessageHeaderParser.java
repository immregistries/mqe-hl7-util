package org.immregistries.dqa.vxu.parse;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import org.immregistries.dqa.vxu.VxuField;

public enum HL7MessageHeaderParser {
	INSTANCE;

	/**
	 * Reads the MSH fields and builds a Message Header object from them. 
	 * @param map map of values from the message!
	 * @return object filled with values!
	 */
	public DqaMessageHeader getMessageHeader(HL7MessageMap map) {
	    
	    MetaParser mp = new MetaParser(map);
	    DqaMessageHeader h = new DqaMessageHeader();
	    h.setField(mp.mapValue(VxuField.MESSAGE_SENDING_APPLICATION));
        h.setField(mp.mapValue(VxuField.MESSAGE_SENDING_FACILITY));
        h.setField(mp.mapValue(VxuField.MESSAGE_RECEIVING_APPLICATION));
        h.setField(mp.mapValue(VxuField.MESSAGE_RECEIVING_FACILITY));
        h.setField(mp.mapValue(VxuField.MESSAGE_DATE));
        h.setField(mp.mapValue(VxuField.MESSAGE_TYPE));
        h.setField(mp.mapValue(VxuField.MESSAGE_TRIGGER));
        h.setField(mp.mapValue(VxuField.MESSAGE_STRUCTURE));
        h.setField(mp.mapValue(VxuField.MESSAGE_CONTROL_ID));
        h.setField(mp.mapValue(VxuField.MESSAGE_PROCESSING_ID));
        h.setField(mp.mapValue(VxuField.MESSAGE_VERSION));
        h.setField(mp.mapValue(VxuField.MESSAGE_ACCEPT_ACK_TYPE));
        h.setField(mp.mapValue(VxuField.MESSAGE_APP_ACK_TYPE));
        h.setField(mp.mapValue(VxuField.MESSAGE_COUNTRY_CODE));
        h.setField(mp.mapValue(VxuField.MESSAGE_CHARACTER_SET));
        h.setField(mp.mapValue(VxuField.MESSAGE_ALT_CHARACTER_SET));
        h.setField(mp.mapValue(VxuField.MESSAGE_PROFILE_ID));
	    
	    return h;
	}
}
