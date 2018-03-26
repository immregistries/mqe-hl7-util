package org.immregistries.dqa.hl7util.parser.model;

public class HL7MessagePart {

  private int segmentIdx = -1;

  private int fieldRepetitionNbr = -1;

  private String valueTx;

  private String locationCd;

  public HL7MessagePart() {

  }

  public HL7MessagePart(int segIdx, String segName) {
    this.segmentIdx = segIdx;
    this.locationCd = segName;
//		if (locationCd.length() > 20) System.out.println(locationCd);
  }

  public HL7MessagePart(int segIdx, String segName, int fieldNum) {
    this(segIdx, segName);
    this.locationCd = locationCd + "-" + fieldNum;
//		if (locationCd.length() > 20) System.out.println(locationCd);
  }

  /**
   * @return the segmentIndex
   */
  public int getSegmentIndex() {
    return segmentIdx;
  }

  /**
   * @param segmentIndex the segmentIndex to set
   */
  public void setSegmentIndex(int segmentIndex) {
    this.segmentIdx = segmentIndex;
  }

  /**
   * @return the fieldRepetition
   */
  public int getFieldRepetition() {
    return fieldRepetitionNbr;
  }

  /**
   * @param fieldRepetition the fieldRepetition to set
   */
  public void setFieldRepetition(int fieldRepetition) {
    this.fieldRepetitionNbr = fieldRepetition;
  }

  public String getLocationCd() {
    return this.locationCd;
  }

  public void setLocationCd(String location) {
    this.locationCd = location;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "HL7MessageEAV [segmentIndex=" + segmentIdx
        + ", fieldRepetition=" + fieldRepetitionNbr + ", value=" + valueTx
        + ", locationCd=" + locationCd + "]";
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fieldRepetitionNbr;
    result = prime * result
        + ((locationCd == null) ? 0 : locationCd.hashCode());
    result = prime * result + segmentIdx;
    result = prime * result + ((valueTx == null) ? 0 : valueTx.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    HL7MessagePart other = (HL7MessagePart) obj;
    if (fieldRepetitionNbr != other.fieldRepetitionNbr) {
      return false;
    }
    if (locationCd == null) {
      if (other.locationCd != null) {
        return false;
      }
    } else if (!locationCd.equals(other.locationCd)) {
      return false;
    }
    if (segmentIdx != other.segmentIdx) {
      return false;
    }
    if (valueTx == null) {
      if (other.valueTx != null) {
        return false;
      }
    } else if (!valueTx.equals(other.valueTx)) {
      return false;
    }
    return true;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return valueTx;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.valueTx = value;
  }

}
