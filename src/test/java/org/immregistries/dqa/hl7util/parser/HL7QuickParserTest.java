package org.immregistries.dqa.hl7util.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HL7QuickParserTest {

	private HL7QuickParser quick = HL7QuickParser.INSTANCE;
	 
	private static final String MSH = "MSH|^~\\&|ECW|1337-44-01|MCIR|MDCH|20140619191115||VXU^V04|61731|P|2.3.1|||AL\r";
	private static final String MSH2 = "MSH|^~\\&|MQE Message Hub App|MQE Facility|||20180504130832-0400||ACK^V04^ACK|20180504130832-0400.1131|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r";

	@Test
	public void testIsProperlyFormedSegment() {
		String date = quick.getMsh7MessageDate(MSH);
		assertEquals("Should match and find", "20140619191115", date);
	}

	@Test
	public void testMSH4() {
		String sender = quick.getMsh4Sender(MSH);
		assertEquals("Should match and find", "1337-44-01", sender);
		sender = quick.getMsh4Sender(MSH2);
		assertEquals("Should match and find", "MQE Facility", sender);
	}
}
