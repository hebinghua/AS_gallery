package com.baidu.mapsdkplatform.comapi;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.baidu.lbsapi.auth.LBSAuthManager;
import com.baidu.mapapi.JNIInitializer;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.mapapi.common.EnvironmentUtilities;
import com.baidu.platform.comapi.util.SysOSUtil;
import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public class c {
    private static boolean a;

    public static void a(Context context, boolean z) {
        if (context != null) {
            if (!(context instanceof Application)) {
                throw new RuntimeException("BDMapSDKException: context must be an ApplicationContext");
            }
            LBSAuthManager.getInstance(context).setPrivacyMode(z);
            return;
        }
        throw new IllegalArgumentException("BDMapSDKException: context can not be null");
    }

    public static void a(Context context, boolean z, String str, String str2, String str3) {
        if (a) {
            return;
        }
        if (context == null) {
            throw new IllegalArgumentException("BDMapSDKException: context can not be null");
        }
        if (!(context instanceof Application)) {
            throw new RuntimeException("BDMapSDKException: context must be an ApplicationContext");
        }
        NativeLoader.setContext(context);
        NativeLoader.a(z, str);
        JNIInitializer.setContext((Application) context);
        SysOSUtil.getInstance().init(new com.baidu.platform.comapi.util.a.b(), new com.baidu.platform.comapi.util.a.a());
        a.a().a(context);
        a.a().c();
        if (a(str2)) {
            EnvironmentUtilities.setSDCardPath(str2);
        }
        EnvironmentUtilities.initAppDirectory(context);
        if (OpenLogUtil.isNativeLogAnalysisEnable()) {
            com.baidu.mapsdkplatform.comapi.b.a.c.a().a(context);
        }
        a = true;
    }

    public static boolean a() {
        return a;
    }

    private static boolean a(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            File file = new File(str + "/check.0");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            if (!file.exists()) {
                return true;
            }
            file.delete();
            return true;
        } catch (IOException e) {
            Log.e("SDKInitializer", "SDCard cache path invalid", e);
            throw new IllegalArgumentException("BDMapSDKException: Provided sdcard cache path invalid can not used.");
        }
    }
}
