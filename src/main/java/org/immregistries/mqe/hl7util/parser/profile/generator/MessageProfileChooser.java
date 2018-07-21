package org.immregistries.mqe.hl7util.parser.profile.generator;


/**
 * Use this to get an instance of a message profile. This returns the generated version that you
 * decide to use.
 *
 * @author Josh
 */
public class MessageProfileChooser {

  public MessageProfile getGeneratedMessageProfile() {
    return new GeneratedHL7Profile20160426();
  }
}
