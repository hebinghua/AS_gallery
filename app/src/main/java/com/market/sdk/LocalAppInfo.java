package com.market.sdk;

/* loaded from: classes.dex */
public class LocalAppInfo {
    public String packageName = "";
    public String displayName = "";
    public int versionCode = 0;
    public String versionName = "";
    public String signature = "";
    public String sourceDir = "";
    public String sourceMD5 = "";
    public boolean isSystem = false;

    public static LocalAppInfo get(String str) {
        LocalAppInfo localAppInfo = new LocalAppInfo();
        localAppInfo.packageName = str;
        return localAppInfo;
    }
}
