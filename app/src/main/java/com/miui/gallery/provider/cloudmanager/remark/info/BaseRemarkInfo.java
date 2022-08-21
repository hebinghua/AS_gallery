package com.miui.gallery.provider.cloudmanager.remark.info;

import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public abstract class BaseRemarkInfo implements IRemarkInfo {
    public long mCloudId;
    public String mContentValues;
    public int mOperationType = 0;
    public int mMethodType = 0;

    public String getMethod(int i) {
        switch (i) {
            case 1:
                return "move";
            case 2:
                return "copy";
            case 3:
                return "delete";
            case 4:
                return "rename";
            case 5:
                return "editDateTime";
            case 6:
                return "removeSecret";
            case 7:
                return "moveCloud";
            default:
                return "none";
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.remark.info.IRemarkInfo
    public long getKey() {
        return this.mCloudId;
    }

    @Override // com.miui.gallery.provider.cloudmanager.remark.info.IRemarkInfo
    public Entity getEntity() {
        MediaRemarkEntity mediaRemarkEntity = new MediaRemarkEntity();
        mediaRemarkEntity.setCloudId(this.mCloudId);
        mediaRemarkEntity.setMethod(this.mMethodType);
        mediaRemarkEntity.setOperationType(1);
        mediaRemarkEntity.setContentValues(this.mContentValues);
        DefaultLogger.d("galleryAction_Remark_remarkInfo", toString());
        return mediaRemarkEntity;
    }

    @Override // com.miui.gallery.provider.cloudmanager.remark.info.IRemarkInfo
    public void setCloudId(long j) {
        this.mCloudId = j;
    }

    @Override // com.miui.gallery.provider.cloudmanager.remark.info.IRemarkInfo
    public void setOperationType(int i) {
        this.mOperationType = i;
    }
}
