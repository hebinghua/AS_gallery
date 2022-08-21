package com.qiniu.pili.droid.shortvideo.g;

import android.util.Log;

/* compiled from: Logger.java */
/* loaded from: classes3.dex */
public class e {
    public static final e a = new e("");
    public static final e b = new e("Pili-System");
    public static final e c = new e("Pili-SCREEN");
    public static final e d = new e("Pili-Record");
    public static final e e = new e("Pili-Editor");
    public static final e f = new e("Pili-Capture");
    public static final e g = new e("Pili-Processing");
    public static final e h = new e("Pili-Encode");
    public static final e i = new e("Pili-Decode");
    public static final e j = new e("Pili-OpenGL");
    public static final e k = new e("Pili-Player");
    public static final e l = new e("Pili-Stat");
    public static final e m = new e("Pili-Network");
    public static final e n = new e("Pili-Muxer");
    public static final e o = new e("Pili-Upload");
    public static final e p = new e("Pili-Trim");
    public static final e q = new e("Pili-AudioMix");
    public static final e r = new e("Pili-Resampler");
    public static final e s = new e("Pili-Transcode");
    public static final e t = new e("Pili-Composer");
    public static final e u = new e("Pili-Parser");
    public static final e v = new e("Pili-Transition");
    public static final e w = new e("Pili-Utils");
    public static String x = "PLDroidShortVideo";
    public static int y = 4;
    public final String z;

    public e(String str) {
        this.z = str;
    }

    public void b(String str, String str2) {
        if (y > 3) {
            return;
        }
        String str3 = x;
        Log.d(str3, e(str) + str2);
    }

    public void c(String str, String str2) {
        if (y > 4) {
            return;
        }
        String str3 = x;
        Log.i(str3, e(str) + str2);
    }

    public void d(String str, String str2) {
        if (y > 5) {
            return;
        }
        String str3 = x;
        Log.w(str3, e(str) + str2);
    }

    public void e(String str, String str2) {
        if (y > 6) {
            return;
        }
        String str3 = x;
        Log.e(str3, e(str) + str2);
    }

    public final String e(String str) {
        String str2;
        String str3 = this.z;
        if (str3 == null || "".equals(str3)) {
            str2 = "";
        } else {
            str2 = "" + this.z + ":";
        }
        if (str == null || "".equals(str)) {
            return str2;
        }
        return str2 + str + ":";
    }
}
