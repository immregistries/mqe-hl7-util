package org.immregistries.dqa.hl7util.parser.profile.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * This class reads the immunization-profile.xml provided by NIST at this url:<br />
 * <a href="http://hl7v2-iz-testing.nist.gov/mu-immunization/">http://hl7v2-iz-testing.nist.gov/mu-immunization/</a><br /><br />
 * It is designed to generate a profile class that will answer questions about segments, fields, and components based on the information on the profile.xml. 
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
public class MessageProfileGeneratorHL7 {

  public static void main(String[] args) {
    MessageProfile mp = new MessageProfileSource("VXU");

    Map<String, FieldComplexity> complexityMap = mp.getDataTypeComplexityMap();

    Map<String, String> dataTypeMap = mp.getFieldDataTypeMap();
    Map<String, String> fieldDescMap = mp.getFieldDescriptionMap();

    DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    String date = formatter.format(new Date());
    outputClassStart(date);

    outputComplexityMap(complexityMap);
    outputDataTypeMap(dataTypeMap);
    outputFieldDescriptions(fieldDescMap);

    outputClassEnd();

  }

  private static void outputClassStart(String date) {
    System.out.println(" package org.openimmunizationsoftware.message.profile;");
    System.out.println(" ");
    System.out.println(" import java.util.HashMap;");
    System.out.println(" import java.util.Map;");
    System.out.println(" ");
    System.out.println(" import org.springframework.stereotype.Component;");
    System.out.println(" ");
    System.out.println(" @Component(value=\"generated" + date + "\")");
    System.out.println(" public class GeneratedHL7Profile" + date + " implements MessageProfile{");
    System.out.println(" ");
    System.out.println(" 	/**");
    System.out.println(" 	 * These maps contain the profile information for each segment. ");
    System.out.println(" 	 */");
    System.out.println(
        " 	private final Map<String, String> fieldDataTypeMap = new HashMap<String, String>();");
    System.out.println(" 	");
    System.out.println(" 	/**");
    System.out
        .println(" 	 * This contains complexity information for each datatype encountered.  ");
    System.out.println(" 	 */");
    System.out.println(
        " 	private final Map<String, FieldComplexity> dataTypeComplexityMap = new HashMap<String, FieldComplexity>();;");
    System.out.println(" 	/**");
    System.out.println(
        " 	 * This contains Descriptions of each field defined in the xml profile document.  ");
    System.out.println(" 	 */");
    System.out.println(
        " 	private final Map<String, String> fieldDescMap = new HashMap<String, String>();;");
    System.out.println(" 	");
    System.out.println(" 	public GeneratedHL7Profile" + date + "() {");
    System.out.println(" 		populateProfileMap();");
    System.out.println(" 	}");
    System.out.println(" 	/**");
    System.out.println(
        " 	 * This puts values into the maps.  This could possibly be in the constructor...");
    System.out
        .println(" 	 * except that in the XML version, it's going to be doing disk IO, and that");
    System.out.println(" 	 * would be terrible in a constructor. ");
    System.out.println(" 	 */");
    System.out.println(" 	public void populateProfileMap() {");
    System.out.println(" 	     populateFieldDataTypes();");
    System.out.println(" 	     populateDataTypeComplexity();");
    System.out.println(" 	     populateFieldDescriptions();");
    System.out.println("	}");
  }

  private static void outputClassEnd() {

    System.out.println("	/**");
    System.out.println("	 * @return the fieldDataTypeMap");
    System.out.println("	 */");
    System.out.println("	public Map<String, String> getFieldDataTypeMap() {");
    System.out.println("		return fieldDataTypeMap;");
    System.out.println("	}");
    System.out.println("");
    System.out.println("	/**");
    System.out.println("	 * @return the fieldDescriptionMap");
    System.out.println("	 */");
    System.out.println("	public Map<String, String> getFieldDescriptionMap() {");
    System.out.println("		return fieldDescMap;");
    System.out.println("	}");
    System.out.println("");
    System.out.println("	/**");
    System.out.println("	 * @return the dataTypeComplexityMap");
    System.out.println("	 */");
    System.out.println("	public Map<String, FieldComplexity> getDataTypeComplexityMap() {");
    System.out.println("		return dataTypeComplexityMap;");
    System.out.println("	}");
    System.out.println("}");
  }

  /**
   * @param complex
   * @param fieldName
   * @param dataType
   */
  private static void outputComplexityMap(Map<String, FieldComplexity> complexityMap) {
    System.out.println(" 	public void populateDataTypeComplexity() {");

    for (String field : complexityMap.keySet()) {
      FieldComplexity complexity = complexityMap.get(field);
      String line =
          "				dataTypeComplexityMap.put(\"" + field + "\", FieldComplexity." + complexity
              + ");";
      System.out.println(line);
    }
    System.out.println(" 	}");
  }

  /**
   * @param complex
   * @param fieldName
   * @param dataType
   */
  private static void outputDataTypeMap(Map<String, String> dataTypeMap) {

    System.out.println(" 	public void populateFieldDataTypes() {");
    int total = 0;
    int methodNum = 0;
    for (String field : dataTypeMap.keySet()) {
      String type = dataTypeMap.get(field);
      String line = " 			fieldDataTypeMap.put(\"" + field + "\", \"" + type + "\");";
      total += line.length();
      if (total > 60000) {
        System.out.println(" 			populateFieldDataTypes" + ++methodNum + "();");
        System.out.println(" 	}");
        System.out.println(" 	public void populateFieldDataTypes" + methodNum + "() {");
        total = 0;
      }
      System.out.println(line);
    }
    System.out.println(" 	}");
  }

  /**
   * @param complex
   * @param fieldName
   * @param dataType
   */
  private static void outputFieldDescriptions(Map<String, String> fieldDescs) {

    System.out.println(" 	public void populateFieldDescriptions() {");
    int total = 0;
    int methodNum = 0;
    for (String field : fieldDescs.keySet()) {
      String desc = fieldDescs.get(field);
      String line = " 			fieldDescMap.put(\"" + field + "\", \"" + desc + "\");";
      total += line.length();
      if (total > 60000) {
        System.out.println(" 			populateFieldDescriptions" + ++methodNum + "();");
        System.out.println(" 	}");
        System.out.println(" 	public void populateFieldDescriptions" + methodNum + "() {");
        total = 0;
      }
      System.out.println(line);
    }
    System.out.println(" 	}");
  }
}
