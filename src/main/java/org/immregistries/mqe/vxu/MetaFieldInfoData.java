package org.immregistries.mqe.vxu;

import java.util.List;
import org.immregistries.mqe.hl7util.model.MetaFieldInfo;

public interface MetaFieldInfoData {

  public MetaFieldInfo getMetaFieldInfo(VxuField vxuField);
  public List<DetectionInfo> getDetectionList();
}
