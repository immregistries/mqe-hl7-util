package org.immregistries.mqe.vxu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MqeMessageReceived {

  /**
   * This is metadata about this message. There may be more metadata to add to this.
   */
  private Date receivedDate = new Date();

  /*
   * below here are all objects taken from the message itself.
   */
  private MqeMessageHeader messageHeader = new MqeMessageHeader();
  private MqePatient patient = new MqePatient();
  private List<MqeNextOfKin> nextOfKins = new ArrayList<MqeNextOfKin>();
  private List<MqeVaccination> vaccinations = new ArrayList<MqeVaccination>();
  private HashMap<String, String> detectionsOverride = new HashMap<String, String>();

  @Override
  public String toString() {
    return "MqeMessageReceived{" + "receivedDate=" + receivedDate + ", messageHeader="
        + messageHeader + ", patient=" + patient + ", nextOfKins="
        + nextOfKins + ", vaccinations=" + vaccinations + '}';
  }

  public List<MqeNextOfKin> getNextOfKins() {
    return nextOfKins;
  }

  public MqePatient getPatient() {
    return patient;
  }

  public List<MqeVaccination> getVaccinations() {
    return vaccinations;
  }

  public void setNextOfKins(List<MqeNextOfKin> nextOfKins) {
    this.nextOfKins = nextOfKins;
  }

  public void setPatient(MqePatient patient) {
    this.patient = patient;
  }

  public void setVaccinations(List<MqeVaccination> vaccinations) {
    this.vaccinations = vaccinations;
  }

  public Date getReceivedDate() {
    return receivedDate;
  }

  public void setReceivedDate(Date receivedDate) {
    this.receivedDate = receivedDate;
  }

  public MqeMessageHeader getMessageHeader() {
    return messageHeader;
  }

  public void setMessageHeader(MqeMessageHeader messageHeader) {
    this.messageHeader = messageHeader;
  }

public HashMap<String, String> getDetectionsOverride() {
	return detectionsOverride;
}

public void setDetectionsOverride(HashMap<String, String> detectionsOverride) {
	this.detectionsOverride = detectionsOverride;
}

}
