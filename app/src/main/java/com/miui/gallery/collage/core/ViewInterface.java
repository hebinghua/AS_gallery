package com.miui.gallery.collage.core;

import android.content.Context;
import android.graphics.Bitmap;

/* loaded from: classes.dex */
public interface ViewInterface {
    Bitmap[] getBitmaps();

    Context getContext();

    void onReplaceBitmap(Bitmap bitmap);

    void onSaveFinish(String str, boolean z);

    void onSaving();
}
