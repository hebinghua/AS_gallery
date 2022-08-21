package com.nexstreaming.app.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/* compiled from: UserInfo.java */
/* loaded from: classes3.dex */
public class o {
    private static String a(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                stringBuffer.append(Integer.toString((b & 255) + 256, 16).substring(1));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String a(Context context) {
        String uuid;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = defaultSharedPreferences.getString("nex_tl_app_id", null);
        if (string != null) {
            return string;
        }
        if (a() >= 9) {
            uuid = Settings.Secure.getString(context.getContentResolver(), "android_id");
        } else {
            uuid = UUID.randomUUID().toString();
        }
        String c = c(context);
        String a = a(uuid + c);
        if (a != null) {
            Log.d("[UserInfo]", "deviceId length : " + a.length());
            Log.d("[UserInfo]", "deviceId : " + a);
            defaultSharedPreferences.edit().putString("nex_tl_app_id", a).commit();
        }
        return a;
    }

    public static String b(Context context) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        String str = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
        Log.d("[UserInfo]", "appName : " + str);
        return str;
    }

    public static String c(Context context) {
        String str;
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            str = "";
        }
        Log.d("[UserInfo]", "package : " + str);
        return str;
    }

    public static String d(Context context) {
        String str;
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            str = "";
        }
        Log.e("[UserInfo]", "Version Information ===== ");
        Log.e("[UserInfo]", "version name : " + str);
        return str;
    }

    public static int a() {
        return Build.VERSION.SDK_INT;
    }

    public static String b() {
        String b;
        String b2;
        String b3 = NexEditor.b("ro.board.platform");
        if (b3 != null && b3.trim().equalsIgnoreCase("msm8960") && (b2 = NexEditor.b("ro.product.board")) != null && b2.trim().equalsIgnoreCase("8x30")) {
            b3 = "MSM8x30n";
        }
        if (b3 != null && b3.equalsIgnoreCase("exynos5") && (b = NexEditor.b("ro.product.board")) != null && b.equalsIgnoreCase("universal7420")) {
            b3 = b;
        }
        if (b3 == null || b3.trim().length() < 1) {
            b3 = NexEditor.b("ro.mediatek.platform");
        }
        if (b3 == null || b3.trim().length() < 1) {
            b3 = NexEditor.b("ro.hardware");
        }
        return (b3 == null || b3.trim().length() < 1) ? "unknown" : b3;
    }
}
