package org.immregistries.mqe.vxu.parse;

import static org.immregistries.mqe.vxu.VxuField.MESSAGE_ACCEPT_ACK_TYPE;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_ALT_CHARACTER_SET;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_APP_ACK_TYPE;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_CHARACTER_SET;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_CONTROL_ID;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_COUNTRY_CODE;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_DATE;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_PROCESSING_ID;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_PROFILE_ID;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_RECEIVING_APPLICATION;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_RECEIVING_FACILITY;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_SENDING_APPLICATION;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_SENDING_FACILITY;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_STRUCTURE;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_TRIGGER;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_TYPE;
import static org.immregistries.mqe.vxu.VxuField.MESSAGE_VERSION;

import org.immregistries.mqe.hl7util.parser.HL7MessageMap;
import org.immregistries.mqe.vxu.MqeMessageHeader;

public enum HL7MessageHeaderParser {

  INSTANCE;

  /**
   * Reads the MSH fields and builds a Message Header object from them.
   *
   * @param map map of values from the message!
   * @return object filled with values!
   */
  public MqeMessageHeader getMessageHeader(HL7MessageMap map) {

    MetaParser mp = new MetaParser(map);

    MqeMessageHeader h = new MqeMessageHeader();
    h.setFields(mp.mapValues(1,
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
