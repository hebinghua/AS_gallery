package com.miui.gallery.cloud.base;

/* loaded from: classes.dex */
public interface SyncTask<T> {
    String getIdentifier();

    GallerySyncResult<T> run() throws Exception;
}
