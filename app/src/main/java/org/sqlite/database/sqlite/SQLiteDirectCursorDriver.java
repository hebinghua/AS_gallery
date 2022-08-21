package org.sqlite.database.sqlite;

import android.database.Cursor;
import android.os.CancellationSignal;
import org.sqlite.database.sqlite.SQLiteDatabase;

/* loaded from: classes3.dex */
public final class SQLiteDirectCursorDriver implements SQLiteCursorDriver {
    public final CancellationSignal mCancellationSignal;
    public final SQLiteDatabase mDatabase;
    public final String mEditTable;
    public SQLiteProgram mQuery;
    public final String mSql;

    @Override // org.sqlite.database.sqlite.SQLiteCursorDriver
    public void cursorClosed() {
    }

    @Override // org.sqlite.database.sqlite.SQLiteCursorDriver
    public void cursorDeactivated() {
    }

    @Override // org.sqlite.database.sqlite.SQLiteCursorDriver
    public void cursorRequeried(Cursor cursor) {
    }

    public SQLiteDirectCursorDriver(SQLiteDatabase sQLiteDatabase, String str, String str2, CancellationSignal cancellationSignal) {
        this.mDatabase = sQLiteDatabase;
        this.mEditTable = str2;
        this.mSql = str;
        this.mCancellationSignal = cancellationSignal;
    }

    @Override // org.sqlite.database.sqlite.SQLiteCursorDriver
    public Cursor query(SQLiteDatabase.CursorFactory cursorFactory, String[] strArr) {
        Cursor newCursor;
        SQLiteProgram sQLiteProgram = null;
        try {
            if (cursorFactory == null) {
                SQLiteDatabase.CursorFactory cursorFactory2 = SQLiteCursor.FACTORY;
                sQLiteProgram = cursorFactory2.newQuery(this.mDatabase, this.mSql, strArr, this.mCancellationSignal);
                newCursor = cursorFactory2.newCursor(this.mDatabase, this, this.mEditTable, sQLiteProgram);
            } else {
                sQLiteProgram = cursorFactory.newQuery(this.mDatabase, this.mSql, strArr, this.mCancellationSignal);
                newCursor = cursorFactory.newCursor(this.mDatabase, this, this.mEditTable, sQLiteProgram);
            }
            this.mQuery = sQLiteProgram;
            return newCursor;
        } catch (RuntimeException e) {
            if (sQLiteProgram != null) {
                sQLiteProgram.close();
            }
            throw e;
        }
    }

    public String toString() {
        return "SQLiteDirectCursorDriver: " + this.mSql;
    }
}
