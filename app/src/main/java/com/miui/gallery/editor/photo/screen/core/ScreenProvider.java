package com.miui.gallery.editor.photo.screen.core;

import android.content.Context;

/* loaded from: classes2.dex */
public abstract class ScreenProvider {
    public boolean mIsInitialized;

    public abstract void onActivityCreate(Context context);

    public abstract void onActivityDestroy();

    public boolean isInitialized() {
        return this.mIsInitialized;
    }
}
