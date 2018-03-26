/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 *
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.
 */

package org.immregistries.dqa.vxu;

public class DqaPatientAddress extends DqaAddress {

    private long addressId = 0;

    public DqaPatientAddress() {
        //default constructor.
    }

    public DqaPatientAddress(DqaAddress a) {
        if (a != null) {
            this.setStreet(a.getStreet());
            this.setStreet2(a.getStreet2());
            this.setCity(a.getCity());
            this.setStateCode(a.getStateCode());
            this.setZip(a.getZip());
            this.setCountryCode(a.getCountryCode());
            this.setTypeCode(a.getTypeCode());
            this.setCountyParishCode(a.getCountyParishCode());
        }
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

//    @Override protected void setFieldFromMetaFieldInfo(MetaFieldInfo metaFieldInfo) {
//        String value = metaFieldInfo.getValue();
//        int pos = metaFieldInfo.getHl7Location().getFieldRepetition();
//        switch (metaFieldInfo.getVxuField()) {
//        case PATIENT_ADDRESS:
//            break;
//        case PATIENT_ADDRESS_CITY:
//            this.setCity(value);
//            break;
//        case PATIENT_ADDRESS_COUNTRY:
//            this.setCountryCode(value);
//            break;
//        case PATIENT_ADDRESS_COUNTY:
//            this.setCountyParishCode(value);
//            break;
//        case PATIENT_ADDRESS_STATE:
//            this.setStateCode(value);
//            break;
//        case PATIENT_ADDRESS_STREET:
//            this.setStreet(value);
//            break;
//        case PATIENT_ADDRESS_STREET2:
//            this.setStreet2(value);
//            break;
//        case PATIENT_ADDRESS_TYPE:
//            this.setTypeCode(value);
//            break;
//        case PATIENT_ADDRESS_ZIP:
//            this.setZip(value);
//            break;
//        }
//    }
}
