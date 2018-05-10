package org.immregistries.dqa.hl7util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.dqa.hl7util.model.Hl7Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HL7MessageMap {

  protected static final Logger LOGGER = LoggerFactory.getLogger(HL7MessageMap.class);

  private Map<Hl7Location, String> locationValueMap = new TreeMap<>();

  /**
   * This is a map of the segments, and their lines. So what we have here is:
   * <li>1. in the keySet, a list of all the segments.
   * <li>2. the lines of the message segments stored in the Integer
   * <li>3. The sequence for the segment, based on where the line lives in the list. This is in
   * relation to all other segments of this type. <br />
   * <p>
   * <strong>Item number three requires a little extra explanation. </strong>
   * <li>Sequence is in relation to other segments of the same type in the message. So if there are 3
   * OBX segments in a message, the sequences will be 1, 2, and 3. But the lines might be 5, 8, and 10.
   * <li>If there are more than one of any type of segment, the order in which they appear in the
   * list will be the sequence.
   * <li>Going between sequence and line is easy. The line is stored in the Integer.
   * The Sequence is derived from the position in the list.
   */
  private Map<String, List<Integer>> segmentIndexes = new HashMap<String, List<Integer>>();

  /**
   * This is a map of fields and repetitions. It does not contain components/subcomponents. the
   * locations in the key will contain: seg[absSegIdx]-fld
   */
  Map<String, Integer> fieldRepetitions = new HashMap<>();

  private List<String> segmentMessageOrder = null;

  /**
   * The basic assumption is that this method will fill in any details you don't. The minimum
   * required is to tell the segment and the field. For example:
   *
   * <p>
   * <code>map.get("MSH-4");</code>
   * </p>
   *
   * <p>
   * The easy way to tell what it will return is this: If you don't provide a value, this method
   * will look for the first.
   * </p>
   *
   * <p>
   * If you do send something in, and it includes the segment number, it is expected that this is an
   * index, not an segment sequence.
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
    Hl7Location locH7 = new Hl7Location(location);
    return this.get(locH7);
  }

  public String get(Hl7Location hl7Location) {
    String value = locationValueMap.get(hl7Location);
    LOGGER.trace("HL7MessageMap.get result: " + value);
    return value;
  }

  /**
   * This assumes an absolute index for the segment.
   * <p>
   * For example, if there are two RXA segments, in absolute positions 7 and 11 , it would be
   * correct to send in either 7 or 11.
   * <p>
   * In the previous example sending in a 1 or 2 would be incorrect.
   *
   * @param locationCd
   * @param lineNumber
   * @param fieldRepetition
   * @return
   */
  public String getAtLine(String locationCd, int lineNumber, int fieldRepetition) {
    Hl7Location loc = new Hl7Location(locationCd);
    int seq = this.getSegmentSequenceFromLineNumber(lineNumber);
    loc.setSegmentSequence(seq);
    loc.setLine(lineNumber);
    loc.setFieldRepetition(fieldRepetition);
    return this.get(loc);
  }

  /**
   * This assumes an absolute index for the segment, and it assumes you want the first field
   * repetition.
   * <p>
   * For example, if there are two RXA segments, in absolute positions 7 and 11 , it would be
   * correct to send in either 7 or 11.
   * <p>
   * In the previous example sending in a 1 or 2 would be incorrect.
   *
   * @param locationCd
   * @param lineNumber
   * @return
   */
  public String getAtLine(String locationCd, int lineNumber) {
    return getAtLine(locationCd, lineNumber, 1);
  }


  /**
   * <p>
   * The intention of this method is to be able to request the nth iteration of a specific segment
   * to find the given location.
   * <p>
   * For example, if there are two RXA segments, in absolute positions 7 and 11 , it would be
   * correct to send in either 1 or 2.
   * <p>
   * In the previous example sending in a 7 or 11 would be incorrect.
   *
   * @param locationCd
   * @param ordinal
   * @param fieldRepetition
   * @return
   */
  String getAtOrdinal(String locationCd, int ordinal, int fieldRepetition) {
//    int absoluteSegIdx = getLineNumberForSegmentSequenceInLoc(locationCd, ordinal);
//    return this.getAtLine(locationCd, absoluteSegIdx, fieldRepetition);
    Hl7Location loc = new Hl7Location(locationCd);
    loc.setSegmentSequence(ordinal);
    loc.setFieldRepetition(fieldRepetition);
    return this.get(loc);
  }

  /**
   * This assumes an absolute segment index coming in.
   *
   * @param locationCd
   * @param segmentIndex
   * @param fieldRepetition
   * @return
   */
  String generateLocatorForIndex(String locationCd, int segmentIndex, int fieldRepetition) {
    Hl7Location loc = new Hl7Location(locationCd, segmentIndex);
    loc.setFieldRepetition(fieldRepetition);
    return loc.toString();
  }



  /**
   * Returns a list of Segment INDEXES of segments which hold this value.
   *
   * @param targetValues
   * @param location
   * @return
   */
  List<Integer> findAllIndexesForSegmentWithValues(String[] targetValues, String location) {
    Hl7Location loc = new Hl7Location(location);
    String segment = loc.getSegmentId();
    List<Integer> segList = this.getIndexesForSegmentName(segment);

    if (segList != null) {
      int last = segList.size();
      return findAllSegmentRepsWithValuesWithinRange(targetValues, location, 1, last, 1);
    } else {
      return new ArrayList<>();
    }
  }

  public int getLineForSegmentName(String segName) {
    List<Integer> indexList = getIndexesForSegmentName(segName);
    if (indexList.size() > 0) {
      return indexList.get(0);
    }
    return -1;
  }

  public int findFirstSegmentWhereFieldHas(String location, String... searchCodes) {
    if (location == null || location.length() < 3) {
      return -1;
    }

    Hl7Location loc = new Hl7Location(location);
    String seg = loc.getSegmentId();
    List<Integer> segs = this.getIndexesForSegmentName(seg);
    for (Integer i : segs) {
      //find rel code:
      String value = this.getAtLine(location, i);
      if (Arrays.asList(searchCodes).contains(value)) {
        return i;
      }
    }
    return -1;
  }

  public List<Integer> getIndexesForSegmentName(String segName) {
    List<Integer> indexes = this.segmentIndexes.get(segName);
    if (indexes == null) {
      return new TreeList<>();
    }
    return indexes;
  }

  /**
   * Returns a list of segment ORDINALS of segments which hold this value.
   * <p>
   * Example - for RXA, a value of 1 would mean the first RXA in the message.
   *
   * @param targetValues
   * @param location
   * @param fieldRep
   * @return
   */
  public List<Integer> findAllSegmentRepsWithValuesWithinRange(String[] targetValues,
      String location, int ordinalSegStart, int ordinalSegStop, int fieldRep) {

    List<Integer> list = new ArrayList<>();

    if (targetValues == null || targetValues.length == 0) {
      return list;
    }

    List<String> valueList = Arrays.asList(targetValues);

    int currentOrdinal = ordinalSegStart;

    while (currentOrdinal <= ordinalSegStop) {
      String value = this.getAtOrdinal(location, currentOrdinal, fieldRep);
      LOGGER.trace("checking segment " + currentOrdinal + " for values in " + location
          + " current value: " + value);
      if (valueList.contains(value)) {
        list.add(currentOrdinal);
      }
      currentOrdinal++;
    }

    return list;
  }

  /**
   * This is NOT case sensitive.
   * <p>
   * This will find the first field repetition with any component that has a value that matches the
   * value sent in.
   * <p>
   * The RXA-5 value is a good example. Here are a few example RXA-5 values:
   * <li><code>141^Influenza, seasonal, injectable^CVX
   * <li><code>21^Varicella^CVX^90716^Varicella^CPT
   * <li><code>20^DTaP^CVX^90700^DTaP^CPT
   * <p>
   * The specification for RXA-5 is as follows:
   * <li>RXA-5 - CE_IZ - Administered Code (complex)
   * <li>RXA-5-1 - ST - Administered Code:Identifier (simple)
   * <li>RXA-5-2 - ST - Administered Code:Text (simple)
   * <li>RXA-5-3 - ID - Administered Code:Name of Coding System (simple)
   * <li>RXA-5-4 - ST - Administered Code:Alternate Identifier (simple)
   * <li>RXA-5-5 - ST - Administered Code:Alternate Text (simple)
   * <li>RXA-5-6 - ID - Administered Code:Name of Alternate Coding System (simple)
   *
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
    Hl7Location loc = new Hl7Location(location);
    loc.setSegmentSequence(segmentOrdinal);
    int line = this.getLineNumberForSegmentSequence(loc.getSegmentId(), segmentOrdinal);
    loc.setLine(line);
    // LOGGER.debug("Is the fieldLoc busted? " + fieldLoc + " or Is the fieldRep map busted??? " +
    // this.fieldRepetitions);
    Integer fieldReps = getFieldRepCountFor(loc);

    if (fieldReps == null) {
      return 0;
    }

    while (i <= fieldReps) {
      String value = this.getAtLine(location, line, i);
      // LOGGER.debug("findFieldRepWithValue Found["+value+"] in " + location + " seg: " +
      // ordinalSegmentIndex + " rep: " + i);
      if ((targetValue != null && targetValue.equalsIgnoreCase(value))
          || (value == null && targetValue == null)) {
        return i;
      }
      i++;
    }

    return 0;
  }

  public Integer getFieldRepCountFor(Hl7Location loc) {
    Integer i = this.fieldRepetitions.get(loc.getFieldLoc());
    return (i == null ? Integer.valueOf(0) : i);
  }

  public String reassemble() {
    StringBuilder sb = new StringBuilder();
    Map<String, StringBuilder> lineMap = new HashMap<>();
    String lastSpotIsaw = "";
    for (Hl7Location l : locationValueMap.keySet()) {
//      key:   PID[1]-5~1-1-1
//      value: biran
      String spot = l.getSegmentId() + l.getSegmentSequence();

      if (!spot.equals(lastSpotIsaw)) {
        lineMap.put(spot, sb);
        sb = new StringBuilder();
        sb.append(l.getSegmentId());
        sb.append("|");
        lastSpotIsaw = spot;
      }

      Integer f = l.getFieldPosition();
      Integer pipes = StringUtils.countMatches(sb.toString(), "|");
      if (pipes < f) {
        for (int piper = 0 ; piper < f - pipes; piper++) {
          sb.append("|");
        }
      }

      String value = this.locationValueMap.get(l);
      sb.append(value);
//      List<Integer> i = segmentIndexes.get(l.getSegmentId());
//      Integer x = i.get(l.getSegmentSequence() - 1);
    }
    lineMap.put(lastSpotIsaw, sb);

    StringBuilder all = new StringBuilder();
    for (StringBuilder s : lineMap.values()) {
      all.append(s.toString());
      all.append("\r");
    }
    return all.toString();
  }

  /**
   * Takes a value and locator and puts it in the map.
   * @param loc
   * @param value
   */
  public void put(Hl7Location loc, String value) {
    LOGGER.trace("Putting " + loc + " value " + value);
    indexTheLocatorSegment(loc);
    indexTheFieldRep(loc);
    locationValueMap.put(loc, value);
  }

  private void indexTheFieldRep(Hl7Location loc) {
    Integer fieldRep = loc.getFieldRepetition();
    Integer reps = fieldRepetitions.get(loc.getFieldLoc());
    if (reps == null || reps < fieldRep) {
      fieldRepetitions.put(loc.getFieldLoc(), fieldRep);
    }
  }

  public void put(String locationCd, int segmentSequence, int line, int fieldRepetition, String value) {
    LOGGER.trace("Putting " + locationCd + " segIdx: " + line + " segSeq: " + segmentSequence + " fieldRep: "
        + fieldRepetition + " value: " + value);
//    String properLocator = this.makeLocatorString(locationCd, segmentIndex, fieldRepetition);
    Hl7Location loc = new Hl7Location(locationCd);
    loc.setSegmentSequence(segmentSequence);
    loc.setLine(line);
    loc.setFieldRepetition(fieldRepetition);

    LOGGER.trace("Normalized Locator: " + loc);
    this.put(loc, value);
  }

  protected void indexTheLocation(String segId, int segIdx) {
    List<Integer> segList = segmentIndexes.get(segId);
    if (segList == null) {
      segList = new TreeList<Integer>();
      segmentIndexes.put(segId, segList);
    }
    LOGGER.trace("SEG IDX: " + segIdx + " for " + segId);

    if (!segList.contains(segIdx)) {
      segList.add(segIdx);
    }
  }

  void indexTheLocatorSegment(Hl7Location h7l) {
    String seg = h7l.getSegmentId();
    int idx = h7l.getLine();
    this.indexTheLocation(seg, idx);
  }

  /**
   * This method returns the number of segments of the given name that are present in the message.
   * <p>
   * This is most relevant for the NK1 segments.
   *
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

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "HL7MessageMap [map=" + locationValueMap + ", segmentCounts=" + segmentIndexes + "]";
  }

  /**
   * This seems way over complicated. Refactor once you verify it works.
   *
   * @return
   */
  public List<String> getMessageSegments() {

    if (this.segmentMessageOrder == null) {
      // To reconstruct the original order of the message (with the absolute indexes) you need to go
      // trough the map, and put them in the right order... this might be better done as the map is
      // built. Let's see.
      int segCnt = 0;
      // SO let's reconstruct that list. Maybe I just make a string array the right size, and loop
      // through the stuff and put the segment names in the right places.
      for (String segmentName : segmentIndexes.keySet()) {
        List<Integer> absolutePos = segmentIndexes.get(segmentName);
        segCnt += absolutePos.size();
      }

      String[] messageOrigOrder = new String[segCnt];

      for (String segmentName : segmentIndexes.keySet()) {
        List<Integer> absolutePos = segmentIndexes.get(segmentName);
        for (Integer igr : absolutePos) {
          messageOrigOrder[igr-1] = segmentName;
        }
      }

      this.segmentMessageOrder = Arrays.asList(messageOrigOrder);
    }

    return this.segmentMessageOrder;
  }

  /**
   * @param location
   * @param absSegmentIdx
   * @param fieldRepetition
   * @return
   */
  public String getFromAbsIndex(String location, int absSegmentIdx, int fieldRepetition) {

    // int segOrdinal = getSegmentSequenceFromLineNumber(absSegmentIdx);
    // if (segOrdinal == -1) {
    // LOGGER.warn("No segment at that index");
    // return null;
    // }

    String value = this.getAtLine(location, absSegmentIdx, fieldRepetition);
    return value;
  }

  /**
   * this is all one-based. So send in a 1 based ordinal segment . that means the first apperaance
   * of a segment will get a ordinal of 1. <br />
   * This method will return the absolute index that the segment appears for the segments ordinal.
   *
   * @param location
   * @param ordinal
   * @return
   */
  public int getLineNumberForSegmentSequenceInLoc(String location, int ordinal) {
    Hl7Location loc = new Hl7Location(location);
    String seg = loc.getSegmentId();
    LOGGER.trace("getAbsoluteIndexForLocation Segment[" + seg + "]");
    return getLineNumberForSegmentSequence(seg, ordinal);
    // minus one, because we're trying to maintain a 1 based index system.
    // So we expect that what comes in will be a 1 based ordinal
    // So the first RXA will have a ordinal of 1.
    // THen we add one, because coming in, it was in a 0 based system... actually...
    // Maybe I should just translate it to a 1 based system coming in.
    // TODO: turn the stored absolute indexes into 1 based instead of 0 based.
  }

  /**
   * This method takes a segment name, and an ordinal, and returns the index at which that segment
   * appears in the message at that ordinal.
   *
   * For example, if there are two NK1 records like this:
   * <ul>
   * <li>MSH
   * <li>PID
   * <li>NK1
   * <li>NK1
   *
   * if you send in a "2" to the method for NK1, the appropriate answer would be 4 since our indexes
   * are 1 based.
   *
   * <br />
   * If you send in an ordinal that does not exist in the message, like if you sent in a "3" for the
   * previous example, you would see a -1 in return as an indicator that it doesn't exist.
   *
   * @param segmentName
   * @param segmentSequence
   * @return
   */
  public int getLineNumberForSegmentSequence(String segmentName, int segmentSequence) {
    List<Integer> segList = segmentIndexes.get(segmentName);
    // A little protective code:
    if (segList == null || segmentSequence > segList.size()) {
      // This means there's not a segment at that ordinal. Like there's not a second or third
      // repetition...
      return -1;
    }
    //The segment sequence is one based...  array list is zero based.  Subtract one.
    return segList.get(segmentSequence - 1);
  }

  public int getLineFromSequence(String segId, int segSequence) {
    if (this.segmentIndexes.get(segId).size() >= segSequence) {
      return this.segmentIndexes.get(segId).get(segSequence - 1);
    }
    return 0;
  }

  public int getSequenceFromLine(String segId, int line) {
    if (this.segmentIndexes.get(segId) != null &&
        this.segmentIndexes.get(segId).contains(line)) {
      return (this.segmentIndexes.get(segId).indexOf(line)) + 1;
    }
    return 0;
  }


  /**
   * A absolute index is assumed to be one based. That means the MSH should always be the first
   * segment. <br />
   * The way this works:
   * <ol>
   * <li>get the list of all the message segment names in order that they appear in the message.
   * <li>find the segment name at the absolute index sent into the method. This is how you can tell
   * which segment we're talking about.
   * <li>For that segment, get the list of absolute indexes where it appears in the message.
   * <li>determine where in the list of absolute indexes the index sent in falls. This is the
   * segment sequence.
   *
   * @param lineNumber
   * @return
   */
  public int getSegmentSequenceFromLineNumber(int lineNumber) {
    if (lineNumber <= 0) {
      return -1;
    }

    List<String> segList = this.getMessageSegments();
    String seg = segList.get(lineNumber - 1);
    LOGGER.trace("getSegmentSequenceFromLineNumber Segment[" + seg + "]");
    List<Integer> segRelList = segmentIndexes.get(seg);
    LOGGER.trace("getSegmentSequenceFromLineNumber segments: " + segRelList);
    // Segments are stored in a zero based way for "absolute" indexing.
    LOGGER.trace("getSegmentSequenceFromLineNumber line: " + lineNumber);
    int seq = -1;
    if (segRelList.contains(lineNumber)) {
      // find the position of the
      seq = segRelList.indexOf(lineNumber) + 1;
    }
    return seq;
  }


  public String getSegmentAtLine(int line) {
    for (String segment : segmentIndexes.keySet()) {
      List<Integer> seglist = segmentIndexes.get(segment);
      if (seglist.contains(line)) {
        return segment;
      }
    }
    return null;
  }

  // If I were going to make this "generic", I would put this next method into an extension
  // of this class for ImmunizationMapping

  /**
   * This returns a zero based index for the start of the next immunization record.
   *
   * @param line
   * @return
   */
  public int getNextImmunizationAfterLine(int line) {
    List<String> segList = this.getMessageSegments();
    int startLine = line + 1;
    boolean orcCameFirst = false;

    if (startLine < 1) {
      startLine = 1;
    }

    if (segList == null || segList.size() == 0 || segList.size() < startLine) {
      return -1;
    }

    // See what the starting point is that was sent in.
    String startingSegmentId = segList.get(startLine-1);

    LOGGER.trace("getNextImmunizationAfterLine - startingSegmentId: " + startingSegmentId);
    if ("ORC".equals(startingSegmentId)) {
      orcCameFirst = true;
    }

    LOGGER.trace("getNextImmunizationAfterLine - orcCameFirst = " + orcCameFirst);
    // Loop through the segments. Every time you find a new ORC or RXA,
    // that's a new shot boundary.

    boolean foundRxa = false;

    for (int thisLine = startLine + 1; thisLine < segList.size(); thisLine++) {
      String segName = segList.get(thisLine - 1);
      LOGGER.trace("getNextImmunizationAfterLine - Evaluating " + segName);
      if ("ORC".equals(segName)) {
        return thisLine;
      } else if ("RXA".equals(segName)) {
        // The ORC segment should come first. If it's missing,
        // the RXA will have to serve as the boundary instead of ORC.
        if (!orcCameFirst || foundRxa) {
          return thisLine;
        }
        // Finding a second rxa before an ORC would indicate a new vaccine.
        foundRxa = true;
      }
    }

    // If a new starting point hasn't been found, return the ending index of the list.
    return segList.size();
  }
}
