package org.immregistries.mqe.hl7util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.mqe.hl7util.model.Hl7Location;
import org.immregistries.mqe.hl7util.parser.model.HL7MessagePart;
import org.immregistries.mqe.hl7util.parser.profile.generator.FieldComplexity;
import org.immregistries.mqe.hl7util.parser.profile.generator.MessageProfileReader;
import org.immregistries.mqe.hl7util.parser.profile.generator.MessageProfileReaderNIST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageParserHL7 implements MessageParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageParserHL7.class);
  String fieldSeparator = "|";//This is not an optional value, but it is specified in MSH-1
  String componentSeparator = "^";
  String subComponentSeparator = "&";
  String repetitionSeparator = "~";
  String escapeChar = "\\";
  private static final String LINE_SEPARATOR_REGEX = "[\n\r\f]+";

  private static final MessageProfileReader MESSAGE_PROFILE_READER = new MessageProfileReaderNIST();

  private static final String LOC_DELIM = "-";

  /**
   * The indexes in the separator map correspond to the node-depth that the separator would be used
   * at. Depth, parth, default separator:<br /> 0 - Field - | 1 - Component - ^ 2 - Sub Component -
   * &
   */
  private String[] separatorMap = {fieldSeparator, componentSeparator, subComponentSeparator};

  Pattern separatorList = Pattern.compile(
      "[\\" + componentSeparator + "\\" + subComponentSeparator + "\\" + fieldSeparator + "\\"
          + repetitionSeparator + "]+");

  protected void setDelimitersFromMessage(String hl7MessageText) {
    /*
     * Delimiter values are found in MSH-2.  There are a standard set that are recommended:
     * MSH|^~\&
     * The field delimiter (usually "|") is in the fourth character position in the MSH segment. It's address is MSH-1.
     * That drives the parsing.  It seems like this is not an optional value.
     * For the message specifiable values, here are the categories:
     * Delimiter Name    - recommended value - position in MSH-2
     * Component Separator - ^ - 1
     * Subcomponent Separator - & - 4
     * Repetition Separator - ~ - 2
     * Escape Character - \ - 3
     */
    if (StringUtils.isNotBlank(hl7MessageText) && hl7MessageText.length() > 8) {
//			MSH-1
      String msh1fieldSeparator = hl7MessageText.substring(3, 4);
      this.fieldSeparator = msh1fieldSeparator;

//			MSH-2
      char[] delimiterSplit = hl7MessageText.substring(3, 8).toCharArray();
      this.componentSeparator = String.valueOf(delimiterSplit[1]);
      this.subComponentSeparator = String.valueOf(delimiterSplit[4]);
      this.repetitionSeparator = String.valueOf(delimiterSplit[2]);
      this.escapeChar = String.valueOf(delimiterSplit[3]);
    }
  }

  public List<HL7MessagePart> getMessagePartList(String message) {
    this.setDelimitersFromMessage(message);
    String[] segments = this.splitSegments(message);
    List<HL7MessagePart> dataList = new ArrayList<HL7MessagePart>();
    int line = 1;//1 based lines.
    Map<String, Integer> segSequence = new HashMap<>();
    for (String segment : segments) {
      String segName = segment.substring(0,3);
      Integer segSeqCnt = segSequence.get(segName);
      if (segSeqCnt == null) {
        segSeqCnt = 0;
      }
      segSeqCnt += 1;
      segSequence.put(segName, segSeqCnt);
      dataList.addAll(mapSegment(segment, line++, segSeqCnt));
    }
    return dataList;
  }

  /**
   * If you don't already have the message part list and you want the message map, call this method
   * with the part list.
   */
  public HL7MessageMap getMessagePartMap(String message) {
    if (message == null || !message.startsWith("MSH")) {
      return generateBlankMap(message);
    }
    List<HL7MessagePart> partList = getMessagePartList(message);
    return getMessageMapFromPartList(partList);
  }

  private HL7MessageMap generateBlankMap(String message) {
    HL7MessageMap hmm = new HL7MessageMap();
//    Hl7Location loc = new Hl7Location("MSH-10");
//    String fakeControlId = message != null ? message.substring(0, (message.length() > 3) ? 3 : message.length()) : "---";
//    hmm.put(loc, fakeControlId);
//    loc = new Hl7Location("PID-6-1");
//    hmm.put(loc, "unreadable message");
    return hmm;
  }

  /**
   * If you already have the message part list for persisting the parts, use this method to
   * transform it into a map in order to read the message to consume it in your application.
   */
  public HL7MessageMap getMessageMapFromPartList(List<HL7MessagePart> list) {
    HL7MessageMap map = new HL7MessageMap();
    for (HL7MessagePart messagePart : list) {
      Hl7Location loc = new Hl7Location(messagePart.getLocationCd());
      loc.setSegmentSequence(messagePart.getSegmentSeq());
      loc.setLine(messagePart.getSegmentIndex());
      loc.setFieldRepetition(messagePart.getFieldRepetition());
      map.put(loc, messagePart.getValue());
    }
    return map;
  }

  protected List<HL7MessagePart> mapSegment(String segment, int line, int segSeq) {
    List<HL7MessagePart> dataList = new ArrayList<HL7MessagePart>();

    String[] fields = splitFields(segment);

    int fldNum = 0;

    String segName = this.getSegmentName(segment);

    HL7MessagePart seg = new HL7MessagePart(line, segName);
    seg.setSegmentSeq(segSeq);

//		LOGGER.info("Number Of Fields in segment " + segName + " is " + fields.length);
    for (String field : fields) {
      HL7MessagePart fieldLoc = this.getChildLocator(seg, fldNum++);
      //Because MSH-2 has a repetition separator in it, we
      //need to actively avoid splitting the field on that separator.
      field = field != null ? field.trim() : "";
      fieldLoc.setValue(field);
      fieldLoc.setFieldRepetition(1);

      List<HL7MessagePart> fieldParts = mapField(fieldLoc);
      dataList.addAll(fieldParts);
    }

    return dataList;
  }


  protected String[] splitSegments(String inMsg) {
    String[] firstSplit = splitSegmentsByLineBreak(inMsg);
    //Segment names must be a three character identifier.
    //Every once in a while, a line break will be thrown into a value.  We can detect those, and put them back together as a value.
    return cleanSegmentBreaks(firstSplit);
  }

  protected String[] splitSegmentsByLineBreak(String inMsg) {
    String[] segments = inMsg.split(LINE_SEPARATOR_REGEX);
    return segments;
  }

  protected String[] cleanSegmentBreaks(String[] split) {
    int i = 0;
    List<String> list = new ArrayList<String>();
    for (String s : split) {
      if (!isProperlyFormedSegment(s) && i > 0) {
        i--;
        String combined = list.get(i) + s;
        list.set(i, combined);
      } else {
        list.add(s);
      }
      i++;
    }
    return list.toArray(new String[0]);
  }

  protected boolean isProperlyFormedSegment(String segmentPoser) {
    int firstSeparatorAt = segmentPoser.indexOf(this.fieldSeparator);

    boolean fieldSeparatorIndicatesSegNameIs3Chars = (firstSeparatorAt == 3);
    if (fieldSeparatorIndicatesSegNameIs3Chars) {
      return true;
    }

    boolean lengthOfSegmentIndcatesEmptyNamedSegment = segmentPoser.length() == 3;
    if (lengthOfSegmentIndcatesEmptyNamedSegment) {
      return true;
    }

    boolean positionOfLineSeparatorIndcatesEmptyNamedSegment =
        StringUtils.indexOf(segmentPoser, LINE_SEPARATOR_REGEX) == 3;
    if (positionOfLineSeparatorIndcatesEmptyNamedSegment) {
      return true;
    }

    return false;

  }

  protected String getSegmentName(String segment) {
    int firstFieldSeparatorIdx = segment.indexOf(this.fieldSeparator);
    if (firstFieldSeparatorIdx == -1) {
      return segment;
    }

    String name = segment.substring(0, firstFieldSeparatorIdx);
    return name;
  }

  protected List<HL7MessagePart> mapField(HL7MessagePart fieldLoc) {

//		LOGGER.info("mapField " + fieldLoc.getReference() + " value: " + fieldLoc.getValue());

    List<HL7MessagePart> dataList = new ArrayList<HL7MessagePart>();

    if ("MSH-2".equals(fieldLoc.getLocationCd())) {
      dataList.addAll(mapFieldValue(fieldLoc));
    } else {
      String[] fieldReps = splitRepetitions(fieldLoc.getValue());
      int fieldRep = 1;
      for (String field : fieldReps) {
        HL7MessagePart loc = this.getLocatorCopy(fieldLoc);
        loc.setValue(field);
        loc.setFieldRepetition(fieldRep++);
        dataList.addAll(mapFieldValue(loc));
      }
    }

    return dataList;
  }

  /**
   * A child locator is what you get when you split a field value.
   *
   * For example, in RXR-2, you could get a child locator for 1,2,3 for the various parts. <p>A
   * child would be RXR-2-1, or RXR-2-2 <p>The child number is what you want to point to at the next
   * node level. so in the RXR example, 1,2, or 3.
   */
  public HL7MessagePart getChildLocator(HL7MessagePart part, int childNumber) {
    HL7MessagePart loc = getLocatorCopy(part);
    String newLoc = part.getLocationCd() + LOC_DELIM + childNumber;

    loc.setLocationCd(newLoc);

    return loc;
  }

  protected HL7MessagePart getLocatorCopy(HL7MessagePart part) {
    HL7MessagePart loc = new HL7MessagePart();
    loc.setSegmentIndex(part.getSegmentIndex());
    loc.setFieldRepetition(part.getFieldRepetition());
    loc.setLocationCd(part.getLocationCd());
    loc.setSegmentSeq(part.getSegmentSeq());
    return loc;
  }

  protected List<HL7MessagePart> mapFieldValue(HL7MessagePart parentLoc) {
    List<HL7MessagePart> dataList = new ArrayList<HL7MessagePart>();
    //Don't need to map it if it's an empty value.  Either null or empty string.
    if (StringUtils.isBlank(parentLoc.getValue())) {
//      parentLoc.setValue(" ");
      dataList.add(parentLoc);
      return dataList;
    }

    FieldComplexity fc = determineComplexity(parentLoc);

    /*
     * Not 100% sure about this.
     * I'm not sure I would need to have the complex value as well as the "primitive" values.
     * without the SIMPLE condition, it always puts the value of the current node in.
     * with the condition, it only puts the value in when it's a "primitive" value for the field.
     * for example, with the IF, it would only store:
     * RXR(6) RXR-2-1 > RT
     * RXR(6) RXR-2-2 > Right Thigh
     * RXR(6) RXR-2-3 > HL70163
     *
     * without the IF, it stores the RXR-2 value as well:
     * RXR(6) RXR-2 > RT^Right Thigh^HL70163
     * RXR(6) RXR-2-1 > RT
     * RXR(6) RXR-2-2 > Right Thigh
     * RXR(6) RXR-2-3 > HL70163
     *
     *  Storing the root value makes it possible to do a little bit more... but it's not needed for the process I'm working on, so I'm not putting it in.
     *
     *  Not:  if you don't have the SIMPLE check, you need to have the complex check.
     */

    if (fc == FieldComplexity.SIMPLE) {
      dataList.add(parentLoc);
    } else {
//		if (FieldComplexity.COMPLEX == parentLoc.getComplexity()) {
      int subIdx = 1;
      int nodeDepth = this.getNodeDepth(parentLoc);
//			LOGGER.info("well... " + parentLoc.getLocationCd() + " here's the parts: " + parentLoc.getValue());
      String[] subParts = splitParts(nodeDepth, parentLoc.getValue());
      for (String subValue : subParts) {
        HL7MessagePart childLoc = this.getChildLocator(parentLoc, subIdx++);
        childLoc.setValue(subValue);
        dataList.addAll(mapFieldValue(childLoc));
      }
    }

    return dataList;
  }

  public int getNodeDepth(HL7MessagePart part) {
    int depth = StringUtils.countMatches(part.getLocationCd(), LOC_DELIM);
    return depth;
  }

  protected FieldComplexity determineComplexity(HL7MessagePart location) {
//		if (location.getNodeDepth() == 1) {
//			return FieldComplexity.SIMPLE;
//		}
//		
    String locationRef = location.getLocationCd();

    FieldComplexity complexity = FieldComplexity.UNKNOWN;

    if (MESSAGE_PROFILE_READER != null) {
      complexity = MESSAGE_PROFILE_READER.getComplexity(locationRef);
    } else if (isDefaultSimpleComplexity(locationRef)) {
      LOGGER.info("messageProfileReader is null... using defaults");
      complexity = FieldComplexity.SIMPLE;
    }
//		LOGGER.info("mapFieldValues " + location.getLocationCd() + " value: " + location.getValue() + " complex: " + complexity);
    if (FieldComplexity.UNKNOWN == complexity) {
      complexity = determineComplexityFromValue(location.getValue());
//			LOGGER.info(locationRef + " complexity unknown. value: " + value + " determined to be " + complexity);
    }

    return complexity;
  }

  protected boolean isDefaultSimpleComplexity(String fieldLocCd) {
    if ("MSH-1".equals(fieldLocCd) || "MSH-2".equals(fieldLocCd)) {
      return true;
    }

    return false;
//		switch (fieldLocCd) {
//			case "MSH-1": 
//				return true;
//			case "MSH-2": 
//				return true;
//			default: 
//				return false;
//		}
  }

  protected FieldComplexity determineComplexityFromValue(String value) {
    FieldComplexity complexity;
    boolean hasSeparators = hasSeparators(value);
    if (hasSeparators) {
      complexity = FieldComplexity.COMPLEX;
    } else {
      complexity = FieldComplexity.SIMPLE;
    }
    return complexity;
  }

  /**
   * Node depths: Field = 0 Component = 1 Sub-Component = 2
   */
  protected String[] splitParts(int nodeDepth, String value) {
//		LOGGER.info("node depth: " + nodeDepth + " val " + value);
    String delimiter = this.separatorMap[nodeDepth];
    String[] split = value.split("\\" + delimiter);
    return split;
  }

  protected String[] splitRepetitions(String field) {
    String splitter = "\\" + this.repetitionSeparator;
    try {
      String[] repetitions = field.split(splitter);
      return repetitions;
    } catch (Exception e) {
      LOGGER.error("Error Parsing message part: " + field + " with separator ["+splitter+"]", e);
      return new String[] {field};
    }
  }

  protected String[] splitFields(String segment) {
    if (StringUtils.isBlank(segment)) {
      return new String[]{};
    }

    String[] fields = segment.split("\\" + this.fieldSeparator);
    //The MSH segment needs some special splitting.  The MSH-1 field needs to be the field delimiter.
    if ("MSH".equals(this.getSegmentName(segment))) {
      //the first field delimiter is actually the value for MSH-1.
      List<String> fieldList = new ArrayList<>(Arrays.asList(fields));
      fieldList.add(1, this.fieldSeparator);
      fields = fieldList.toArray(fields);
    }
    return fields;
  }

  protected boolean hasSeparators(String value) {
    Matcher m = separatorList.matcher(value);
    boolean containsSeparators = m.find();
//		LOGGER.info(value + " contains separators: " + containsSeparators);
    return containsSeparators;
  }

  public String maskSsnInMessage(String requestMessage) {

    HL7MessageMap hl7XdmMap = getMessagePartMap(requestMessage);

    boolean foundSsn =
    maskConditionalSsn("PID-3-5", "PID-3-1", hl7XdmMap) |
    maskConditionalSsn("NK1-33-5", "NK1-33-1", hl7XdmMap) |
    maskSsnAtLocation("PID-19-1", hl7XdmMap) |
    maskSsnAtLocation("NK1-37-1", hl7XdmMap);

    if (foundSsn) {
      String maskedSsnMessage = hl7XdmMap.reassemble();
      return maskedSsnMessage;
    }

    return requestMessage;
  }

  protected boolean maskSsnAtLocation(String ssnLocation, HL7MessageMap hl7MessageMap) {
    return maskSsnAtLocation(new Hl7Location(ssnLocation), hl7MessageMap);
  }

  protected boolean maskSsnAtLocation(Hl7Location ssnLocation, HL7MessageMap hl7MessageMap) {
    String ssnMask = "nnn-nn-nnnn";
    if (StringUtils.isNotBlank(hl7MessageMap.getValue(ssnLocation))) {
      hl7MessageMap.put(new Hl7Location(ssnLocation), ssnMask);
      LOGGER.info("SSN found at: " + ssnLocation + " in HL7 Message.");
      return true;
    }
    return false;
  }

    protected boolean maskConditionalSsn(String ssnConditional, String ssnLocation, HL7MessageMap hl7MessageMap) {
    String hl7OldTypeCode = "SSN";
    String hl7251TypeCode = "SS";
    Hl7Location ssnHl7Location = new Hl7Location(ssnLocation);
    Hl7Location ssnHl7ConditionalLocation = new Hl7Location(ssnConditional);

    Integer fieldRepCount = hl7MessageMap.getFieldRepCountFor(ssnHl7Location);

    for(int rep = 1; rep <= fieldRepCount; rep++) {

      ssnHl7Location.setFieldRepetition(rep);
      ssnHl7ConditionalLocation.setFieldRepetition(rep);

      if (hl7251TypeCode.equals(hl7MessageMap.getValue(ssnHl7ConditionalLocation))
       || hl7OldTypeCode.equals(hl7MessageMap.getValue(ssnHl7ConditionalLocation))) {
        return maskSsnAtLocation(ssnHl7Location, hl7MessageMap);
      }
    }
    return false;
  }

}
