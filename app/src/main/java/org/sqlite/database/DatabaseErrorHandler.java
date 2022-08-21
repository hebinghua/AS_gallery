package org.sqlite.database;

import org.sqlite.database.sqlite.SQLiteDatabase;

/* loaded from: classes3.dex */
public interface DatabaseErrorHandler {
    void onCorruption(SQLiteDatabase sQLiteDatabase);
}
