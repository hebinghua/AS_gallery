package com.miui.gallery.preference;

import android.content.SharedPreferences;
import com.miui.gallery.GalleryApp;

/* loaded from: classes2.dex */
public class ThumbnailPreference {
    public SharedPreferences mSharedPreferences;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static ThumbnailPreference sInstance = new ThumbnailPreference();
    }

    public ThumbnailPreference() {
        this.mSharedPreferences = GalleryApp.sGetAndroidContext().getSharedPreferences("com.miui.gallery_thumbnails", 0);
    }

    public static SharedPreferences getPreferences() {
        return SingletonHolder.sInstance.mSharedPreferences;
    }

    public static void putThumbnailKey(String str) {
        getPreferences().edit().putString(str, "").commit();
    }

    public static boolean containsThumbnailKey(String str) {
        return getPreferences().contains(str);
    }

    public static boolean clear() {
        return getPreferences().edit().clear().commit();
    }
}
