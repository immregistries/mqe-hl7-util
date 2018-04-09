package org.immregistries.dqa.hl7util.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HL7QuickParserTest {

	private HL7QuickParser quick = HL7QuickParser.INSTANCE;
	 
	private static final String MSH = "MSH|^~\\&|ECW|1337-44-01|MCIR|MDCH|20140619191115||VXU^V04|61731|P|2.3.1|||AL\r";

	@Test
	public void testIsProperlyFormedSegment() {
		String date = quick.getMsh7MessageDate(MSH);
		assertEquals("Should match and find", "20140619191115", date);
	}
}
