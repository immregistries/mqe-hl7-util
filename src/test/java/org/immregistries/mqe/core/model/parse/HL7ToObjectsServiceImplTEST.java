package org.immregistries.mqe.core.model.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.immregistries.mqe.core.TestMessageGenerator;
import org.immregistries.mqe.vxu.MqeMessageHeader;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.parse.HL7MessageHeaderParser;
import org.immregistries.mqe.vxu.parse.HL7MessageParser;
import org.junit.Before;
import org.junit.Test;

public class HL7ToObjectsServiceImplTEST {

	TestMessageGenerator genr = new TestMessageGenerator();
	HL7MessageParser toObj = HL7MessageParser.INSTANCE;
	private HL7MessageHeaderParser mshParser = HL7MessageHeaderParser.INSTANCE;
	MqeMessageReceived mr;
	
	@Before
	public void setUp() {
		String msg = genr.getExampleVXU_1();
		this.mr = toObj.extractMessageFromText(msg);
	}
	
	@Test
	public void itDoesnFail() {
		assertTrue(this.mr != null);
	}
	
	@Test
	public void mshHasExpectedParts() {
		MqeMessageHeader msh = this.mr.getMessageHeader();
		assertNotNull(msh);
		assertEquals("date", "20140619191115", msh.getMessageDateString());
		assertEquals("message identifier", "61731", msh.getMessageControl());
		assertEquals("pin", "1337-44-01", msh.getSendingFacility());
	}
	
	@Test
	public void pidHasExpectedParts() {
		MqePatient pid = this.mr.getPatient();
		
		assertFalse(pid==null);
		assertEquals("birthDate", "20120604", pid.getBirthDateString());
		assertEquals("race", "RACECD", pid.getRaceCode());
		assertEquals("address street", "100 Main", pid.getPatientAddress().getStreet());
		assertEquals("MR", "23456", pid.getIdSubmitterNumber());
	}

	@Test
	public void showMessage() {
		System.out.println(mr);
	}

}
