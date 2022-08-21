package cn.kuaipan.android.utils;

import android.database.sqlite.SQLiteDatabase;

/* loaded from: classes.dex */
public class SQLUtility {
    public static final String[] CONFLICT_VALUES = {"", "ROLLBACK", "ABORT", "FAIL", "IGNORE", "REPLACE"};

    public static void createTable(SQLiteDatabase sQLiteDatabase, String str, String str2) {
        sQLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s);", str, str2));
    }

    public static String getSelection(String str) {
        return String.format("%s=?", str);
    }

    public static String getSelectionWithTemplete(String str, String... strArr) {
        return String.format(str, strArr);
    }
}
