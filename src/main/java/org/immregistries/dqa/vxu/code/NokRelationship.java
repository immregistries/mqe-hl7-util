package org.immregistries.dqa.vxu.code;

import java.util.HashMap;
import java.util.Map;


public enum NokRelationship {
	    RELATIONSHIP_BROTHER("BRO")
	  , RELATIONSHIP_CARE_GIVER("CGV")
	  , RELATIONSHIP_CHILD("CHD")
	  , RELATIONSHIP_FATHER("FTH")
	  , RELATIONSHIP_FOSTER_CHILD("FCH")
	  , RELATIONSHIP_GRANDPARENT("GRP")
	  , RELATIONSHIP_GUARDIAN("GRD")
	  , RELATIONSHIP_MOTHER("MTH")
	  , RELATIONSHIP_OTHER("OTH")
	  , RELATIONSHIP_PARENT("PAR")
	  , RELATIONSHIP_SELF("SEL")
	  , RELATIONSHIP_SIBLING("SIB")
	  , RELATIONSHIP_SISTER("SIS")
	  , RELATIONSHIP_SPOUSE("SPO")
	  , RELATIONSHIP_STEPCHILD("SCH")
	  , UNKNOWN("");

	public String code;
	private static final Map<String, NokRelationship> codeMap = new HashMap<String, NokRelationship>();

	static {
		for (NokRelationship rel : NokRelationship.values()) {
			codeMap.put(rel.code.toUpperCase(), rel);
		}
	}

	private NokRelationship(String code) {
		this.code = code;
	}

	public static NokRelationship get(String code) {
		NokRelationship r = null;
		if (code != null) {
			r = codeMap.get(code.toUpperCase());
		}
		if (r == null) {
			r = UNKNOWN;
			r.code = code;
		}
		return r;
	}

	public boolean isResponsibleRelationship() {
		switch (this) {
        case RELATIONSHIP_CARE_GIVER:
        case RELATIONSHIP_FATHER:
        case RELATIONSHIP_GRANDPARENT:
        case RELATIONSHIP_MOTHER:
        case RELATIONSHIP_PARENT:
        case RELATIONSHIP_GUARDIAN:
           return true;
        default:
           return false;
		}
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
