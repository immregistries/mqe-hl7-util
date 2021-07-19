package org.immregistries.mqe.hl7util.transform.procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import org.immregistries.mqe.hl7util.transform.TransformRequest;
import org.immregistries.mqe.hl7util.transform.Transformer;

public class RemoveVaccinationGroupsProcedure implements ProcedureInterface {

  public void setTransformer(Transformer transformer) {
    // not needed
  }

  // run procedure Remove_Vaccination_Groups where RXA-20 equals 'RE'
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    String token = "";
    while (!token.equalsIgnoreCase("where") && !tokenList.isEmpty()) {
      token = tokenList.removeFirst();
    }
    if (token.equalsIgnoreCase("where") && !tokenList.isEmpty()) {
      String hl7Ref = tokenList.removeFirst();
      if (!tokenList.isEmpty()) {
        token = tokenList.removeFirst();
        if (token.equalsIgnoreCase("equals") && !tokenList.isEmpty()) {
          String valueSearch = tokenList.removeFirst();
          BufferedReader inResult =
              new BufferedReader(new StringReader(transformRequest.getResultText()));
          String lineResult;
          String finalMessage = "";
          String vaccinationGroup = "";
          while ((lineResult = inResult.readLine()) != null) {
            lineResult = lineResult.trim();
            if (lineResult.startsWith("ORC|")) {
              if (!vaccinationGroup.equals("")) {
                String valueActual =
                    Transformer.getValueFromHL7(hl7Ref, vaccinationGroup, transformRequest);
                if (!valueSearch.equalsIgnoreCase(valueActual)) {
                  finalMessage += vaccinationGroup;
                }
              }
              vaccinationGroup = lineResult + transformRequest.getSegmentSeparator();
            } else if (!vaccinationGroup.equals("")) {
              vaccinationGroup += lineResult + transformRequest.getSegmentSeparator();
            } else {
              finalMessage += lineResult + transformRequest.getSegmentSeparator();
            }
          }
          if (!vaccinationGroup.equals("")) {
            String valueActual =
                Transformer.getValueFromHL7(hl7Ref, vaccinationGroup, transformRequest);
            if (!valueSearch.equalsIgnoreCase(valueActual)) {
              finalMessage += vaccinationGroup;
            }
          }
          transformRequest.setResultText(finalMessage);
        }
      }
    }

  }
}
