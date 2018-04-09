package org.immregistries.dqa.vxu;

import org.immregistries.dqa.hl7util.SeverityLevel;

public interface DetectionInfo {
  String getDisplayText();
  VxuField getTargetField();
  SeverityLevel getSeverity();
}
