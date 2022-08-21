package com.xiaomi.skyprocess;

import android.util.Log;

/* loaded from: classes3.dex */
public class WrapSegmentEngine {
    private static String TAG = "WrapSegmentEngine";
    private long mPlayer;

    private static native long ConstructWrapSegmentEngineJni();

    private static native void DestructWrapSegmentEngineJni();

    private static native boolean getExchangeResultJni();

    private static native boolean getSegmentResultJni();

    private static native void initSegmentJni();

    private static native void releaseSegmentJni();

    private static native void setExchangeResultJni(boolean z);

    private static native void setGLSurfaceAvalibaleJni(boolean z);

    private static native void setImagePathJni(String str);

    private static native void setRGBDataSourceJni(int i, int i2, byte[] bArr, int i3);

    public void DestructWrapSegmentEngine() {
        Log.d(TAG, "desctruct EffectPlayer");
        DestructWrapSegmentEngineJni();
    }

    public long ConstructWrapSegmentEngine() {
        Log.d(TAG, "desctruct EffectPlayer");
        return ConstructWrapSegmentEngineJni();
    }

    public void setImagePath(String str) {
        Log.d(TAG, "setImagePath");
        setImagePathJni(str);
    }

    public void setRGBDataSource(int i, int i2, byte[] bArr, int i3) {
        String str = TAG;
        Log.d(str, "setRGBDataSource w:" + i + " h:" + i2 + "stride:" + i3 + " data len:" + bArr.length);
        setRGBDataSourceJni(i, i2, bArr, i3);
    }

    public boolean getSegmentResult() {
        return getSegmentResultJni();
    }

    public boolean getExchangeResult() {
        return getExchangeResultJni();
    }

    public void setExchangeResult(boolean z) {
        setExchangeResultJni(z);
    }

    public void initSegment() {
        initSegmentJni();
    }

    public void setGLSurfaceAvalibale(boolean z) {
        setGLSurfaceAvalibaleJni(z);
    }

    public void releaseSegment() {
        releaseSegmentJni();
    }
}
