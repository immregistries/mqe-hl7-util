package org.immregistries.dqa.hl7util.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.immregistries.dqa.hl7util.parser.model.HL7MessagePart;
import org.junit.Test;
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = ParserAppConfig.class)
public class MessageNormalizerTest {

//	@Autowired
	MessageParser mn = new MessageParserHL7();
	
	@Test
	public void test() throws Exception {
		String message = getTestString();
		assertNotNull("The String should not be null.  there should be at least one message in that file", message);
		List<HL7MessagePart> list = mn.getMessagePartList(message);
		assertTrue("There sould be a bunch of parts. like 50 or more", 50 < list.size());
	}
	
	static String getTestString() throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get("src/test/resources/hl7DtapMessage.txt"));
		return new String(encoded, StandardCharsets.UTF_8);
	}

}
