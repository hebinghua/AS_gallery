package com.xiaomi.skyprocess;

import android.util.Log;
import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class SubtitleEngine {
    private static String TAG = "SubtitleEngine";

    private static native long ConstructSubtitleEngineJni(String str);

    private static native void ConvertText2ImageJni(String str, int i, int i2, ByteBuffer byteBuffer);

    private static native void DestructSubtitleEngineJni();

    public void DestructSubtitleEngine() {
        Log.d(TAG, "desctruct Subtitle Engine");
        DestructSubtitleEngineJni();
    }

    public long ConstructSubtitleEngine(String str) {
        Log.d(TAG, "desctruct Subtitle Engine");
        return ConstructSubtitleEngineJni(str);
    }

    public void ConvertText2Image(String str, int i, int i2, ByteBuffer byteBuffer) {
        ConvertText2ImageJni(str, i, i2, byteBuffer);
    }
}
