package org.immregistries.dqa.vxu.code;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum NokRelationship {
  RELATIONSHIP_BROTHER("BRO"),
  RELATIONSHIP_CARE_GIVER("CGV"),
  RELATIONSHIP_CHILD("CHD"),
  RELATIONSHIP_FATHER("FTH"),
  RELATIONSHIP_FOSTER_CHILD("FCH"),
  RELATIONSHIP_GRANDPARENT("GRP"),
  RELATIONSHIP_GUARDIAN("GRD"),
  RELATIONSHIP_MOTHER("MTH"),
  RELATIONSHIP_OTHER("OTH"),
  RELATIONSHIP_PARENT("PAR"),
  RELATIONSHIP_SELF("SEL"),
  RELATIONSHIP_SIBLING("SIB"),
  RELATIONSHIP_SISTER("SIS"),
  RELATIONSHIP_SPOUSE("SPO"),
  RELATIONSHIP_STEPCHILD("SCH"),
  UNKNOWN("");

  public final String code;
  private static final Map<String, NokRelationship> codeMap = new HashMap<String, NokRelationship>();
  private static final List<String> RESPONSIBLE_CODES_LIST;

  static {
    for (NokRelationship rel : NokRelationship.values()) {
      codeMap.put(rel.code.toUpperCase(), rel);
    }
    RESPONSIBLE_CODES_LIST = Arrays.asList(
        RELATIONSHIP_CARE_GIVER.code
        , RELATIONSHIP_FATHER.code
        , RELATIONSHIP_GRANDPARENT.code
        , RELATIONSHIP_MOTHER.code
        , RELATIONSHIP_PARENT.code
        , RELATIONSHIP_GUARDIAN.code
    );
  }

  NokRelationship(String code) {
    this.code = code;
  }

  public static String[] getResponsibleCodes() {
    return RESPONSIBLE_CODES_LIST.toArray(new String[]{});
  }

  public static NokRelationship get(String code) {
    NokRelationship r = null;

    if (code != null) {
      r = codeMap.get(code.toUpperCase());
    }

    if (r == null) {
      r = UNKNOWN;
    }

    return r;
  }

  public static boolean isResponsibleRelationship(String code) {
    return RESPONSIBLE_CODES_LIST.contains(code);
  }

  public boolean isResponsibleRelationship() {
    return isResponsibleRelationship(this.code);
  }

  public boolean isChildRelationship() {
    switch (this) {
      case RELATIONSHIP_CHILD:
      case RELATIONSHIP_FOSTER_CHILD:
      case RELATIONSHIP_STEPCHILD:
        return true;
      default:
        return false;
    }
  }

  public boolean isSelf(NokRelationship rel) {
    return RELATIONSHIP_SELF.equals(rel);
  }
}
