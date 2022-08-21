package com.miui.gallery.settingssync;

import android.content.Context;

/* loaded from: classes2.dex */
public class GallerySettingsSyncHelper {
    public static void doUpload(Context context) {
        getSyncAdapter(context, getModel(getRepository())).performUpload();
    }

    public static void doDownload(Context context) {
        getSyncAdapter(context, getModel(getRepository())).performDownload();
    }

    public static GallerySettingsSyncContract$SyncAdapter getSyncAdapter(Context context, GallerySettingsSyncContract$Model gallerySettingsSyncContract$Model) {
        return new GallerySettingsSyncAdapter(context, gallerySettingsSyncContract$Model);
    }

    public static GallerySettingsSyncContract$Model getModel(GallerySettingsSyncContract$Repository gallerySettingsSyncContract$Repository) {
        return new GallerySettingSyncModel(gallerySettingsSyncContract$Repository);
    }

    public static GallerySettingsSyncContract$Repository getRepository() {
        return new GallerySyncableSettingsRepository();
    }
}
