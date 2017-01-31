package org.immregistries.dqa.hl7util.transport.cdc;

@SuppressWarnings("serial")
public class SecurityFault extends Fault {
  public SecurityFault(String message) {
    super(message, FaultDetail.SECURITY);
  }
}
