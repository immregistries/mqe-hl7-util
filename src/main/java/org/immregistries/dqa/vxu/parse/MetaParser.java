package org.immregistries.dqa.vxu.parse;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.dqa.hl7util.model.Hl7Location;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;
import org.immregistries.dqa.hl7util.parser.HL7MessageMap;
import org.immregistries.dqa.vxu.VxuField;

public class MetaParser {

    private final HL7MessageMap map;
    private int positionId = 1;
    private int absoluteSegmentIndex = 0;
 
    public int getPositionId() {
      return positionId;
    }

    public void setPositionId(int positionId) {
      this.positionId = positionId;
    }

    public int getAbsoluteSegmentIndex() {
      return absoluteSegmentIndex;
    }

    public void setAbsoluteSegmentIndex(int absoluteSegmentIndex) {
      this.absoluteSegmentIndex = absoluteSegmentIndex;
    }

    MetaParser(HL7MessageMap map) {
        this.map = map;
    }

    public MetaFieldInfo mapValue(VxuField vxuField) {
        Hl7Location hl7Location = new Hl7Location(vxuField.getHl7Locator());
        hl7Location.setSegmentSequence(positionId);
        hl7Location.setLine(absoluteSegmentIndex + 1);
        String value = getValue( hl7Location);

        MetaFieldInfo meta = new MetaFieldInfo();
        meta.setVxuField(vxuField);
        meta.setValue(value);
        meta.setHl7Location(hl7Location);
        return meta;
    }

    /**
     * This method is search for a set of possible CodeTable types.
     * <p>an example is an RXA-5, which may have CVX, CPT, NDC.  So if you want to get
     * the NDC values, you send in the code tables that might indicate an NDC.</p>
     * <p>This does not account for field repetitions</p>
     * @param vxuField the is the field reference - like RXA-5
     * @param map this holds all the values from a message.
     * @param searchForCodeTables this holds a list of code types you're interested in.
     * @return a single MetaFieldInfo object, with the value specified for the first
     * code table found, searching in the order presented in the array sent in.
     */
    public MetaFieldInfo mapCodedValue(VxuField vxuField, String... searchForCodeTables) {
        //If no code table is sent in to search for, then don't even try.
        if (searchForCodeTables == null || searchForCodeTables.length < 1) {
            return null;
        }

        Hl7Location hl7Location = new Hl7Location(vxuField.getHl7Locator());
        hl7Location.setSegmentSequence(positionId);
        hl7Location.setLine(absoluteSegmentIndex + 1);
        hl7Location.setComponentNumber(1);
        String value = getValue( hl7Location);
        hl7Location.setComponentNumber(3);
        String tableName = getValue(hl7Location);
        hl7Location.setComponentNumber(4);
        String valueAlt = getValue( hl7Location);
        hl7Location.setComponentNumber(6);
        String tableNameAlt = getValue( hl7Location);
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

    public String getValue(Hl7Location hl7Location) {
        String value;
        if (absoluteSegmentIndex > 0) {
            value = map.getAtIndex(hl7Location.getMessageMapLocator(), absoluteSegmentIndex);
        } else {
            value = map.get(hl7Location.getMessageMapLocator());
        }
        return value;
    }

    public MetaFieldInfo mapValueForTypes(VxuField vxuField, String selectHL7Ref, String... searchForCodeTypes) {
        for (String type : searchForCodeTypes) {
            MetaFieldInfo mfi = mapValue(vxuField, selectHL7Ref, type);
            if (mfi != null) {
                return mfi;
            }
        }
        return null;
    }

    public MetaFieldInfo mapValue(VxuField vxuField, String selectHL7Ref, String searchForCodeType) {
        {
            Hl7Location hl7Location = new Hl7Location(selectHL7Ref);
            hl7Location.setSegmentSequence(positionId);
            hl7Location.setLine(absoluteSegmentIndex + 1);
            selectHL7Ref = hl7Location.getMessageMapLocator();
        }

        int fieldRep = map.findFieldRepWithValue(searchForCodeType, selectHL7Ref, positionId);

        if (fieldRep > 0) {
            Hl7Location hl7Location = new Hl7Location(vxuField.getHl7Locator());
            hl7Location.setSegmentSequence(positionId);
            hl7Location.setLine(absoluteSegmentIndex + 1);
            hl7Location.setFieldRepetition(fieldRep);
            MetaFieldInfo meta = createMetaField(vxuField, hl7Location);
            return meta;
        }
        return null;
    }

    public List<MetaFieldInfo> mapAllRepetitions(VxuField vxuField) {
        int fieldCount = 0;

        Hl7Location location = new Hl7Location(vxuField.getHl7Locator());
        location.setSegmentSequence(positionId);
        location.setLine(absoluteSegmentIndex + 1); 
        location.setComponentNumber(1);
        fieldCount = map.getFieldRepCountFor(location.getMessageMapLocatorFieldOnly());

        List<MetaFieldInfo> metaFieldInfoList = new ArrayList<>();
        for (int i = 1; i <= fieldCount; i++) {
            Hl7Location el = new Hl7Location(vxuField.getHl7Locator());
            el.setSegmentSequence(positionId);
            el.setLine(absoluteSegmentIndex + 1); 
            el.setFieldRepetition(i);
            metaFieldInfoList.add(createMetaField(vxuField, el));
        }
        return metaFieldInfoList;
    }

    private MetaFieldInfo createMetaField( VxuField vxuField, Hl7Location hl7Location) {
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
