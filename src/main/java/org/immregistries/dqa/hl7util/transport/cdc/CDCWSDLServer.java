package org.immregistries.dqa.hl7util.transport.cdc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class CDCWSDLServer {
  private static final String CDATA_END = "]]>";
  private static final String CDATA_START = "<![CDATA[";

  private Processor processor = new Processor(this);

  public void setProcessorName(String processorName) {
    processor = ProcessorFactory.createProcessor(processorName, this);
  }

  @SuppressWarnings("resource")
  public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String xmlMessage = getBody(req);
    resp.setContentType("application/soap+xml; charset=UTF-8; action=\"urn:cdc:iisb:2011:connectivityTest\"");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    try {
      if (isConnectivityTest(xmlMessage)) {
        String echoBack = getEchoBack(xmlMessage);
        processor.doConnectivityTest(out, echoBack);
      } else if (isSubmitSingleMessage(xmlMessage)) {
        SubmitSingleMessage ssm = getSubmitSingleMessage(xmlMessage);
        authorize(ssm);
        processor.doProcessMessage(out, ssm);
      } else {
        throw new UnsupportedOperationFault("Expected either connectivityTest or SubmitSingleMessage");
      }
    } catch (MessageTooLargeFault mtlf) {
      out = quitelyResetBuffer(resp, out);
      processor.doPrintException(out, mtlf);
    } catch (SecurityFault sf) {
      out = quitelyResetBuffer(resp, out);
      processor.doPrintException(out, sf);
    } catch (UnsupportedOperationFault uof) {
      out = quitelyResetBuffer(resp, out);
      processor.doPrintException(out, uof);
    } catch (UnknownFault uf) {
      out = quitelyResetBuffer(resp, out);
      processor.doPrintException(out, uf);
    } catch (Exception e) {
      UnknownFault uf = new UnknownFault("Exception ocurred", e);
      out = quitelyResetBuffer(resp, out);
      processor.doPrintException(out, uf);
    } finally {
      out.close();
    }
  }

  public PrintWriter quitelyResetBuffer(HttpServletResponse resp, PrintWriter out)  {
    try {
      resp.resetBuffer();
      return new PrintWriter(resp.getOutputStream());
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
      // ignore, will just have to print as buffer has already been flushed
    }
    catch (IllegalStateException ise)
    {
      ise.printStackTrace();
      // ignore, will just have to print as buffer has already been flushed
    }
    return out;
  }

  public abstract void authorize(SubmitSingleMessage ssm) throws SecurityFault;

  public abstract void process(SubmitSingleMessage ssm, PrintWriter out) throws Fault;

  public abstract String getEchoBackMessage(String message);

  private static String getBody(HttpServletRequest req) throws IOException {

    String body = null;
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;

    try {
      InputStream inputStream = req.getInputStream();
      if (inputStream != null) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[128];
        int bytesRead = -1;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
          stringBuilder.append(charBuffer, 0, bytesRead);
        }
      } else {
        stringBuilder.append("");
      }
    } catch (IOException ex) {
      throw ex;
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException ex) {
          throw ex;
        }
      }
    }

    body = stringBuilder.toString();
    return body;
  }

  private boolean isConnectivityTest(String xmlMessage) {
    if (xmlMessage.indexOf("<connectivityTest ") > 0 || xmlMessage.indexOf(":connectivityTest ") > 0 || 
    		xmlMessage.indexOf("<connectivityTest>") > 0 || xmlMessage.indexOf(":connectivityTest>") > 0) {
      return true;
    }
    return false;
  }

  private boolean isSubmitSingleMessage(String xmlMessage) {
    if (xmlMessage.indexOf("<submitSingleMessage ") > 0 || xmlMessage.indexOf(":submitSingleMessage ") > 0
    		|| xmlMessage.indexOf("<submitSingleMessage>") > 0 || xmlMessage.indexOf(":submitSingleMessage>") > 0) {
      return true;
    }
    return false;
  }

  private String getEchoBack(String xmlMessage) {
    int startPos = xmlMessage.indexOf("<connectivityTest");
    if (startPos < 0) {
      startPos = xmlMessage.indexOf(":connectivityTest");
    }
    if (startPos > 0) {
      startPos = xmlMessage.indexOf("echoBack", startPos);
      if (startPos > 0) {
        startPos = xmlMessage.indexOf(">", startPos);
        if (startPos > 0) {
          startPos++;
          int endPos = xmlMessage.indexOf("</", startPos);
          if (endPos > 0) {
            return xmlMessage.substring(startPos, endPos);
          }
        }
      }
    }
    return "";
  }

  private SubmitSingleMessage getSubmitSingleMessage(String xmlMessage) {
    SubmitSingleMessage submitSingleMessage = new SubmitSingleMessage();
    int startPos = xmlMessage.indexOf("<submitSingleMessage");
    if (startPos < 0) {
      startPos = xmlMessage.indexOf(":submitSingleMessage");
    }
    if (startPos > 0) {
      submitSingleMessage.setUsername(readField(xmlMessage, startPos, "username"));
      submitSingleMessage.setPassword(readField(xmlMessage, startPos, "password"));
      submitSingleMessage.setFacilityID(readField(xmlMessage, startPos, "facilityID"));
      submitSingleMessage.setHl7Message(readField(xmlMessage, startPos, "hl7Message"));
    }
    return submitSingleMessage;
  }

  private String readField(String xmlMessage, int startPos, String field) {
    String value = "";
    int fieldStartPos = xmlMessage.indexOf(field, startPos);
    if (fieldStartPos > 0) {
      fieldStartPos = xmlMessage.indexOf(">", fieldStartPos);
      if (fieldStartPos > 0) {
        fieldStartPos++;
        int fieldEndPos = xmlMessage.indexOf("<", fieldStartPos);
        if (fieldEndPos > 0) {
          value = xmlMessage.substring(fieldStartPos, fieldEndPos).trim();
          if (value.startsWith(CDATA_START) && value.endsWith(CDATA_END)) {
            value = value.substring(CDATA_START.length(), value.length() - CDATA_END.length());
          } else {
            value = value.replaceAll("\\Q&amp;\\E", "&").replaceAll("\\Q&#xd;\\E", "\r");
          }
        }
      }
    }
    return value;
  }

  
  public static void printWSDL(PrintWriter out, String url)
  {
    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    out.println("<definitions xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" ");
    out.println("             xmlns:wsp=\"http://www.w3.org/ns/ws-policy\" ");
    out.println("             xmlns:wsp1_2=\"http://schemas.xmlsoap.org/ws/2004/09/policy\" ");
    out.println("             xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\" ");
    out.println("             xmlns:wsaw=\"http://www.w3.org/2005/08/addressing\"");
    out.println("             xmlns:soap12=\"http://schemas.xmlsoap.org/wsdl/soap12/\" ");
    out.println("             xmlns:tns=\"urn:cdc:iisb:2011\" ");
    out.println("             xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ");
    out.println("             xmlns=\"http://schemas.xmlsoap.org/wsdl/\" ");
    out.println("             targetNamespace=\"urn:cdc:iisb:2011\"");
    out.println("             name=\"IISServiceNew\">");
    out.println("");
    out.println("<!-- schema for types -->");
    out.println("<types>");
    out.println("    <xsd:schema elementFormDefault=\"qualified\" targetNamespace=\"urn:cdc:iisb:2011\">");
    out.println("    ");
    out.println("    <xsd:complexType name=\"connectivityTestRequestType\">");
    out.println("      <xsd:sequence>");
    out.println("        <xsd:element name=\"echoBack\" type=\"xsd:string\" minOccurs=\"1\" maxOccurs=\"1\" nillable=\"true\"/>");
    out.println("      </xsd:sequence>");
    out.println("    </xsd:complexType>");
    out.println("    ");
    out.println("    <xsd:complexType name=\"connectivityTestResponseType\">");
    out.println("      <xsd:sequence>");
    out.println("        <xsd:element name=\"return\" type=\"xsd:string\" minOccurs=\"1\" maxOccurs=\"1\" nillable=\"true\"/>");
    out.println("      </xsd:sequence>");
    out.println("    </xsd:complexType>");
    out.println("  ");
    out.println("    <xsd:complexType name=\"submitSingleMessageRequestType\">");
    out.println("      <xsd:sequence>");
    out.println("        <xsd:element name=\"username\" type=\"xsd:string\" minOccurs=\"0\" maxOccurs=\"1\" nillable=\"true\"/>");
    out.println("        <xsd:element name=\"password\" type=\"xsd:string\" minOccurs=\"0\" maxOccurs=\"1\" nillable=\"true\"/>");
    out.println("        <xsd:element name=\"facilityID\" type=\"xsd:string\"  minOccurs=\"0\" maxOccurs=\"1\" nillable=\"true\"/>");
    out.println("        <xsd:element name=\"hl7Message\" type=\"xsd:string\" minOccurs=\"1\" maxOccurs=\"1\" nillable=\"true\"/>");
    out.println("      </xsd:sequence>");
    out.println("    </xsd:complexType>");
    out.println("    ");
    out.println("    <xsd:complexType name=\"submitSingleMessageResponseType\">");
    out.println("      <xsd:sequence>");
    out.println("        <xsd:element name=\"return\" type=\"xsd:string\" minOccurs=\"1\" maxOccurs=\"1\" nillable=\"true\"/>");
    out.println("      </xsd:sequence>    ");
    out.println("    </xsd:complexType>");
    out.println("    ");
    out.println("    <xsd:complexType name=\"soapFaultType\">");
    out.println("       <xsd:sequence>");
    out.println("        <xsd:element name=\"Code\" type=\"xsd:integer\" minOccurs=\"1\"/>");
    out.println("        <xsd:element name=\"Reason\" type=\"xsd:string\" minOccurs=\"1\"/>");
    out.println("        <xsd:element name=\"Detail\" type=\"xsd:string\" minOccurs=\"1\"/>");
    out.println("       </xsd:sequence>");
    out.println("    </xsd:complexType> ");
    out.println("  ");
    out.println("    <xsd:complexType name=\"UnsupportedOperationFaultType\">");
    out.println("       <xsd:sequence>");
    out.println("        <xsd:element name=\"Code\" type=\"xsd:integer\" minOccurs=\"1\"/>");
    out.println("        <xsd:element name=\"Reason\" fixed=\"UnsupportedOperation\"/>");
    out.println("        <xsd:element name=\"Detail\" type=\"xsd:string\" minOccurs=\"1\"/>");
    out.println("       </xsd:sequence>    ");
    out.println("    </xsd:complexType>");
    out.println("    ");
    out.println("    <xsd:complexType name=\"SecurityFaultType\">");
    out.println("       <xsd:sequence>");
    out.println("        <xsd:element name=\"Code\" type=\"xsd:integer\" minOccurs=\"1\"/>");
    out.println("        <xsd:element name=\"Reason\" fixed=\"Security\"/>");
    out.println("        <xsd:element name=\"Detail\" type=\"xsd:string\" minOccurs=\"1\"/>");
    out.println("       </xsd:sequence>    ");
    out.println("    </xsd:complexType>");
    out.println("    ");
    out.println("    <xsd:complexType name=\"MessageTooLargeFaultType\">");
    out.println("       <xsd:sequence>");
    out.println("        <xsd:element name=\"Code\" type=\"xsd:integer\" minOccurs=\"1\"/>");
    out.println("        <xsd:element name=\"Reason\" fixed=\"MessageTooLarge\"/>");
    out.println("        <xsd:element name=\"Detail\" type=\"xsd:string\" minOccurs=\"1\"/>");
    out.println("       </xsd:sequence>    ");
    out.println("    </xsd:complexType>        ");
    out.println("      ");
    out.println("    <xsd:element name=\"connectivityTest\" type=\"tns:connectivityTestRequestType\"/>");
    out.println("    <xsd:element name=\"connectivityTestResponse\" type=\"tns:connectivityTestResponseType\"/>");
    out.println("    <xsd:element name=\"submitSingleMessage\" type=\"tns:submitSingleMessageRequestType\"/>");
    out.println("    <xsd:element name=\"submitSingleMessageResponse\" type=\"tns:submitSingleMessageResponseType\"/>");
    out.println("    <xsd:element name=\"fault\" type=\"tns:soapFaultType\"/>");
    out.println("    <xsd:element name=\"UnsupportedOperationFault\" type=\"tns:UnsupportedOperationFaultType\"/>");
    out.println("    <xsd:element name=\"SecurityFault\" type=\"tns:SecurityFaultType\"/>");
    out.println("    <xsd:element name=\"MessageTooLargeFault\" type=\"tns:MessageTooLargeFaultType\"/>");
    out.println("    ");
    out.println("    </xsd:schema>");
    out.println("  </types>");
    out.println("");
    out.println("  <!-- Message definitions -->");
    out.println("  <message name=\"connectivityTest_Message\">");
    out.println("    <documentation>connectivity test request</documentation>");
    out.println("    <part name=\"parameters\" element=\"tns:connectivityTest\" />");
    out.println("  </message>");
    out.println("");
    out.println("  <message name=\"connectivityTestResponse_Message\">");
    out.println("    <documentation>connectivity test  response</documentation>");
    out.println("    <part name=\"parameters\" element=\"tns:connectivityTestResponse\" />");
    out.println("  </message>");
    out.println("");
    out.println("  <message name=\"submitSingleMessage_Message\">");
    out.println("    <documentation>submit single message request.</documentation>");
    out.println("    <part name=\"parameters\" element=\"tns:submitSingleMessage\" />");
    out.println("  </message>");
    out.println("");
    out.println("  <message name=\"submitSingleMessageResponse_Message\">");
    out.println("    <documentation>submit single message response</documentation>");
    out.println("    <part name=\"parameters\" element=\"tns:submitSingleMessageResponse\" />");
    out.println("  </message>");
    out.println("    ");
    out.println("  <message name=\"UnknownFault_Message\">");
    out.println("    <part name=\"fault\" element=\"tns:fault\"/>");
    out.println("  </message>");
    out.println("  ");
    out.println("  <message name=\"UnsupportedOperationFault_Message\">");
    out.println("  <part name=\"fault\" element=\"tns:UnsupportedOperationFault\"/>");
    out.println("  </message>");
    out.println("  ");
    out.println("  <message name=\"SecurityFault_Message\">");
    out.println("    <part name=\"fault\" element=\"tns:SecurityFault\"/>");
    out.println("  </message>");
    out.println("  <message name=\"MessageTooLargeFault_Message\">");
    out.println("    <part name=\"fault\" element=\"tns:MessageTooLargeFault\"/>");
    out.println("  </message>");
    out.println("    ");
    out.println("  <!-- Operation/transaction declarations -->");
    out.println("  <portType name=\"IIS_PortType\">");
    out.println("    <operation name=\"connectivityTest\">");
    out.println("      <documentation>the connectivity test</documentation>");
    out.println("      <input message=\"tns:connectivityTest_Message\" wsaw:Action=\"urn:cdc:iisb:2011:connectivityTest\"/>");
    out.println("      <output message=\"tns:connectivityTestResponse_Message\" wsaw:Action=\"urn:cdc:iisb:2011:connectivityTestResponse\"/>");
    out.println("      <fault name=\"UnknownFault\" message=\"tns:UnknownFault_Message\"/>  <!-- a general soap fault -->");
    out.println("      <fault name=\"UnsupportedOperationFault\" message=\"tns:UnsupportedOperationFault_Message\"/>  <!-- The UnsupportedOperation soap fault -->");
    out.println("    </operation>");
    out.println("");
    out.println("    <operation name=\"submitSingleMessage\">");
    out.println("      <documentation>submit single message</documentation>");
    out.println("      <input message=\"tns:submitSingleMessage_Message\" wsaw:Action=\"urn:cdc:iisb:2011:submitSingleMessage\"/>");
    out.println("      <output message=\"tns:submitSingleMessageResponse_Message\" wsaw:Action=\"urn:cdc:iisb:2011:submitSingleMessageResponse\"/>");
    out.println("      <fault name=\"UnknownFault\" message=\"tns:UnknownFault_Message\"/>  <!-- a general soap fault -->");
    out.println("      <fault name=\"SecurityFault\" message=\"tns:SecurityFault_Message\"/>");
    out.println("      <fault name=\"MessageTooLargeFault\" message=\"tns:MessageTooLargeFault_Message\"/>");
    out.println("    </operation>");
    out.println("  </portType>");
    out.println("");
    out.println("  <!-- SOAP 1.2 Binding -->");
    out.println("  <binding name=\"client_Binding_Soap12\" type=\"tns:IIS_PortType\">");
    out.println("    <soap12:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\" />");
    out.println("    <operation name=\"connectivityTest\">");
    out.println("      <soap12:operation soapAction=\"urn:cdc:iisb:2011:connectivityTest\" />");
    out.println("      <input><soap12:body use=\"literal\" /></input>");
    out.println("      <output><soap12:body use=\"literal\" /></output>");
    out.println("      <fault name=\"UnknownFault\"><soap12:fault use=\"literal\" name=\"UnknownFault\"/></fault>");
    out.println("      <fault name=\"UnsupportedOperationFault\"><soap12:fault use=\"literal\" name=\"UnsupportedOperationFault\"/></fault>");
    out.println("    </operation>");
    out.println("    <operation name=\"submitSingleMessage\">");
    out.println("      <soap12:operation soapAction=\"urn:cdc:iisb:2011:submitSingleMessage\" />");
    out.println("      <input><soap12:body use=\"literal\" /></input>");
    out.println("      <output><soap12:body use=\"literal\" /></output>");
    out.println("      <fault name=\"UnknownFault\"><soap12:fault use=\"literal\" name=\"UnknownFault\"/></fault>");
    out.println("      <fault name=\"SecurityFault\"><soap12:fault use=\"literal\" name=\"SecurityFault\"/></fault>");
    out.println("      <fault name=\"MessageTooLargeFault\"><soap12:fault use=\"literal\" name=\"MessageTooLargeFault\"/></fault>");
    out.println("    </operation>");
    out.println("  </binding>");
    out.println("");
    out.println("  <!-- Service definition -->");
    out.println("  <service name=\"client_Service\">");
    out.println("    <port binding=\"tns:client_Binding_Soap12\" name=\"client_Port_Soap12\">");
    out.println("      <soap12:address location=\"" + url + "\" />");
    out.println("    </port>");
    out.println("  </service>");
    out.println("</definitions>");

  }
}
