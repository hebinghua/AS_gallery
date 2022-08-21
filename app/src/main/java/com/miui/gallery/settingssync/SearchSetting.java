package com.miui.gallery.settingssync;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class SearchSetting implements GallerySettingsSyncContract$SyncableSetting {
    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$SyncableSetting
    public String getName() {
        return MiStat.Event.SEARCH;
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$SyncableSetting
    public String getValue() {
        return null;
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$SyncableSetting
    public boolean isExport() {
        return true;
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$SyncableSetting
    public Boolean isEnabled() {
        if (!AIAlbumStatusHelper.isLocalSearchSet()) {
            return null;
        }
        return Boolean.valueOf(AIAlbumStatusHelper.isLocalSearchOpen(true));
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$SyncableSetting
    public void writeValue(Boolean bool, Object obj) {
        if (bool != null) {
            if (!AIAlbumStatusHelper.isLocalSearchSet() && !bool.booleanValue()) {
                return;
            }
            AIAlbumStatusHelper.setLocalSearchStatus(GalleryApp.sGetAndroidContext(), bool.booleanValue(), false);
        }
    }
}
