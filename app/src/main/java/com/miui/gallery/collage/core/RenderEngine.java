package com.miui.gallery.collage.core;

import android.content.Context;
import android.graphics.Bitmap;
import com.miui.gallery.collage.BitmapManager;

/* loaded from: classes.dex */
public abstract class RenderEngine {
    public final BitmapManager mBitmapManager;
    public final Context mContext;

    public abstract Bitmap render(RenderData renderData);

    public RenderEngine(Context context, BitmapManager bitmapManager) {
        this.mContext = context.getApplicationContext();
        this.mBitmapManager = bitmapManager;
    }
}
