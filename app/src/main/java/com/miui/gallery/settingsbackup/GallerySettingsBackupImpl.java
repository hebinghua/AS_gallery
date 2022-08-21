package com.miui.gallery.settingsbackup;

import android.content.Context;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import com.xiaomi.settingsdk.backup.ICloudBackup;
import com.xiaomi.settingsdk.backup.data.DataPackage;
import com.xiaomi.settingsdk.backup.data.SettingItem;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GallerySettingsBackupImpl implements ICloudBackup {
    public int getCurrentVersion(Context context) {
        return 1;
    }

    public void onBackupSettings(Context context, DataPackage dataPackage) {
        DefaultLogger.d("GallerySettingsBackupImpl", "onBackupSettings start");
        dataPackage.addKeyJson("com.miui.gallery.settings", logJSON(GallerySettingsBackupHelper.backupToCloud(context)));
        DefaultLogger.d("GallerySettingsBackupImpl", "onBackupSettings end");
    }

    public void onRestoreSettings(Context context, DataPackage dataPackage, int i) {
        SettingItem settingItem;
        DefaultLogger.d("GallerySettingsBackupImpl", "onRestoreSettings start");
        if (dataPackage != null && (settingItem = dataPackage.get("com.miui.gallery.settings")) != null) {
            GallerySettingsBackupHelper.restoreFromCloud(context, logJSON((JSONObject) settingItem.getValue()));
        }
        DefaultLogger.d("GallerySettingsBackupImpl", "onRestoreSettings end");
    }

    public static JSONObject logJSON(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        if (Rom.IS_ALPHA || Rom.IS_DEV) {
            DefaultLogger.d("GallerySettingsBackupImpl", jSONObject.toString());
        }
        return jSONObject;
    }
}
