package org.immregistries.dqa.hl7util.parser.profile.intf;

import java.util.List;

public interface HL7Segment {

  /**
   * Gets the value of the field property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the field property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getField().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link Field }
   *
   *
   */
  public abstract List<Field> getField();

  /**
   * Gets the value of the longName property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public abstract String getLongName();

  /**
   * Gets the value of the name property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public abstract String getName();

}
