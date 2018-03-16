package org.immregistries.dqa.hl7util.model;

import org.immregistries.dqa.vxu.VxuField;

public class MetaFieldInfo {
  private String value;
  private Hl7Location hl7Location = null;
  private VxuField vxuField = null;

  public VxuField getVxuField() {
    return vxuField;
  }

  public void setVxuField(VxuField vxuField) {
    this.vxuField = vxuField;
  }

  public MetaFieldInfo() {
    // default
  }

  public MetaFieldInfo(String value, Hl7Location hl7Location) {
    this.value = value;
    this.hl7Location = hl7Location;
  }

  public Hl7Location getHl7Location() {
    return hl7Location;
  }

  public void setHl7Location(Hl7Location hl7Location) {
    this.hl7Location = hl7Location;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    if (value == null) {
      this.value = "";
    } else {
      this.value = value;
    }
  }
}
