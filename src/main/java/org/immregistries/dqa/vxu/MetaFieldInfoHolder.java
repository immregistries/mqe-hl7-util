package org.immregistries.dqa.vxu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immregistries.dqa.hl7util.model.MetaFieldInfo;

public abstract class MetaFieldInfoHolder {
  private Map<VxuField, MetaFieldInfo> metaFieldInfoMap = new HashMap<>();
  private int positionId = 0;

  public int getPositionId() {
    return this.positionId;
  }

  public void setPositionId(int positionId) {
    this.positionId = positionId;
  }

  public Map<VxuField, MetaFieldInfo> getMetaFieldInfoMap() {
    return this.metaFieldInfoMap;
  }

  public MetaFieldInfo getMetaFieldInfo(VxuField vxuField) {
    return this.metaFieldInfoMap.get(vxuField);
  }

  public void setField(List<MetaFieldInfo> metaFieldInfoList) {
    for (MetaFieldInfo meta : metaFieldInfoList) {
      this.setField(meta);
    }
  }

  public void setField(MetaFieldInfo metaFieldInfo) {
    if (metaFieldInfo != null) {
      if (metaFieldInfo.getVxuField() == null) {
        throw new IllegalArgumentException("Meta Field Info must have VXU field");
      }
      this.metaFieldInfoMap.put(metaFieldInfo.getVxuField(), metaFieldInfo);
      this.setFieldFromMetaFieldInfo(metaFieldInfo);
    }
  }

  protected abstract void setFieldFromMetaFieldInfo(MetaFieldInfo metaFieldInfo);

}
