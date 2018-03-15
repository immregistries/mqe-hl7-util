package org.immregistries.dqa.vxu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immregistries.dqa.hl7util.model.ErrorLocation;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;

public abstract class MetaFieldInfoHolder {
  private Map<VxuField, MetaFieldInfo> metaFieldInfoMap = new HashMap<>();
  private int positionId = 0;
  private int positionAbsolute = 0;

  public int getPositionAbsolute() {
    return positionAbsolute;
  }

  public void setPositionAbsolute(int positionAbsolute) {
    this.positionAbsolute = positionAbsolute;
  }

  public int getPositionId() {
    return positionId;
  }

  public void setPositionId(int positionId) {
    this.positionId = positionId;
  }

  public Map<VxuField, MetaFieldInfo> getMetaFieldInfoMap() {
    return metaFieldInfoMap;
  }

  public MetaFieldInfo getMetaFieldInfo(VxuField vxuField) {
    return metaFieldInfoMap.get(vxuField);
  }

  public void setField(List<MetaFieldInfo> metaFieldInfoList) {
    for (MetaFieldInfo meta : metaFieldInfoList) {
      setField(meta);
    }
  }

  public void setField(MetaFieldInfo metaFieldInfo) {
    if (metaFieldInfo != null) {
      if (metaFieldInfo.getVxuField() == null) {
        throw new IllegalArgumentException("Meta Field Info must have VXU field");
      }
      metaFieldInfoMap.put(metaFieldInfo.getVxuField(), metaFieldInfo);
      readRegisteredMetaFieldInfo(metaFieldInfo);
    }
  }

  protected abstract void readRegisteredMetaFieldInfo(MetaFieldInfo metaFieldInfo);

  public void mapAndSet(VxuField vxuField, HL7MessageMap map) {
    ErrorLocation errorLocation = new ErrorLocation(vxuField.getHl7Field());
    String value = mapValue(map, errorLocation);

    MetaFieldInfo meta = new MetaFieldInfo();
    meta.setVxuField(vxuField);
    meta.setValue(value);
    meta.setErrorLocation(errorLocation);
    setField(meta);
  }

  public void mapAndSetCodedValue(VxuField vxuField, HL7MessageMap map, String[] codeTableNames) {
    ErrorLocation errorLocation = new ErrorLocation(vxuField.getHl7Field());
    errorLocation.setComponentNumber(1);
    String value = mapValue(map, errorLocation);
    errorLocation.setComponentNumber(3);
    String tableName = mapValue(map, errorLocation);
    errorLocation.setComponentNumber(4);
    String valueAlt = mapValue(map, errorLocation);
    errorLocation.setComponentNumber(6);
    String tableNameAlt = mapValue(map, errorLocation);
    errorLocation.setComponentNumber(1);

    boolean valueFound = false;
    for (String s : codeTableNames) {
      if (s.equalsIgnoreCase(tableName)) {
        valueFound = true;
      }
    }
    if (!valueFound) {
      for (String s : codeTableNames) {
        if (s.equalsIgnoreCase(tableNameAlt)) {
          valueFound = true;
        }
      }
      if (valueFound) {
        value = valueAlt;
        errorLocation.setComponentNumber(4);
      }
    }
    if (valueFound) {
      MetaFieldInfo meta = new MetaFieldInfo();
      meta.setVxuField(vxuField);
      meta.setValue(value);
      meta.setErrorLocation(errorLocation);
      setField(meta);
    }
  }

  public String mapValue(HL7MessageMap map, ErrorLocation errorLocation) {
    String value;
    if (positionAbsolute > 1) {
      value = map.getAtIndex(errorLocation.getMessageMapLocator(), positionAbsolute);
    } else {
      value = map.get(errorLocation.getMessageMapLocator());
    }
    return value;
  }

  public void mapAndSet(VxuField vxuField, HL7MessageMap map, String selectHL7Ref,
      String selectValue) {
    {
      ErrorLocation errorLocation = new ErrorLocation(selectHL7Ref);
      selectHL7Ref = errorLocation.getMessageMapLocator();
    }
    int fieldRep = map.findFieldRepWithValue(selectValue, selectHL7Ref, 1);

    if (fieldRep > 0) {
      ErrorLocation errorLocation = new ErrorLocation(vxuField.getHl7Field());
      errorLocation.setFieldRepetition(fieldRep);
      String value = map.get(errorLocation.getMessageMapLocator());

      MetaFieldInfo meta = new MetaFieldInfo();
      meta.setVxuField(vxuField);
      meta.setValue(value);
      meta.setErrorLocation(errorLocation);
      setField(meta);

    }

  }

  public void mapAndSetAll(VxuField vxuField, HL7MessageMap map) {
    int fieldCount = 0;
    {
      ErrorLocation errorLocation = new ErrorLocation(vxuField.getHl7Field());
      errorLocation.setComponentNumber(1);
      fieldCount = map.getFieldRepCountFor(errorLocation.getMessageMapLocatorFieldOnly());
    }

    for (int i = 1; i <= fieldCount; i++) {
      ErrorLocation errorLocation = new ErrorLocation(vxuField.getHl7Field());
      errorLocation.setFieldRepetition(i);
      String value = map.get(errorLocation.getMessageMapLocator());
      MetaFieldInfo meta = new MetaFieldInfo();
      meta.setVxuField(vxuField);
      meta.setValue(value);
      meta.setErrorLocation(errorLocation);
      setField(meta);
    }
  }

}
