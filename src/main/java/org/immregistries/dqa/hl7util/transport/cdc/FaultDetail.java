package org.immregistries.dqa.hl7util.transport.cdc;

public class FaultDetail {
  
  public static final FaultDetail MESSAGE_TOO_LARGE = new FaultDetail();
  static
  {
    MESSAGE_TOO_LARGE.setCode("1313");
    MESSAGE_TOO_LARGE.setReason("MessageTooLarge");
    MESSAGE_TOO_LARGE.setDetail("More than one HL7 message was submitted. Please submit one and only one HL7 message per transaction");
  }

  public static final FaultDetail SECURITY = new FaultDetail();
  static
  {
    SECURITY.setCode("10");
    SECURITY.setReason("Security");
    SECURITY.setDetail("Invalid Username or Password");
  }

  public static final FaultDetail UNSUPPORTED_OPERATION = new FaultDetail();
  static
  {
    UNSUPPORTED_OPERATION.setCode("8675309");
    UNSUPPORTED_OPERATION.setReason("UnsupportedOperation");
    UNSUPPORTED_OPERATION.setDetail("This Operation is not supported");
  }

  public static final FaultDetail UNKNOWN = new FaultDetail();
  static
  {
    UNKNOWN.setCode("54353");
    UNKNOWN.setReason("My Reason");
    UNKNOWN.setDetail("An unexpected problem occured");
  }
  
  
  private String code = "";
  private String reason = "";
  private String detail = "";
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getReason() {
    return reason;
  }
  public void setReason(String reason) {
    this.reason = reason;
  }
  public String getDetail() {
    return detail;
  }
  public void setDetail(String detail) {
    this.detail = detail;
  }
}
