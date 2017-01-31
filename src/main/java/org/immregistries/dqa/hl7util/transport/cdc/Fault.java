package org.immregistries.dqa.hl7util.transport.cdc;

import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressWarnings("serial")
public class Fault extends Exception {
  private String faultCodeValue = "";
  private String faultReasonText = "";
  private String detailCode = "";
  private String detailReason = "";
  private String detailDetail = "";
 
  
  public Fault(String message, Throwable throwable, FaultDetail faultDetail)
  {
    super(message, throwable);
    faultCodeValue = "Sender";
    faultReasonText = message;
    detailCode = faultDetail.getCode();
    detailReason = faultDetail.getReason();
    StringWriter sw = new StringWriter();
    PrintWriter out = new PrintWriter(sw);
    throwable.printStackTrace(out);
    out.close();
    detailDetail = sw.toString();
  }

  
  public Fault(String message, FaultDetail faultDetail)
  {
    super(message);
    faultCodeValue = "Sender";
    faultReasonText = message;
    detailCode = faultDetail.getCode();
    detailReason = faultDetail.getReason();
    detailDetail = faultDetail.getDetail();
  }

  public String getFaultCodeValue() {
    return faultCodeValue;
  }
  public void setFaultCodeValue(String faultCodeValue) {
    this.faultCodeValue = faultCodeValue;
  }
  public String getFaultReasonText() {
    return faultReasonText;
  }
  public void setFaultReasonText(String faultReasonText) {
    this.faultReasonText = faultReasonText;
  }
  public String getDetailCode() {
    return detailCode;
  }
  public void setDetailCode(String detailCode) {
    this.detailCode = detailCode;
  }
  public String getDetailReason() {
    return detailReason;
  }
  public void setDetailReason(String detailReason) {
    this.detailReason = detailReason;
  }
  public String getDetailDetail() {
    return detailDetail;
  }
  public void setDetailDetail(String detailDetail) {
    this.detailDetail = detailDetail;
  }
}
