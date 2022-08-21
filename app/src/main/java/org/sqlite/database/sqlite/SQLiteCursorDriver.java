package org.sqlite.database.sqlite;

import android.database.Cursor;
import org.sqlite.database.sqlite.SQLiteDatabase;

/* loaded from: classes3.dex */
public interface SQLiteCursorDriver {
    void cursorClosed();

    void cursorDeactivated();

    void cursorRequeried(Cursor cursor);

    Cursor query(SQLiteDatabase.CursorFactory cursorFactory, String[] strArr);
}
