package org.immregistries.dqa.hl7util.parser.profile.generator;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
//import java.util.regex.Pattern;





import org.immregistries.dqa.hl7util.parser.profile.ack.generated.AckConformanceProfile;
import org.immregistries.dqa.hl7util.parser.profile.ack.generated.AckConformanceProfile.Segments;
import org.immregistries.dqa.hl7util.parser.profile.ack.generated.AckObjectFactory;
import org.immregistries.dqa.hl7util.parser.profile.generated.HL7V2XConformanceProfile;
import org.immregistries.dqa.hl7util.parser.profile.generated.SegmentGroup;
import org.immregistries.dqa.hl7util.parser.profile.generated.StaticDef;
import org.immregistries.dqa.hl7util.parser.profile.intf.Field;
import org.immregistries.dqa.hl7util.parser.profile.intf.HL7Component;
import org.immregistries.dqa.hl7util.parser.profile.intf.HL7Segment;
import org.immregistries.dqa.hl7util.parser.profile.intf.HL7SubComponent;
import org.immregistries.dqa.hl7util.parser.profile.intf.Segment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class reads the immunization-profile.xml provided by NIST at this url:<br />
 * <a href="http://hl7v2-iz-testing.nist.gov/mu-immunization/">http://hl7v2-iz-testing.nist.gov/mu-immunization/</a><br /><br />
 * It is designed to answer questions about segments, fields, and components based on the information on the profile.xml. 
 * <br />When a complex data field only has the first component, sometimes the message will come through with <strong>just the value, and no field/component separators.</strong>  For example, a coded element might come through as: <br /><br />
 * <code><strong>1234</strong></code> instead of <code><strong>1234^^</strong></code><br /><br />
 * And we want to store the value as specifically as we can.  So if the above field is in MSH-3, the address of that data item should be <strong>MSH-3-1</strong> regardless of whether they include the component separators. <br /><br />
 *  
 * We make it so that the first component is always addressed at the root level. <br /><br />
 * <ul>
 * 	<li>Benefits: 
 * 		<ul><li>Don't have two addresses for one field
 * 		<li>it's clear how to find a field
 * 		<li>it's clear how to store it.  No need to find out if its a complex field if there are no delimiters. If there are no delimiters, then it can be given the root address.  
 * 		</ul>
 *  <li>Drawbacks: 
 *  	<ul><li>It's not intuitive.  You have to know that strategy was taken in order to know how to find the first field in a complex field.
 *  	</ul>
 *  </ul>  
 * <br />
 * This class <strong>requires</strong> there to be a <strong>immunization-profile.xml</strong> file in the project at the classpath root. This is typically in the <strong>src/main/resources</strong> folder. <br /><br />
 * @author Josh
 *
 */
