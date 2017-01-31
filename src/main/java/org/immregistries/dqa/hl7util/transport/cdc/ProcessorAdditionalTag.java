package org.immregistries.dqa.hl7util.transport.cdc;

import java.io.PrintWriter;

public class ProcessorAdditionalTag extends Processor
{

  public ProcessorAdditionalTag(CDCWSDLServer server) {
    super(server);
  }

  public static void printExplanation(PrintWriter out, String processorName) {
    out.println("<h3>Additional Tag</h3>");
    out.println("<p>This responds correctly except the response contains an additional tag that is not expected.  </p>");
  }

  public void doProcessMessage(PrintWriter out, SubmitSingleMessage ssm) throws Fault {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("  <Header />");
    out.println("   <Body>");
    out.println("      <submitSingleMessageResponse xmlns=\"urn:cdc:iisb:2011\">");
    out.print("        <return><![CDATA[");
    server.process(ssm, out);
    out.println("]]></return>");
    out.println("      <additionalUnexpectedTag reason=\"Receiving system is not expecting this tag this based on the WSDL\"/>");
    out.println("      </submitSingleMessageResponse>");
    out.println("  </Body>");
    out.println("</Envelope>");
  }

  public void doConnectivityTest(PrintWriter out, String echoBack) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("  <Body>");
    out.println("    <connectivityTestResponse xmlns=\"urn:cdc:iisb:2011\">");
    out.println("      <return>" + server.getEchoBackMessage(echoBack) + "</return>   ");
    out.println("      <additionalUnexpectedTag reason=\"Receiving system is not expecting this tag this based on the WSDL\"/>");
    out.println("    </connectivityTestResponse>");
    out.println("  </Body>");
    out.println("</Envelope>");
  }

  public void doPrintException(PrintWriter out, UnknownFault unknownFault) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("   <Body>");
    out.println("      <Fault xmlns:ns4=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    out.println("         <Code>");
    out.println("            <Value>" + unknownFault.getFaultCodeValue() + "</Value>");
    out.println("         </Code>");
    out.println("         <Reason>");
    out.println("            <Text xml:lang=\"en\">" + unknownFault.getFaultReasonText() + "</Text>");
    out.println("         </Reason>");
    out.println("         <Detail>");
    out.println("            <fault xmlns=\"urn:cdc:iisb:2011\">");
    out.println("               <Code>" + unknownFault.getDetailCode() + "</Code>");
    out.println(
        "               <Reason xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
            + unknownFault.getDetailReason() + "</Reason>");
    out.println("               <Detail>" + unknownFault.getDetailDetail() + "</Detail>");
    out.println("            </fault>");
    out.println("         </Detail>");
    out.println("         <additionalUnexpectedTag reason=\"Receiving system is not expecting this tag this based on the WSDL\"/>");
    out.println("      </Fault>");
    out.println("   </Body>");
    out.println("</Envelope>");
  }

  public void doPrintException(PrintWriter out, MessageTooLargeFault messageTooLargeFault) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:junk=\"urn:foo:bar\">");
    out.println("  <Body>");
    out.println("    <Fault>");
    out.println("      <Code>");
    out.println("        <Value>" + messageTooLargeFault.getFaultCodeValue() + "</Value>");
    out.println("      </Code>");
    out.println("      <Reason>");
    out.println("        <Text xml:lang=\"en\">" + messageTooLargeFault.getFaultReasonText() + "</Text>");
    out.println("      </Reason>");
    out.println("      <Detail>");
    out.println("        <MessageTooLargeFault xmlns=\"urn:cdc:iisb:2011\">");
    out.println("          <Code>" + messageTooLargeFault.getDetailCode() + "</Code>");
    out.println(
        "          <Reason xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
            + messageTooLargeFault.getDetailReason() + "</Reason>");
    out.println("          <Detail>" + messageTooLargeFault.getDetailDetail() + "</Detail>");
    out.println("        </MessageTooLargeFault>");
    out.println("        <additionalUnexpectedTag reason=\"Receiving system is not expecting this tag this based on the WSDL\"/>");
    out.println("      </Detail>");
    out.println("    </Fault>");
    out.println("  </Body>");
    out.println("</Envelope>");
  }

  public void doPrintException(PrintWriter out, UnsupportedOperationFault unsupportedOperationFault) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("   <Body>");
    out.println("      <Fault xmlns:ns4=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    out.println("         <Code>");
    out.println("            <Value>" + unsupportedOperationFault.getFaultCodeValue() + "</Value>");
    out.println("         </Code>");
    out.println("         <Reason>");
    out.println("            <Text xml:lang=\"en\">" + unsupportedOperationFault.getFaultReasonText() + "</Text>");
    out.println("         </Reason>");
    out.println("         <Detail>");
    out.println("            <UnsupportedOperationFault xmlns=\"urn:cdc:iisb:2011\">");
    out.println("               <Code>" + unsupportedOperationFault.getDetailCode() + "</Code>");
    out.println(
        "               <Reason xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
            + unsupportedOperationFault.getDetailReason() + "</Reason>");
    out.println("               <Detail>" + unsupportedOperationFault.getDetailDetail() + "</Detail>");
    out.println("               <additionalUnexpectedTag reason=\"Receiving system is not expecting this tag this based on the WSDL\"/>");
    out.println("            </UnsupportedOperationFault>");
    out.println("         </Detail>");
    out.println("      </Fault>");
    out.println("   </Body>");
    out.println("</Envelope>");
  }

  
  public void doPrintException(PrintWriter out, SecurityFault securityFault) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("   <Body>");
    out.println("      <Fault xmlns:ns4=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    out.println("         <Code>");
    out.println("            <Value>" + securityFault.getFaultCodeValue() + "</Value>");
    out.println("         </Code>");
    out.println("         <Reason>");
    out.println("            <Text xml:lang=\"en\">" + securityFault.getFaultReasonText() + "</Text>");
    out.println("         </Reason>");
    out.println("         <Detail>");
    out.println("            <SecurityFault xmlns=\"urn:cdc:iisb:2011\">");
    out.println("               <Code>" + securityFault.getDetailCode() + "</Code>");
    out.println("               <Reason xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
            + securityFault.getDetailReason() + "</Reason>");
    out.println("               <Detail>" + securityFault.getDetailDetail() + "</Detail>");
    out.println("               <additionalUnexpectedTag reason=\"Receiving system is not expecting this tag this based on the WSDL\"/>");
    out.println("            </SecurityFault>");
    out.println("         </Detail>");
    out.println("      </Fault>");
    out.println("   </Body>");
    out.println("</Envelope>");
  }
}
