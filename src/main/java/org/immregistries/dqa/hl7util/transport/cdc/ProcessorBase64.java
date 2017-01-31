package org.immregistries.dqa.hl7util.transport.cdc;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.codec.binary.Base64;

public class ProcessorBase64 extends Processor
{

  public ProcessorBase64(CDCWSDLServer server) {
    super(server);
  }

  public static void printExplanation(PrintWriter out, String processorName) {
    out.println("<h3>Base 64</h3>");
    out.println("<p>This responds with the correct XML but has base64 encoded content.  </p>");
  }

  public void doProcessMessage(PrintWriter out, SubmitSingleMessage ssm) throws Fault {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("  <Header />");
    out.println("   <Body>");
    out.println("      <submitSingleMessageResponse xmlns=\"urn:cdc:iisb:2011\">");
    out.print("        <return>");
    StringWriter sw = new StringWriter();
    PrintWriter outIgnore = new PrintWriter(sw);
    server.process(ssm, outIgnore);
    outIgnore.close();
    out.print(new String(Base64.encodeBase64(sw.toString().getBytes())));
    out.println("</return>");
    out.println("      </submitSingleMessageResponse>");
    out.println("  </Body>");
    out.println("</Envelope>");
  }

  public void doConnectivityTest(PrintWriter out, String echoBack) {
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">");
    out.println("  <Body>");
    out.println("    <connectivityTestResponse xmlns=\"urn:cdc:iisb:2011\">");
    out.println("      <return>" + new String(Base64.encodeBase64(server.getEchoBackMessage(echoBack).getBytes()))
        + "</return>   ");
    out.println("    </connectivityTestResponse>");
    out.println("  </Body>");
    out.println("</Envelope>");
  }

}