public class MessageProfileSourceV2 implements MessageProfile {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProfileSourceV2.class);
	
	/**
	 * These maps contain the profile information for each segment. 
	 */
	private final Map<String, String> fieldDataTypeMap = new HashMap<String, String>();
	
	/**
	 * This contains complexity information for each datatype encountered.  
	 */
	private final Map<String, FieldComplexity> dataTypeComplexityMap = new HashMap<String, FieldComplexity>();

	private Map<String, String> fieldDescriptionMap = new HashMap<String, String>();
	
	public MessageProfileSourceV2(String type) {
		switch (type) {
		case "ACK":
			this.populateAckProfileMap("/V2/ACK-Z23_Profile.xml");
			break;
		case "QBP":
			this.populateAckProfileMap("/V2/QBP-Z44_Profile.xml");
			break;
		default:
			this.populateVXUProfileMap("/V2/VXU-Z22_Profile.xml");
			break;
		}
	}
	
	public MessageProfileSourceV2() {
		this.populateProfileMap();
	}
	
	public void populateProfileMap() {
		//deafult to doing the VXU map. 
		this.populateVXUProfileMap("/immunization-profile.xml");
	}
	
	
	/**
	 * Reads the NIST XML file describing the HL7 profile of an immunization message. 
	 */
	public void populateAckProfileMap(String fileName) {
		
		//This procedure is reading in an XML file that contains the HL7 profile.
		//See this link for info on how this works: 
		//http://stackoverflow.com/questions/11611704/access-files-and-folders-in-executable-jars
//		InputStream is = MessageProfileXML.class.getResourceAsStream("/immunization-profile.xml");
		InputStream is = MessageProfileSourceV2.class.getResourceAsStream(fileName);
		
		if (is == null) {
			LOGGER.warn("immunization-profile.xml is not present.  Using basic message profile");
			} else {
			LOGGER.info("Found immunization-profile.xml.  Parsing.");
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(AckConformanceProfile.class, AckObjectFactory.class);
				Unmarshaller jaxbUM = jaxbContext.createUnmarshaller();
				AckConformanceProfile hcp = (AckConformanceProfile) jaxbUM.unmarshal(is);
				
				//first map datatypes???
				hcp.getDatatypes();
				for (AckConformanceProfile.Datatypes.Datatype dt : hcp.getDatatypes().getDatatype()) {
					if (this.dataTypeComplexityMap.get(dt.getName()) == null) {
						boolean complex = dt.getContent().size() > 0;
						dataTypeComplexityMap.put(dt.getName(),  (complex ? FieldComplexity.COMPLEX : FieldComplexity.SIMPLE));
					}
					
				}
				
				Segments segObj = hcp.getSegments();
				for (Segment s : segObj.getSegment()) {
					mapOldStyleSegment(s);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		//these fields are critical in parsing the message correctly.  
		//we need at least these values no matter what:
//			FIELD_COMPLEXITY_MAP.put("MSH-1", FieldComplexity.SIMPLE);
		fieldDataTypeMap.put("MSH-1", "ST");
//			FIELD_COMPLEXITY_MAP.put("MSH-2", FieldComplexity.SIMPLE);
		fieldDataTypeMap.put("MSH-2", "ST");
		
		//OBX-5 is a "varies" datatype, meaning it's defined by OBX-3. 
		//To be completely accurate, you would get the datatype name from OBX-3, and then 
		//look up the complexity of that datatype.  Typically it's a CE element, which has components.  We'll
		//just let it get coded out based on the delimiters by forcing it to be coded "unknown"
//			FIELD_COMPLEXITY_MAP.put("OBX-5",  FieldComplexity.UNKNOWN);
		fieldDataTypeMap.put("OBX-5", "variable");
		
		dataTypeComplexityMap.put("variable", FieldComplexity.UNKNOWN);
		dataTypeComplexityMap.put("unknown", FieldComplexity.UNKNOWN);
		
//		for (String key : FIELD_MAP.keySet()) {
//			System.out.println(key + " " + FIELD_MAP.get(key));
//		}
	}
	
	/**
	 * Reads the NIST XML file describing the HL7 profile of an immunization message. 
	 */
	public void populateVXUProfileMap(String fileName) {
		
		//This procedure is reading in an XML file that contains the HL7 profile.
		//See this link for info on how this works: 
		//http://stackoverflow.com/questions/11611704/access-files-and-folders-in-executable-jars
//		InputStream is = MessageProfileXML.class.getResourceAsStream("/immunization-profile.xml");
		InputStream is = MessageProfileSourceV2.class.getResourceAsStream(fileName);
		
		if (is == null) {
			LOGGER.warn("immunization-profile.xml is not present.  Using basic message profile");
			} else {
			LOGGER.info("Found immunization-profile.xml.  Parsing.");
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(HL7V2XConformanceProfile.class);
				Unmarshaller jaxbUM = jaxbContext.createUnmarshaller();
				HL7V2XConformanceProfile hcp = (HL7V2XConformanceProfile) jaxbUM.unmarshal(is);
				
				StaticDef sd = hcp.getHL7V2XStaticDef();
				
				for (HL7Segment s : sd.getSegment()) {
					mapOldStyleSegment(s);
				}
				
				for (SegmentGroup sg : sd.getSegGroup()) {
					for (HL7Segment s : sg.getSegment()) {
						mapOldStyleSegment(s);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		//these fields are critical in parsing the message correctly.  
		//we need at least these values no matter what:
//			FIELD_COMPLEXITY_MAP.put("MSH-1", FieldComplexity.SIMPLE);
		fieldDataTypeMap.put("MSH-1", "ST");
//			FIELD_COMPLEXITY_MAP.put("MSH-2", FieldComplexity.SIMPLE);
		fieldDataTypeMap.put("MSH-2", "ST");
		
		//OBX-5 is a "varies" datatype, meaning it's defined by OBX-3. 
		//To be completely accurate, you would get the datatype name from OBX-3, and then 
		//look up the complexity of that datatype.  Typically it's a CE element, which has components.  We'll
		//just let it get coded out based on the delimiters by forcing it to be coded "unknown"
//			FIELD_COMPLEXITY_MAP.put("OBX-5",  FieldComplexity.UNKNOWN);
		fieldDataTypeMap.put("OBX-5", "variable");
		
		dataTypeComplexityMap.put("variable", FieldComplexity.UNKNOWN);
		dataTypeComplexityMap.put("unknown", FieldComplexity.UNKNOWN);
		
//		for (String key : FIELD_MAP.keySet()) {
//			System.out.println(key + " " + FIELD_MAP.get(key));
//		}
	}
	
	//new style...  all the fields are defined with datatypes. 
	//and you can derive the locations and descriptions from that. 
	
	/**
	 * This is where the magic happens.  This method maps an entire
	 * segment from the values pulled from the XML.  
	 * @param s
	 */
	private void mapOldStyleSegment(HL7Segment s) {
		//Put the 0 segment into the map.  
//		FIELD_COMPLEXITY_MAP.put(s.getName() + "-0", FieldComplexity.SIMPLE);
		//Put the name of the segment as the first value in the segment...
		String segmentZero = s.getName() + "-0";
		fieldDataTypeMap.put(segmentZero,  "ST");
		fieldDescriptionMap.put(segmentZero, s.getLongName());
		
		int idx = 1;
		
		for (Field f : s.getField()) {
			//the new style of XML doesn't have component under field.  it's under datatype. 
			//you have to look up the datatype to tell if it's complex.  see if it has components. 
			boolean complex = f.getComponent().size() > 0;
			String fieldName = s.getName() + "-" + idx;
			String fieldDesc = f.getName();
			mapDataLocation(complex, fieldName, fieldDesc, f.getDatatype());
			if (complex) {
				int cdx = 1;
				for (HL7Component c : f.getComponent()) {
					String componentName = fieldName + "-" + cdx;
					String componentDesc = fieldDesc + " - " + c.getName();
					boolean subComplex = c.getSubComponent().size() > 0;
					mapDataLocation(subComplex, componentName,  componentDesc, c.getDatatype());
					if (subComplex) {
						int sdx = 1;
						for (HL7SubComponent sc : c.getSubComponent()) {
							String subComponentName = componentName + "-" + sdx;
							String subComponentDesc = componentDesc + " - " + sc.getName();
							mapDataLocation(false, subComponentName, subComponentDesc, sc.getDatatype());
							sdx++;
						}
					}
					cdx++;
				}
			}
			idx++;
		}
	}
	
	/**
	 * @param complex
	 * @param fieldName
	 * @param datatype
	 */
	private void mapDataLocation(boolean complex, String fieldName, String fieldDesc, String datatype) {
		
		fieldDataTypeMap.put(fieldName,  datatype);
		
		FieldComplexity cplx = dataTypeComplexityMap.get(datatype);
		if (cplx == null) {
			dataTypeComplexityMap.put(datatype,  (complex ? FieldComplexity.COMPLEX : FieldComplexity.SIMPLE));
		} else if (complex && cplx != FieldComplexity.COMPLEX) {
			dataTypeComplexityMap.put(datatype, FieldComplexity.COMPLEX);
		}
		
		fieldDescriptionMap.put(fieldName, fieldDesc);
		
	}
	
	/**
	 * @return the fieldDatatypeMap
	 */
	public Map<String, String> getFieldDataTypeMap() {
		return fieldDataTypeMap;
	}
	/**
	 * @return the datatypeComplexityMap
	 */
	public Map<String, FieldComplexity> getDataTypeComplexityMap() {
		return dataTypeComplexityMap;
	}
	
	public Map<String, String> getFieldDescriptionMap() {
		return fieldDescriptionMap;
	}
}
