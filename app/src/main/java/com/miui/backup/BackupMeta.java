package com.miui.backup;

/* loaded from: classes.dex */
public class BackupMeta {
    public static final int META_VERSION = 2;
    public int appVersionCode;
    public String appVersionName;
    public long createTime;
    public String deviceName;
    public int feature;
    public int metaVersion = 2;
    public String miuiVersion;
    public String packageName;
    public int version;

    public int getMetaVersion() {
        return 2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public int getAppVersionCode() {
        return this.appVersionCode;
    }

    public String getAppVersionName() {
        return this.appVersionName;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public int getVersion() {
        return this.version;
    }

    public int getFeature() {
        return this.feature;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getMiuiVersion() {
        return this.miuiVersion;
    }
}
