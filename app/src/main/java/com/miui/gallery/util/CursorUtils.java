package com.miui.gallery.util;

import android.database.Cursor;

/* loaded from: classes2.dex */
public class CursorUtils {
    public static String getCursorString(Cursor cursor, int i) {
        String string = isCursorColumnIndexValid(cursor, i) ? cursor.getString(i) : null;
        return string == null ? "" : string;
    }

    public static long getCursorLong(Cursor cursor, int i) {
        if (isCursorColumnIndexValid(cursor, i)) {
            return cursor.getLong(i);
        }
        return 0L;
    }

    public static int getCursorInt(Cursor cursor, int i) {
        if (isCursorColumnIndexValid(cursor, i)) {
            return cursor.getInt(i);
        }
        return 0;
    }

    public static boolean isCursorColumnIndexValid(Cursor cursor, int i) {
        return i < cursor.getColumnCount();
    }

    public static String getCursorString(Cursor cursor, String str) {
        int columnIndex = cursor.getColumnIndex(str);
        return -1 != columnIndex ? cursor.getString(columnIndex) : "";
    }

    public static long getCursorLong(Cursor cursor, String str) {
        int columnIndex = cursor.getColumnIndex(str);
        if (-1 != columnIndex) {
            return cursor.getLong(columnIndex);
        }
        return 0L;
    }

    public static int getCursorInt(Cursor cursor, String str) {
        int columnIndex = cursor.getColumnIndex(str);
        if (-1 != columnIndex) {
            return cursor.getInt(columnIndex);
        }
        return 0;
    }

    public static int getColumnIndex(Cursor cursor, String str) {
        return cursor.getColumnIndex(str);
    }
}
