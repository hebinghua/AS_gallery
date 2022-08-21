package com.miui.gallery.dao.base;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

/* loaded from: classes.dex */
public class EntityTransaction {
    public SupportSQLiteDatabase db;

    public EntityTransaction(SupportSQLiteOpenHelper supportSQLiteOpenHelper) {
        this.db = supportSQLiteOpenHelper.getWritableDatabase();
    }

    public void begin() {
        this.db.beginTransaction();
    }

    public void commit() {
        this.db.setTransactionSuccessful();
    }

    public void end() {
        this.db.endTransaction();
        this.db = null;
    }
}
