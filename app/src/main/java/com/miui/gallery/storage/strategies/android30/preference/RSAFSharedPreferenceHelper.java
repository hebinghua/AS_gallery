package com.miui.gallery.storage.strategies.android30.preference;

import android.content.SharedPreferences;
import com.miui.gallery.util.StaticContext;
import java.util.Map;

/* loaded from: classes2.dex */
public class RSAFSharedPreferenceHelper {
    public final SharedPreferences mSharedPreferences;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final RSAFSharedPreferenceHelper sInstance = new RSAFSharedPreferenceHelper();
    }

    public RSAFSharedPreferenceHelper() {
        this.mSharedPreferences = StaticContext.sGetAndroidContext().getSharedPreferences("com.miui.gallery_r_storage_access_framework", 0);
    }

    public static SharedPreferences getPreferences() {
        return SingletonHolder.sInstance.mSharedPreferences;
    }

    public static void putString(String str, String str2) {
        getPreferences().edit().putString(str, str2).apply();
    }

    public static String getString(String str, String str2) {
        return getPreferences().getString(str, str2);
    }

    public static Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    public static void remove(String str) {
        getPreferences().edit().remove(str).apply();
    }

    public static void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }
}
