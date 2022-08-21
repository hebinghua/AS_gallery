package com.miui.gallery.settingssync;

import org.json.JSONObject;

/* loaded from: classes2.dex */
public interface GallerySettingsSyncContract$Model {
    JSONObject getUploadSettings();

    boolean isDirty();

    void markDirty(boolean z);

    boolean onDownloadSettings(JSONObject jSONObject);
}
