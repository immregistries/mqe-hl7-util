package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.hl7util.model.Hl7Location;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.VxuField;

public enum MetaParser {
    INSTANCE;

    public MetaFieldInfo mapValue(VxuField vxuField, HL7MessageMap map) {
        return mapValue(0, vxuField, map);
    }

    public MetaFieldInfo mapValue(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map) {
        Hl7Location hl7Location = new Hl7Location(vxuField.getHl7Locator());
        String value = getValue(absoluteSegmentIndex, map, hl7Location);

        MetaFieldInfo meta = new MetaFieldInfo();
        meta.setVxuField(vxuField);
        meta.setValue(value);
        meta.setHl7Location(hl7Location);
        return meta;
    }

    public MetaFieldInfo mapCodedValue(VxuField vxuField, HL7MessageMap map, String... codeTableNames) {
        return mapCodedValue(0, vxuField, map, codeTableNames);
    }

    /**
     * This method is search for a set of possible CodeTable types.
     * <p>an example is an RXA-5, which may have CVX, CPT, NDC.  So if you want to get
     * the NDC values, you send in the code tables that might indicate an NDC.</p>
     * <p>This does not account for field repetitions</p>
     * @param absoluteSegmentIndex this is the index of the segment starting with 1 in the MSH
     * @param vxuField the is the field reference - like RXA-5
     * @param map this holds all the values from a message.
     * @param searchForCodeTables this holds a list of code types you're interested in.
     * @return a single MetaFieldInfo object, with the value specified for the first
     * code table found, searching in the order presented in the array sent in.
     */
    public MetaFieldInfo mapCodedValue(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map, String... searchForCodeTables) {
        //If no code table is sent in to search for, then don't even try.
        if (searchForCodeTables == null || searchForCodeTables.length < 1) {
            return null;
        }

        String locator = vxuField.getHl7Locator();
        Hl7Location hl7Location = new Hl7Location(vxuField.getHl7Locator());
        hl7Location.setComponentNumber(1);
        String value = getValue(absoluteSegmentIndex, map, hl7Location);
        hl7Location.setComponentNumber(3);
        String tableName = getValue(absoluteSegmentIndex, map, hl7Location);
        hl7Location.setComponentNumber(4);
        String valueAlt = getValue(absoluteSegmentIndex, map, hl7Location);
        hl7Location.setComponentNumber(6);
        String tableNameAlt = getValue(absoluteSegmentIndex, map, hl7Location);
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

    public String getValue(int absoluteSegmentIndex, HL7MessageMap map, Hl7Location hl7Location) {
        String value;
        if (absoluteSegmentIndex > 1) {
            value = map.getAtIndex(hl7Location.getMessageMapLocator(), absoluteSegmentIndex);
        } else {
            value = map.get(hl7Location.getMessageMapLocator());
        }
        return value;
    }

    public MetaFieldInfo mapValueForTypes(VxuField vxuField, HL7MessageMap map, String selectHL7Ref, String... searchForCodeTypes) {
        for (String type : searchForCodeTypes) {
            MetaFieldInfo mfi = mapValue(vxuField, map, selectHL7Ref, type);
            if (mfi != null) {
                return mfi;
            }
        }
        return null;
    }

    public MetaFieldInfo mapValue(VxuField vxuField, HL7MessageMap map, String selectHL7Ref, String searchForCodeType) {
        return mapValue(0, vxuField, map, selectHL7Ref, searchForCodeType);
    }


    public MetaFieldInfo mapValue(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map, String selectHL7Ref, String searchForCodeType) {
        {
            Hl7Location hl7Location = new Hl7Location(selectHL7Ref);
            selectHL7Ref = hl7Location.getMessageMapLocator();
        }

        int segOrdinal = 1;
        //I don't like that we're using zero as a condition switch. how could this be fixed... hmm...
        if (absoluteSegmentIndex > 0) {
            segOrdinal = map.getSegmentOrdinalFromAbsoluteIndex(absoluteSegmentIndex);
        }
        int fieldRep = map.findFieldRepWithValue(searchForCodeType, selectHL7Ref, segOrdinal);

        if (fieldRep > 0) {
            Hl7Location hl7Location = new Hl7Location(vxuField.getHl7Locator());
            hl7Location.setFieldRepetition(fieldRep);
            MetaFieldInfo meta = createMetaField(absoluteSegmentIndex, vxuField, map, hl7Location);
            return meta;
        }
        return null;
    }

    public List<MetaFieldInfo> mapAllRepetitions(VxuField vxuField, HL7MessageMap map) {
        return mapAllRepetitions(0, vxuField, map);
    }

    public List<MetaFieldInfo> mapAllRepetitions(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map) {

        int fieldCount = 0;

        Hl7Location location = new Hl7Location(vxuField.getHl7Locator());
        location.setComponentNumber(1);
        fieldCount = map.getFieldRepCountFor(location.getMessageMapLocatorFieldOnly());

        List<MetaFieldInfo> metaFieldInfoList = new ArrayList<>();
        for (int i = 1; i <= fieldCount; i++) {
            Hl7Location el = new Hl7Location(vxuField.getHl7Locator());
            el.setFieldRepetition(i);
            metaFieldInfoList.add(createMetaField(absoluteSegmentIndex, vxuField, map, el));
        }
        return metaFieldInfoList;
    }

    private MetaFieldInfo createMetaField(int absoluteSegmentIndex, VxuField vxuField, HL7MessageMap map, Hl7Location hl7Location) {
        String value;
        String mapLocator = hl7Location.getMessageMapLocator();
        if (absoluteSegmentIndex > 0) {
            value = map.getAtIndex(mapLocator, absoluteSegmentIndex);
        } else {
            value = map.get(mapLocator);
        }
        MetaFieldInfo meta = new MetaFieldInfo();
        meta.setVxuField(vxuField);
        meta.setValue(value);
        meta.setHl7Location(hl7Location);
        return meta;
    }
}
