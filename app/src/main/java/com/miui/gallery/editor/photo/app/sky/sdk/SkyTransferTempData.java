package com.miui.gallery.editor.photo.app.sky.sdk;

import android.graphics.Bitmap;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes2.dex */
public class SkyTransferTempData {
    public byte[] mMaskData;
    public int mSegHeight;
    public int mSegWidth;
    public int mSkyMode = 2;
    public CountDownLatch mCountDownLatch = new CountDownLatch(1);

    public SkyTransferTempData(Bitmap bitmap) {
        this.mSegWidth = bitmap.getWidth();
        int height = bitmap.getHeight();
        this.mSegHeight = height;
        this.mMaskData = new byte[this.mSegWidth * height];
    }
}
