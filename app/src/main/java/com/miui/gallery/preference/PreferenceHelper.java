package com.miui.gallery.preference;

import android.content.SharedPreferences;
import com.miui.gallery.util.StaticContext;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public class PreferenceHelper {
    public SharedPreferences mSharedPreferences;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static PreferenceHelper sInstance = new PreferenceHelper();
    }

    public PreferenceHelper() {
        this.mSharedPreferences = StaticContext.sGetAndroidContext().getSharedPreferences("com.miui.gallery_preferences_new", 0);
    }

    public static SharedPreferences getPreferences() {
        return SingletonHolder.sInstance.mSharedPreferences;
    }

    public static void putLong(String str, long j) {
        getPreferences().edit().putLong(str, j).apply();
    }

    public static long getLong(String str, long j) {
        return getPreferences().getLong(str, j);
    }

    public static void putInt(String str, int i) {
        getPreferences().edit().putInt(str, i).apply();
    }

    public static int getInt(String str, int i) {
        return getPreferences().getInt(str, i);
    }

    public static void putString(String str, String str2) {
        getPreferences().edit().putString(str, str2).apply();
    }

    public static String getString(String str, String str2) {
        return getPreferences().getString(str, str2);
    }

    public static void putStringSet(String str, Set<String> set) {
        getPreferences().edit().putStringSet(str, set).apply();
    }

    public static Set<String> getStringSet(String str, Set<String> set) {
        return getPreferences().getStringSet(str, set);
    }

    public static void putBoolean(String str, boolean z) {
        getPreferences().edit().putBoolean(str, z).apply();
    }

    public static boolean getBoolean(String str, boolean z) {
        return getPreferences().getBoolean(str, z);
    }

    public static void putFloat(String str, float f) {
        getPreferences().edit().putFloat(str, f).apply();
    }

    public static void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        getPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public static boolean contains(String str) {
        return getPreferences().contains(str);
    }

    public static Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    public static void removeKey(String str) {
        getPreferences().edit().remove(str).apply();
    }
}
