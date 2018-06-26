//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.06 at 03:25:36 PM EDT 
//


package org.immregistries.mqe.hl7util.parser.profile.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.immregistries.mqe.hl7util.parser.profile.intf.Segment;


/**
 * <p>Java class for staticDef complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="staticDef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MetaData" type="{}metaData"/>
 *         &lt;element name="Segment" type="{}segment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SegGroup" type="{}segmentGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="EventDesc" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="EventType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="MsgStructID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="MsgType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="OrderControl" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Role" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "staticDef", propOrder = {
    "metaData",
    "segment",
    "segGroup"
})
public class StaticDef {

  @XmlElement(name = "MetaData", required = true)
  protected MetaData metaData;
  @XmlElement(name = "Segment")
  protected List<Segment> segment;
  @XmlElement(name = "SegGroup")
  protected List<SegmentGroup> segGroup;
  @XmlAttribute(name = "EventDesc")
  protected String eventDesc;
  @XmlAttribute(name = "EventType")
  protected String eventType;
  @XmlAttribute(name = "MsgStructID")
  protected String msgStructID;
  @XmlAttribute(name = "MsgType")
  protected String msgType;
  @XmlAttribute(name = "OrderControl")
  protected String orderControl;
  @XmlAttribute(name = "Role")
  protected String role;

  /**
   * Gets the value of the metaData property.
   *
   * @return possible object is {@link MetaData }
   */
  public MetaData getMetaData() {
    return metaData;
  }

  /**
   * Sets the value of the metaData property.
   *
   * @param value allowed object is {@link MetaData }
   */
  public void setMetaData(MetaData value) {
    this.metaData = value;
  }

  /**
   * Gets the value of the segment property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the segment property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getSegment().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link Segment }
   */
  public List<Segment> getSegment() {
    if (segment == null) {
      segment = new ArrayList<Segment>();
    }
    return this.segment;
  }

  /**
   * Gets the value of the segGroup property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the segGroup property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getSegGroup().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link SegmentGroup }
   */
  public List<SegmentGroup> getSegGroup() {
    if (segGroup == null) {
      segGroup = new ArrayList<SegmentGroup>();
    }
    return this.segGroup;
  }

  /**
   * Gets the value of the eventDesc property.
   *
   * @return possible object is {@link String }
   */
  public String getEventDesc() {
    return eventDesc;
  }

  /**
   * Sets the value of the eventDesc property.
   *
   * @param value allowed object is {@link String }
   */
  public void setEventDesc(String value) {
    this.eventDesc = value;
  }

  /**
   * Gets the value of the eventType property.
   *
   * @return possible object is {@link String }
   */
  public String getEventType() {
    return eventType;
  }

  /**
   * Sets the value of the eventType property.
   *
   * @param value allowed object is {@link String }
   */
  public void setEventType(String value) {
    this.eventType = value;
  }

  /**
   * Gets the value of the msgStructID property.
   *
   * @return possible object is {@link String }
   */
  public String getMsgStructID() {
    return msgStructID;
  }

  /**
   * Sets the value of the msgStructID property.
   *
   * @param value allowed object is {@link String }
   */
  public void setMsgStructID(String value) {
    this.msgStructID = value;
  }

  /**
   * Gets the value of the msgType property.
   *
   * @return possible object is {@link String }
   */
  public String getMsgType() {
    return msgType;
  }

  /**
   * Sets the value of the msgType property.
   *
   * @param value allowed object is {@link String }
   */
  public void setMsgType(String value) {
    this.msgType = value;
  }

  /**
   * Gets the value of the orderControl property.
   *
   * @return possible object is {@link String }
   */
  public String getOrderControl() {
    return orderControl;
  }

  /**
   * Sets the value of the orderControl property.
   *
   * @param value allowed object is {@link String }
   */
  public void setOrderControl(String value) {
    this.orderControl = value;
  }

  /**
   * Gets the value of the role property.
   *
   * @return possible object is {@link String }
   */
  public String getRole() {
    return role;
  }

  /**
   * Sets the value of the role property.
   *
   * @param value allowed object is {@link String }
   */
  public void setRole(String value) {
    this.role = value;
  }

}