package com.baidu.platform.comapi.util;

import com.baidu.vi.VIContext;

/* loaded from: classes.dex */
public class SysOSUtil {
    private static SysOSUtil g = new SysOSUtil();
    private com.baidu.platform.comapi.util.a.b a = null;
    private com.baidu.platform.comapi.util.a.a b = null;
    private boolean c = false;
    private String d = "";
    private String e = "";
    private String f = "";

    private SysOSUtil() {
    }

    public static SysOSUtil getInstance() {
        return g;
    }

    public String getCompatibleSdcardPath() {
        com.baidu.platform.comapi.util.a.b bVar = this.a;
        return bVar != null ? bVar.c() : "";
    }

    public float getDensity() {
        com.baidu.platform.comapi.util.a.a aVar = this.b;
        if (aVar != null) {
            return aVar.c();
        }
        return 1.0f;
    }

    public int getDensityDPI() {
        com.baidu.platform.comapi.util.a.a aVar = this.b;
        if (aVar != null) {
            return aVar.d();
        }
        return 1;
    }

    public String getExternalFilesDir() {
        com.baidu.platform.comapi.util.a.b bVar = this.a;
        return bVar != null ? bVar.e() : "";
    }

    public String getGLRenderer() {
        return this.f;
    }

    public String getGLVersion() {
        return this.e;
    }

    public String getNetType() {
        return this.d;
    }

    public String getOutputCache() {
        com.baidu.platform.comapi.util.a.b bVar = this.a;
        return bVar != null ? bVar.d() : "";
    }

    public String getOutputDirPath() {
        com.baidu.platform.comapi.util.a.b bVar = this.a;
        return bVar != null ? bVar.a() : "";
    }

    public int getScreenHeight() {
        com.baidu.platform.comapi.util.a.a aVar = this.b;
        if (aVar != null) {
            return aVar.b();
        }
        return 0;
    }

    public int getScreenWidth() {
        com.baidu.platform.comapi.util.a.a aVar = this.b;
        if (aVar != null) {
            return aVar.a();
        }
        return 0;
    }

    public String getSdcardPath() {
        com.baidu.platform.comapi.util.a.b bVar = this.a;
        return bVar != null ? bVar.b() : "";
    }

    public void init(com.baidu.platform.comapi.util.a.b bVar, com.baidu.platform.comapi.util.a.a aVar) {
        if (!this.c) {
            this.a = bVar;
            this.b = aVar;
            if (bVar == null) {
                this.a = new com.baidu.platform.comapi.util.a.b();
            }
            if (this.b == null) {
                this.b = new com.baidu.platform.comapi.util.a.a();
            }
            this.a.a(VIContext.getContext());
            this.b.a(VIContext.getContext());
            this.d = NetworkUtil.getCurrentNetMode(VIContext.getContext());
            this.c = true;
        }
    }

    public void setGLInfo(String str, String str2) {
        if (!this.f.equals(str2) || !this.e.equals(str)) {
            this.e = str;
            this.f = str2;
        }
    }

    public void updateNetType(String str) {
        this.d = str;
    }
}
