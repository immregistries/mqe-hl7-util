package org.immregistries.dqa.hl7util.transport.cdc;

import java.io.PrintWriter;

public class ProcessorNoWhitespace extends Processor
{

  public ProcessorNoWhitespace(CDCWSDLServer server) {
    super(server);
  }

  public static void printExplanation(PrintWriter out, String processorName) {
    out.println("<h3>No White Space</h3>");
    out.println("<p>Same as the default but there is no white space in XML. </p>");
  }

  public void doProcessMessage(PrintWriter out, SubmitSingleMessage ssm) throws Fault {
    out.print("<?xml version='1.0' encoding='UTF-8'?>");
    out.print("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.print("<Header />");
    out.print("<Body>");
    out.print("<submitSingleMessageResponse xmlns=\"urn:cdc:iisb:2011\">");
    out.print("<return><![CDATA[");
    server.process(ssm, out);
    out.print("]]></return>");
    out.print("</submitSingleMessageResponse>");
    out.print("</Body>");
    out.print("</Envelope>");
  }

  public void doConnectivityTest(PrintWriter out, String echoBack) {
    out.print("<?xml version='1.0' encoding='UTF-8'?>");
    out.print("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.print("<Body>");
    out.print("<connectivityTestResponse xmlns=\"urn:cdc:iisb:2011\">");
    out.print("<return>" + server.getEchoBackMessage(echoBack) + "</return>");
    out.print("</connectivityTestResponse>");
    // out.print("</Body>");
    out.print("</Envelope>");
  }

  public void doPrintException(PrintWriter out, UnknownFault unknownFault) {
    out.print("<?xml version='1.0' encoding='UTF-8'?>");
    out.print("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.print("<Body>");
    out.print("<Fault xmlns:ns4=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    out.print("<Code>");
    out.print("<Value>" + unknownFault.getFaultCodeValue() + "</Value>");
    out.print("</Code>");
    out.print("<Reason>");
    out.print("<Text xml:lang=\"en\">" + unknownFault.getFaultReasonText() + "</Text>");
    out.print("</Reason>");
    out.print("<Detail>");
    out.print("<fault xmlns=\"urn:cdc:iisb:2011\">");
    out.print("<Code>" + unknownFault.getDetailCode() + "</Code>");
    out.print(
        "<Reason xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
            + unknownFault.getDetailReason() + "</Reason>");
    out.print("<Detail>" + unknownFault.getDetailDetail() + "</Detail>");
    out.print("</fault>");
    out.print("</Detail>");
    out.print("</Fault>");
    out.print("</Body>");
    out.print("</Envelope>");
  }

  public void doPrintException(PrintWriter out, MessageTooLargeFault messageTooLargeFault) {
    out.print("<?xml version='1.0' encoding='UTF-8'?>");
    out.print("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:junk=\"urn:foo:bar\">");
    out.print("<Body>");
    out.print("<Fault>");
    out.print("<Code>");
    out.print("<Value>" + messageTooLargeFault.getFaultCodeValue() + "</Value>");
    out.print("</Code>");
    out.print("<Reason>");
    out.print("<Text xml:lang=\"en\">" + messageTooLargeFault.getFaultReasonText() + "</Text>");
    out.print("</Reason>");
    out.print("<Detail>");
    out.print("<MessageTooLargeFault xmlns=\"urn:cdc:iisb:2011\">");
    out.print("<Code>" + messageTooLargeFault.getDetailCode() + "</Code>");
    out.print(
        "<Reason xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
            + messageTooLargeFault.getDetailReason() + "</Reason>");
    out.print("<Detail>" + messageTooLargeFault.getDetailDetail() + "</Detail>");
    out.print("</MessageTooLargeFault>");
    out.print("</Detail>");
    out.print("</Fault>");
    out.print("</Body>");
    out.print("</Envelope>");
  }

  public void doPrintException(PrintWriter out, UnsupportedOperationFault unsupportedOperationFault) {
    out.print("<?xml version='1.0' encoding='UTF-8'?>");
    out.print("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.print("<Body>");
    out.print("<Fault xmlns:ns4=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    out.print("<Code>");
    out.print("<Value>" + unsupportedOperationFault.getFaultCodeValue() + "</Value>");
    out.print("</Code>");
    out.print("<Reason>");
    out.print("<Text xml:lang=\"en\">" + unsupportedOperationFault.getFaultReasonText() + "</Text>");
    out.print("</Reason>");
    out.print("<Detail>");
    out.print("<UnsupportedOperationFault xmlns=\"urn:cdc:iisb:2011\">");
    out.print("<Code>" + unsupportedOperationFault.getDetailCode() + "</Code>");
    out.print(
        "<Reason xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
            + unsupportedOperationFault.getDetailReason() + "</Reason>");
    out.print("<Detail>" + unsupportedOperationFault.getDetailDetail() + "</Detail>");
    out.print("</UnsupportedOperationFault>");
    out.print("</Detail>");
    out.print("</Fault>");
    out.print("</Body>");
    out.print("</Envelope>");
  }

  public void doPrintException(PrintWriter out, SecurityFault securityFault) {
    out.print("<?xml version='1.0' encoding='UTF-8'?>");
    out.print("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.print("<Body>");
    out.print("<Fault xmlns:ns4=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    out.print("<Code>");
    out.print("<Value>" + securityFault.getFaultCodeValue() + "</Value>");
    out.print("</Code>");
    out.print("<Reason>");
    out.print("<Text xml:lang=\"en\">" + securityFault.getFaultReasonText() + "</Text>");
    out.print("</Reason>");
    out.print("<Detail>");
    out.print("<SecurityFault xmlns=\"urn:cdc:iisb:2011\">");
    out.print("<Code>" + securityFault.getDetailCode() + "</Code>");
    out.print(
        "<Reason xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
            + securityFault.getDetailReason() + "</Reason>");
    out.print("<Detail>" + securityFault.getDetailDetail() + "</Detail>");
    out.print("</SecurityFault>");
    out.print("</Detail>");
    out.print("</Fault>");
    out.print("</Body>");
    out.print("</Envelope>");
  }

}
