package org.immregistries.mqe.hl7util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.mqe.hl7util.model.Hl7Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HL7MessageMap {

  protected static final Logger LOGGER = LoggerFactory.getLogger(HL7MessageMap.class);

  private Map<Hl7Location, String> locationValueMap = new TreeMap<>();

  /**
   * This is a map of the segments, and their lines. It contains:
   * <li>1. a list of all the segments.
   * <li>2. a list of segment line numbers
   * <li>3. the sequence of the segments
   * <br />
   * <p>
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
  public String getValue(String location) {
    Hl7Location locH7 = new Hl7Location(location);
    locH7 = getLocWithLine(locH7);
    return this.getValue(locH7);
  }

  public String getValue(Hl7Location hl7Location) {
    String value = locationValueMap.get(hl7Location);
    LOGGER.trace("HL7MessageMap.get result: " + value);
    return value;
  }
  /**
   * This assumes a line number for the segment.
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
  public String getValue(String locationCd, int lineNumber, int fieldRepetition) {
    Hl7Location loc = new Hl7Location(locationCd);
    int seq = this.getSequenceFromLine(lineNumber);
    loc.setSegmentSequence(seq);
    loc.setLine(lineNumber);
    loc.setFieldRepetition(fieldRepetition);
    return this.getValue(loc);
  }

  public int getLineForSegmentName(String segName) {
    List<Integer> indexList = getLinesForSegmentName(segName);
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
    List<Integer> segs = this.getLinesForSegmentName(loc.getSegmentId());
    int seq = 1;
    for (Integer i : segs) {
      //find rel code:
      loc.setLine(i);
      loc.setSegmentSequence(seq++);

      String value = this.getValue(loc);
      if (Arrays.asList(searchCodes).contains(value)) {
        return i;
      }
    }
    return -1;
  }

  private List<Integer> getLinesForSegmentName(String segName) {
    List<Integer> indexes = this.segmentIndexes.get(segName);
    if (indexes == null) {
      return new TreeList<>();
    }
    return indexes;
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
   * @param segmentSequence
   * @return
   */
  public int findFieldRepWithValue(String targetValue, String location, int segmentSequence) {

    if (location == null) {
      return 0;
    }

    int i = 1;
    Hl7Location loc = new Hl7Location(location);
    loc.setSegmentSequence(segmentSequence);
    int line = this.getLineFromSequence(loc.getSegmentId(), segmentSequence);
    loc.setLine(line);
    Integer fieldReps = getFieldRepCountFor(loc);

    if (fieldReps == null) {
      return 0;
    }

    while (i <= fieldReps) {
      String value = this.getValue(location, line, i);
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
    StringBuilder line = new StringBuilder();
    Hl7Location prevLoc = new Hl7Location();
    List<String> lineList = new ArrayList<>();
    boolean first = true;

    //This is so much simpler if these things are in order... use a tree set!
    for (Hl7Location l : new TreeSet<>(locationValueMap.keySet())) {
      if (l.getLine() != prevLoc.getLine()) {
        //make a new line!
        if (!first) {
          lineList.add(line.toString());
        }
        first = false;
        line.setLength(0);
        line.append(l.getSegmentId());
      } else if (line.length() == 0) {
        line.append(l.getSegmentId());
      }

      if (l.getFieldPosition() != 0 && !("MSH".equals(l.getSegmentId()) && l.getFieldPosition() == 1)) {
        line.append(getPadding(prevLoc, l));
        line.append(locationValueMap.get(l));
      }
      prevLoc = l;
    }
    //Catch the last line...  and put it in the array.  I think that just means putting it there.
    lineList.add(line.toString());

    StringBuilder all = new StringBuilder();
    boolean firstLine = true;
    for (String s : lineList) {
      if (!firstLine) {
        all.append("\r");
      }
      firstLine = false;
      all.append(s);
    }
    return all.toString();
  }

  protected String getPadding(Hl7Location prevLoc, Hl7Location l) {
    StringBuilder padding = new StringBuilder();
    int fields = 0;
    int repetitions = 0;
    int components = 0;
    int subComponents = 0;

    if (l.getLine() != prevLoc.getLine()) {
      //only have to pad for the current loc.
      fields = l.getFieldPosition();
      repetitions = l.getFieldRepetition();
      components = l.getComponentNumber();
      subComponents = l.getSubComponentNumber();
    } else {
      //bad between locations.
      fields = l.getFieldPosition() - prevLoc.getFieldPosition();
      if (fields != 0) {
        repetitions = l.getFieldRepetition() - 1;
        components = l.getComponentNumber() - 1;
        subComponents = l.getSubComponentNumber() - 1;
      } else {
        repetitions = l.getFieldRepetition() - prevLoc.getFieldRepetition();
        components = l.getComponentNumber() - prevLoc.getComponentNumber();
        subComponents = l.getSubComponentNumber() - prevLoc.getSubComponentNumber();
      }
    }

    padding.append(getPadding("|", fields));
    padding.append(getPadding("~", repetitions));
    padding.append(getPadding("^", components));
    padding.append(getPadding("&", subComponents));

    return padding.toString();
  }

  protected String getPadding(String separator, int numberOfSeparators) {
    return StringUtils.rightPad("", numberOfSeparators, separator);
  }

  protected StringBuilder padSeparators(StringBuilder start, char separator, int valuePosition) {
    StringBuilder sb = new StringBuilder(start);
    Integer separators = StringUtils.countMatches(start, String.valueOf(separator));

    //you always need one less than the value position.

    if (separators < valuePosition) {
      for (int piper = 0 ; piper < valuePosition - separators; piper++) {
        sb.append(separator);
      }
    }
    return sb;
  }

  /**
   * Takes a value and locator and puts it in the map.
   * @param loc
   * @param value
   */
  public void put(Hl7Location loc, String value) {
    loc = getLocWithLine(loc);
    indexTheLineSegment(loc);
    indexTheFieldRep(loc);
    locationValueMap.put(loc, value);
  }

  /**
   * This solves the issue where we want to send in a location
   * either to PUT or GET a value from the value map, but
   * the location does not have a line number.
   * Programatically we don't know the line number, when requesting a value.
   * this helps fill that in.
   * @param loc
   * @return
   */
  public Hl7Location getLocWithLine(Hl7Location loc) {
    Hl7Location newLoc = new Hl7Location(loc);
    if (newLoc.getLine() == 0) {
      int line = this.getLineFromSequence(newLoc.getSegmentId(), newLoc.getSegmentSequence());
      newLoc.setLine(line);
    }
    return newLoc;
  }

  private void indexTheFieldRep(Hl7Location loc) {
    Integer fieldRep = loc.getFieldRepetition();
    Integer reps = fieldRepetitions.get(loc.getFieldLoc());
    if (reps == null || reps < fieldRep) {
      fieldRepetitions.put(loc.getFieldLoc(), fieldRep);
    }
  }

  private void indexTheLineSegment(String segId, int segIdx) {
    List<Integer> segList = segmentIndexes.get(segId);
    if (segList == null) {
      segList = new TreeList<>();
      segmentIndexes.put(segId, segList);
    }
    LOGGER.trace("SEG IDX: " + segIdx + " for " + segId);

    if (!segList.contains(segIdx)) {
      segList.add(segIdx);
    }
  }

  private void indexTheLineSegment(Hl7Location h7l) {
    String seg = h7l.getSegmentId();
    int idx = h7l.getLine();
    this.indexTheLineSegment(seg, idx);
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
      // To reconstruct the original order of the message (with the line numbers) you need to go
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


  public int getSequenceFromLine(int lineNumber) {
    if (lineNumber <= 0) {
      return -1;
    }
    String seg = getSegIdAtLine(lineNumber);
    return this.getSequenceFromLine(seg, lineNumber);
  }

  public int getSequenceFromLine(String segId, int line) {
    if (this.segmentIndexes.get(segId) != null &&
        this.segmentIndexes.get(segId).contains(line)) {
      return (this.segmentIndexes.get(segId).indexOf(line)) + 1;
    }
    return 0;
  }

  String getSegIdAtLine(int line) {
    List<String> segList = this.getMessageSegments();
    return segList.get(line - 1);
  }

  public int getLineFromSequence(String segmentName, int segmentSequence) {
    Hl7Location loc = new Hl7Location(segmentName);
    List<Integer> segList = segmentIndexes.get(loc.getSegmentId());
    if (segList == null || segmentSequence > segList.size()) {
      return 0;
    }
    return segList.get(segmentSequence - 1);
  }

  /**
   * This returns the last line before the next immunization, or if there are no more,
   * it returns the last line.
   *
   * @param line
   * @return
   */
  public int getImmunizationBoundaryEnd(int line) {

    List<String> segList = this.getMessageSegments();
    int startLine = line + 1;
    boolean orcCameFirst = false;

    if (startLine < 1) {
      startLine = 1;
    }

    if (segList == null || segList.size() < startLine) {
      return -1;
    }

    // See what the starting point is that was sent in.
    String startingSegmentId = segList.get(startLine-1);

    LOGGER.trace("getImmunizationBoundaryEnd - startingSegmentId: " + startingSegmentId);
    if ("ORC".equals(startingSegmentId)) {
      orcCameFirst = true;
    }

    LOGGER.trace("getImmunizationBoundaryEnd - orcCameFirst = " + orcCameFirst);
    // Loop through the segments. Every time you find a new ORC or RXA,
    // that's a new shot boundary.

    boolean foundRxa = false;

    for (int thisLine = startLine + 1; thisLine < segList.size(); thisLine++) {
      String segName = segList.get(thisLine - 1);
      LOGGER.trace("getImmunizationBoundaryEnd - Evaluating " + segName);
      if ("ORC".equals(segName)) {
        return thisLine - 1;//Return the line before it.
      } else if ("RXA".equals(segName)) {
        // The ORC segment should come first. If it's missing,
        // the RXA will have to serve as the boundary instead of ORC.
        if (!orcCameFirst || foundRxa) {
          return thisLine - 1;
        }
        // Finding a second rxa before an ORC would indicate a new vaccine.
        foundRxa = true;
      }
    }

    // If a new starting point hasn't been found, return the ending index of the list.
    return segList.size();
  }
}
