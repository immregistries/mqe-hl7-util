package org.immregistries.mqe.util.validation;

import org.immregistries.mqe.hl7util.ApplicationErrorCode;
import org.immregistries.mqe.hl7util.SeverityLevel;
import org.immregistries.mqe.hl7util.builder.AckERRCode;
import org.immregistries.mqe.vxu.VxuField;
import org.immregistries.mqe.vxu.VxuObject;

public interface MqeDetection {
  ApplicationErrorCode getApplicationErrorCode();
  String getDisplayText();
  MqeDetectionType getDetectionType();
  VxuField getTargetField();
  VxuObject getTargetObject();
  SeverityLevel getSeverity();
  String getMqeMqeCode();
  AckERRCode getHl7ErrorCode();
}
