package org.sqlite.database.sqlite;

import android.database.CursorWindow;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.CancellationSignal;
import org.sqlite.database.sqlite.SQLiteConnection;
import org.sqlite.database.trace.TraceUtil;

/* loaded from: classes3.dex */
public final class SQLiteSession {
    public SQLiteConnection mConnection;
    public int mConnectionFlags;
    public final SQLiteConnectionPool mConnectionPool;
    public int mConnectionUseCount;
    public Transaction mTransactionPool;
    public Transaction mTransactionStack;

    public SQLiteSession(SQLiteConnectionPool sQLiteConnectionPool) {
        if (sQLiteConnectionPool == null) {
            throw new IllegalArgumentException("connectionPool must not be null");
        }
        this.mConnectionPool = sQLiteConnectionPool;
    }

    public boolean hasTransaction() {
        return this.mTransactionStack != null;
    }

    public void beginTransaction(int i, SQLiteTransactionListener sQLiteTransactionListener, int i2, CancellationSignal cancellationSignal) {
        throwIfTransactionMarkedSuccessful();
        beginTransactionUnchecked(i, sQLiteTransactionListener, i2, cancellationSignal);
    }

    /* JADX WARN: Finally extract failed */
    public final void beginTransactionUnchecked(int i, SQLiteTransactionListener sQLiteTransactionListener, int i2, CancellationSignal cancellationSignal) {
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
        }
        if (this.mTransactionStack == null) {
            acquireConnection(null, i2, cancellationSignal);
        }
        TraceUtil.trackBegin("beginTransactionUnchecked");
        try {
            if (this.mTransactionStack == null) {
                if (i == 1) {
                    this.mConnection.execute("BEGIN IMMEDIATE;", null, cancellationSignal);
                } else if (i == 2) {
                    this.mConnection.execute("BEGIN EXCLUSIVE;", null, cancellationSignal);
                } else {
                    this.mConnection.execute("BEGIN;", null, cancellationSignal);
                }
            }
            if (sQLiteTransactionListener != null) {
                try {
                    sQLiteTransactionListener.onBegin();
                } catch (RuntimeException e) {
                    if (this.mTransactionStack == null) {
                        this.mConnection.execute("ROLLBACK;", null, cancellationSignal);
                    }
                    throw e;
                }
            }
            Transaction obtainTransaction = obtainTransaction(i, sQLiteTransactionListener);
            obtainTransaction.mParent = this.mTransactionStack;
            this.mTransactionStack = obtainTransaction;
        } catch (Throwable th) {
            if (this.mTransactionStack == null) {
                releaseConnection();
            }
            throw th;
        }
    }

    public void setTransactionSuccessful() {
        throwIfNoTransaction();
        throwIfTransactionMarkedSuccessful();
        this.mTransactionStack.mMarkedSuccessful = true;
    }

    public void endTransaction(CancellationSignal cancellationSignal) {
        throwIfNoTransaction();
        endTransactionUnchecked(cancellationSignal, false);
    }

    public final void endTransactionUnchecked(CancellationSignal cancellationSignal, boolean z) {
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
        }
        TraceUtil.trackEnd();
        Transaction transaction = this.mTransactionStack;
        boolean z2 = false;
        boolean z3 = (transaction.mMarkedSuccessful || z) && !transaction.mChildFailed;
        SQLiteTransactionListener sQLiteTransactionListener = transaction.mListener;
        if (sQLiteTransactionListener != null) {
            try {
                if (z3) {
                    sQLiteTransactionListener.onCommit();
                } else {
                    sQLiteTransactionListener.onRollback();
                }
            } catch (RuntimeException e) {
                e = e;
            }
        }
        z2 = z3;
        e = null;
        this.mTransactionStack = transaction.mParent;
        recycleTransaction(transaction);
        Transaction transaction2 = this.mTransactionStack;
        if (transaction2 == null) {
            try {
                if (z2) {
                    this.mConnection.execute("COMMIT;", null, cancellationSignal);
                } else {
                    this.mConnection.execute("ROLLBACK;", null, cancellationSignal);
                }
            } finally {
                releaseConnection();
            }
        } else if (!z2) {
            transaction2.mChildFailed = true;
        }
        if (e == null) {
            return;
        }
        throw e;
    }

    public void prepare(String str, int i, CancellationSignal cancellationSignal, SQLiteStatementInfo sQLiteStatementInfo) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
        }
        acquireConnection(str, i, cancellationSignal);
        TraceUtil.trackBegin("prepare: " + str);
        try {
            this.mConnection.prepare(str, sQLiteStatementInfo);
        } finally {
            TraceUtil.trackEnd();
            releaseConnection();
        }
    }

    public void execute(String str, Object[] objArr, int i, CancellationSignal cancellationSignal) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        if (executeSpecial(str, objArr, i, cancellationSignal)) {
            return;
        }
        acquireConnection(str, i, cancellationSignal);
        TraceUtil.trackBegin("execute: " + str);
        try {
            this.mConnection.execute(str, objArr, cancellationSignal);
        } finally {
            TraceUtil.trackEnd();
            releaseConnection();
        }
    }

    public long executeForLong(String str, Object[] objArr, int i, CancellationSignal cancellationSignal) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        if (executeSpecial(str, objArr, i, cancellationSignal)) {
            return 0L;
        }
        acquireConnection(str, i, cancellationSignal);
        TraceUtil.trackBegin("executeForLong: " + str);
        try {
            return this.mConnection.executeForLong(str, objArr, cancellationSignal);
        } finally {
            TraceUtil.trackEnd();
            releaseConnection();
        }
    }

    public int executeForChangedRowCount(String str, Object[] objArr, int i, CancellationSignal cancellationSignal) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        if (executeSpecial(str, objArr, i, cancellationSignal)) {
            return 0;
        }
        acquireConnection(str, i, cancellationSignal);
        TraceUtil.trackBegin("executeForChangedRowCount: " + str);
        try {
            return this.mConnection.executeForChangedRowCount(str, objArr, cancellationSignal);
        } finally {
            TraceUtil.trackEnd();
            releaseConnection();
        }
    }

    public long executeForLastInsertedRowId(String str, Object[] objArr, int i, CancellationSignal cancellationSignal) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        if (executeSpecial(str, objArr, i, cancellationSignal)) {
            return 0L;
        }
        acquireConnection(str, i, cancellationSignal);
        TraceUtil.trackBegin("executeForLastInsertedRowId: " + str);
        try {
            return this.mConnection.executeForLastInsertedRowId(str, objArr, cancellationSignal);
        } finally {
            TraceUtil.trackEnd();
            releaseConnection();
        }
    }

    public int executeForCursorWindow(String str, Object[] objArr, CursorWindow cursorWindow, int i, int i2, boolean z, int i3, CancellationSignal cancellationSignal) {
        if (str != null) {
            if (cursorWindow == null) {
                throw new IllegalArgumentException("window must not be null.");
            }
            if (executeSpecial(str, objArr, i3, cancellationSignal)) {
                cursorWindow.clear();
                return 0;
            }
            acquireConnection(str, i3, cancellationSignal);
            TraceUtil.trackBegin("executeForCursorWindow: " + str);
            try {
                return this.mConnection.executeForCursorWindow(str, objArr, cursorWindow, i, i2, z, cancellationSignal);
            } finally {
                TraceUtil.trackEnd();
                releaseConnection();
            }
        }
        throw new IllegalArgumentException("sql must not be null.");
    }

    public final boolean executeSpecial(String str, Object[] objArr, int i, CancellationSignal cancellationSignal) {
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
        }
        int sqlStatementType = DatabaseUtils.getSqlStatementType(str);
        if (sqlStatementType == 4) {
            beginTransaction(2, null, i, cancellationSignal);
            return true;
        } else if (sqlStatementType == 5) {
            setTransactionSuccessful();
            endTransaction(cancellationSignal);
            return true;
        } else if (sqlStatementType != 6) {
            return false;
        } else {
            endTransaction(cancellationSignal);
            return true;
        }
    }

    public final void acquireConnection(String str, int i, CancellationSignal cancellationSignal) {
        if (this.mConnection == null) {
            this.mConnection = this.mConnectionPool.acquireConnection(str, i, cancellationSignal);
            this.mConnectionFlags = i;
        }
        this.mConnectionUseCount++;
    }

    public final void releaseConnection() {
        int i = this.mConnectionUseCount - 1;
        this.mConnectionUseCount = i;
        if (i == 0) {
            try {
                this.mConnectionPool.releaseConnection(this.mConnection);
            } finally {
                this.mConnection = null;
            }
        }
    }

    public SQLiteConnection.PreparedStatement acquirePreparedStatement(String str, int i, boolean z) {
        acquireConnection(str, i, null);
        return this.mConnection.acquirePreparedStatement(str);
    }

    public void releasePreparedStatement(SQLiteConnection.PreparedStatement preparedStatement) {
        SQLiteConnection sQLiteConnection = this.mConnection;
        if (sQLiteConnection != null) {
            sQLiteConnection.releasePreparedStatement(preparedStatement);
            releaseConnection();
        }
    }

    public final void throwIfNoTransaction() {
        if (this.mTransactionStack != null) {
            return;
        }
        throw new IllegalStateException("Cannot perform this operation because there is no current transaction.");
    }

    public final void throwIfTransactionMarkedSuccessful() {
        Transaction transaction = this.mTransactionStack;
        if (transaction == null || !transaction.mMarkedSuccessful) {
            return;
        }
        throw new IllegalStateException("Cannot perform this operation because the transaction has already been marked successful.  The only thing you can do now is call endTransaction().");
    }

    public final Transaction obtainTransaction(int i, SQLiteTransactionListener sQLiteTransactionListener) {
        Transaction transaction = this.mTransactionPool;
        if (transaction != null) {
            this.mTransactionPool = transaction.mParent;
            transaction.mParent = null;
            transaction.mMarkedSuccessful = false;
            transaction.mChildFailed = false;
        } else {
            transaction = new Transaction();
        }
        transaction.mMode = i;
        transaction.mListener = sQLiteTransactionListener;
        return transaction;
    }

    public final void recycleTransaction(Transaction transaction) {
        transaction.mParent = this.mTransactionPool;
        transaction.mListener = null;
        this.mTransactionPool = transaction;
    }

    /* loaded from: classes3.dex */
    public static final class Transaction {
        public boolean mChildFailed;
        public SQLiteTransactionListener mListener;
        public boolean mMarkedSuccessful;
        public int mMode;
        public Transaction mParent;

        public Transaction() {
        }
    }
}
