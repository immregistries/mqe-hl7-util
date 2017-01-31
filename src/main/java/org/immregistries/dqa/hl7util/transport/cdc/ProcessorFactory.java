package org.immregistries.dqa.hl7util.transport.cdc;

import java.io.PrintWriter;

public class ProcessorFactory
{
  public static final String PROCESSOR_DEFAULT = "default";
  public static final String PROCESSOR_NO_WHITESPACE = "nowhitespace";
  public static final String PROCESSOR_MISTAKES = "mistakes";
  public static final String PROCESSOR_BAD_XML = "badxml";
  public static final String PROCESSOR_BASE_64 = "base64";
  public static final String PROCESSOR_INCORRECT_IMPLEMENTATION = "incorrectImplementation";
  public static final String PROCESSOR_URL_ENCODED = "urlEncoded";
  public static final String PROCESSOR_ADDITIONAL_TAG = "additionalTag";

  public static Processor createProcessor(String processorName, CDCWSDLServer server) {
    if (processorName.equals(PROCESSOR_NO_WHITESPACE)) {
      return new ProcessorNoWhitespace(server);
    }
    else if (processorName.equals(PROCESSOR_MISTAKES)) {
      return new ProcessorMistakes(server);
    }
    else if (processorName.equals(PROCESSOR_BAD_XML)) {
      return new ProcessorBadXml(server);
    }
    else if (processorName.equals(PROCESSOR_BASE_64)) {
      return new ProcessorBase64(server);
    }
    else if (processorName.equals(PROCESSOR_INCORRECT_IMPLEMENTATION)) {
      return new ProcessorIncorrectImplementation(server);
    }
    else if (processorName.equals(PROCESSOR_URL_ENCODED)) {
      return new ProcessorUrlEncoded(server);
    }
    else if (processorName.equals(PROCESSOR_ADDITIONAL_TAG)) {
      return new ProcessorAdditionalTag(server);
    }
    return new Processor(server);
  }

  public static void printExplanations(PrintWriter out) {
    Processor.printExplanation(out, PROCESSOR_DEFAULT);
    addLink(out, PROCESSOR_DEFAULT);
    ProcessorNoWhitespace.printExplanation(out, PROCESSOR_NO_WHITESPACE);
    addLink(out, PROCESSOR_NO_WHITESPACE);
    ProcessorMistakes.printExplanation(out, PROCESSOR_MISTAKES);
    addLink(out, PROCESSOR_MISTAKES);
    ProcessorBadXml.printExplanation(out, PROCESSOR_BAD_XML);
    addLink(out, PROCESSOR_BAD_XML);
    ProcessorIncorrectImplementation.printExplanation(out, PROCESSOR_INCORRECT_IMPLEMENTATION);
    addLink(out, PROCESSOR_INCORRECT_IMPLEMENTATION);
    ProcessorAdditionalTag.printExplanation(out, PROCESSOR_ADDITIONAL_TAG);
    addLink(out, PROCESSOR_ADDITIONAL_TAG);
    ProcessorBase64.printExplanation(out, PROCESSOR_BASE_64);
    addLink(out, PROCESSOR_BASE_64);
    ProcessorUrlEncoded.printExplanation(out, PROCESSOR_URL_ENCODED);
    addLink(out, PROCESSOR_URL_ENCODED);
  }

  public static void addLink(PrintWriter out, String processorName) {
    String link = "wsdl-demo/" + processorName + "?wsdl=true";
    out.println("<p><a href=\"" + link + "\">wsdl-demo/" + processorName + "</a></p>");
  }

}
