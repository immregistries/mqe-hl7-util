package org.immregistries.mqe.hl7util.parser.profile.generator;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import java.util.regex.Pattern;

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
 * This class <strong>requires</strong> there to be a <strong>immunization-profile.xml</strong> file in the project at the classpath root. This is typically in the <strong>src/main/resources</strong> folder. If this file is not present
 * the parser will still work, but it will not have any complexity information, so everything will be stored at the complexity level that is represented
 * in the message.<br /><br />
 * @author Josh
 *
 */
public class MessageProfileReaderNIST implements MessageProfileReader {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageProfileReaderNIST.class);

  private static final MessageProfile PROFILE = new MessageProfileChooser()
      .getGeneratedMessageProfile();

  public String getDatatype(String dataLocation) {
    String datatype = PROFILE.getFieldDataTypeMap().get(dataLocation);

    if (datatype == null) {
      datatype = "unknown";
    }
    return datatype;
  }

  public String getFieldDescription(String location) {
    String description = PROFILE.getFieldDescriptionMap().get(location);
    if (description == null) {
      String parentLoc = StringUtils.substringBeforeLast(location, "-");
//			LOGGER.info("Parent loc: " + parentLoc);
      if (parentLoc != null && parentLoc.contains("-")) {
        description = PROFILE.getFieldDescriptionMap().get(parentLoc);
        if (description != null) {
          description = description + " - component";
        }
      }
    }
    return description;
  }

  public FieldComplexity getDatatypeComplexity(String datatype) {
    FieldComplexity complexity = PROFILE.getDataTypeComplexityMap().get(datatype);

    if (complexity == null) {
      complexity = FieldComplexity.UNKNOWN;
    }

    return complexity;
  }

  public FieldComplexity getComplexity(String dataLocation) {
//		LOGGER.info("profile:" + profile);
    Map<String, String> fieldDataType = PROFILE.getFieldDataTypeMap();
    String dataType = fieldDataType.get(dataLocation);
    FieldComplexity complexity = getDatatypeComplexity(dataType);
    return complexity;
  }


}
