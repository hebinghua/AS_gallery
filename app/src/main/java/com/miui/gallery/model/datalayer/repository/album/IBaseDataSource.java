package com.miui.gallery.model.datalayer.repository.album;

/* loaded from: classes2.dex */
public interface IBaseDataSource {
    int getSourceType();

    default <T> void onFinish(int i, T t) {
    }
}
