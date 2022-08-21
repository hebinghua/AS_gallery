package com.miui.gallery.provider.cloudmanager.remark.info;

import com.miui.gallery.dao.base.Entity;

/* loaded from: classes2.dex */
public interface IRemarkInfo extends ICheckable {
    Entity getEntity();

    long getKey();

    void setCloudId(long j);

    void setOperationType(int i);
}
