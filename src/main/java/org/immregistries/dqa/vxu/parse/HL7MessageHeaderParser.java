package org.immregistries.dqa.vxu.parse;

import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.DqaMessageHeader;
import static org.immregistries.dqa.vxu.VxuField.*;

public enum HL7MessageHeaderParser {
	INSTANCE;

	/**
	 * Reads the MSH fields and builds a Message Header object from them. 
	 * @param map map of values from the message!
	 * @return object filled with values!
	 */
	public DqaMessageHeader getMessageHeader(HL7MessageMap map) {
	    
	    MetaParser mp = new MetaParser(map);

	    int index = map.getIndexForSegmentName("MSH");

	    DqaMessageHeader h = new DqaMessageHeader();
	    h.setFields(mp.mapValues(index,
                MESSAGE_SENDING_APPLICATION
              , MESSAGE_SENDING_FACILITY
              , MESSAGE_RECEIVING_APPLICATION
              , MESSAGE_RECEIVING_FACILITY
              , MESSAGE_DATE
              , MESSAGE_TYPE
              , MESSAGE_TRIGGER
              , MESSAGE_STRUCTURE
              , MESSAGE_CONTROL_ID
              , MESSAGE_PROCESSING_ID
              , MESSAGE_VERSION
              , MESSAGE_ACCEPT_ACK_TYPE
              , MESSAGE_APP_ACK_TYPE
              , MESSAGE_COUNTRY_CODE
              , MESSAGE_CHARACTER_SET
              , MESSAGE_ALT_CHARACTER_SET
              , MESSAGE_PROFILE_ID));
	    return h;
	}
}
