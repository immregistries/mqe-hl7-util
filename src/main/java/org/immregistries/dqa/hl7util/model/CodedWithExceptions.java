package org.immregistries.dqa.hl7util.model;

public class CodedWithExceptions
{
  private String identifier = "";
  private String text = "";
  private String nameOfCodingSystem = "";
  private String alternateIdentifier = "";
  private String alternateText = "";
  private String nameOfAlternateCodingSystem = "";

  public CodedWithExceptions() {
    // default
  }

  public boolean hasIdentifier()
  {
    return identifier != null && !identifier.equals("");
  }

  public boolean hasAlternateIdentifier()
  {
    return alternateIdentifier != null && !alternateIdentifier.equals("");
  }

  public String getIdentifier()
  {
    return identifier;
  }

  public void setIdentifier(String identifier)
  {
    this.identifier = identifier;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public String getNameOfCodingSystem()
  {
    return nameOfCodingSystem;
  }

  public void setNameOfCodingSystem(String nameOfCodingSystem)
  {
    this.nameOfCodingSystem = nameOfCodingSystem;
  }

  public String getAlternateIdentifier()
  {
    return alternateIdentifier;
  }

  public void setAlternateIdentifier(String alternateIdentifier)
  {
    this.alternateIdentifier = alternateIdentifier;
  }

  public String getAlternateText()
  {
    return alternateText;
  }

  public void setAlternateText(String alternateText)
  {
    this.alternateText = alternateText;
  }

  public String getNameOfAlternateCodingSystem()
  {
    return nameOfAlternateCodingSystem;
  }

  public void setNameOfAlternateCodingSystem(String nameOfAlternatedingSystem)
  {
    this.nameOfAlternateCodingSystem = nameOfAlternatedingSystem;
  }

  public CodedWithExceptions(String nameOfCodingSystem) {
    this.nameOfCodingSystem = nameOfCodingSystem;
  }

}
