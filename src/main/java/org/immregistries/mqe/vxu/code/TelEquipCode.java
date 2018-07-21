package org.immregistries.mqe.vxu.code;

import java.util.HashMap;
import java.util.Map;

enum TelEquipCode {
  PH("PH", "Telephone"),
  FX("FX", "Fax"),
  MD("MD", "Modem"),
  CP("CP", "Cellular phone"),
  BP("BP", "Beeper"),
  Internet("Internet", "Internet address: Use only if telecommunication use code is NET"),
  X400("X.400", "X.400 email address: Use only if telecommunication use code is NET"),
  TDD("TDD", "Telecommunications Device for the Deaf"),
  TTY("TTY", "Teletypewriter");
  final String DESC;
  final String CODE;
  private static final Map<String, TelEquipCode> codeMap = new HashMap<>();

  static {
    for (TelEquipCode tuc : TelEquipCode.values()) {
      codeMap.put(tuc.CODE, tuc);
    }
  }

  TelEquipCode(String code, String desc) {
    this.CODE = code;
    this.DESC = desc;
  }

  public static TelEquipCode get(String code) {
    return codeMap.get(code);
  }
}
