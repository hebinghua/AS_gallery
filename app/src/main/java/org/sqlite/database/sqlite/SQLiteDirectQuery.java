package org.sqlite.database.sqlite;

import android.os.CancellationSignal;
import android.util.Log;
import org.sqlite.database.sqlite.SQLiteConnection;

/* loaded from: classes3.dex */
public class SQLiteDirectQuery extends SQLiteProgram {
    public static final int[] SQLITE_TYPE_MAPPING = {3, 1, 2, 3, 4, 0};
    public final CancellationSignal mCancellationSignal;

    private static native byte[] nativeGetBlob(long j, int i);

    private static native double nativeGetDouble(long j, int i);

    private static native long nativeGetLong(long j, int i);

    private static native String nativeGetString(long j, int i);

    private static native int nativeGetType(long j, int i);

    private static native int nativeStep(long j, int i);

    public SQLiteDirectQuery(SQLiteDatabase sQLiteDatabase, String str, Object[] objArr, CancellationSignal cancellationSignal) {
        super(sQLiteDatabase, str, objArr, cancellationSignal);
        this.mCancellationSignal = cancellationSignal;
    }

    public long getLong(int i) {
        return nativeGetLong(this.mPreparedStatement.getPtr(), i);
    }

    public double getDouble(int i) {
        return nativeGetDouble(this.mPreparedStatement.getPtr(), i);
    }

    public String getString(int i) {
        return nativeGetString(this.mPreparedStatement.getPtr(), i);
    }

    public byte[] getBlob(int i) {
        return nativeGetBlob(this.mPreparedStatement.getPtr(), i);
    }

    public int getType(int i) {
        return SQLITE_TYPE_MAPPING[nativeGetType(this.mPreparedStatement.getPtr(), i)];
    }

    public int step(int i) {
        try {
            if (acquirePreparedStatement(false)) {
                this.mPreparedStatement.beginOperation("directQuery", getBindArgs());
                this.mPreparedStatement.attachCancellationSignal(this.mCancellationSignal);
            }
            return nativeStep(this.mPreparedStatement.getPtr(), i);
        } catch (RuntimeException e) {
            if (e instanceof SQLiteException) {
                Log.e("SQLiteDirectQuery", "Got exception on stepping: " + e.getMessage() + ", SQL: " + getSql());
                checkCorruption((SQLiteException) e);
            }
            SQLiteConnection.PreparedStatement preparedStatement = this.mPreparedStatement;
            if (preparedStatement != null) {
                preparedStatement.detachCancellationSignal(this.mCancellationSignal);
                this.mPreparedStatement.failOperation(e);
            }
            releasePreparedStatement();
            throw e;
        }
    }

    public synchronized void reset(boolean z) {
        SQLiteConnection.PreparedStatement preparedStatement = this.mPreparedStatement;
        if (preparedStatement != null) {
            preparedStatement.reset(false);
            if (z) {
                this.mPreparedStatement.detachCancellationSignal(this.mCancellationSignal);
                this.mPreparedStatement.endOperation(null);
                releasePreparedStatement();
            }
        }
    }

    @Override // org.sqlite.database.sqlite.SQLiteProgram, org.sqlite.database.sqlite.SQLiteClosable
    public void onAllReferencesReleased() {
        synchronized (this) {
            SQLiteConnection.PreparedStatement preparedStatement = this.mPreparedStatement;
            if (preparedStatement != null) {
                preparedStatement.detachCancellationSignal(this.mCancellationSignal);
                this.mPreparedStatement.endOperation(null);
            }
        }
        super.onAllReferencesReleased();
    }
}
