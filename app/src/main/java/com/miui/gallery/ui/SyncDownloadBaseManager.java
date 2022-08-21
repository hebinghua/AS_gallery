package com.miui.gallery.ui;

import android.content.Context;

/* loaded from: classes2.dex */
public abstract class SyncDownloadBaseManager {
    public Context mContext;
    public final int mKey;

    public SyncDownloadBaseManager(Context context, int i) {
        this.mContext = context;
        this.mKey = i;
    }

    public final int getKey() {
        return this.mKey;
    }

    public Context getContext() {
        return this.mContext;
    }
}
