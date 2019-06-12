package org.immregistries.mqe.util.validation;

import java.util.ArrayList;
import java.util.List;

public interface MqeValidatedObject {

    List<MqeValidationReport> report = new ArrayList<>();

    default void addValidationReport(MqeValidationReport vr) {
        this.report.add(vr);
    };
    default List<MqeValidationReport> getValidationReports() {
      return this.report;
    };

}
