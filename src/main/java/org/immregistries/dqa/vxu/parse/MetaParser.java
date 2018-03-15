package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.hl7util.model.ErrorLocation;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.VxuField;

public enum MetaParser {
    INSTANCE;

    public MetaFieldInfo mapValue(VxuField vxuField, HL7MessageMap map) {
        return mapValue(0, vxuField, map);
    }

    public MetaFieldInfo mapValue(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map) {
        ErrorLocation errorLocation = new ErrorLocation(vxuField.getHl7Field());
        String value = mapValue(absoluteSegmentIndex, map, errorLocation);

        MetaFieldInfo meta = new MetaFieldInfo();
        meta.setVxuField(vxuField);
        meta.setValue(value);
        meta.setErrorLocation(errorLocation);
        return meta;
    }

    public MetaFieldInfo mapCodedValue(VxuField vxuField, HL7MessageMap map, String[] codeTableNames) {
        return mapCodedValue(0, vxuField, map, codeTableNames);
    }

    public MetaFieldInfo mapCodedValue(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map, String[] codeTableNames) {
        ErrorLocation errorLocation = new ErrorLocation(vxuField.getHl7Field());
        errorLocation.setComponentNumber(1);
        String value = mapValue(absoluteSegmentIndex, map, errorLocation);
        errorLocation.setComponentNumber(3);
        String tableName = mapValue(absoluteSegmentIndex, map, errorLocation);
        errorLocation.setComponentNumber(4);
        String valueAlt = mapValue(absoluteSegmentIndex, map, errorLocation);
        errorLocation.setComponentNumber(6);
        String tableNameAlt = mapValue(absoluteSegmentIndex, map, errorLocation);
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
            return meta;
        }
        return null;
    }

    public String mapValue(int absoluteSegmentIndex, HL7MessageMap map, ErrorLocation errorLocation) {
        String value;
        if (absoluteSegmentIndex > 1) {
            value = map.getAtIndex(errorLocation.getMessageMapLocator(), absoluteSegmentIndex);
        } else {
            value = map.get(errorLocation.getMessageMapLocator());
        }
        return value;
    }

    public MetaFieldInfo mapValue(VxuField vxuField, HL7MessageMap map, String selectHL7Ref, String selectValue) {
        return mapValue(0, vxuField, map, selectHL7Ref, selectValue);
    }

    public MetaFieldInfo mapValue(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map, String selectHL7Ref, String selectValue) {
        {
            ErrorLocation errorLocation = new ErrorLocation(selectHL7Ref);
            selectHL7Ref = errorLocation.getMessageMapLocator();
        }
        int fieldRep = map.findFieldRepWithValue(selectValue, selectHL7Ref, 1);

        if (fieldRep > 0) {
            ErrorLocation errorLocation = new ErrorLocation(vxuField.getHl7Field());
            errorLocation.setFieldRepetition(fieldRep);
            MetaFieldInfo meta = createMetaField(absoluteSegmentIndex, vxuField, map, errorLocation);
            return meta;
        }
        return null;
    }

    public List<MetaFieldInfo> mapAndSetAll(VxuField vxuField, HL7MessageMap map) {
        return mapAndSetAll(0, vxuField, map);
    }

    public List<MetaFieldInfo> mapAndSetAll(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map) {

        int fieldCount = 0;

        ErrorLocation location = new ErrorLocation(vxuField.getHl7Field());
        location.setComponentNumber(1);
        fieldCount = map.getFieldRepCountFor(location.getMessageMapLocatorFieldOnly());

        List<MetaFieldInfo> metaFieldInfoList = new ArrayList<>();
        for (int i = 1; i <= fieldCount; i++) {
            ErrorLocation el = new ErrorLocation(vxuField.getHl7Field());
            el.setFieldRepetition(i);
            metaFieldInfoList.add(createMetaField(absoluteSegmentIndex, vxuField, map, el));
        }
        return metaFieldInfoList;
    }

    private MetaFieldInfo createMetaField(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map, ErrorLocation errorLocation) {
        String value = null;
        String mapLocator = errorLocation.getMessageMapLocator();
        if (absoluteSegmentIndex > 0) {
            value = map.getAtIndex(mapLocator, absoluteSegmentIndex);
        } else {
            value = map.get(mapLocator);
        }
        MetaFieldInfo meta = new MetaFieldInfo();
        meta.setVxuField(vxuField);
        meta.setValue(value);
        meta.setErrorLocation(errorLocation);
        return meta;
    }
}
