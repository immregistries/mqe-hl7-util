package org.immregistries.mqe.hl7util.test;

import org.immregistries.mqe.hl7util.model.Hl7Location;
import org.immregistries.mqe.hl7util.parser.HL7MessageMap;
import org.immregistries.mqe.hl7util.parser.MessageParser;
import org.immregistries.mqe.hl7util.parser.MessageParserHL7;

public enum QuickMessageModifier {
  INSTANCE;
  private MessageParser mpp = new MessageParserHL7();

  public String changeMessage(String message, String location, String newValue) {
    Hl7Location loc = new Hl7Location(location);
    return changeMessage(message, loc, newValue);
  }

  public String changeMessage(String message, Hl7Location location, String newValue) {
    HL7MessageMap map = mpp.getMessagePartMap(message);
    map.put(location, newValue);
    return map.reassemble();
  }
}
