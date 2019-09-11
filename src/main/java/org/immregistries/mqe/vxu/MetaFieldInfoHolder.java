package org.immregistries.mqe.vxu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.immregistries.mqe.hl7util.model.MetaFieldInfo;
import org.immregistries.mqe.util.validation.MqeValidatedObject;

public abstract class MetaFieldInfoHolder implements MetaFieldInfoData, MqeValidatedObject {
  private Map<VxuField, MetaFieldInfo> metaFieldInfoMap = new HashMap<>();
  private int positionId = 0;
  public abstract TargetType getTargetType();
  public final int getPositionId() {
    return this.positionId;
  }
  public final void setPositionId(int positionId) {
    this.positionId = positionId;
  }

  public Map<VxuField, MetaFieldInfo> getMetaFieldInfoMap() {
    return this.metaFieldInfoMap;
  }

  public MetaFieldInfo getMetaFieldInfo(VxuField vxuField) {
    return this.metaFieldInfoMap.get(vxuField);
  }

  public void setFields(List<MetaFieldInfo> metaFieldInfoList) {
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
