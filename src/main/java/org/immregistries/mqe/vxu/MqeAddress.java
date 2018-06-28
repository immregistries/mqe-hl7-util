package org.immregistries.mqe.vxu;

public class MqeAddress {

  @Override
  public String toString() {
    return "MqeAddress{" +
        "street='" + street + '\'' +
        ", street2='" + street2 + '\'' +
        ", city='" + city + '\'' +
        ", stateCode='" + stateCode + '\'' +
        ", zip='" + zip + '\'' +
        ", country='" + country + '\'' +
        ", countyParish='" + countyParish + '\'' +
        ", type='" + type + '\'' +
        ", cleansingResultCode='" + cleansingResultCode + '\'' +
        ", lattitude=" + lattitude +
        ", longitude=" + longitude +
        ", clean=" + clean +
        ", cleansingAttempted=" + cleansingAttempted +
        '}';
  }

  private String street = "";
  private String street2 = "";
  private String city = "";
  private String stateCode = "";
  private String zip = "";
  private String country = "";
  private String countyParish = "";
  private String type = "";// new CodedEntity(CodesetType.ADDRESS_TYPE);
  private String cleansingResultCode;
  private double lattitude;
  private double longitude;
  private boolean clean = false;
  private boolean cleansingAttempted;

  public MqeAddress() {
  }

  public MqeAddress(MqeAddress d) {
    this.street = d.street;
    this.street2 = d.street2;
    this.city = d.city;
    this.stateCode = d.stateCode;
    this.zip = d.zip;
    this.country = d.country;
    this.countyParish = d.countyParish;
    this.type = d.type;
    this.cleansingResultCode = d.cleansingResultCode;
    this.clean = d.clean;
    this.lattitude = d.lattitude;
    this.longitude = d.longitude;
  }

  public String getCleansingResultCode() {
    return cleansingResultCode;
  }

  public void setCleansingResultCode(String cleansingResultCode) {
    this.cleansingResultCode = cleansingResultCode;
  }

  public boolean isCleansingAttempted() {
    return cleansingAttempted;
  }

  public void setCleansingAttempted(boolean cleansingAttempted) {
    this.cleansingAttempted = cleansingAttempted;
  }

  public double getLattitude() {
    return lattitude;
  }

  public void setLatitude(double lattitude) {
    this.lattitude = lattitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public boolean isClean() {
    return clean;
  }

  public void setClean(boolean clean) {
    this.clean = clean;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getStreet2() {
    return street2;
  }

  public void setStreet2(String street2) {
    this.street2 = street2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStateCode() {
    return stateCode;
  }

  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCountryCode() {
    return country;
  }

  public void setCountryCode(String countryCode) {
    this.country = countryCode;
  }

  public void setCountyParishCode(String countyParishCode) {
    this.countyParish = countyParishCode;
  }

  public String getCountyParishCode() {
    return countyParish;
  }

  public String getType() {
    return type;
  }

  public String getTypeCode() {
    return type;
  }

  public void setTypeCode(String typeCode) {
    this.type = typeCode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    // result = prime * result + ((country == null) ? 0 :
    // country.hashCode());
    // result = prime * result + ((getCountyParishCode() == null) ? 0 :
    // getCountyParishCode().hashCode());
    result = prime * result
        + ((getStateCode() == null) ? 0 : getStateCode().hashCode());
    result = prime * result + ((street == null) ? 0 : street.hashCode());
    result = prime * result + ((street2 == null) ? 0 : street2.hashCode());
    // result = prime * result + ((zip == null) ? 0 : zip.hashCode());
    return result;
  }

  public boolean isEmpty() {
    return this.equals(new MqeAddress());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    // we only care about city/state/street/street2, which are shared between Address and its subclasses (e.g. PatientAddress), so this should be OK
    if (getClass() != obj.getClass() && !getClass().isAssignableFrom(obj.getClass())) {
      return false;
    }
    MqeAddress other = (MqeAddress) obj;

    // City/State/Street/Street2
    if (city == null) {
      if (other.city != null) {
        return false;
      }
    } else if (!city.equals(other.city)) {
      return false;
    }
    if (getStateCode() == null) {
      if (other.getStateCode() != null) {
        return false;
      }
    } else if (!getStateCode().equals(other.getStateCode())) {
      return false;
    }
    if (street == null) {
      if (other.street != null) {
        return false;
      }
    } else if (!street.equals(other.street)) {
      return false;
    }
    if (street2 == null) {
      if (other.street2 != null) {
        return false;
      }
    } else if (!street2.equals(other.street2)) {
      return false;
    }

    return true;
  }
}
