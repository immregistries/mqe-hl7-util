package org.immregistries.dqa.vxu;

import java.io.Serializable;
import java.util.Date;

import org.immregistries.dqa.hl7util.model.MetaFieldInfo;

public class DqaMessageHeader extends MetaFieldInfoHolder implements Serializable {
  private static final long serialVersionUID = 1l;

  private int headerId = 0;
  private String ackTypeAccept = "";// new CodedEntity(CodesetType.ACKNOWLEDGEMENT_TYPE);
  private String ackTypeApplication = "";// new CodedEntity(CodesetType.ACKNOWLEDGEMENT_TYPE);
  private String characterSet = "";
  private String characterSetAlt = "";
  private String country = "";
  private String messageControl = "";
  private Date messageDate = null;
  private String messageDateString;
  private String messageProfile = "";
  private String messageStructure = "";
  private String messageTrigger = "";
  private String messageType = "";
  private String processingStatus = "";// new CodedEntity(CodesetType.MESSAGE_PROCESSING_ID);
  private String receivingApplication = "";
  private String receivingFacility = "";
  private String sendingApplication = "";
  private String sendingFacility = "";
  private String messageVersion = "";

  @Override
  public String toString() {
    return "DqaMessageHeader{" + "headerId=" + headerId + ", ackTypeAccept='" + ackTypeAccept + '\''
        + ", ackTypeApplication='" + ackTypeApplication + '\'' + ", characterSet='" + characterSet
        + '\'' + ", characterSetAlt='" + characterSetAlt + '\'' + ", country='" + country + '\''
        + ", messageControl='" + messageControl + '\'' + ", messageDate=" + messageDate
        + ", messageDateString='" + messageDateString + '\'' + ", messageProfile='" + messageProfile
        + '\'' + ", messageStructure='" + messageStructure + '\'' + ", messageTrigger='"
        + messageTrigger + '\'' + ", messageType='" + messageType + '\'' + ", processingStatus='"
        + processingStatus + '\'' + ", receivingApplication='" + receivingApplication + '\''
        + ", receivingFacility='" + receivingFacility + '\'' + ", sendingApplication='"
        + sendingApplication + '\'' + ", sendingFacility='" + sendingFacility + '\''
        + ", messageVersion='" + messageVersion + '\'' + '}';
  }

  public int getHeaderId() {
    return headerId;
  }

  public void setHeaderId(int headerId) {
    this.headerId = headerId;
  }

  public String getAckTypeAcceptCode() {
    return ackTypeAccept;
  }

  public String getAckTypeApplicationCode() {
    return ackTypeApplication;
  }

  public String getCharacterSet() {
    return characterSet;
  }

  public String getCharacterSetAlt() {
    return characterSetAlt;
  }

  public String getCharacterSetCode() {
    return characterSet;
  }

  public String getCharacterSetAltCode() {
    return characterSetAlt;
  }

  public String getCountry() {
    return country;
  }

  public String getCountryCode() {
    return country;
  }

  public String getMessageControl() {
    return messageControl;
  }

  public Date getMessageDate() {
    return messageDate;
  }

  public String getMessageProfile() {
    return messageProfile;
  }

  public String getMessageStructure() {
    return messageStructure;
  }

  public String getMessageTrigger() {
    return messageTrigger;
  }

  public String getMessageType() {
    return messageType;
  }

  public String getProcessingStatus() {
    return processingStatus;
  }

  public String getProcessingStatusCode() {
    return processingStatus;
  }

  public void setProcessingStatusCode(String code) {
    processingStatus = code;
  }

  public String getReceivingApplication() {
    return receivingApplication;
  }

  public String getReceivingFacility() {
    return receivingFacility;
  }

  public String getSendingApplication() {
    return sendingApplication;
  }

  public String getSendingFacility() {
    return sendingFacility;
  }

  public String getMessageVersion() {
    return messageVersion;
  }

  public void setAckTypeAcceptCode(String ackTypeAccept) {
    this.ackTypeAccept = ackTypeAccept;
  }

  public void setAckTypeApplicationCode(String ackTypeApplication) {
    this.ackTypeApplication = ackTypeApplication;
  }

  public void setCharacterSetCode(String characterSet) {
    this.characterSet = characterSet;
  }

  public void setCharacterSetAltCode(String characterSetAlt) {
    this.characterSetAlt = characterSetAlt;
  }

  public void setCountryCode(String countryCode) {
    this.country = countryCode;
  }

  public void setMessageControl(String messageControl) {
    this.messageControl = messageControl;
  }

  public void setMessageDate(Date messageDate) {
    this.messageDate = messageDate;
  }

  public void setMessageProfile(String messageProfileId) {
    this.messageProfile = messageProfileId;
  }

  public void setMessageStructure(String messageStructure) {
    this.messageStructure = messageStructure;
  }

  public void setMessageTrigger(String messageTrigger) {
    this.messageTrigger = messageTrigger;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public void setProcessingIdCode(String processingId) {
    this.processingStatus = processingId;
  }

  public void setReceivingApplication(String receivingApplication) {
    this.receivingApplication = receivingApplication;
  }

  public void setReceivingFacility(String receivingFacility) {
    this.receivingFacility = receivingFacility;
  }

  public void setSendingApplication(String sendingApplication) {
    this.sendingApplication = sendingApplication;
  }

  public void setSendingFacility(String sendingFacility) {
    this.sendingFacility = sendingFacility;
  }

  public void setMessageVersion(String versionId) {
    this.messageVersion = versionId;
  }

  public void setMessageDateString(String sentDate) {
    this.messageDateString = sentDate;

  }

  public String getMessageDateString() {
    return this.messageDateString;
  }

  @Override
  protected void readRegisteredMetaFieldInfo(MetaFieldInfo metaFieldInfo) {
    // TODO Auto-generated method stub

  }

}
