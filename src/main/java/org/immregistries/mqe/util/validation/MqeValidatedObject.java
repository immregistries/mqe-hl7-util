package org.immregistries.mqe.util.validation;

import java.util.ArrayList;
import java.util.List;
import org.immregistries.mqe.vxu.MetaFieldInfoHolder;

public abstract class MqeValidatedObject extends MetaFieldInfoHolder {
    List<MqeValidationReport> report = new ArrayList<>();

    public void addValidationReport(MqeValidationReport vr) {
        this.report.add(vr);
    };

    public List<MqeValidationReport> getValidationReports() {
      return this.report;
    }
}
