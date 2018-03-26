package org.immregistries.dqa.vxu;

public class DqaAddress {
    @Override
    public String toString() {
        return "Address {street=" + street + ", street2=" + street2 + ", city="
                + city + ", state=" + state + ", zip=" + zip + ", country="
                + country + ", countyParish=" + countyParish + ", type=" + type
                + "}";
    }

    private String street = "";
    private String street2 = "";
    private String city = "";
    private String state = "";
    private String zip = "";
    private String country = "";
    private String countyParish = "";
    private String type = "";// new CodedEntity(CodesetType.ADDRESS_TYPE);

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

    public String getState() {
        return state;
    }

    public String getStateCode() {
        return state;
    }

    public void setStateCode(String stateCode) {
        this.state = stateCode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return country;
    }

    public void setCountryCode(String countryCode) {
        this.country = countryCode;
    }

    public String getCountyParish() {
        return countyParish;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        // we only care about city/state/street/street2, which are shared between Address and its subclasses (e.g. PatientAddress), so this should be OK
        if (getClass() != obj.getClass() && !getClass().isAssignableFrom(obj.getClass()))
            return false;
        DqaAddress other = (DqaAddress) obj;

        // City/State/Street/Street2
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (getStateCode() == null) {
            if (other.getStateCode() != null)
                return false;
        } else if (!getStateCode().equals(other.getStateCode()))
            return false;
        if (street == null) {
            if (other.street != null)
                return false;
        } else if (!street.equals(other.street))
            return false;
        if (street2 == null) {
            if (other.street2 != null)
                return false;
        } else if (!street2.equals(other.street2))
            return false;

        return true;
    }
}
