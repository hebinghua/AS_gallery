package com.miui.pickdrag.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.mi.mibridge.DeviceLevel;

/* loaded from: classes3.dex */
public class Device {
    public static Boolean isHighDevice;
    public static int sRealScreenHeight;
    public static int sScreenHeight;
    public static int sScreenWidth;

    public static int getScreenWidth() {
        if (sScreenWidth == 0) {
            acquireScreenAttr();
        }
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        if (sScreenHeight == 0) {
            acquireScreenAttr();
        }
        return sScreenHeight;
    }

    public static void acquireScreenAttr(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            sScreenWidth = displayMetrics.widthPixels;
            sScreenHeight = displayMetrics.heightPixels;
            DisplayMetrics displayMetrics2 = new DisplayMetrics();
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics2);
            sRealScreenHeight = displayMetrics2.heightPixels;
        } catch (Exception e) {
            Log.e("device", "acquireScreenAttr", e);
        }
    }

    public static void acquireScreenAttr() {
        sScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getRealDisplayHeight(Context context) {
        if (sRealScreenHeight == 0) {
            acquireScreenAttr(context);
        }
        return sRealScreenHeight;
    }

    public static boolean isHighDevice() {
        if (isHighDevice == null) {
            boolean z = true;
            if (DeviceLevel.getDeviceLevel(1) != DeviceLevel.HIGH) {
                z = false;
            }
            isHighDevice = Boolean.valueOf(z);
        }
        return isHighDevice.booleanValue();
    }
}
