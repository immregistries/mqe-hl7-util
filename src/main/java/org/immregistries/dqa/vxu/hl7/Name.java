/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.immregistries.dqa.vxu.hl7;


public class Name
{
  private String last = "";
  private String first = "";
  private String middle = "";
  private String suffix = "";
  private String prefix = "";
  private String type = "";//new CodedEntity(CodesetType.PERSON_NAME_TYPE);

  public String getLast()
  {
    return last;
  }

  public void setLast(String last)
  {
    this.last = last;
  }

  public String getFirst()
  {
    return first;
  }

  public void setFirst(String first)
  {
    this.first = first;
  }

  public String getMiddle()
  {
    return middle;
  }

  public void setMiddle(String middle)
  {
    this.middle = middle;
  }

  public String getSuffix()
  {
    return suffix;
  }

  public void setSuffix(String suffix)
  {
    this.suffix = suffix;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  public String getType()
  {
    return type;
  }

  public String getTypeCode()
  {
    return type;
  }

  public void setTypeCode(String typeCode)
  {
    this.type = typeCode;
  }

  public String getFullName()
  {
    StringBuilder sb = new StringBuilder();
    if (prefix != null && !prefix.equals(""))
    {
      sb.append(prefix);
      sb.append(" ");
    }
    if (first != null && !first.equals(""))
    {
      sb.append(first);
      sb.append(" ");
    }
    if (middle != null && !middle.equals(""))
    {
      sb.append(middle);
      sb.append(" ");
    }
    if (last != null && !last.equals(""))
    {
      sb.append(last);
      sb.append(" ");
    }
    if (suffix != null && !suffix.equals(""))
    {
      sb.append(suffix);
      sb.append(" ");
    }
    return sb.toString().trim();
  }

    @Override public String toString() {
        return "Name{" + "last='" + last + '\'' + ", first='" + first + '\'' + ", middle='" + middle + '\'' + ", suffix='" + suffix + '\'' + ", prefix='"
            + prefix + '\'' + ", type='" + type + '\'' + '}';
    }
}
