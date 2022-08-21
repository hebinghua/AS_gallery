package com.miui.gallery.editor.blocksdk;

import android.graphics.Bitmap;

/* loaded from: classes.dex */
public class BlockSdkUtils {
    public static native boolean bindBitmap(Bitmap bitmap);

    public static native void readPixelsAndMerge(int i, int i2, int i3, int i4, int i5, int i6);

    public static native boolean unbindBitmap(Bitmap bitmap);

    public static native void updateTextureWidthStride(int i, int i2, int i3, int i4, int i5);

    static {
        System.loadLibrary("gallery_block_sdk");
    }
}
