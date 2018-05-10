package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.dqa.hl7util.model.Hl7Location;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.VxuField;

public class MetaParser {

  private final HL7MessageMap map;

  MetaParser(HL7MessageMap map) {
    this.map = map;
  }


  List<MetaFieldInfo> mapValues(int line, VxuField... vxuField) {
    List<MetaFieldInfo> mfiList = new ArrayList<>();
    for (VxuField field : vxuField) {
      if (field.getHl7Locator() != null) {
        mfiList.add(mapValue(line, field));
      }
    }
    return mfiList;
  }

  private MetaFieldInfo mapValue(int line, VxuField vxuField) {
    if (line < 0 || StringUtils.isBlank(vxuField.getHl7Locator())) {
      return new MetaFieldInfo(null, vxuField, -1, line);
    }

    Hl7Location hl7Location = new Hl7Location(vxuField.getHl7Locator(), line);
    int sequence = map.getSequenceFromLine(hl7Location.getSegmentId(), line);
    hl7Location.setSegmentSequence(sequence);
    String value = this.getValue(hl7Location);
    return new MetaFieldInfo(value, vxuField, sequence, line);
  }

  /**
   * This method is search for a set of possible CodeTable types. <p>an example is an RXA-5, which
   * may have CVX, CPT, NDC.  So if you want to get the NDC values, you send in the code tables that
   * might indicate an NDC.</p> <p>This does not account for field repetitions</p>
   *
   * @param vxuField the is the field reference - like RXA-5
   * @param searchForCodeTables this holds a list of code types you're interested in.
   * @return a single MetaFieldInfo object, with the value specified for the first code table found,
   * searching in the order presented in the array sent in.
   */
  public MetaFieldInfo mapCodedValue(int lineNumber, VxuField vxuField,
      String... searchForCodeTables) {
    //If no code table is sent in to search for, then don't even try.
    if (searchForCodeTables == null || searchForCodeTables.length < 1) {
      return null;
    }

    Hl7Location hl7Location = getLocationfor(vxuField, lineNumber);
    hl7Location.setComponentNumber(1);
    String value = getValue(hl7Location);
    hl7Location.setComponentNumber(3);
    String tableName = getValue(hl7Location);
    hl7Location.setComponentNumber(4);
    String valueAlt = getValue(hl7Location);
    hl7Location.setComponentNumber(6);
    String tableNameAlt = getValue(hl7Location);
    hl7Location.setComponentNumber(1);

    boolean valueFound = false;
    for (String searchForCodeTable : searchForCodeTables) {
      if (searchForCodeTable.equalsIgnoreCase(tableName)) {
        valueFound = true;
      }
    }
    if (!valueFound) {
      for (String s : searchForCodeTables) {
        if (s.equalsIgnoreCase(tableNameAlt)) {
          valueFound = true;
        }
      }
      if (valueFound) {
        value = valueAlt;
        hl7Location.setComponentNumber(4);
      }
    }
    if (valueFound) {
      MetaFieldInfo meta = new MetaFieldInfo();
      meta.setVxuField(vxuField);
      meta.setValue(value);
      meta.setHl7Location(hl7Location);
      return meta;
    }
    return null;
  }

  String getValue(Hl7Location hl7Location) {
    return map.get(hl7Location);
  }

  private Hl7Location getLocationfor(VxuField vxuField, int line) {
    return getLocationfor(vxuField.getHl7Locator(), line);
  }

  private Hl7Location getLocationfor(String vxuFieldRef, int line) {
    int segmentSequence = map.getSegmentSequenceFromLineNumber(line);
    return new Hl7Location(vxuFieldRef, line, segmentSequence);
  }

  MetaFieldInfo mapFieldWhere(int lineNumber, VxuField vxuField, String selectHL7Ref,
      String searchForCodeType) {

    Hl7Location hl7Location = getLocationfor(selectHL7Ref, lineNumber);
    int fieldRep = map.findFieldRepWithValue(searchForCodeType, selectHL7Ref, hl7Location.getSegmentSequence());

    if (fieldRep > 0) {
      Hl7Location loc = getLocationfor(vxuField, lineNumber);
      loc.setFieldRepetition(fieldRep);
      return createMetaField(vxuField, loc);
    }

    return null;
  }

  List<MetaFieldInfo> mapAllRepetitions(int lineNumber, VxuField... vxuFields) {
    List<MetaFieldInfo> mfiList = new ArrayList<>();
    for (VxuField field : vxuFields) {
      mfiList.addAll(mapRepetitions(lineNumber, field));
    }
    return mfiList;
  }

  List<MetaFieldInfo> mapRepetitions(int lineNumber, VxuField vxuField) {
    int fieldCount = 0;

    Hl7Location location = new Hl7Location(vxuField.getHl7Locator(), lineNumber);
    location.setSegmentSequence(map.getSequenceFromLine(location.getSegmentId(), lineNumber));
    fieldCount = map.getFieldRepCountFor(location);

    List<MetaFieldInfo> metaFieldInfoList = new ArrayList<>();

    for (int i = 1; i <= fieldCount; i++) {
      Hl7Location el = new Hl7Location(location.toString());
      el.setFieldRepetition(i);
      metaFieldInfoList.add(createMetaField(vxuField, el));
    }
    return metaFieldInfoList;
  }

  private MetaFieldInfo createMetaField(VxuField vxuField, Hl7Location hl7Location) {
    String value = map.get(hl7Location);
    MetaFieldInfo meta = new MetaFieldInfo();
    meta.setVxuField(vxuField);
    meta.setValue(value);
    meta.setHl7Location(hl7Location);
    return meta;
  }
}
