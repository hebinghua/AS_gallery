package com.miui.gallery.settingssync;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AIAlbumStatusHelper;

/* loaded from: classes2.dex */
public class FaceSetting implements GallerySettingsSyncContract$SyncableSetting {
    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$SyncableSetting
    public String getName() {
        return "face";
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
        if (AIAlbumStatusHelper.isFaceSwitchSet()) {
            return Boolean.valueOf(AIAlbumStatusHelper.isFaceAlbumEnabled());
        }
        return null;
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$SyncableSetting
    public void writeValue(Boolean bool, Object obj) {
        if (bool != null) {
            if (!bool.booleanValue() && AIAlbumStatusHelper.isFaceSwitchSet() && AIAlbumStatusHelper.isFaceAlbumEnabled()) {
                SamplingStatHelper.recordCountEvent("face", "settings_sync_face_status_to_close");
            }
            AIAlbumStatusHelper.setFaceAlbumStatus(GalleryApp.sGetAndroidContext(), bool.booleanValue(), false);
        }
    }
}
