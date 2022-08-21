package com.miui.gallery.preference;

import android.content.SharedPreferences;

/* loaded from: classes2.dex */
public class MemoryPreferenceHelper {
    public SharedPreferences mSharedPreferences;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final MemoryPreferenceHelper INSTANCE = new MemoryPreferenceHelper();
    }

    public MemoryPreferenceHelper() {
        this.mSharedPreferences = new MemoryPreferencesImpl();
    }

    public static SharedPreferences getPreferences() {
        return SingletonHolder.INSTANCE.mSharedPreferences;
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

    public static void putBoolean(String str, boolean z) {
        getPreferences().edit().putBoolean(str, z).apply();
    }

    public static boolean getBoolean(String str, boolean z) {
        return getPreferences().getBoolean(str, z);
    }

    public static boolean contains(String str) {
        return getPreferences().contains(str);
    }

    public static void clear() {
        getPreferences().edit().clear().apply();
    }
}
