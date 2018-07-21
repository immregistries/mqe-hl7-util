package org.immregistries.mqe.vxu;

import java.util.HashMap;
import java.util.Map;

public enum VxuObject {
  GENERAL("General", "GEN"),
  MESSAGE_HEADER("Message Header", "MSH"),
  NEXT_OF_KIN("Next-of-kin", "NK1"),
  OBSERVATION("Observation", "OBX"),
  PATIENT("Patient", "PID"),
  VACCINATION("Vaccination", "VAC");

  private static Map<String, VxuObject> mappd = new HashMap<String, VxuObject>();

  static {
    for (VxuObject obj : VxuObject.values()) {
      mappd.put(obj.getDescription(), obj);
    }
  }

  private String description;
  private String location;

  private VxuObject(String desc, String location) {
    this.description = desc;
    this.location = location;
  }

  public String getDescription() {
    return description;
  }

  public static VxuObject getByDesc(String desc) {
    return mappd.get(desc);
  }

  public String getLocation() {
    return location;
  }

}
