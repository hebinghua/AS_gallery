package org.sqlite.database.sqlite;

import android.database.Cursor;
import android.os.CancellationSignal;
import android.util.Log;
import org.sqlite.database.AbstractCursor;
import org.sqlite.database.sqlite.SQLiteDatabase;

/* loaded from: classes3.dex */
public class SQLiteDirectCursor extends AbstractCursor {
    public static final SQLiteDatabase.CursorFactory FACTORY = new SQLiteDatabase.CursorFactory() { // from class: org.sqlite.database.sqlite.SQLiteDirectCursor.1
        @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
        public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteProgram sQLiteProgram) {
            return new SQLiteDirectCursor(sQLiteCursorDriver, str, (SQLiteDirectQuery) sQLiteProgram);
        }

        @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
        public SQLiteProgram newQuery(SQLiteDatabase sQLiteDatabase, String str, Object[] objArr, CancellationSignal cancellationSignal) {
            return new SQLiteDirectQuery(sQLiteDatabase, str, objArr, cancellationSignal);
        }
    };
    public final String[] mColumns;
    public int mCount;
    public boolean mCountFinished;
    public final SQLiteCursorDriver mDriver;
    public final SQLiteDirectQuery mQuery;

    public SQLiteDirectCursor(SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteDirectQuery sQLiteDirectQuery) {
        if (sQLiteDirectQuery == null) {
            throw new IllegalArgumentException("query object cannot be null");
        }
        this.mQuery = sQLiteDirectQuery;
        this.mDriver = sQLiteCursorDriver;
        this.mColumns = sQLiteDirectQuery.getColumnNames();
        this.mCount = -1;
        this.mCountFinished = false;
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        super.close();
        this.mQuery.close();
        this.mDriver.cursorClosed();
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor
    public void deactivate() {
        super.deactivate();
        this.mDriver.cursorDeactivated();
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor
    public String[] getColumnNames() {
        return this.mColumns;
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor
    public String getString(int i) {
        return this.mQuery.getString(i);
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor
    public byte[] getBlob(int i) {
        return this.mQuery.getBlob(i);
    }

    @Override // android.database.Cursor
    public short getShort(int i) {
        return (short) this.mQuery.getLong(i);
    }

    @Override // android.database.Cursor
    public int getInt(int i) {
        return (int) this.mQuery.getLong(i);
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor
    public long getLong(int i) {
        return this.mQuery.getLong(i);
    }

    @Override // android.database.Cursor
    public float getFloat(int i) {
        return (float) this.mQuery.getDouble(i);
    }

    @Override // android.database.Cursor
    public double getDouble(int i) {
        return this.mQuery.getDouble(i);
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor
    public int getType(int i) {
        return this.mQuery.getType(i);
    }

    @Override // android.database.Cursor
    public boolean isNull(int i) {
        return getType(i) == 0;
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor
    public boolean moveToPosition(int i) {
        int step;
        int i2;
        if (i < 0) {
            this.mQuery.reset(false);
            this.mPos = -1;
            return false;
        } else if (this.mCountFinished && i >= (i2 = this.mCount)) {
            this.mPos = i2;
            return false;
        } else {
            int i3 = this.mPos;
            if (i < i3) {
                Log.w("SQLiteDirectCursor", "Moving backward on SQLiteDirectCursor is slow. Get rid of backward movement or use other implementations.");
                this.mQuery.reset(false);
                step = this.mQuery.step(i + 1) - 1;
            } else if (i == i3) {
                return true;
            } else {
                step = i3 + this.mQuery.step(i - i3);
            }
            if (step < i) {
                int i4 = step + 1;
                this.mCount = i4;
                this.mCountFinished = true;
                this.mPos = i4;
            } else {
                this.mPos = step;
                if (step >= this.mCount) {
                    this.mCount = step + 1;
                    this.mCountFinished = false;
                }
            }
            return this.mPos < this.mCount;
        }
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor
    public int getCount() {
        if (!this.mCountFinished) {
            Log.w("SQLiteDirectCursor", "Count query on SQLiteDirectCursor is slow. Iterate through the end to get count or use other implementations.");
            this.mCount = this.mPos + this.mQuery.step(Integer.MAX_VALUE) + 1;
            this.mCountFinished = true;
            this.mQuery.reset(false);
            this.mPos = this.mQuery.step(this.mPos + 1) - 1;
        }
        return this.mCount;
    }

    @Override // org.sqlite.database.AbstractCursor, android.database.Cursor
    public boolean requery() {
        if (isClosed()) {
            return false;
        }
        synchronized (this) {
            if (!this.mQuery.getDatabase().isOpen()) {
                return false;
            }
            this.mPos = -1;
            this.mCountFinished = false;
            this.mCount = -1;
            this.mDriver.cursorRequeried(this);
            this.mQuery.reset(false);
            try {
                return super.requery();
            } catch (IllegalStateException e) {
                Log.w("SQLiteDirectCursor", "requery() failed " + e.getMessage(), e);
                return false;
            }
        }
    }
}
