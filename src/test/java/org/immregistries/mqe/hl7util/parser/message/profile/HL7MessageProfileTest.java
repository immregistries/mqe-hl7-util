package org.immregistries.mqe.hl7util.parser.message.profile;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.immregistries.mqe.hl7util.parser.profile.generator.FieldComplexity;
import org.immregistries.mqe.hl7util.parser.profile.generator.MessageProfileReader;
import org.immregistries.mqe.hl7util.parser.profile.generator.MessageProfileReaderNIST;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = ParserAppConfig.class)
public class HL7MessageProfileTest {

	/*
	 * Tests intially: 
	 * 	if you construct this without a profile input, then it should get defaults. 
	 * 	Default should be a learning profile. 
	 * 	Learning profile: 
	 * 		Field is as complex as the most complex field encountered.  
	 * 		
	 * 
	 *  Other profiles:  
	 *  	basic definition is whether the field is complex. That's the only thing neccessary so far. 
	 *  	each field locator would have an indicator of whether its simple or complex. 
	 *  	but that could be done at the datatype level.  So then each locator could indicate the data type. 
	 *  	The value in using the datatype is that it's in line with all of the published materials.  It would 
	 *  	be possible to import a profile and use it to parse the message if we used the same notation, which the datatypes would allow.  
	 *  	I like that...
	 *  
	 *  So start with an unknown field, how to handle: 
	 * 	1. look it up in the map. 
	 * 		NOT FOUND
	 * 	2. Put an entry in the map: field location + default datatype (ST). 
	 *  	Complex = UNKN_CPLX
	 *  	Simple  = UNKN_SMPL
	 *  3. Each location gets its own entry.  So for a UNKN_CPLX, it can have any number of components.The number is unimportant. Each component will have to define if its a ST or UNKN_CPLX
	 *  
	 *  So let's say RXA-40 = "this^is^complex"
	 *     new entry gets put for RXA-40:
	 *     		RXA-40, UNKN_CPLX
	 *     Then it gets parsed into sub-parts: 
	 *     		RXA-41 = "this". 
	 *     new entry gets put for RXA-40-1:
	 *     		RXA-40-1 = UNKN_SMPL
	 *     From then on, all RXA-40's get treated as complex. 
	 *     
	 *     So let's test that out: 
	 * 	
	 */
	private MessageProfileReader h7p = new MessageProfileReaderNIST();;
	
	@Test
	public void testGetUnknownFieldProfile() {
		FieldComplexity complexity = h7p.getComplexity("RXA-40");
		assertEquals("RXA-40", FieldComplexity.UNKNOWN, complexity);
		
		complexity = h7p.getComplexity("RXA-17-2");
		assertEquals("RXA-17-2", FieldComplexity.SIMPLE, complexity);
		
		complexity = h7p.getComplexity("RXA-17");
		assertEquals("RXA-17", FieldComplexity.COMPLEX, complexity);
		
		complexity = h7p.getComplexity("MSH-3-1");
		assertEquals("MSH-3-1", FieldComplexity.SIMPLE, complexity);
		
		complexity = h7p.getComplexity("MSH-0");
		assertEquals("MSH-3-1", FieldComplexity.SIMPLE, complexity);
		
		complexity = h7p.getComplexity("MSH-1");
		assertEquals("MSH-3-1", FieldComplexity.SIMPLE, complexity);
		
		complexity = h7p.getComplexity("OBX-5");
		assertEquals("MSH-3-1", FieldComplexity.UNKNOWN, complexity);
	}
	
	@Test
	public void testGetDescription() {
		String description = h7p.getFieldDescription("RXA-5");
		assertEquals("well...  should be equal", "Administered Code", description);
		
		description = h7p.getFieldDescription("OBX-5-1");
		assertEquals("well...  should be equal", "Observation Value - component", description);
	}
	
	@Test 
	public void testGetParentLocation() {
		String loc = "OBX-5-1";
		String parentLoc = StringUtils.substringBeforeLast(loc, "-");
		assertEquals("parentloc should have one less node", "OBX-5", parentLoc);
	}
	
	@Test
	public void testGetFieldDatatype() {
		String datatype = h7p.getDatatype("RXA-17-2");
		assertEquals("RXA-17-2", "ST", datatype);
	}
}
