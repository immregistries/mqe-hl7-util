package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;
import org.immregistries.dqa.hl7util.model.Hl7Location;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.VxuField;

public class MetaParser {

  private final HL7MessageMap map;

  MetaParser(HL7MessageMap map) {
    this.map = map;
  }


  public List<MetaFieldInfo> mapValues(int segmentIndex, VxuField... vxuField) {
    List<MetaFieldInfo> mfiList = new ArrayList<>();
    for (VxuField field : vxuField) {
      mfiList.add(mapValue(segmentIndex, field));
    }
    return mfiList;
  }

  MetaFieldInfo mapValue(int absoluteSegmentIndex, VxuField vxuField) {
    if (absoluteSegmentIndex < 0) {
      return new MetaFieldInfo(null, vxuField, -1, absoluteSegmentIndex);
    }

    int positionId = map.getSegmentOrdinalFromAbsoluteIndex(absoluteSegmentIndex + 1);
    Hl7Location hl7Location = new Hl7Location(vxuField.getHl7Locator(),
        absoluteSegmentIndex + 1, positionId);
    String value = this.getValue(hl7Location);
    return new MetaFieldInfo(value, vxuField, positionId, absoluteSegmentIndex);
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
  public MetaFieldInfo mapCodedValue(int absoluteSegmentIndex, VxuField vxuField,
      String... searchForCodeTables) {
    //If no code table is sent in to search for, then don't even try.
    if (searchForCodeTables == null || searchForCodeTables.length < 1) {
      return null;
    }

    Hl7Location hl7Location = getLocationfor(vxuField, absoluteSegmentIndex);
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
    return map.getAtLocation(hl7Location);
  }

  public MetaFieldInfo mapValueForTypes(int absoluteSegmentIndex, VxuField vxuField,
      String selectHL7Ref, String... searchForCodeTypes) {
    for (String type : searchForCodeTypes) {
      MetaFieldInfo mfi = mapFieldWhere(absoluteSegmentIndex, vxuField, selectHL7Ref, type);
      if (mfi != null) {
        return mfi;
      }
    }
    return null;
  }

  Hl7Location getLocationfor(VxuField vxuField, int index) {
    int positionId = map.getSegmentOrdinalFromAbsoluteIndex(index);
    return getLocationfor(vxuField.getHl7Locator(), index);
  }

  Hl7Location getLocationfor(String vxuFieldRef, int index) {
    int positionId = map.getSegmentOrdinalFromAbsoluteIndex(index);
    return new Hl7Location(vxuFieldRef, index + 1, positionId);
  }

  MetaFieldInfo mapFieldWhere(int absoluteSegmentIndex, VxuField vxuField, String selectHL7Ref,
      String searchForCodeType) {

    Hl7Location hl7Location = getLocationfor(selectHL7Ref, absoluteSegmentIndex);
    selectHL7Ref = hl7Location.getMessageMapLocator();
    int fieldRep = map
        .findFieldRepWithValue(searchForCodeType, selectHL7Ref, hl7Location.getSegmentSequence());

    if (fieldRep > 0) {
      Hl7Location loc = getLocationfor(vxuField, absoluteSegmentIndex);
      loc.setFieldRepetition(fieldRep);
      return createMetaField(vxuField, loc);
    }

    return null;
  }

  public List<MetaFieldInfo> mapAllRepetitions(int absoluteSegmentIndex, VxuField... vxuFields) {
    List<MetaFieldInfo> mfiList = new ArrayList<>();
    for (VxuField field : vxuFields) {
      mfiList.addAll(mapRepetitions(absoluteSegmentIndex, field));
    }
    return mfiList;
  }

  public List<MetaFieldInfo> mapRepetitions(int absoluteSegmentIndex, VxuField vxuField) {
    int fieldCount = 0;

    Hl7Location location = getLocationfor(vxuField, absoluteSegmentIndex);
    fieldCount = map.getFieldRepCountFor(location.getMessageMapLocatorFieldOnly());

    List<MetaFieldInfo> metaFieldInfoList = new ArrayList<>();
    for (int i = 1; i <= fieldCount; i++) {
      Hl7Location el = getLocationfor(vxuField, absoluteSegmentIndex);
      el.setFieldRepetition(i);
      metaFieldInfoList.add(createMetaField(vxuField, el));
    }
    return metaFieldInfoList;
  }

  private MetaFieldInfo createMetaField(VxuField vxuField, Hl7Location hl7Location) {
    String value;
    String mapLocator = hl7Location.getMessageMapLocator();
    value = map.get(mapLocator);
    MetaFieldInfo meta = new MetaFieldInfo();
    meta.setVxuField(vxuField);
    meta.setValue(value);
    meta.setHl7Location(hl7Location);
    return meta;
  }
}
