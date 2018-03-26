package org.immregistries.dqa.hl7util.parser.profile.intf;

import java.util.List;

public interface HL7Component {

  public abstract List<SubComponent> getSubComponent();

  /**
   * Gets the value of the datatype property.
   *
   * @return possible object is {@link String }
   */
  public abstract String getDatatype();

  /**
   * Gets the value of the name property.
   *
   * @return possible object is {@link String }
   */
  public abstract String getName();

}
