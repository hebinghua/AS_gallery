package com.miui.gallery.settingssync;

/* loaded from: classes2.dex */
public interface GallerySettingsSyncContract$SyncableSetting {
    String getName();

    String getValue();

    Boolean isEnabled();

    boolean isExport();

    void writeValue(Boolean bool, Object obj);
}
