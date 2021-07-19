package org.immregistries.mqe.hl7util.transform.procedure;

import org.immregistries.mqe.hl7util.transform.Transformer;

public class ProcedureFactory {
  public static final String REMOVE_VACCINATION_GROUPS = "REMOVE_VACCINATION_GROUPS";
  public static final String ADD_FUNDING_ELGIBILITY_TO_ALL_RXA =
      "ADD_FUNDING_ELGIBILITY_TO_ALL_RXA";

  public static final String ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA =
      "ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA";
  public static final String ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA =
      "ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA";
  public static final String ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA =
      "ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA";
  public static final String ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA =
      "ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA";

  public static final String ANONYMIZE_AND_UPDATE_RECORD = "ANONYMIZE_AND_UPDATE_RECORD";

  public static ProcedureInterface getProcedure(String procedureName, Transformer transformer) {
    ProcedureInterface procedureInterface = null;
    if (procedureName.equalsIgnoreCase(REMOVE_VACCINATION_GROUPS)) {
      procedureInterface = new RemoveVaccinationGroupsProcedure();
    } else if (procedureName.equalsIgnoreCase(ADD_FUNDING_ELGIBILITY_TO_ALL_RXA)) {
      procedureInterface = new AddFundingEligibilityToAllRxa();
    } else if (procedureName.equalsIgnoreCase(ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA)) {
      procedureInterface =
          new AddFundingToRxa(AddFundingToRxa.Type.ELIGIBILITY, AddFundingToRxa.VaccinationGroups.ALL);
    } else if (procedureName.equalsIgnoreCase(ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA)) {
      procedureInterface =
          new AddFundingToRxa(AddFundingToRxa.Type.SOURCE, AddFundingToRxa.VaccinationGroups.ALL);
    } else if (procedureName.equalsIgnoreCase(ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA)) {
      procedureInterface = new AddFundingToRxa(AddFundingToRxa.Type.SOURCE,
          AddFundingToRxa.VaccinationGroups.ADMINISTERED_ONLY);
    } else if (procedureName
        .equalsIgnoreCase(ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA)) {
      procedureInterface = new AddFundingToRxa(AddFundingToRxa.Type.ELIGIBILITY,
          AddFundingToRxa.VaccinationGroups.ADMINISTERED_ONLY);
    } else if (procedureName.equalsIgnoreCase(ANONYMIZE_AND_UPDATE_RECORD)) {
      procedureInterface = new AnonymizeAndUpdateRecord();
    }
    if (procedureInterface != null) {
      procedureInterface.setTransformer(transformer);
    }
    return procedureInterface;
  }
}
