package org.immregistries.dqa.vxu;

import java.util.List;
import org.immregistries.dqa.hl7util.model.MetaFieldInfo;

public interface MetaFieldInfoData {

  public MetaFieldInfo getMetaFieldInfo(VxuField vxuField);
  public List<DetectionInfo> getDetectionList();
}
