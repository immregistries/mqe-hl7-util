package org.immregistries.dqa.vxu;

import java.util.Date;
import org.immregistries.dqa.core.util.DateUtility;

public class VaccinationVIS {

  private DateUtility datr = DateUtility.INSTANCE;

  private int visId = 0;
  private DqaVaccination vaccination = null;
  private int positionId = 0;
  private boolean skipped = false;
  private String document = "";// new
  // CodedEntity(CodesetType.VACCINATION_VIS_DOC_TYPE);
  private String cvx = "";// new
  // CodedEntity(CodesetType.VACCINATION_VIS_CVX_CODE);
  private String publishedDateString = "";
  private String presentedDateString = "";

  public String getCvxCode() {
    return cvx;
  }

  public void setCvxCode(String cvxCode) {
    cvx = cvxCode;
  }

  public int getVisId() {
    return visId;
  }

  public void setVisId(int visId) {
    this.visId = visId;
  }

  public DqaVaccination getVaccination() {
    return vaccination;
  }

  public void setVaccination(DqaVaccination vaccination) {
    this.vaccination = vaccination;
  }

  public int getPositionId() {
    return positionId;
  }

  public void setPositionId(int positionId) {
    this.positionId = positionId;
  }

  public boolean isSkipped() {
    return skipped;
  }

  public void setSkipped(boolean skipped) {
    this.skipped = skipped;
  }

  public String getDocumentCode() {
    return document;
  }

  public void setDocumentCode(String documentCode) {
    this.document = documentCode;
  }

  public Date getPublishedDate() {
    return datr.parseDate(publishedDateString);
  }

  public Date getPresentedDate() {
    return datr.parseDate(presentedDateString);
  }

  public String getDocument() {
    return document;
  }

  public String getPublishedDateString() {
    return publishedDateString;
  }

  public void setPublishedDateString(String publishedDateString) {
    this.publishedDateString = publishedDateString;
  }

  public String getPresentedDateString() {
    return presentedDateString;
  }

  public void setPresentedDateString(String presentedDateString) {
    this.presentedDateString = presentedDateString;
  }

  @Override
  public String toString() {
    return "VaccinationVIS [visId=" + visId
        + ", vaccination=" + vaccination + ", positionId=" + positionId
        + ", skipped=" + skipped + ", document=" + document + ", cvx="
        + cvx + ", publishedDateString=" + publishedDateString
        + ", presentedDateString=" + presentedDateString + "]";
  }

}
