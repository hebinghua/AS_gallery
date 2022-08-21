package com.miui.gallery.ui.album.main.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.PreferenceHelper;
import com.miui.gallery.util.deprecated.BaseDeprecatedPreference;

/* loaded from: classes2.dex */
public class AlbumConfigSharedPreferences {
    public SharedPreferencesProvider mProvider;

    /* loaded from: classes2.dex */
    public interface SharedPreferencesProvider {
        SharedPreferences providerSharedPreferences();
    }

    public AlbumConfigSharedPreferences() {
        this.mProvider = new DefaultSharedPreferencesProvider();
    }

    public static AlbumConfigSharedPreferences getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final AlbumConfigSharedPreferences INSTANCE = new AlbumConfigSharedPreferences();
    }

    public SharedPreferences.Editor edit() {
        return getPreferences().edit();
    }

    public void removeKey(String str) {
        getPreferences().edit().remove(str).apply();
    }

    public void putLong(String str, long j) {
        getPreferences().edit().putLong(str, j).apply();
    }

    public long getLong(String str, long j) {
        return getPreferences().getLong(str, j);
    }

    public void putInt(String str, int i) {
        getPreferences().edit().putInt(str, i).apply();
    }

    public int getInt(String str, int i) {
        return getPreferences().getInt(str, i);
    }

    public void putString(String str, String str2) {
        getPreferences().edit().putString(str, str2).apply();
    }

    public String getString(String str, String str2) {
        return getPreferences().getString(str, str2);
    }

    public void putBoolean(String str, boolean z) {
        getPreferences().edit().putBoolean(str, z).apply();
    }

    public boolean getBoolean(String str, boolean z) {
        return getPreferences().getBoolean(str, z);
    }

    public boolean contains(String str) {
        return getPreferences().contains(str);
    }

    public SharedPreferences getPreferences() {
        return this.mProvider.providerSharedPreferences();
    }

    /* loaded from: classes2.dex */
    public static class DefaultSharedPreferencesProvider implements SharedPreferencesProvider {
        public final SharedPreferences mAlbumSharedPreferences = GalleryApp.sGetAndroidContext().getSharedPreferences("private_sp_gallery_album", 0);

        public DefaultSharedPreferencesProvider() {
            restoreFromOldSharedPreferences();
        }

        public final void restoreFromOldSharedPreferences() {
            if (!this.mAlbumSharedPreferences.getBoolean("is_new_sp", false)) {
                SharedPreferences.Editor putInt = this.mAlbumSharedPreferences.edit().putBoolean(GalleryPreferences.PrefKeys.IS_LOCAL_HAVE_TRASH_FILE, PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.IS_LOCAL_HAVE_TRASH_FILE, false)).putBoolean(GalleryPreferences.PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_AI_ALBUM_INDEX, PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_AI_ALBUM_INDEX, false)).putBoolean(GalleryPreferences.PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_OTHER_ALBUM_INDEX, PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_OTHER_ALBUM_INDEX, false)).putBoolean(GalleryPreferences.PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_TRASH_ALBUM_INDEX, PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_TRASH_ALBUM_INDEX, false)).putBoolean("is_grid_album_page", BaseDeprecatedPreference.sGetDefaultPreferences().getBoolean("is_grid_album_page", true)).putBoolean(GalleryPreferences.PrefKeys.IS_FIRST_SHOW_DRAG_TIP_VIEW, PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.IS_FIRST_SHOW_DRAG_TIP_VIEW, false)).putString(GalleryPreferences.PrefKeys.SORT_POSITION_AI_ALBUM_INDEX, PreferenceHelper.getString(GalleryPreferences.PrefKeys.SORT_POSITION_AI_ALBUM_INDEX, String.valueOf(994L))).putString(GalleryPreferences.PrefKeys.SORT_POSITION_OTHER_ALBUM_INDEX, PreferenceHelper.getString(GalleryPreferences.PrefKeys.SORT_POSITION_OTHER_ALBUM_INDEX, String.valueOf(2147484647L))).putString(GalleryPreferences.PrefKeys.SORT_POSITION_TRASH_ALBUM_INDEX, PreferenceHelper.getString(GalleryPreferences.PrefKeys.SORT_POSITION_TRASH_ALBUM_INDEX, String.valueOf(2147483747L))).putInt(GalleryPreferences.PrefKeys.SORT_POSITION_NANO_NEXT_INDEX, PreferenceHelper.getInt(GalleryPreferences.PrefKeys.SORT_POSITION_NANO_NEXT_INDEX, 0));
                String string = PreferenceHelper.getString(GalleryPreferences.PrefKeys.SORT_POSITION_VIDEO_ALBUM_INDEX, "");
                if (!TextUtils.isEmpty(string)) {
                    putInt.putString(GalleryPreferences.PrefKeys.SORT_POSITION_VIDEO_ALBUM_INDEX, string);
                }
                String string2 = PreferenceHelper.getString(GalleryPreferences.PrefKeys.SORT_POSITION_RECENT_ALBUM_INDEX, "");
                if (!TextUtils.isEmpty(string2)) {
                    putInt.putString(GalleryPreferences.PrefKeys.SORT_POSITION_RECENT_ALBUM_INDEX, string2);
                }
                String string3 = PreferenceHelper.getString(GalleryPreferences.PrefKeys.SORT_POSITION_FAVORITES_ALBUM_INDEX, "");
                if (!TextUtils.isEmpty(string3)) {
                    putInt.putString(GalleryPreferences.PrefKeys.SORT_POSITION_FAVORITES_ALBUM_INDEX, string3);
                }
                String string4 = PreferenceHelper.getString(GalleryPreferences.PrefKeys.SORT_POSITION_BABY_FIRST_INDEX, "");
                if (!TextUtils.isEmpty(string4)) {
                    putInt.putString(GalleryPreferences.PrefKeys.SORT_POSITION_BABY_FIRST_INDEX, string4);
                }
                String string5 = PreferenceHelper.getString(GalleryPreferences.PrefKeys.SORT_POSITION_NEXT_TOP_ALBUM, "");
                if (!TextUtils.isEmpty(string5)) {
                    putInt.putString(GalleryPreferences.PrefKeys.SORT_POSITION_NEXT_TOP_ALBUM, string5);
                }
                String string6 = PreferenceHelper.getString(GalleryPreferences.PrefKeys.SORT_POSITION_BABY_ALBUM_PREV_INDEX, "");
                if (!TextUtils.isEmpty(string6)) {
                    putInt.putString(GalleryPreferences.PrefKeys.SORT_POSITION_BABY_ALBUM_PREV_INDEX, string6);
                }
                putInt.apply();
                this.mAlbumSharedPreferences.edit().putBoolean("is_new_sp", true).apply();
            }
        }

        @Override // com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences.SharedPreferencesProvider
        public SharedPreferences providerSharedPreferences() {
            return this.mAlbumSharedPreferences;
        }
    }
}
