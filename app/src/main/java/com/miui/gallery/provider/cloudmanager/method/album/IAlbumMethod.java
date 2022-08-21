package com.miui.gallery.provider.cloudmanager.method.album;

import com.miui.gallery.provider.cloudmanager.method.IMethod;

/* loaded from: classes2.dex */
public interface IAlbumMethod extends IMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    default boolean isNeedDoneRemark() {
        return false;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    default boolean isNeedFileHandle() {
        return false;
    }
}
