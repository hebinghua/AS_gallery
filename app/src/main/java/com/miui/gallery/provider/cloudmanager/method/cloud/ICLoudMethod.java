package com.miui.gallery.provider.cloudmanager.method.cloud;

import com.miui.gallery.provider.cloudmanager.method.IMethod;

/* loaded from: classes2.dex */
public interface ICLoudMethod extends IMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    default boolean isNeedDoneRemark() {
        return true;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    default boolean isNeedFileHandle() {
        return true;
    }
}
