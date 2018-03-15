package org.immregistries.dqa.hl7util.model;

import org.immregistries.dqa.vxu.VxuField;

public class MetaFieldInfo {
  private String value;
  private ErrorLocation errorLocation = null;
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

  public MetaFieldInfo(String value, ErrorLocation errorLocation) {
    this.value = value;
    this.errorLocation = errorLocation;
  }

  public ErrorLocation getErrorLocation() {
    return errorLocation;
  }

  public void setErrorLocation(ErrorLocation errorLocation) {
    this.errorLocation = errorLocation;
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
