package org.immregistries.mqe.vxu;

import org.immregistries.mqe.hl7util.SeverityLevel;

public interface DetectionInfo {
  String getDisplayText();
  VxuField getTargetField();
  SeverityLevel getSeverity();
}
