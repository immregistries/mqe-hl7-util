package org.immregistries.dqa.hl7util.transport.cdc;

import java.io.PrintWriter;

public class ProcessorMistakes extends Processor
{

  public ProcessorMistakes(CDCWSDLServer server) {
    super(server);
  }

  public static void printExplanation(PrintWriter out, String processorName) {
    out.println("<h3>Mistakes</h3>");
    out.println("<p>This responds with properly formed XML but with problems with which tags are being used.  </p>");
  }

  public void doProcessMessage(PrintWriter out, SubmitSingleMessage ssm) throws Fault {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("  <head />");
    out.println("   <body>");
    out.println("      <SubmitSingleMessageResponse xmlns=\"urn:cdc:iisb:2011\">");
    out.print("        <message><![CDATA[");
    server.process(ssm, out);
    out.println("]]></message>");
    out.println("      </SubmitSingleMessageResponse>");
    out.println("  </body>");
    out.println("</Envelope>");
  }

  public void doConnectivityTest(PrintWriter out, String echoBack) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("  <body>");
    out.println("    <connectivityTestResponse xmlns=\"urn:cdc:iisb:2011\">");
    out.println("      <ack>" + server.getEchoBackMessage(echoBack) + "</ack>   ");
    out.println("    </connectivityTestResponse>");
    out.println("  </body>");
    out.println("</Envelope>");
  }

  public void doPrintException(PrintWriter out, UnknownFault unknownFault) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("   <Body>");
    out.println("      <Fault xmlns:ns4=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    out.println("         <Code>");
    out.println("            <Value></Value>");
    out.println("         </Code>");
    out.println("      </Fault>");
    out.println("   </Body>");
    out.println("</Envelope>");
  }

  public void doPrintException(PrintWriter out, MessageTooLargeFault messageTooLargeFault) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:junk=\"urn:foo:bar\">");
    out.println("  <Body>");
    out.println("    <Fault>");
    out.println("    </Fault>");
    out.println("  </Body>");
    out.println("</Envelope>");
  }

  public void doPrintException(PrintWriter out, UnsupportedOperationFault unsupportedOperationFault) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("   <Body>");
    out.println("      <Fault xmlns:ns4=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    out.println("         <Reason>");
    out.println("            <Text xml:lang=\"en\">" + unsupportedOperationFault.getFaultReasonText() + "</Text>");
    out.println("         </Reason>");
    out.println("      </Fault>");
    out.println("   </Body>");
    out.println("</Envelope>");
  }

  public void doPrintException(PrintWriter out, SecurityFault securityFault) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("   <Body>");
    out.println("      <Fault xmlns:ns4=\"http://schemas.xmlsoap.org/soap/envelope/\">");
    out.println("         <Detail>");
    out.println("            <SecurityFault xmlns=\"urn:cdc:iisb:2011\">");
    out.println(
        "               <Reason xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">"
            + securityFault.getDetailReason() + "</Reason>");
    out.println("               <Detail>" + securityFault.getDetailDetail() + "</Detail>");
    out.println("            </SecurityFault>");
    out.println("         </Detail>");
    out.println("      </Fault>");
    out.println("   </Body>");
    out.println("</Envelope>");
  }

}
