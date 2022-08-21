package com.miui.gallery.provider.cache;

import android.database.Cursor;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class ParseUtils {
    public static String getString(Cursor cursor, int i) {
        if (cursor.isNull(i)) {
            return null;
        }
        return cursor.getString(i);
    }

    public static Long getLong(Cursor cursor, int i) {
        if (cursor.isNull(i)) {
            return null;
        }
        return Long.valueOf(cursor.getLong(i));
    }

    public static Integer getInt(Cursor cursor, int i) {
        if (cursor.isNull(i)) {
            return null;
        }
        return Integer.valueOf(cursor.getInt(i));
    }

    public static Character getChar(Cursor cursor, int i) {
        if (cursor.isNull(i)) {
            return null;
        }
        String string = cursor.getString(i);
        if (!TextUtils.isEmpty(string)) {
            return Character.valueOf(string.charAt(0));
        }
        return null;
    }

    public static byte[] getBlob(Cursor cursor, int i) {
        if (cursor.isNull(i)) {
            return null;
        }
        return cursor.getBlob(i);
    }
}
