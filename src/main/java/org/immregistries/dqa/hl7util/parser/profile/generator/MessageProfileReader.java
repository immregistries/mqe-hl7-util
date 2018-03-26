package org.immregistries.dqa.hl7util.parser.profile.generator;

public interface MessageProfileReader {

  /**
   * These escape values are defined in the HL7 2.5.1 definition chapter 2 page 2-18. <li>\H\ start
   * highlighting <li>\N\ normal text (end highlighting) \F\ field separator <li>\S\ component
   * separator <li>\T\ subcomponent separator <li>\R\ repetition separator <li>\E\ escape character
   * <li>\Xdddd...\ hexadecimal data <li>\Zdddd...\ locally defined escape sequence
   **/

  public abstract String getDatatype(String dataLocation);

  public abstract String getFieldDescription(String dataLocation);

  public abstract FieldComplexity getDatatypeComplexity(String datatype);

  public abstract FieldComplexity getComplexity(String dataLocation);

}
