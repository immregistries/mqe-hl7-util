package org.immregistries.dqa.hl7util.transport.cdc;

@SuppressWarnings("serial")
public class UnknownFault extends Fault {
  public UnknownFault(String message, Throwable throwable) {
    super(message, throwable, FaultDetail.UNKNOWN);
  }

  public UnknownFault(String message) {
    super(message, FaultDetail.UNKNOWN);
  }

}
