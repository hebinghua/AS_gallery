package com.miui.backup;

/* loaded from: classes.dex */
class BackupMetaUtils {
    public static BackupMeta translate(miui.app.backup.BackupMeta backupMeta) {
        BackupMeta backupMeta2 = new BackupMeta();
        backupMeta2.appVersionCode = backupMeta.getAppVersionCode();
        backupMeta2.appVersionName = backupMeta.getAppVersionName();
        backupMeta2.createTime = backupMeta.getCreateTime();
        backupMeta2.deviceName = backupMeta.getDeviceName();
        backupMeta2.feature = backupMeta.getFeature();
        backupMeta2.metaVersion = backupMeta.getMetaVersion();
        backupMeta2.miuiVersion = backupMeta.getMiuiVersion();
        backupMeta2.packageName = backupMeta.getPackageName();
        backupMeta2.version = backupMeta.getVersion();
        return backupMeta2;
    }
}
