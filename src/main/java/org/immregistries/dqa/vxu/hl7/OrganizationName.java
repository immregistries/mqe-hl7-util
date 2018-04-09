/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 *
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.
 */
package org.immregistries.dqa.vxu.hl7;


public class OrganizationName {

  private String name = "";
  private String id;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdNumber() {
    return id;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String in) {
    this.id = in;
  }
}
