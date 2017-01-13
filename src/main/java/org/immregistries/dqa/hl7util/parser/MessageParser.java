package org.immregistries.dqa.hl7util.parser;

import java.util.List;

import org.immregistries.dqa.hl7util.parser.model.HL7MessagePart;

public interface MessageParser {
	
	public abstract List<HL7MessagePart> getMessagePartList(String message);
	
	public abstract HL7MessageMap getMessagePartMap(String message);
	
	public abstract HL7MessageMap getMessageMapFromPartList(List<HL7MessagePart> list);
	
}