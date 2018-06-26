package org.immregistries.mqe.vxu.parse;

import java.util.List;
import org.immregistries.mqe.hl7util.parser.HL7MessageMap;
import org.immregistries.mqe.hl7util.parser.MessageParserHL7;
import org.immregistries.mqe.vxu.MqeMessageHeader;
import org.immregistries.mqe.vxu.MqeMessageReceived;
import org.immregistries.mqe.vxu.MqeNextOfKin;
import org.immregistries.mqe.vxu.MqePatient;
import org.immregistries.mqe.vxu.MqeVaccination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ul> <li>This class will parse a VXU message and put it into objects that represent the business
 * concepts.
 *
 * <li>Declaration: This class will not get data from any external sources. Any data transformations
 * will happen in other classes, as augmentation after this class is invoked. This will give you
 * ONLY what's in the message, and will not look up anything, or add it it. it WILL interpret the
 * information that exists in the message so that the values are put into the appropriate places in
 * the object model. Keep in mind this strategic difference between transforming the data V.S.
 * Interpreting the data. <br /> <br />
 *
 * @author Josh Hull
 */
public enum HL7MessageParser {
  INSTANCE;

  protected static final Logger LOGGER = LoggerFactory.getLogger(HL7MessageParser.class);

  private MessageParserHL7 parser = new MessageParserHL7();

  private HL7MessageHeaderParser mshParser = HL7MessageHeaderParser.INSTANCE;
  private HL7PatientParser patientParser = HL7PatientParser.INSTANCE;
  private HL7NokParser nokParser = HL7NokParser.INSTANCE;
  private HL7VaccinationParser vaccineParser = HL7VaccinationParser.INSTANCE;

  /**
   * Extracts all the values from the message into objects that can be used to process the message.
   *
   * @param message VXU text
   * @return a set of objects representing the business concepts
   */
  public MqeMessageReceived extractMessageFromText(String message) {
    HL7MessageMap map = parser.getMessagePartMap(message);
    MqeMessageReceived mr = extractFromMessage(map);
    return mr;
  }

  protected MqeMessageReceived extractFromMessage(HL7MessageMap map) {
    MqeMessageReceived container = new MqeMessageReceived();

    // Message Header
    MqeMessageHeader header = mshParser.getMessageHeader(map);
    container.setMessageHeader(header);

    // Child from message
    MqePatient child = patientParser.getPatient(map);
    container.setPatient(child);

    // Shots from message
    List<MqeVaccination> shots = vaccineParser.getVaccinationList(map);
    container.setVaccinations(shots);

    // Resp Parties from message.
    List<MqeNextOfKin> respParties = nokParser.getNk1List(map);
    container.setNextOfKins(respParties);

    return container;
  }
}
