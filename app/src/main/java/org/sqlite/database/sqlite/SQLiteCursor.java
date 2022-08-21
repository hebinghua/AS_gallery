package org.sqlite.database.sqlite;

import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import org.sqlite.database.DatabaseUtils;
import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteDebug;

/* loaded from: classes3.dex */
public class SQLiteCursor extends AbstractWindowedCursor {
    public static final SQLiteDatabase.CursorFactory FACTORY = new SQLiteDatabase.CursorFactory() { // from class: org.sqlite.database.sqlite.SQLiteCursor.1
        @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
        public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteProgram sQLiteProgram) {
            return new SQLiteCursor(sQLiteCursorDriver, str, (SQLiteQuery) sQLiteProgram);
        }

        @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
        public SQLiteProgram newQuery(SQLiteDatabase sQLiteDatabase, String str, Object[] objArr, CancellationSignal cancellationSignal) {
            return new SQLiteQuery(sQLiteDatabase, str, objArr, cancellationSignal);
        }
    };
    public Map<String, Integer> mColumnNameMap;
    public final String[] mColumns;
    public int mCount = -1;
    public int mCursorWindowCapacity;
    public final SQLiteCursorDriver mDriver;
    public final String mEditTable;
    public boolean mFillWindowForwardOnly;
    public final SQLiteQuery mQuery;
    public int mWindowSizeBytes;

    public SQLiteCursor(SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
        if (sQLiteQuery == null) {
            throw new IllegalArgumentException("query object cannot be null");
        }
        this.mDriver = sQLiteCursorDriver;
        this.mEditTable = str;
        this.mColumnNameMap = null;
        this.mQuery = sQLiteQuery;
        this.mColumns = sQLiteQuery.getColumnNames();
    }

    public SQLiteDatabase getDatabase() {
        return this.mQuery.getDatabase();
    }

    @Override // android.database.AbstractCursor, android.database.CrossProcessCursor
    public boolean onMove(int i, int i2) {
        CursorWindow cursorWindow = ((AbstractWindowedCursor) this).mWindow;
        if (cursorWindow == null || i2 < cursorWindow.getStartPosition() || i2 >= ((AbstractWindowedCursor) this).mWindow.getStartPosition() + ((AbstractWindowedCursor) this).mWindow.getNumRows()) {
            fillWindow(i2);
            int startPosition = ((AbstractWindowedCursor) this).mWindow.getStartPosition() + ((AbstractWindowedCursor) this).mWindow.getNumRows();
            if (startPosition > i2) {
                return true;
            }
            this.mCount = startPosition;
            return true;
        }
        return true;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getCount() {
        if (this.mCount == -1) {
            fillWindow(0);
        }
        return this.mCount;
    }

    public final void awc_clearOrCreateWindow(String str) {
        CursorWindow cursorWindow;
        CursorWindow window = getWindow();
        if (window == null) {
            if (Build.VERSION.SDK_INT >= 28 && this.mWindowSizeBytes > 0) {
                cursorWindow = new CursorWindow(str, this.mWindowSizeBytes);
            } else {
                cursorWindow = new CursorWindow(str);
            }
            setWindow(cursorWindow);
            return;
        }
        window.clear();
    }

    public final void awc_closeWindow() {
        setWindow(null);
    }

    public final void fillWindow(int i) {
        awc_clearOrCreateWindow(getDatabase().getPath());
        try {
            if (this.mCount == -1) {
                this.mCount = this.mQuery.fillWindow(((AbstractWindowedCursor) this).mWindow, i, i, true);
                this.mCursorWindowCapacity = ((AbstractWindowedCursor) this).mWindow.getNumRows();
                if (!SQLiteDebug.NoPreloadHolder.DEBUG_SQL_LOG) {
                    return;
                }
                Log.d("SQLiteCursor", "received count(*) from native_fill_window: " + this.mCount);
                return;
            }
            this.mQuery.fillWindow(((AbstractWindowedCursor) this).mWindow, this.mFillWindowForwardOnly ? i : DatabaseUtils.cursorPickFillWindowStartPosition(i, this.mCursorWindowCapacity), i, false);
        } catch (RuntimeException e) {
            awc_closeWindow();
            throw e;
        }
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getColumnIndex(String str) {
        if (this.mColumnNameMap == null) {
            String[] strArr = this.mColumns;
            int length = strArr.length;
            HashMap hashMap = new HashMap(length, 1.0f);
            for (int i = 0; i < length; i++) {
                hashMap.put(strArr[i], Integer.valueOf(i));
            }
            this.mColumnNameMap = hashMap;
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf != -1) {
            Exception exc = new Exception();
            Log.e("SQLiteCursor", "requesting column name with table name -- " + str, exc);
            str = str.substring(lastIndexOf + 1);
        }
        Integer num = this.mColumnNameMap.get(str);
        if (num != null) {
            return num.intValue();
        }
        return -1;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String[] getColumnNames() {
        return this.mColumns;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void deactivate() {
        super.deactivate();
        this.mDriver.cursorDeactivated();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        super.close();
        synchronized (this) {
            this.mQuery.close();
            this.mDriver.cursorClosed();
        }
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean requery() {
        if (isClosed()) {
            return false;
        }
        synchronized (this) {
            if (!this.mQuery.getDatabase().isOpen()) {
                return false;
            }
            CursorWindow cursorWindow = ((AbstractWindowedCursor) this).mWindow;
            if (cursorWindow != null) {
                cursorWindow.clear();
            }
            ((AbstractWindowedCursor) this).mPos = -1;
            this.mCount = -1;
            this.mDriver.cursorRequeried(this);
            try {
                return super.requery();
            } catch (IllegalStateException e) {
                Log.w("SQLiteCursor", "requery() failed " + e.getMessage(), e);
                return false;
            }
        }
    }

    @Override // android.database.AbstractWindowedCursor
    public void setWindow(CursorWindow cursorWindow) {
        super.setWindow(cursorWindow);
        this.mCount = -1;
    }

    public void setFillWindowForwardOnly(boolean z) {
        this.mFillWindowForwardOnly = z;
    }

    public void setWindowSizeBytes(int i) {
        this.mWindowSizeBytes = i;
    }

    @Override // android.database.AbstractCursor
    public void finalize() {
        try {
            if (((AbstractWindowedCursor) this).mWindow != null) {
                close();
            }
        } finally {
            super.finalize();
        }
    }
}
