package com.miui.gallery.preference;

import android.content.SharedPreferences;
import com.miui.gallery.GalleryApp;

/* loaded from: classes2.dex */
public class ThumbnailWritePreference {
    public SharedPreferences mSharedPreferences;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static ThumbnailWritePreference sInstance = new ThumbnailWritePreference();
    }

    public ThumbnailWritePreference() {
        this.mSharedPreferences = GalleryApp.sGetAndroidContext().getSharedPreferences("com.miui.gallery_thumbnails_write", 0);
    }

    public static SharedPreferences getPreferences() {
        return SingletonHolder.sInstance.mSharedPreferences;
    }

    public static void addKey(String str) {
        getPreferences().edit().putString(str, "").commit();
    }

    public static boolean containsKey(String str) {
        return getPreferences().contains(str);
    }

    public static boolean clear() {
        return getPreferences().edit().clear().commit();
    }
}
