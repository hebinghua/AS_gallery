package com.miui.gallery.magic.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.miui.gallery.magic.tools.MagicUtils;

/* loaded from: classes2.dex */
public class CacheManager {
    public static Context context = MagicUtils.getGalleryApp();
    public static SharedPreferences sp;

    public static boolean addCache(String[] strArr, Object[] objArr) {
        if (strArr.length != objArr.length) {
            return false;
        }
        SharedPreferences.Editor edit = context.getSharedPreferences("setting_key", 0).edit();
        for (int i = 0; i < strArr.length; i++) {
            if (objArr[i] instanceof String) {
                edit.putString(strArr[i], (String) objArr[i]);
            } else if (objArr[i] instanceof Boolean) {
                edit.putBoolean(strArr[i], ((Boolean) objArr[i]).booleanValue());
            } else if (objArr[i] instanceof Integer) {
                edit.putInt(strArr[i], ((Integer) objArr[i]).intValue());
            } else if (objArr[i] instanceof Long) {
                edit.putLong(strArr[i], ((Long) objArr[i]).longValue());
            } else if (objArr[i] instanceof Float) {
                edit.putFloat(strArr[i], ((Float) objArr[i]).floatValue());
            }
        }
        edit.commit();
        return true;
    }

    public static boolean getBoolean(String str, boolean z) {
        SharedPreferences sharedPreferences = sp;
        return sharedPreferences == null ? z : sharedPreferences.getBoolean(str, z);
    }
}
