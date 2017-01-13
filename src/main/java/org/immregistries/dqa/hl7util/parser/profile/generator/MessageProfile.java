package org.immregistries.dqa.hl7util.parser.profile.generator;

import java.util.Map;

public interface MessageProfile {
	/**
	 * @return the fieldDatatypeMap
	 */
	public Map<String, String> getFieldDataTypeMap();
	
	/**
	 * @return the fieldDatatypeMap
	 */
	public Map<String, String> getFieldDescriptionMap();
	
	/**
	 * @return the datatypeComplexityMap
	 */
	public Map<String, FieldComplexity> getDataTypeComplexityMap();
	
	public void populateProfileMap();
}
