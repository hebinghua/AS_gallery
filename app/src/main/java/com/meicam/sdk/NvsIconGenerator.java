package com.meicam.sdk;

import android.graphics.Bitmap;

/* loaded from: classes.dex */
public class NvsIconGenerator {
    private final String TAG = "Meicam";
    private IconCallback m_iconCallback;
    private long m_iconGenerator;

    /* loaded from: classes.dex */
    public interface IconCallback {
        void onIconReady(Bitmap bitmap, long j, long j2);
    }

    private native void nativeCancelTask(long j, long j2);

    private native void nativeClose(long j);

    private native long nativeGetIcon(long j, String str, long j2, int i);

    private native Bitmap nativeGetIconFromCache(long j, String str, long j2, int i);

    private native long nativeInit();

    public NvsIconGenerator() {
        this.m_iconGenerator = 0L;
        NvsUtils.checkFunctionInMainThread();
        this.m_iconGenerator = nativeInit();
    }

    public void release() {
        NvsUtils.checkFunctionInMainThread();
        if (isReleased()) {
            return;
        }
        this.m_iconCallback = null;
        nativeClose(this.m_iconGenerator);
        this.m_iconGenerator = 0L;
    }

    public boolean isReleased() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_iconGenerator == 0;
    }

    public void setIconCallback(IconCallback iconCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_iconCallback = iconCallback;
    }

    public Bitmap getIconFromCache(String str, long j, int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetIconFromCache(this.m_iconGenerator, str, j, i);
    }

    public long getIcon(String str, long j, int i) {
        NvsUtils.checkFunctionInMainThread();
        if (isReleased()) {
            return 0L;
        }
        return nativeGetIcon(this.m_iconGenerator, str, j, i);
    }

    public void cancelTask(long j) {
        NvsUtils.checkFunctionInMainThread();
        if (!isReleased()) {
            nativeCancelTask(this.m_iconGenerator, j);
        }
    }

    public void notifyIconReady(Bitmap bitmap, long j, long j2) {
        IconCallback iconCallback = this.m_iconCallback;
        if (iconCallback != null) {
            iconCallback.onIconReady(bitmap, j, j2);
        }
    }
}
