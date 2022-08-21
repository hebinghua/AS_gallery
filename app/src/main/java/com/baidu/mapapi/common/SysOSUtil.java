package com.baidu.mapapi.common;

import android.text.TextUtils;
import com.baidu.mapsdkplatform.comapi.util.h;

/* loaded from: classes.dex */
public class SysOSUtil {
    public static float getDensity() {
        return h.c;
    }

    public static int getDensityDpi() {
        return h.m();
    }

    public static String getDeviceID() {
        String q = h.q();
        return TextUtils.isEmpty(q) ? q : q.substring(0, q.indexOf("|"));
    }

    public static String getModuleFileName() {
        return h.p();
    }

    public static String getPhoneType() {
        return h.h();
    }

    public static int getScreenSizeX() {
        return h.i();
    }

    public static int getScreenSizeY() {
        return h.k();
    }
}
