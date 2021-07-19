package org.immregistries.mqe.hl7util.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import org.immregistries.mqe.hl7util.transform.TransformRequest;
import org.immregistries.mqe.hl7util.transform.Transformer;

public interface ProcedureInterface {
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException;

  public void setTransformer(Transformer transformer);
}
