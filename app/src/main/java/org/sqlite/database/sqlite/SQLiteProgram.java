package org.sqlite.database.sqlite;

import android.database.DatabaseUtils;
import android.os.CancellationSignal;
import java.util.Arrays;
import org.sqlite.database.sqlite.SQLiteConnection;

/* loaded from: classes3.dex */
public abstract class SQLiteProgram extends SQLiteClosable {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public final Object[] mBindArgs;
    public SQLiteSession mBoundSession;
    public final String[] mColumnNames;
    public final SQLiteDatabase mDatabase;
    public final int mNumParameters;
    public SQLiteConnection.PreparedStatement mPreparedStatement;
    public final boolean mReadOnly;
    public final String mSql;

    public SQLiteProgram(SQLiteDatabase sQLiteDatabase, String str, Object[] objArr, CancellationSignal cancellationSignal) {
        this.mDatabase = sQLiteDatabase;
        String trim = str.trim();
        this.mSql = trim;
        int sqlStatementType = DatabaseUtils.getSqlStatementType(trim);
        if (sqlStatementType == 4 || sqlStatementType == 5 || sqlStatementType == 6) {
            this.mReadOnly = false;
            this.mColumnNames = EMPTY_STRING_ARRAY;
            this.mNumParameters = 0;
        } else {
            boolean z = sqlStatementType != 1 ? false : true;
            SQLiteStatementInfo sQLiteStatementInfo = new SQLiteStatementInfo();
            sQLiteDatabase.getThreadSession().prepare(trim, sQLiteDatabase.getThreadDefaultConnectionFlags(z), cancellationSignal, sQLiteStatementInfo);
            this.mReadOnly = sQLiteStatementInfo.readOnly;
            this.mColumnNames = sQLiteStatementInfo.columnNames;
            this.mNumParameters = sQLiteStatementInfo.numParameters;
        }
        if (objArr != null && objArr.length > this.mNumParameters) {
            throw new IllegalArgumentException("Too many bind arguments.  " + objArr.length + " arguments were provided but the statement needs " + this.mNumParameters + " arguments.");
        }
        int i = this.mNumParameters;
        if (i != 0) {
            Object[] objArr2 = new Object[i];
            this.mBindArgs = objArr2;
            if (objArr != null) {
                System.arraycopy(objArr, 0, objArr2, 0, objArr.length);
            }
        } else {
            this.mBindArgs = null;
        }
        this.mPreparedStatement = null;
        this.mBoundSession = null;
    }

    public final SQLiteDatabase getDatabase() {
        return this.mDatabase;
    }

    public final String getSql() {
        return this.mSql;
    }

    public final Object[] getBindArgs() {
        return this.mBindArgs;
    }

    public final String[] getColumnNames() {
        return this.mColumnNames;
    }

    public final SQLiteSession getSession() {
        return this.mDatabase.getThreadSession();
    }

    public final int getConnectionFlags() {
        return this.mDatabase.getThreadDefaultConnectionFlags(this.mReadOnly);
    }

    public final void onCorruption() {
        this.mDatabase.onCorruption();
    }

    public final void checkCorruption(SQLiteException sQLiteException) {
        boolean z = true;
        if (!(sQLiteException instanceof SQLiteDatabaseCorruptException) && (!(sQLiteException instanceof SQLiteFullException) || !this.mReadOnly)) {
            z = false;
        }
        if (z) {
            this.mDatabase.onCorruption();
        }
    }

    public void bindNull(int i) {
        bind(i, null);
    }

    public void bindLong(int i, long j) {
        bind(i, Long.valueOf(j));
    }

    public void bindDouble(int i, double d) {
        bind(i, Double.valueOf(d));
    }

    public void bindString(int i, String str) {
        if (str == null) {
            throw new IllegalArgumentException("the bind value at index " + i + " is null");
        }
        bind(i, str);
    }

    public void bindBlob(int i, byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("the bind value at index " + i + " is null");
        }
        bind(i, bArr);
    }

    public void clearBindings() {
        Object[] objArr = this.mBindArgs;
        if (objArr != null) {
            Arrays.fill(objArr, (Object) null);
        }
    }

    public void bindAllArgsAsStrings(String[] strArr) {
        if (strArr != null) {
            for (int length = strArr.length; length != 0; length--) {
                bindString(length, strArr[length - 1]);
            }
        }
    }

    @Override // org.sqlite.database.sqlite.SQLiteClosable
    public void onAllReferencesReleased() {
        releasePreparedStatement();
        clearBindings();
    }

    public final void bind(int i, Object obj) {
        if (i < 1 || i > this.mNumParameters) {
            throw new IllegalArgumentException("Cannot bind argument at index " + i + " because the index is out of range.  The statement has " + this.mNumParameters + " parameters.");
        }
        this.mBindArgs[i - 1] = obj;
    }

    public synchronized boolean acquirePreparedStatement(boolean z) {
        SQLiteSession threadSession = this.mDatabase.getThreadSession();
        SQLiteSession sQLiteSession = this.mBoundSession;
        if (threadSession == sQLiteSession) {
            return false;
        }
        if (sQLiteSession != null) {
            throw new IllegalStateException("SQLiteProgram has bound to another thread.");
        }
        SQLiteConnection.PreparedStatement acquirePreparedStatement = threadSession.acquirePreparedStatement(this.mSql, this.mDatabase.getThreadDefaultConnectionFlags(this.mReadOnly), z);
        this.mPreparedStatement = acquirePreparedStatement;
        acquirePreparedStatement.bindArguments(this.mBindArgs);
        this.mBoundSession = threadSession;
        return true;
    }

    public synchronized void releasePreparedStatement() {
        SQLiteSession sQLiteSession = this.mBoundSession;
        if (sQLiteSession == null && this.mPreparedStatement == null) {
            return;
        }
        if (sQLiteSession == null || this.mPreparedStatement == null) {
            throw new IllegalStateException("Internal state error.");
        }
        if (sQLiteSession != this.mDatabase.getThreadSession()) {
            throw new IllegalStateException("SQLiteProgram has bound to another thread.");
        }
        this.mBoundSession.releasePreparedStatement(this.mPreparedStatement);
        this.mPreparedStatement = null;
        this.mBoundSession = null;
    }

    public void finalize() throws Throwable {
        synchronized (this) {
            if (this.mBoundSession != null || this.mPreparedStatement != null) {
                throw new SQLiteMisuseException("Acquired prepared statement is not released.");
            }
        }
        super.finalize();
    }
}
