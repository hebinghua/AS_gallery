package com.cdv.utils;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/* loaded from: classes.dex */
public class NvAndroidDisplayListener implements DisplayManager.DisplayListener {
    private int m_id;

    private static native void notifyDisplayChanged(int i, int i2);

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int i) {
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(int i) {
    }

    private NvAndroidDisplayListener(int i) {
        this.m_id = 0;
        this.m_id = i;
    }

    public boolean Register(Context context) {
        Object systemService;
        if (context == null || (systemService = context.getSystemService("display")) == null) {
            return false;
        }
        try {
            ((DisplayManager) systemService).registerDisplayListener(this, new Handler(Looper.getMainLooper()));
            return true;
        } catch (Exception e) {
            Log.e("NvAndroidDisplayListener", "" + e.getMessage());
            return false;
        }
    }

    public void Unregister(Context context) {
        Object systemService;
        if (context == null || (systemService = context.getSystemService("display")) == null) {
            return;
        }
        ((DisplayManager) systemService).unregisterDisplayListener(this);
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(int i) {
        notifyDisplayChanged(this.m_id, i);
    }
}
