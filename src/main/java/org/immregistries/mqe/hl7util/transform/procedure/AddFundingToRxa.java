package org.immregistries.mqe.hl7util.transform.procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import org.immregistries.mqe.hl7util.transform.TransformRequest;
import org.immregistries.mqe.hl7util.transform.Transformer;

public class AddFundingToRxa implements ProcedureInterface {

  private static final String SOURCE_LOINC = "30963-3";
  private static final String ELIGIBILITY_LOINC = "64994-7";
  private static final String ELIGIBILITY_OBX =
      "OBX|%d|CE|64994-7^Vaccine funding program eligibility category^LN|%d|V01^Not VFC eligible^HL70064||||||F|||20150817|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|";
  private static final String SOURCE_OBX =
      "OBX|%d|CE|30963-3^Vaccine Funding Source^LN|%d|VXC50^Public^CDCPHINVS||||||F|";

  public static enum Type {
                           SOURCE,
                           ELIGIBILITY
  }

  public static enum VaccinationGroups {
                                        ALL,
                                        ADMINISTERED_ONLY
  }

  private VaccinationGroups vaccinationGroups;
  private String lookingForObx = "|";
  private String obxSegmentToInsert;

  public AddFundingToRxa(Type type, VaccinationGroups vaccinationGroups) {
    this.vaccinationGroups = vaccinationGroups;
    switch (type) {
      case ELIGIBILITY:
        lookingForObx += ELIGIBILITY_LOINC;
        obxSegmentToInsert = ELIGIBILITY_OBX;
        break;
      case SOURCE:
        lookingForObx += SOURCE_LOINC;
        obxSegmentToInsert = SOURCE_OBX;
        break;
    }
  }

  public void setTransformer(Transformer transformer) {
    // not needed
  }


  // run procedure ADD_FUNDING_SOURCE_TO_ALL_RXA
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    BufferedReader inResult =
        new BufferedReader(new StringReader(transformRequest.getResultText()));
    String lineResult;
    String finalMessage = "";
    boolean needToAddObx = false;

    int obxId = 1;
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      if (lineResult.length() > 3) {
        if (needToAddObx) {
          if (lineResult.startsWith("OBX|")) {
            if (lineResult.indexOf(lookingForObx) > 0) {
              needToAddObx = false;
            }
            try {
              String s = lineResult.substring(4);
              int pos = s.indexOf('|');
              if (pos > 0) {
                s = s.substring(0, pos);
                obxId = Integer.parseInt(s) + 1;
              }
            } catch (NumberFormatException nfe) {
              obxId++;
            }
          } else if (lineResult.startsWith("ORC|") || lineResult.startsWith("RXA|")) {
            finalMessage = addObx(transformRequest, finalMessage, obxId);
            needToAddObx = false;
          }
        } else {
          if (lineResult.startsWith("RXA|")) {
            switch (vaccinationGroups) {
              case ADMINISTERED_ONLY:
                int fieldCount = 9;
                String s = lineResult;
                int barPos;
                while (fieldCount > 0 && (barPos = s.indexOf('|')) >= 0) {
                  s = s.substring(barPos + 1);
                  fieldCount--;
                }
                needToAddObx = fieldCount == 0 && s.startsWith("00^");
                break;
              case ALL:
                needToAddObx = true;
                break;
            }
            obxId = 1;
          }
        }
        finalMessage += lineResult + transformRequest.getSegmentSeparator();
      }
    }
    if (needToAddObx) {
      finalMessage = addObx(transformRequest, finalMessage, obxId);
    }

    transformRequest.setResultText(finalMessage);
  }

  private String addObx(TransformRequest transformRequest, String finalMessage, int pos) {
    finalMessage +=
        String.format(obxSegmentToInsert, pos, pos) + transformRequest.getSegmentSeparator();
    return finalMessage;
  }
}
