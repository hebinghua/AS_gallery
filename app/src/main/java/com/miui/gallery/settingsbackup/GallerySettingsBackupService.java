package com.miui.gallery.settingsbackup;

import com.xiaomi.settingsdk.backup.CloudBackupServiceBase;
import com.xiaomi.settingsdk.backup.ICloudBackup;

/* loaded from: classes2.dex */
public class GallerySettingsBackupService extends CloudBackupServiceBase {
    public ICloudBackup getBackupImpl() {
        return new GallerySettingsBackupImpl();
    }
}
