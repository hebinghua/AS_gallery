package com.xiaomi.stat.a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes3.dex */
public class a extends SQLiteOpenHelper {
    private static final String a = "mistat_ev";
    private static final int b = 1;
    private static final String c = "CREATE TABLE events (_id INTEGER PRIMARY KEY AUTOINCREMENT,e TEXT,eg TEXT,tp TEXT,ps TEXT,ts INTEGER,sub TEXT,is_am INTEGER,priority INTEGER)";

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public a(Context context) {
        super(context, "mistat_ev", (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(c);
    }
}
