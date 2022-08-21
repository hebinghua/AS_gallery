package com.miui.gallery.settingssync;

import android.text.TextUtils;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GallerySettingSyncModel implements GallerySettingsSyncContract$Model {
    public final GallerySettingsSyncContract$Repository mRepository;

    public GallerySettingSyncModel(GallerySettingsSyncContract$Repository gallerySettingsSyncContract$Repository) {
        this.mRepository = gallerySettingsSyncContract$Repository;
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$Model
    public boolean isDirty() {
        return GalleryPreferences.SettingsSync.isDirty() || !GalleryPreferences.SettingsSync.isFirstUploadComplete();
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$Model
    public void markDirty(boolean z) {
        GalleryPreferences.SettingsSync.markDirty(z);
        if (z || GalleryPreferences.SettingsSync.isFirstUploadComplete()) {
            return;
        }
        GalleryPreferences.SettingsSync.setFirstUploadComplete();
        DefaultLogger.d("GallerySettingSyncModel", "First setting upload complete");
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$Model
    public JSONObject getUploadSettings() {
        List<GallerySettingsSyncContract$SyncableSetting> syncableSettings = this.mRepository.getSyncableSettings();
        if (!BaseMiscUtil.isValid(syncableSettings)) {
            DefaultLogger.d("GallerySettingSyncModel", "No syncable settings found!");
            return null;
        }
        try {
            JSONArray jSONArray = null;
            JSONArray jSONArray2 = null;
            for (GallerySettingsSyncContract$SyncableSetting gallerySettingsSyncContract$SyncableSetting : syncableSettings) {
                JSONObject convertSettingToJSON = convertSettingToJSON(gallerySettingsSyncContract$SyncableSetting);
                if (convertSettingToJSON != null) {
                    if (gallerySettingsSyncContract$SyncableSetting.isExport()) {
                        if (jSONArray == null) {
                            jSONArray = new JSONArray();
                        }
                        jSONArray.put(convertSettingToJSON);
                    } else {
                        if (jSONArray2 == null) {
                            jSONArray2 = new JSONArray();
                        }
                        jSONArray2.put(convertSettingToJSON);
                    }
                }
            }
            if (jSONArray == null && jSONArray2 == null) {
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            if (jSONArray != null) {
                jSONObject.put("itemList", jSONArray);
            }
            if (jSONArray2 != null) {
                jSONObject.put("extraConfig", jSONArray2);
            }
            return jSONObject;
        } catch (JSONException e) {
            DefaultLogger.e("GallerySettingSyncModel", "Failed form settings to JSONObject, %s", e);
            return null;
        }
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$Model
    public boolean onDownloadSettings(JSONObject jSONObject) {
        if (jSONObject == null) {
            DefaultLogger.w("GallerySettingSyncModel", "No download settings received!");
            return true;
        }
        List<GallerySettingsSyncContract$SyncableSetting> syncableSettings = this.mRepository.getSyncableSettings();
        if (!BaseMiscUtil.isValid(syncableSettings)) {
            DefaultLogger.d("GallerySettingSyncModel", "No syncable settings found!");
            return true;
        }
        try {
            JSONArray optJSONArray = jSONObject.optJSONArray("itemList");
            JSONArray optJSONArray2 = jSONObject.optJSONArray("extraConfig");
            saveJSONToSettings(syncableSettings, optJSONArray);
            saveJSONToSettings(syncableSettings, optJSONArray2);
            return true;
        } catch (JSONException e) {
            DefaultLogger.e("GallerySettingSyncModel", "Failed save settings from JSONObject, %s", e);
            return false;
        }
    }

    public final JSONObject convertSettingToJSON(GallerySettingsSyncContract$SyncableSetting gallerySettingsSyncContract$SyncableSetting) throws JSONException {
        Boolean isEnabled = gallerySettingsSyncContract$SyncableSetting.isEnabled();
        String value = gallerySettingsSyncContract$SyncableSetting.getValue();
        if (isEnabled == null && value == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("name", gallerySettingsSyncContract$SyncableSetting.getName());
        if (isEnabled != null) {
            jSONObject.put(CallMethod.RESULT_ENABLE_BOOLEAN, isEnabled);
        }
        if (value != null) {
            jSONObject.put("value", value);
        }
        return jSONObject;
    }

    public final void saveJSONToSettings(List<GallerySettingsSyncContract$SyncableSetting> list, JSONArray jSONArray) throws JSONException {
        GallerySettingsSyncContract$SyncableSetting findSettingByName;
        if (jSONArray == null || jSONArray.length() <= 0) {
            return;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            String optString = jSONObject.optString("name");
            if (!TextUtils.isEmpty(optString) && (findSettingByName = findSettingByName(list, optString)) != null) {
                saveJSONToSetting(findSettingByName, jSONObject);
            }
        }
    }

    public final void saveJSONToSetting(GallerySettingsSyncContract$SyncableSetting gallerySettingsSyncContract$SyncableSetting, JSONObject jSONObject) throws JSONException {
        String str = null;
        Boolean valueOf = jSONObject.has(CallMethod.RESULT_ENABLE_BOOLEAN) ? Boolean.valueOf(jSONObject.getBoolean(CallMethod.RESULT_ENABLE_BOOLEAN)) : null;
        if (jSONObject.has("value")) {
            str = jSONObject.getString("value");
        }
        gallerySettingsSyncContract$SyncableSetting.writeValue(valueOf, str);
    }

    public final GallerySettingsSyncContract$SyncableSetting findSettingByName(List<GallerySettingsSyncContract$SyncableSetting> list, String str) {
        for (GallerySettingsSyncContract$SyncableSetting gallerySettingsSyncContract$SyncableSetting : list) {
            if (str.endsWith(gallerySettingsSyncContract$SyncableSetting.getName())) {
                return gallerySettingsSyncContract$SyncableSetting;
            }
        }
        return null;
    }
}
