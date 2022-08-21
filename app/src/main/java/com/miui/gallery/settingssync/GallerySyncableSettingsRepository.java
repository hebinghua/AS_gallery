package com.miui.gallery.settingssync;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class GallerySyncableSettingsRepository implements GallerySettingsSyncContract$Repository {
    public static List<GallerySettingsSyncContract$SyncableSetting> sSyncableSettings;

    static {
        ArrayList arrayList = new ArrayList();
        sSyncableSettings = arrayList;
        arrayList.add(new FaceSetting());
        sSyncableSettings.add(new SearchSetting());
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$Repository
    public List<GallerySettingsSyncContract$SyncableSetting> getSyncableSettings() {
        return sSyncableSettings;
    }
}
