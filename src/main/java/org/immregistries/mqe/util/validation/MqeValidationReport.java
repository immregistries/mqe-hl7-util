package org.immregistries.mqe.util.validation;

import org.immregistries.mqe.hl7util.Reportable;

public interface MqeValidationReport extends Reportable {
  MqeDetection getDetection();
  String getValueReceived();
  int getPositionId();
  boolean isError();
  String getMessage();
}
