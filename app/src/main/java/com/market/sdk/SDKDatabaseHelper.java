package com.market.sdk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.market.sdk.utils.Log;

/* loaded from: classes.dex */
public class SDKDatabaseHelper extends SQLiteOpenHelper {
    public static SDKDatabaseHelper mHelper;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public static SDKDatabaseHelper getHelper(Context context) {
        if (mHelper == null) {
            mHelper = new SDKDatabaseHelper(context);
        }
        return mHelper;
    }

    public SDKDatabaseHelper(Context context) {
        super(context, "xiaomi_market_sdk_update.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.d("MarketSDKDatabaseHelper", "create database");
        createDownloadTable(sQLiteDatabase);
    }

    public final void createDownloadTable(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE update_download (_id INTEGER PRIMARY KEY AUTOINCREMENT,package_name TEXT,download_id INTEGER, version_code INTEGER, apk_url TEXT, apk_hash TEXT, diff_url TEXT, diff_hash TEXT, apk_path TEXT, UNIQUE(package_name));");
    }

    public Cursor query(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5) {
        return getReadableDatabase().query(str, strArr, str2, strArr2, str3, str4, str5);
    }
}
