package org.immregistries.dqa.hl7util.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

public class MessageParserApplicationTest {

	MessageParser hnp = new MessageParserHL7();
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void fileExists() {
		File msgFile = new File("src/test/resources/hl7DtapMessage.txt");
		assertEquals("File should exists", true, msgFile.exists());
	}
//	
	@Test
	public void fileHasContents() throws IOException {
		String message = getTestString();
		assertNotNull("the test data should be complete. It is null", message);
	}
	
	static String getTestString() throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get("src/test/resources/hl7DtapMessage.txt"));
		return new String(encoded, StandardCharsets.UTF_8);
	}

}
