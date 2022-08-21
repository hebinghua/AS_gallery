package com.qiniu.pili.droid.shortvideo;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class PLVideoFrame {
    public byte[] mData;
    public a mDataFormat;
    public int mHeight;
    public boolean mIsKeyFrame;
    public int mRotation;
    public long mTimestampMs;
    public int mWidth;

    /* loaded from: classes3.dex */
    public enum a {
        ARGB_8888,
        RGB_565
    }

    public void setTimestampMs(long j) {
        this.mTimestampMs = j;
    }

    public void setData(byte[] bArr) {
        this.mData = bArr;
    }

    public void setDataFormat(a aVar) {
        this.mDataFormat = aVar;
    }

    public void setIsKeyFrame(boolean z) {
        this.mIsKeyFrame = z;
    }

    public void setWidth(int i) {
        this.mWidth = i;
    }

    public void setHeight(int i) {
        this.mHeight = i;
    }

    public void setRotation(int i) {
        this.mRotation = i;
    }

    public Bitmap toBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, this.mDataFormat == a.ARGB_8888 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(this.mData));
        return createBitmap;
    }
}
