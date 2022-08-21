package com.miui.gallery.settingsbackup;

import android.content.Context;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.PreferenceHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GallerySettingsBackupHelper {
    public static final JSONObject backupToCloud(Context context) {
        JSONObject jSONObject = new JSONObject();
        List<String> autoBackupPrefKeys = GalleryPreferences.getAutoBackupPrefKeys();
        if (autoBackupPrefKeys != null) {
            try {
                for (String str : autoBackupPrefKeys) {
                    savePrefEntry(str, jSONObject);
                }
            } catch (JSONException e) {
                DefaultLogger.e("GallerySettingsBackupHelper", e);
            }
        }
        return jSONObject;
    }

    public static final void restoreFromCloud(Context context, JSONObject jSONObject) {
        List<String> autoBackupPrefKeys;
        if (jSONObject == null || (autoBackupPrefKeys = GalleryPreferences.getAutoBackupPrefKeys()) == null) {
            return;
        }
        for (String str : autoBackupPrefKeys) {
            restorePrefEntry(str, jSONObject);
        }
    }

    public static void savePrefEntry(String str, JSONObject jSONObject) throws JSONException {
        Object obj = PreferenceHelper.getAll().get(str);
        if (obj != null) {
            jSONObject.put(str, obj);
        }
    }

    public static void restorePrefEntry(String str, JSONObject jSONObject) {
        Object opt = jSONObject.opt(str);
        if (opt != null) {
            if (opt instanceof Boolean) {
                PreferenceHelper.putBoolean(str, ((Boolean) opt).booleanValue());
            } else if (opt instanceof Integer) {
                PreferenceHelper.putInt(str, ((Integer) opt).intValue());
            } else if (opt instanceof Float) {
                PreferenceHelper.putFloat(str, ((Float) opt).floatValue());
            } else if (opt instanceof Long) {
                PreferenceHelper.putLong(str, ((Long) opt).longValue());
            } else if (!(opt instanceof String)) {
            } else {
                PreferenceHelper.putString(str, (String) opt);
            }
        }
    }
}
