package org.immregistries.dqa.vxu.code;

import java.util.HashMap;
import java.util.Map;

enum TelUseCode {
    PRN ("PRN", "Primary residence number"),
		ORN ("ORN", "Other residence number"),
		WPN ("WPN", "Work number"),
		VHN ("VHN", "Vacation home number"),
		ASN ("ASN", "Answering service number"),
		EMR ("EMR", "Emergency number"),
		NET ("NET", "Network (email) address"),
		BPN ("BPN", "Beeper number");
		final String DESC;final String CODE;
		private static final Map<String, TelUseCode> codeMap = new HashMap<>();
		static {for (TelUseCode tuc : TelUseCode.values()) {codeMap.put(tuc.CODE, tuc);}}
		TelUseCode(String code, String desc) {this.CODE = code;this.DESC = desc;}
		public static TelUseCode get(String code) {return codeMap.get(code);}
	}
