package com.market.sdk.utils;

import android.content.SharedPreferences;
import android.os.Looper;

/* loaded from: classes.dex */
public class PrefUtils {

    /* loaded from: classes.dex */
    public enum PrefFile {
        DEFAULT("com.xiaomi.market.sdk_pref", false);
        
        public final String fileName;
        public boolean isMultiProcess;

        PrefFile(String str, boolean z) {
            this.fileName = str;
            this.isMultiProcess = z;
        }
    }

    public static SharedPreferences getSharedPref(PrefFile prefFile) {
        return AppGlobal.getContext().getSharedPreferences(prefFile.fileName, prefFile.isMultiProcess ? 4 : 0);
    }

    public static SharedPreferences getSharedPrefFromParams(PrefFile[] prefFileArr) {
        PrefFile prefFile;
        if (prefFileArr.length == 0) {
            prefFile = PrefFile.DEFAULT;
        } else {
            prefFile = prefFileArr[0];
        }
        return getSharedPref(prefFile);
    }

    public static void applyOrCommit(SharedPreferences.Editor editor) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static void setInt(String str, int i, PrefFile... prefFileArr) {
        SharedPreferences.Editor edit = getSharedPrefFromParams(prefFileArr).edit();
        edit.putInt(str, i);
        applyOrCommit(edit);
    }

    public static int getInt(String str, PrefFile... prefFileArr) {
        return getSharedPrefFromParams(prefFileArr).getInt(str, 0);
    }

    public static void setLong(String str, long j, PrefFile... prefFileArr) {
        SharedPreferences.Editor edit = getSharedPrefFromParams(prefFileArr).edit();
        edit.putLong(str, j);
        applyOrCommit(edit);
    }

    public static long getLong(String str, PrefFile... prefFileArr) {
        return getSharedPrefFromParams(prefFileArr).getLong(str, 0L);
    }

    public static String getString(String str, String str2, PrefFile... prefFileArr) {
        return getSharedPrefFromParams(prefFileArr).getString(str, str2);
    }

    public static void setString(String str, String str2, PrefFile... prefFileArr) {
        SharedPreferences.Editor edit = getSharedPrefFromParams(prefFileArr).edit();
        edit.putString(str, str2);
        applyOrCommit(edit);
    }
}
