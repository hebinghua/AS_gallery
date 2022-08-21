package com.baidu.mapapi;

/* loaded from: classes.dex */
public class OpenLogUtil {
    private static ModuleName a = null;
    private static boolean b = true;
    private static boolean c = false;
    private static String d;

    public static String getMapLogFilePath() {
        return d;
    }

    public static boolean isMapLogEnable() {
        return c;
    }

    public static boolean isNativeLogAnalysisEnable() {
        return b;
    }

    public static void setMapLogEnable(boolean z) {
        c = z;
    }

    public static void setMapLogFilePath(String str) {
        d = str;
    }

    public static void setModuleLogEnable(ModuleName moduleName, boolean z) {
        a = moduleName;
        com.baidu.mapsdkplatform.comjni.tools.a.a(z, moduleName.ordinal());
    }

    public static void setNativeLogAnalysisEnable(boolean z) {
        b = z;
    }
}
