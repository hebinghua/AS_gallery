package com.miui.gallery.util.deprecated;

import android.content.Context;
import android.content.SharedPreferences;
import com.miui.gallery.preference.GalleryPreferences;

/* loaded from: classes2.dex */
public class Preference extends BaseDeprecatedPreference {
    public static void setDBUpgradeTo42() {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("database_upgrade_to_42_clean_data", true).apply();
    }

    public static boolean getSyncFetchSyncExtraInfoFromV2ToV3() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("sync_fetch_syncextreinfo_from_v2_to_v3", false);
    }

    public static void setSyncFetchSyncExtraInfoFromV2ToV3(boolean z) {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("sync_fetch_syncextreinfo_from_v2_to_v3", z).apply();
    }

    public static boolean getSyncFetchAllPrivateData() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("sync_fetch_all_private_data", false);
    }

    public static void setSyncFetchAllPrivateData() {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("sync_fetch_all_private_data", true).commit();
    }

    public static boolean getSyncFetchPrivateVideo() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("sync_fetch_private_video", false);
    }

    public static void setSyncFetchPrivateVideo() {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("sync_fetch_private_video", true).commit();
    }

    public static void sSetCloudGallerySpaceFull(boolean z) {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("cloud_gallery_space_full", z).putLong("cloud_gallery_space_full_time", System.currentTimeMillis()).commit();
    }

    public static boolean sGetCloudGallerySpaceFull() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("cloud_gallery_space_full", false);
    }

    public static long sGetCloudGallerySpaceFullTime() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getLong("cloud_gallery_space_full_time", 0L);
    }

    public static void sSetCloudGalleryRecyclebinFull(boolean z) {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("cloud_gallery_recyclebin_full", z).commit();
    }

    public static boolean sGetCloudGalleryRecyclebinFull() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("cloud_gallery_recyclebin_full", false);
    }

    public static void sSetIsInternationalAccount(int i) {
        BaseDeprecatedPreference.sGetDefaultEditor().putInt("is_international_account", i).commit();
    }

    public static int sIsInternationalAccount() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getInt("is_international_account", 2);
    }

    public static boolean getSyncShouldClearDataBase() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("sync_should_clean_data", false);
    }

    public static void setSyncShouldClearDataBase(boolean z) {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("sync_should_clean_data", z).commit();
    }

    public static int getDeleteAccountStrategy() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getInt("account_delete_data_strategy", 0);
    }

    public static void setDeleteAccountStrategy(int i) {
        BaseDeprecatedPreference.sGetDefaultEditor().putInt("account_delete_data_strategy", i);
    }

    public static boolean sIsFirstSynced() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("first_synced", false);
    }

    public static void sSetFirstSynced() {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("first_synced", true).commit();
    }

    public static boolean sIsShareImageScanned() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("share_image_scanned", false);
    }

    public static void sSetShareImageScanned() {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("share_image_scanned", true).commit();
    }

    public static boolean sIsMediaStoreSupportGalleryScan() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("media_store_support_gallery_scan", false);
    }

    public static void sSetMediaStoreSupportGalleryScan() {
        BaseDeprecatedPreference.sGetDefaultEditor().putBoolean("media_store_support_gallery_scan", true).commit();
    }

    public static void sRemoveCloudSettings() {
        BaseDeprecatedPreference.sGetDefaultEditor().remove("last_slowscan_time").remove("cloud_gallery_space_full").remove("cloud_gallery_recyclebin_full").remove("sync_only_in_wifi").remove("sync_fetch_syncextreinfo_from_v2_to_v3").remove("sync_fetch_all_private_data").remove("sync_fetch_private_video").remove("first_synced").remove("is_international_account").remove(GalleryPreferences.PrefKeys.FACE_CLOUD_STATUS).remove(GalleryPreferences.PrefKeys.FACE_CLOUD_STATUS_NEXT_CHECK_TIME).remove(GalleryPreferences.PrefKeys.FACE_FEATURE_SWITCH_PENDING).remove(GalleryPreferences.PrefKeys.FACE_URL_FOR_WAITING).remove(GalleryPreferences.PrefKeys.FACE_URL_FOR_QUEUING).remove("micloud_vip_level").remove("has_ever_synced_system_album").remove("share_image_scanned").commit();
        SharedPreferences.Editor sGetDefaultEditor = BaseDeprecatedPreference.sGetDefaultEditor();
        for (String str : BaseDeprecatedPreference.sGetDefaultPreferences().getAll().keySet()) {
            if (isAutoUploadAlbumPreferenceKey(str)) {
                sGetDefaultEditor.remove(str);
            }
        }
        sGetDefaultEditor.commit();
    }

    public static long sGetFilterMinSize() {
        try {
            return Long.parseLong(BaseDeprecatedPreference.sGetDefaultPreferences().getString("filter_min_size", String.valueOf(0L)));
        } catch (NumberFormatException unused) {
            return 0L;
        }
    }

    public static boolean sGetIsAlbumAutoUploadOpen(String str) {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean(createAutoUploadAlbumPreferenceKey(str), false);
    }

    public static boolean sGetIsScreenShotAutoUploadOpen() {
        return BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean(createAutoUploadAlbumPreferenceKey("auto-upload-screenshot"), true);
    }

    public static boolean isAutoUploadAlbumPreferenceKey(String str) {
        return str.startsWith("auto_upload_album_preference_key_prefix_");
    }

    public static String createAutoUploadAlbumPreferenceKey(String str) {
        return "auto_upload_album_preference_key_prefix_" + str;
    }

    public static void sSetLastSlowScanTime(long j) {
        BaseDeprecatedPreference.sGetDefaultEditor().putLong("last_slowscan_time", j).apply();
    }

    public static SharedPreferences getMultiProcessSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName() + "_preferences_multi", 4);
    }

    public static boolean sHaveCheckBabyForNewService(Context context) {
        return getMultiProcessSharedPreferences(context).getBoolean("have_check_baby_for_new_face_service", false);
    }

    public static void sSetHaveCheckBabyForNewService(Context context) {
        getMultiProcessSharedPreferences(context).edit().putBoolean("have_check_baby_for_new_face_service", true).commit();
    }
}
