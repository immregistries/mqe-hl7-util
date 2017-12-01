package org.immregistries.dqa.hl7util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.list.TreeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HL7MessageMap {
	protected static final Logger LOGGER = LoggerFactory.getLogger(HL7MessageMap.class);
	  
	Map<String, String> locationValueMap = new HashMap<String, String>();	

	/**
	 * This is a map of the segments, and their absolute indexes.  So what we have here is: 
	 * <li>1. in the keySet, a list of all the segments. 
	 * <li>2. the absolute indexes of the message segments stored in the Integer
	 * <li>3. The ordinal for the segment, based on where the index lives in the list. This is in relation to all other segments of this type.  
	 * <br />
	 * <p><strong>Item number three requires a little extra explanation. </strong> 
	 * <li>Ordinal is in relation to other segments of the same type in the message.  So if there are 3 OBX segments in a message, the ordinal positions will be 1, 2, and 3.  But the absolute positions might be 5, 8, and 10. 
	 * <li>If there are more than one of any type of segment, the order in which they appear in the list will be the ordinal position.  
	 * <li>Going between ordinal and absolute is easy. The absolute position is stored in the Integer.  The ordinal position is derived from the position in the list.  
	 */
	Map<String, List<Integer>> segmentIndexes = new HashMap<String, List<Integer>>();
	
	/**
	 * This is a map of fields and repetitions.  It does not contain components/subcomponents. 
	 * the locations in the key will contain: seg[absSegIdx]-fld
	 */
	Map<String, Integer> fieldRepetitions = new HashMap<String, Integer>();

	private List<String> segmentMessageOrder = null;
	

	/**
	 * The basic assumption is that this method will fill in any details you don't.  
	 * The minimum required is to tell the segment and the field.  For example: 
	 * 
	 * <p><code>map.get("MSH-4");</code></p>
	 * 
	 * <p>The easy way to tell what it will return is this: If you don't provide a value, this method will look for the first. </p>
	 * 
	 * <p>If you do send something in, and it includes the segment number, it is expected that this is an 
	 * index, not an ordinal.   
	 * 
	 * So here are what will be auto-filled in: 
	 * <ul>
	 * <li>If no segment index is provided, this will return the first
	 * <li>If no field repetition is provided, this will return the first
	 * <li>If no component is provided, this will return the first
	 * <li>If no sub-component is provided, this will return the first. 
	 * </ul>
	 * 
	 * @param location
	 * @return
	 */
	public String get(String location) {
		location = this.fillInLocation(location);

//		int idx = this.getSegmentIndexFromLocation(location);
//		
//		if (idx <= 0) {
//			return null;
//		}
		
		//Gets the segment name...
//		String seg = this.getSegmentNameFromLocator(location);
		//Get a list of segment indexes...
//		List<Integer> locs = segmentIndexes.get(seg);
		
//		if (locs != null && locs.size() >= idx) {
//			int absoluteIdx = locs.get(idx-1);
//			String absoluteLocator = this.replaceSegIndexWith(location, absoluteIdx);
			String value = locationValueMap.get(location);
			LOGGER.trace("HL7MessageMap.get result: " + value);
			return value;
//		}
		
//		return null;
	}
	
//	public String ordinalToAbsoluteLocator(String ordinalLocation) {
//		//this means the segment number is an ordinal, rather than an index. 
//		
//	}
	
	/**
	 * This assumes an absolute index for the segment. 
	 * <p>For example, if there are two RXA segments, in absolute positions 7 and 11
	 * , it would be correct to send in either 7 or 11.  
	 * <p>In the previous example sending in a 1 or 2 would be incorrect.
	 * @param locationCd
	 * @param segmentIndex
	 * @param fieldRepetition
	 * @return
	 */
	public String getAtIndex(String locationCd, int segmentIndex, int fieldRepetition) {
		String locator = generateLocatorForIndex(locationCd, segmentIndex, fieldRepetition);
		return this.get(locator);
	}
	
	/**
	 * This assumes an absolute index for the segment, and it assumes you want the first field repetition. 
	 * <p>For example, if there are two RXA segments, in absolute positions 7 and 11
	 * , it would be correct to send in either 7 or 11.  
	 * <p>In the previous example sending in a 1 or 2 would be incorrect.
	 * @param locationCd
	 * @param absoluteSegmentIndex
	 * @param fieldRepetition
	 * @return
	 */
	public String getAtIndex(String locationCd, int absoluteSegmentIndex) {
		return getAtIndex(locationCd, absoluteSegmentIndex, 1);
	}

	
	/**
	 * <p>The intention of this method is to be able to request the nth iteration of a 
	 * specific segment to find the given location.  
	 * <p>For example, if there are two RXA segments, in absolute positions 7 and 11
	 * , it would be correct to send in either 1 or 2.  
	 * <p>In the previous example sending in a 7 or 11 would be incorrect.
	 * @param locationCd
	 * @param absoluteSegmentIndex
	 * @param fieldRepetition
	 * @return
	 */
	public String getAtOrdinal(String locationCd, int ordinal, int fieldRepetition) {
		int absoluteSegIdx = getAbsoluteIndexForLocationOrdinal(locationCd, ordinal);
		return this.getAtIndex(locationCd, absoluteSegIdx, fieldRepetition);
	}

	/**
	 * This assumes an absolute segment index coming in. 
	 * @param locationCd
	 * @param segmentIndex
	 * @param fieldRepetition
	 * @return
	 */
	protected String generateLocatorForIndex(String locationCd, int segmentIndex,	int fieldRepetition) {
		String locator = replaceFieldRepWith(locationCd, fieldRepetition);
		locator = replaceSegIndexWith(locator,  segmentIndex);
		locator = fillInComponentAndSubComponent(locator);
		return locator;
	}
		
	
	/**
	 * This returns the segment index (absolute index) where the value resides. 
	 * @param targetValue
	 * @param location
	 * @param fieldRep
	 * @return
	 */
	public int findSegmentIndexWithValue(String targetValue, String location, int fieldRep) {

		if (targetValue == null) {
			return 0;
		}

		String segName = this.getSegmentNameFromLocator(location);
		List<Integer> idxList = this.segmentIndexes.get(segName);
		
		int i = 1;

		String value = this.getAtIndex(location, i, fieldRep);

		while (value != null) {
			
			if (targetValue.equals(value)) {
				return i;
			}
			
			value = this.getAtIndex(location, ++i, fieldRep);
		}

		return 0;
	}

	/**
	 * Returns a list of Segment INDEXES of segments which hold this value. 
	 * @param targetValues
	 * @param location
	 * @param fieldRep
	 * @return
	 */
	public List<Integer> findAllIndexesForSegmentWithValues(String[] targetValues, String location, int fieldRep) {

		String segment = this.getSegmentNameFromLocator(location);
		List<Integer> segList = this.getIndexesForSegmentName(segment);
		System.out.println("segment indexes: " + segList);
		
		if (segList != null) {
			int last = segList.size();
			return findAllSegmentRepsWithValuesWithinRange(targetValues, location, 1, last, fieldRep);
		} else { 
			return new ArrayList<Integer>();
		}
	}
	
	protected List<Integer> getIndexesForSegmentName(String segName) {
		return this.segmentIndexes.get(segName);
	}
	
	/**
	 * Returns a list of segment ORDINALS of segments which hold this value.
	 * <p>Example - for RXA, a value of 1 would mean the first RXA in the message.  
	 * @param targetValues
	 * @param location
	 * @param fieldRep
	 * @return
	 */
	public List<Integer> findAllSegmentRepsWithValuesWithinRange(String[] targetValues, String location, int ordinalSegStart, int ordinalSegStop, int fieldRep) {

		List<Integer> list = new ArrayList<Integer>();
		
		if (targetValues == null || targetValues.length == 0) {
			return list;
		}

		List<String> valueList = Arrays.asList(targetValues);
		
		int currentOrdinal = ordinalSegStart;
		
		while (currentOrdinal <= ordinalSegStop) {
			String value = this.getAtOrdinal(location, currentOrdinal, fieldRep);
			LOGGER.trace("checking segment " + currentOrdinal + " for values in " + location + " current value: " + value);
			if (valueList.contains(value)) {
				list.add(currentOrdinal);
			}
			currentOrdinal++;
		}

		return list;
	}
	
	/**
	 * This is NOT case sensitive. 
	 * <p>This will find the first field repetition with any component that has a value that matches the value sent in. 
	 * <p>The RXA-5 value is a good example.  Here are a few example RXA-5 values: 
	 * <li><code>141^Influenza, seasonal, injectable^CVX
	 * <li><code>21^Varicella^CVX^90716^Varicella^CPT
	 * <li><code>20^DTaP^CVX^90700^DTaP^CPT
	 * <p>The specification for RXA-5 is as follows: 
	 * <li>RXA-5 - CE_IZ - Administered Code (complex)
<li>RXA-5-1 - ST - Administered Code:Identifier (simple) 
<li>RXA-5-2 - ST - Administered Code:Text (simple) 
<li>RXA-5-3 - ID - Administered Code:Name of Coding System (simple) 
<li>RXA-5-4 - ST - Administered Code:Alternate Identifier (simple) 
<li>RXA-5-5 - ST - Administered Code:Alternate Text (simple) 
<li>RXA-5-6 - ID - Administered Code:Name of Alternate Coding System (simple) 
	 * @param targetValue
	 * @param location
	 * @param segmentOrdinal
	 * @return
	 */
	public int findFieldRepWithValue(String targetValue, String location, int segmentOrdinal) {

		if (location == null) {
			return 0;
		}

		int i = 1;
		
		int absSegIdx = this.getAbsoluteIndexForLocationOrdinal(location, segmentOrdinal);
		String filledLocator = this.generateLocatorForIndex(location, absSegIdx, 1);
		
		String fieldLoc = this.getFieldLocator(filledLocator);
//		LOGGER.debug("Is the fieldLoc busted? " + fieldLoc + " or Is the fieldRep map busted??? " + this.fieldRepetitions);
		Integer fieldReps = getFieldRepCountFor(fieldLoc);
		
		if (fieldReps == null) {
			return 0;
		}
		
		while (i <= fieldReps) {
			String value = this.getAtIndex(location, absSegIdx, i);
//			LOGGER.debug("findFieldRepWithValue Found["+value+"] in " + location + " seg: " + ordinalSegmentIndex + " rep: " + i);
			if ((targetValue != null && targetValue.equalsIgnoreCase(value)) || (value == null && targetValue == null) ) {
				return i;
			}
			i++;
		}

		return 0;
	}
	
	/**
	 * This expects a fully formed field with index like this: 
	 * <ul>
	 * <li><code>seg[absSegIdx]-fld</li></ul>
	 * <br /> 
	 * if you need it to build your locator call the other overloaded version of this method. 
	 * <br /> 
	 * @param fieldLoc
	 * @return
	 */
	public Integer getFieldRepCountFor(String fieldLoc) {
		String filledField = addSegmentIndexIfMissing(fieldLoc);
//		LOGGER.debug("getFieldRepsForField field[" + fieldLoc + "] translated to [" + filledField + "]");
		Integer i = this.fieldRepetitions.get(filledField);
//		LOGGER.debug("Got: " + i);
		if (i == null) {
			return 0;
		}
		return i;
	}
	
	/**
	 * This expects a field, and a segment index. 
	 * @param fieldLoc
	 * @return
	 */
	public Integer getFieldRepCountFor(String location, int segIndex) {
		String filledLocator = this.generateLocatorForIndex(location, segIndex, 1);
		String fieldLoc = this.getFieldLocator(filledLocator);
		return getFieldRepCountFor(fieldLoc);
	}

	public void put(String locator, String value) {
		LOGGER.trace("Putting " + locator + " value " + value);
		locator = fillInLocation(locator);
		indexTheLocatorSegment(locator);
		indexTheFieldRep(locator);
		locationValueMap.put(locator, value);
	}
	
	public void indexTheFieldRep(String locator) {
		String fieldLocator = this.getFieldLocator(locator);
		Integer fieldRep = this.getFieldRep(locator);
		Integer reps = fieldRepetitions.get(fieldLocator);
		if (reps == null || reps < fieldRep) {
			fieldRepetitions.put(fieldLocator, fieldRep);
		}
	}
	
	protected String replaceFieldRepWith(String locator, int fieldRep) {
		
		if (!locator.contains("~")) {
			locator = fillInLocation(locator);
		}
		String newFieldRep = locator.replaceFirst("\\~\\d+","~"+fieldRep);
		LOGGER.trace("Transformed " + locator + " to " + newFieldRep);
		return newFieldRep;
	}
	
	protected String replaceSegIndexWith(String locator, int newIndex) {
		if (!locator.contains("[")) {
			locator = fillInLocation(locator);
		}
		String absoluteLocator =  locator.replaceFirst("\\[\\d\\]", "["+newIndex+"]");
		LOGGER.trace("Transformed " + locator + " to " + absoluteLocator);
		return absoluteLocator;
	}
	
	protected String getSegmentNameFromLocator(String locator) {
		//possible values coming into here: 
		//MSH
		//RXA[1]
		//RXA-1
		//RXA[2]-1
		//RXA-2~2-1-1
		//etc...  
		//So basically, either it's the only thing, 
		//or it's separated from the field by a dash, 
		//or it has the segment index in bracket. 
		if (locator != null) {
			String[] nameParts = locator.split("[\\[\\-]");
			return nameParts[0];
		}
		
		return null;
		
	}
	

	public void put(String locationCd, int segmentIndex, int fieldRepetition,
			String value) {

		LOGGER.trace("Putting " + locationCd + " segIdx: " + segmentIndex + " fieldRep: " + fieldRepetition + " value: " + value);
		String properLocator = this.makeLocatorString(locationCd,  segmentIndex,  fieldRepetition);
		LOGGER.trace("Normalized Locator: " + properLocator);
		this.put(properLocator, value);
	}
	
	/**
	 * This is a little more complicated than it would have to be if the message was sent in raw. Because we're getting a locator
	 * rather than the raw message, we're going to put it together as we get it, which may be out of order.  
	 * <br />
	 * <p>This is going to be more powerful in general, because the order its sent in will not matter.  
	 * 
	 * @param locator
	 */
	protected void indexTheLocatorSegment(String locator) {
		String seg = locator.split("\\[")[0];
		List<Integer> segList = segmentIndexes.get(seg);
		if (segList == null) {
			segList = new TreeList<Integer>();
			segmentIndexes.put(seg, segList);
		}
		
		Integer idx = this.getSegmentIndexFromLocation(locator);
		
		LOGGER.trace("SEG IDX: " + idx + " for " + locator);
		
		if (!segList.contains(idx)) {
			segList.add(idx);
		}
		
		LOGGER.trace("Ordinal of " + idx + " is " + (segList.indexOf(idx) + 1));
	}
	
	protected Integer getSegmentIndexFromLocation(String locator) {
		locator = this.addSegmentIndexIfMissing(locator);
		LOGGER.trace("getSegmentIndexFromLocation("+locator+");");
		int i1 = locator.indexOf("[");
		int i2 = locator.indexOf("]");
		String segIdx = locator.substring(i1+1, i2);
		return Integer.valueOf(segIdx);
	}
	
	/**
	 * This method returns the number of segments of the given name that are present in the message.
	 * <p>This is most relevant for the NK1 segments.  
	 * @param segmentName
	 * @return count of segments with that name in the message. 
	 */
	public int getSegmentCount(String segmentName) {
		List<Integer> segSpots = segmentIndexes.get(segmentName);
		
		if (segSpots == null) {
			return 0;
		}
		
		return segSpots.size();
	}
	
	public String getLocatorString(String segmentName, int segIndex, int fieldNum, int fieldRep, int component, int subComponent) {
		return segmentName + "[" + segIndex + "]-" + fieldNum + "~" + fieldRep + "-" + component + "-" + subComponent; 
	}
	
	public String makeLocatorString(String basiclocation, int segIndex, int fieldRep) {
		LOGGER.trace("Making " + basiclocation + " with segIndex: " + segIndex + " fieldRep: " + fieldRep);
		String locator = basiclocation;
		String[] locSplit = basiclocation.split("\\-");
		String seg = locSplit[0];
		String field = locSplit[1];
		
		locator = locator.replace(seg, seg+"["+segIndex+"]");
		locator = locator.replaceFirst("-"+field, "-"+field+"~"+fieldRep);
		locator = fillInComponentAndSubComponent(locator);
		
		return locator;
	}
	
	protected String fillInLocation(String location) {
		location = addSegmentIndexIfMissing(location);
		location = addZeroFieldNumberIfMissing(location);
		location = addFieldRepIfMIssing(location);
		location = fillInComponentAndSubComponent(location);
		return location;
	}
	
	protected String addSegmentIndexIfMissing(String locString) {
		String segName = locString.split("\\-")[0];
		if (!segName.contains("[")) {
			//get the index of the first time the 
			//segment is represented. 
			List<Integer> segIdxList = this.segmentIndexes.get(segName);
			if (segIdxList != null && segIdxList.size() > 0) {
				Integer idx = segIdxList.get(0);
				locString = locString.replace(segName,  segName + "[" + idx + "]");
			}
		}
		return locString;
	}
	
	/**
	 * THe intention of this method is to determine the field repetition
	 * from the locator.
	 * 
	 * If there is no field repetition information, it will assume it's the first field rep. 
	 * @param locString
	 * @return
	 */
	protected Integer getFieldRep(String locString) {
		//Start with something like PID-11~1-7
		String[] parts = locString.split("\\~");
		//Now have two array entries: {"PID-11", "1-7"}
		if (parts.length < 2) {
//			This means there was no ~, and hence, no field rep number. Implies this is the first rep. 
			return 1;
		} else {
			//This means it has a field rep.  There should only be one, so only two parts. .
			//{"PID-11", "1-7"}.  The second part could potentially just be a number... or could be empty. 
			//
			String[] partsII = parts[1].split("\\-");
			Integer fieldRep = Integer.parseInt(partsII[0]);
			return fieldRep;
		}
	}
	
	/**
	 * Keep the segment, seg index, and field number.  
	 * remove the field repetition, and component/subcomponent parts. 
	 * return the result. 
	 * @param locator
	 * @return
	 */
	protected String getFieldLocator(String locator) {
		if (locator == null) {
			return locator;
		}
		
		if (locator.contains("~")) {
			return locator.split("\\~")[0];
		} else {
			String[] parts = locator.split("\\-");
			if (parts.length > 1) {
				return parts[0]+"-"+parts[1];
			};
		}
		//If it gets here, that means it doesn't have a field rep or component information... So it
		//already is how we want it to be. 
		return locator;
	}
	
	/**
	 * Puts a zero for the field number, if there's not one already supplied.  
	 * @param locString
	 * @return
	 */
	protected String addZeroFieldNumberIfMissing(String locString) {
		String[] parts = locString.split("\\-");
		if (parts.length < 2) {
			//This means it doesn't even have a field number. 
			locString = locString + "-0";
		}
		return locString;
	}
	
	
	protected String addFieldRepIfMIssing(String locString) {
		String[] parts = locString.split("\\~");
		if (parts.length < 2) {
			//This means it doesn't even have a field rep. 
			//This assumes this locator has a field number. 
			//Since there's no ~, we can assume the field number is going 
			//to be the second array entry in the split by -
			//And we also assume it's the first rep, if there's no field rep info. 
			String[] locParts = locString.split("\\-");
			if (locParts.length < 2) {
				locString = locString + "-0~1";
			} else {
				String fieldNum = locParts[1];
				locString = locString.replaceFirst("-" + fieldNum, "-" + fieldNum + "~1");
			}
		}
		
		return locString;
	}
	
	protected String fillInComponentAndSubComponent(String locString) {
		String[] locSplit = locString.split("\\-");
		if (locSplit.length < 3) { 
			return locString + "-1-1";
		} else if (locSplit.length < 4) {
			return locString + "-1";
		}
		return locString;
	}
	


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HL7MessageMap [map=" + locationValueMap + ", segmentCounts=" + segmentIndexes
				+ "]";
	}

	/**
	 * This seems way over complicated.  Refactor once you verify it works.
	 * @return
	 */
	public List<String> getMessageSegments() {
		
		if (this.segmentMessageOrder == null) {
			//To reconstruct the original order of the message (with the absolute indexes) you need to go trough the map, and put them in the right order...  this might be better done as the map is built.  Let's see. 
			int segCnt = 0;
			//SO let's reconstruct that list.  Maybe I just make a string array the right size, and loop through the stuff and put the segment names in the right places. 
			for (String segmentName : segmentIndexes.keySet()) {
				List<Integer> absolutePos = segmentIndexes.get(segmentName);
				segCnt += absolutePos.size();
			}
			
			String[] messageOrigOrder = new String[segCnt];
			
			for (String segmentName : segmentIndexes.keySet()) {
				List<Integer> absolutePos = segmentIndexes.get(segmentName);
				for (Integer igr : absolutePos) {
					messageOrigOrder[igr] = segmentName;
				}
			}
			
			this.segmentMessageOrder = Arrays.asList(messageOrigOrder);
		}
		
		return this.segmentMessageOrder ;
	}
	
	/**
	 * @param location
	 * @param absSegmentIdx
	 * @param fieldRepetition
	 * @return
	 */
	public String getFromAbsIndex(String location, int absSegmentIdx, int fieldRepetition) {
		
//		int segOrdinal = getSegmentOrdinalFromAbsoluteIndex(absSegmentIdx);
//		if (segOrdinal == -1) {
//			LOGGER.warn("No segment at that index");
//			return null;
//		}
		
		String value = this.getAtIndex(location, absSegmentIdx, fieldRepetition);
		return value;
	}

	/**
	 * this is all one-based.  So send in a 1 based ordinal segment . 
	 * that means the first apperaance of a segment will get a ordinal of 1. 
	 * <br />
	 * This method will return the absolute index that the segment appears for the segments ordinal. 
	 * @param location
	 * @param ordinal
	 * @return
	 */
	public int getAbsoluteIndexForLocationOrdinal(String location, int ordinal) {
		String seg = this.getSegmentNameFromLocator(location);
		LOGGER.trace("getAbsoluteIndexForLocation Segment[" + seg + "]");
		return getAbsoluteIndexForSegment(seg, ordinal);
		//minus one, because we're trying to maintain a 1 based index system.
		//So we expect that what comes in will be a 1 based ordinal
		//So the first RXA will have a ordinal of 1. 
		
		//THen we add one, because coming in, it was in a 0 based system...  actually...
		//Maybe I should just translate it to a 1 based system coming in.  
		//TODO: turn the stored absolute indexes into 1 based instead of 0 based. 
	}
	
	/**
	 * This method takes a segment name, and an ordinal, and returns the index at which that segment appears
	 * in the message at that ordinal. 
	 * 
	 * For example, if there are two NK1 records like this: 
	 * <ul>
	 * <li>MSH
	 * <li>PID
	 * <li>NK1
	 * <li>NK1
	 * 
	 * if you send in a "2" to the method for NK1, the appropriate answer would be 4 since our indexes are 1 based.
	 * 
	 * <br />
	 * If you send in an ordinal that does not exist in the message, like if you sent in a "3" for the previous example, 
	 * you would see a -1 in return as an indicator that it doesn't exist. 
	 * @param segmentName
	 * @param ordinal
	 * @return
	 */
	public int getAbsoluteIndexForSegment(String segmentName, int ordinal) {
		List<Integer> segList = segmentIndexes.get(segmentName);
		
		//A little protective code: 
		if (segList == null || ordinal > segList.size()) {
			//This means there's not a segment at that ordinal.  Like there's not a second or third repetition...
			return -1;
		}
		
		return segList.get(ordinal-1);
		
	}
	
	
	/**
	 * A absolute index is assumed to be one based.  That means the MSH should always
	 * be the first segment. 
	 * <br />
	 * The way this works: 
	 * <ol>
	 * <li>get the list of all the message segment names in order that they appear in the message. 
	 * <li>find the segment name at the absolute index sent into the method.  This is how you can tell which segment we're talking about. 
	 * <li>For that segment, get the list of absolute indexes where it appears in the message. 
	 * <li>determine where in the list of absolute indexes the index sent in falls.  This is the ordinal.  
	 * @param location
	 * @param absoluteSegmentIndex
	 * @return
	 */
	public int getSegmentOrdinalFromAbsoluteIndex(int absoluteSegmentIndex) {
		List<String> segList = this.getMessageSegments();
	    String seg = segList.get(absoluteSegmentIndex-1);
		LOGGER.trace("getSegmentOrdinalFromAbsoluteIndex Segment[" + seg + "]");
		
		//Absolute index of 
		List<Integer> segRelList = segmentIndexes.get(seg);
		LOGGER.trace("getSegmentOrdinalFromAbsoluteIndex indexes: "+segRelList);
		
		//Segments are stored in a zero based way for "absolute" indexing. 
		int idx = absoluteSegmentIndex-1;
		LOGGER.trace("getSegmentOrdinalFromAbsoluteIndex absoluteIdx: " + absoluteSegmentIndex);
		LOGGER.trace("getSegmentOrdinalFromAbsoluteIndex modified (0 base) absoluteIdx: " + idx);
		int ordinal = -1;
		if (segRelList.contains(idx)) {
			//find the position of the 
			ordinal = segRelList.indexOf(idx) + 1;
		}
		LOGGER.trace("getSegmentOrdinalFromAbsoluteIndex ordinal: "+ordinal);
		return ordinal;
	}
	
	
	
	public String getSegmentAtAbsoluteIndex(int absoluteSegmentIndex) {
		for (String segment : segmentIndexes.keySet()) {
			List<Integer> seglist = segmentIndexes.get(segment);
			if (seglist.contains(absoluteSegmentIndex-1)) {
				return segment;
			}
		}
		return null;
	}
	
	//If I were going to make this "generic", I would put this next method into an extension
	//of this class for ImmunizationMapping
	/**
	 * This returns a zero based index for the start of the next immunization record. 
	 * @param startingPoint
	 * @return
	 */
	public int getNextImmunizationStartingIndex(int startingPoint) {
		List<String> segList = this.getMessageSegments();
		
		boolean orcCameFirst = false;

		if (segList == null || segList.size() == 0) {
			return -1;
		}
		
		//See what the starting point is that was sent in. 
	    String segAtIndex = segList.get(startingPoint);
		LOGGER.trace("getNextImmunizationStartingIndex - segAtIndex: " + segAtIndex);
	    if ("ORC".equals(segAtIndex)) {
			orcCameFirst = true;
		}

    	LOGGER.trace("getNextImmunizationStartingIndex - orcCameFirst = " + orcCameFirst);
		//    Loop through the segments.  Every time you find a new ORC or RXA, 
		//    that's a new shot boundary.
		    	
    	boolean foundRxa = false;
    	
		for (int i = startingPoint + 1; i < segList.size(); i++) {
			String segName = segList.get(i);
			LOGGER.trace("getNextImmunizationStartingIndex - Evaluating " + segName);
			if ("ORC".equals(segName)) {
				return i;
			} else if ("RXA".equals(segName)) {
	//			The ORC segment should come first. If it's missing, 
	//			the RXA will have to serve as the boundary instead of ORC. 
				if (!orcCameFirst || foundRxa) {
					return i;
				}
				//Finding a second rxa before an ORC would indicate a new vaccine. 
				foundRxa = true;
			}
		}
		
		//If a new starting point hasn't been found, return the ending index of the list. 
		return segList.size();
	}
}
