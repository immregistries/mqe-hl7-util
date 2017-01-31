package org.immregistries.dqa.hl7util.transport.cdc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ProcessorUrlEncoded extends Processor
{

  public ProcessorUrlEncoded(CDCWSDLServer server) {
    super(server);
  }

  public static void printExplanation(PrintWriter out, String processorName) {
    out.println("<h3>URL Encoded</h3>");
    out.println("<p>This responds with the correct XML but has URL encoded content.  </p>");
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
    try {
      out.print(URLEncoder.encode(sw.toString(), "UTF-8"));
    } catch (UnsupportedEncodingException usce) {
      // ignore
    }
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
    try {
      out.println("      <return>" + URLEncoder.encode(server.getEchoBackMessage(echoBack), "UTF-8") + "</return>   ");
    } catch (UnsupportedEncodingException usce) {
      // ignore
    }
    out.println("    </connectivityTestResponse>");
    out.println("  </Body>");
    out.println("</Envelope>");
  }

  
}
