package org.immregistries.mqe.util.validation;

import org.immregistries.mqe.hl7util.ApplicationErrorCode;
import org.immregistries.mqe.hl7util.builder.AckERRCode;

public interface MqeDetectionType {
  String getWording();
  String getDescription();
  AckERRCode getAckErrCode();
  ApplicationErrorCode getApplicationErrorCode();
}
