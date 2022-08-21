package org.sqlite.database.sqlite;

import android.database.CursorWindow;
import android.os.CancellationSignal;
import android.os.SystemClock;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;
import android.util.Printer;
import ch.qos.logback.core.CoreConstants;
import com.xiaomi.stat.d;
import java.io.File;
import java.lang.ref.WeakReference;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import org.sqlite.database.DatabaseUtils;
import org.sqlite.database.sqlite.SQLiteDebug;

/* loaded from: classes3.dex */
public final class SQLiteConnection implements CancellationSignal.OnCancelListener {
    public int mCancellationSignalAttachCount;
    public final CloseGuard mCloseGuard;
    public final SQLiteDatabaseConfiguration mConfiguration;
    public final int mConnectionId;
    public long mConnectionPtr;
    public final boolean mIsPrimaryConnection;
    public final boolean mIsReadOnlyConnection;
    public boolean mOnlyAllowReadOnlyOperations;
    public final SQLiteConnectionPool mPool;
    public final PreparedStatementCache mPreparedStatementCache;
    public PreparedStatement mPreparedStatementPool;
    public final OperationLog mRecentOperations;
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static boolean isCacheable(int i) {
        return i == 2 || i == 1;
    }

    private static native void nativeBindBlob(long j, long j2, int i, byte[] bArr);

    private static native void nativeBindDouble(long j, long j2, int i, double d);

    private static native void nativeBindLong(long j, long j2, int i, long j3);

    private static native void nativeBindNull(long j, long j2, int i);

    private static native void nativeBindString(long j, long j2, int i, String str);

    private static native void nativeCancel(long j);

    private static native void nativeClose(long j);

    private static native void nativeExecute(long j, long j2);

    private static native int nativeExecuteForBlobFileDescriptor(long j, long j2);

    private static native int nativeExecuteForChangedRowCount(long j, long j2);

    private static native long nativeExecuteForCursorWindow(long j, long j2, CursorWindow cursorWindow, int i, int i2, boolean z);

    private static native long nativeExecuteForLastInsertedRowId(long j, long j2);

    private static native long nativeExecuteForLong(long j, long j2);

    private static native String nativeExecuteForString(long j, long j2);

    private static native void nativeFinalizeStatement(long j, long j2);

    private static native int nativeGetColumnCount(long j, long j2);

    private static native String nativeGetColumnName(long j, long j2, int i);

    private static native int nativeGetDbLookaside(long j);

    private static native int nativeGetParameterCount(long j, long j2);

    private static native boolean nativeHasCodec();

    private static native boolean nativeIsReadOnly(long j, long j2);

    private static native long nativeOpen(String str, int i, String str2, boolean z, boolean z2, int i2, int i3);

    private static native long nativePrepareStatement(long j, String str);

    private static native void nativeRegisterCustomFunction(long j, SQLiteCustomFunction sQLiteCustomFunction);

    private static native void nativeRegisterLocalizedCollators(long j, String str);

    private static native void nativeResetCancel(long j, boolean z);

    private static native void nativeResetStatement(long j, long j2, boolean z);

    private static native void nativeResetStatementAndClearBindings(long j, long j2);

    public final void applyBlockGuardPolicy(PreparedStatement preparedStatement) {
    }

    public static boolean hasCodec() {
        return nativeHasCodec();
    }

    public SQLiteConnection(SQLiteConnectionPool sQLiteConnectionPool, SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration, int i, boolean z) {
        CloseGuard closeGuard = CloseGuard.get();
        this.mCloseGuard = closeGuard;
        this.mPool = sQLiteConnectionPool;
        this.mRecentOperations = new OperationLog(sQLiteConnectionPool);
        SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration2 = new SQLiteDatabaseConfiguration(sQLiteDatabaseConfiguration);
        this.mConfiguration = sQLiteDatabaseConfiguration2;
        this.mConnectionId = i;
        this.mIsPrimaryConnection = z;
        this.mIsReadOnlyConnection = (sQLiteDatabaseConfiguration.openFlags & 1) == 0 ? false : true;
        this.mPreparedStatementCache = new PreparedStatementCache(sQLiteDatabaseConfiguration2.maxSqlCacheSize);
        closeGuard.open("close");
    }

    public void finalize() throws Throwable {
        try {
            SQLiteConnectionPool sQLiteConnectionPool = this.mPool;
            if (sQLiteConnectionPool != null && this.mConnectionPtr != 0) {
                sQLiteConnectionPool.onConnectionLeaked();
            }
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    public static SQLiteConnection open(SQLiteConnectionPool sQLiteConnectionPool, SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration, int i, boolean z) {
        SQLiteConnection sQLiteConnection = new SQLiteConnection(sQLiteConnectionPool, sQLiteDatabaseConfiguration, i, z);
        try {
            sQLiteConnection.open();
            return sQLiteConnection;
        } catch (SQLiteException e) {
            sQLiteConnection.dispose(false);
            throw e;
        }
    }

    public void close() {
        dispose(false);
    }

    public final void open() {
        String str = this.mConfiguration.path;
        int beginOperation = this.mRecentOperations.beginOperation("open", null, null);
        try {
            try {
                SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = this.mConfiguration;
                this.mConnectionPtr = nativeOpen(str, sQLiteDatabaseConfiguration.openFlags, sQLiteDatabaseConfiguration.label, SQLiteDebug.NoPreloadHolder.DEBUG_SQL_STATEMENTS, SQLiteDebug.NoPreloadHolder.DEBUG_SQL_TIME, sQLiteDatabaseConfiguration.lookasideSlotSize, sQLiteDatabaseConfiguration.lookasideSlotCount);
                this.mRecentOperations.endOperation(beginOperation);
                setPageSize();
                setForeignKeyModeFromConfiguration();
                setJournalSizeLimit();
                setAutoCheckpointInterval();
                if (!nativeHasCodec()) {
                    setWalModeFromConfiguration();
                    setLocaleFromConfiguration();
                }
                int size = this.mConfiguration.customFunctions.size();
                for (int i = 0; i < size; i++) {
                    nativeRegisterCustomFunction(this.mConnectionPtr, this.mConfiguration.customFunctions.get(i));
                }
            } catch (SQLiteCantOpenDatabaseException e) {
                StringBuilder sb = new StringBuilder("Cannot open database '");
                sb.append(str);
                sb.append(CoreConstants.SINGLE_QUOTE_CHAR);
                Path path = FileSystems.getDefault().getPath(str, new String[0]);
                Path parent = path.getParent();
                if (!Files.isDirectory(parent, new LinkOption[0])) {
                    sb.append(": Directory ");
                    sb.append(parent);
                    sb.append(" doesn't exist");
                } else if (!Files.exists(path, new LinkOption[0])) {
                    sb.append(": File ");
                    sb.append(path);
                    sb.append(" doesn't exist");
                } else if (!Files.isReadable(path)) {
                    sb.append(": File ");
                    sb.append(path);
                    sb.append(" is not readable");
                } else if (Files.isDirectory(path, new LinkOption[0])) {
                    sb.append(": Path ");
                    sb.append(path);
                    sb.append(" is a directory");
                } else {
                    sb.append(": Unknown reason");
                }
                throw new SQLiteCantOpenDatabaseException(sb.toString(), e);
            }
        } catch (Throwable th) {
            this.mRecentOperations.endOperation(beginOperation);
            throw th;
        }
    }

    public final void dispose(boolean z) {
        CloseGuard closeGuard = this.mCloseGuard;
        if (closeGuard != null) {
            if (z) {
                closeGuard.warnIfOpen();
            }
            this.mCloseGuard.close();
        }
        if (this.mConnectionPtr != 0) {
            int beginOperation = this.mRecentOperations.beginOperation("close", null, null);
            try {
                this.mPreparedStatementCache.evictAll();
                nativeClose(this.mConnectionPtr);
                this.mConnectionPtr = 0L;
            } finally {
                this.mRecentOperations.endOperation(beginOperation);
            }
        }
    }

    public final void setPageSize() {
        if (this.mConfiguration.isInMemoryDb() || this.mIsReadOnlyConnection) {
            return;
        }
        long defaultPageSize = SQLiteGlobal.getDefaultPageSize();
        if (executeForLong("PRAGMA page_size", null, null) == defaultPageSize) {
            return;
        }
        execute("PRAGMA page_size=" + defaultPageSize, null, null);
    }

    public final void setAutoCheckpointInterval() {
        if (this.mConfiguration.isInMemoryDb() || this.mIsReadOnlyConnection) {
            return;
        }
        long wALAutoCheckpoint = SQLiteGlobal.getWALAutoCheckpoint();
        if (executeForLong("PRAGMA wal_autocheckpoint", null, null) == wALAutoCheckpoint) {
            return;
        }
        executeForLong("PRAGMA wal_autocheckpoint=" + wALAutoCheckpoint, null, null);
    }

    public final void setJournalSizeLimit() {
        if (this.mConfiguration.isInMemoryDb() || this.mIsReadOnlyConnection) {
            return;
        }
        long journalSizeLimit = SQLiteGlobal.getJournalSizeLimit();
        if (executeForLong("PRAGMA journal_size_limit", null, null) == journalSizeLimit) {
            return;
        }
        executeForLong("PRAGMA journal_size_limit=" + journalSizeLimit, null, null);
    }

    public final void setForeignKeyModeFromConfiguration() {
        if (!this.mIsReadOnlyConnection) {
            long j = this.mConfiguration.foreignKeyConstraintsEnabled ? 1L : 0L;
            if (executeForLong("PRAGMA foreign_keys", null, null) == j) {
                return;
            }
            execute("PRAGMA foreign_keys=" + j, null, null);
        }
    }

    public final void setWalModeFromConfiguration() {
        if (this.mConfiguration.isInMemoryDb() || this.mIsReadOnlyConnection) {
            return;
        }
        SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = this.mConfiguration;
        if ((sQLiteDatabaseConfiguration.openFlags & 536870912) != 0) {
            setJournalMode("WAL");
            String str = this.mConfiguration.syncMode;
            if (str != null) {
                setSyncMode(str);
            } else {
                setSyncMode(SQLiteGlobal.getWALSyncMode());
            }
            maybeTruncateWalFile();
            return;
        }
        String str2 = sQLiteDatabaseConfiguration.journalMode;
        if (str2 == null) {
            str2 = SQLiteGlobal.getDefaultJournalMode();
        }
        setJournalMode(str2);
        String str3 = this.mConfiguration.syncMode;
        if (str3 == null) {
            str3 = SQLiteGlobal.getDefaultSyncMode();
        }
        setSyncMode(str3);
    }

    public final void maybeTruncateWalFile() {
        long wALTruncateSize = SQLiteGlobal.getWALTruncateSize();
        if (wALTruncateSize == 0) {
            return;
        }
        File file = new File(this.mConfiguration.path + "-wal");
        if (!file.isFile()) {
            return;
        }
        long length = file.length();
        if (length < wALTruncateSize) {
            return;
        }
        Log.i("SQLiteConnection", file.getAbsolutePath() + " " + length + " bytes: Bigger than " + wALTruncateSize + "; truncating");
        try {
            executeForString("PRAGMA wal_checkpoint(TRUNCATE)", null, null);
        } catch (SQLiteException e) {
            Log.w("SQLiteConnection", "Failed to truncate the -wal file", e);
        }
    }

    public final void setSyncMode(String str) {
        if (!canonicalizeSyncMode(executeForString("PRAGMA synchronous", null, null)).equalsIgnoreCase(canonicalizeSyncMode(str))) {
            execute("PRAGMA synchronous=" + str, null, null);
        }
    }

    public static String canonicalizeSyncMode(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (str.equals("1")) {
                    c = 1;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "OFF";
            case 1:
                return "NORMAL";
            case 2:
                return "FULL";
            default:
                return str;
        }
    }

    public final void setJournalMode(String str) {
        String executeForString = executeForString("PRAGMA journal_mode", null, null);
        if (!executeForString.equalsIgnoreCase(str)) {
            try {
                if (executeForString("PRAGMA journal_mode=" + str, null, null).equalsIgnoreCase(str)) {
                    return;
                }
            } catch (SQLiteDatabaseLockedException unused) {
            }
            Log.w("SQLiteConnection", "Could not change the database journal mode of '" + this.mConfiguration.label + "' from '" + executeForString + "' to '" + str + "' because the database is locked.  This usually means that there are other open connections to the database which prevents the database from enabling or disabling write-ahead logging mode.  Proceeding without changing the journal mode.");
        }
    }

    public final void setLocaleFromConfiguration() {
        SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = this.mConfiguration;
        if ((sQLiteDatabaseConfiguration.openFlags & 16) != 0) {
            return;
        }
        String locale = sQLiteDatabaseConfiguration.locale.toString();
        nativeRegisterLocalizedCollators(this.mConnectionPtr, locale);
        if (this.mIsReadOnlyConnection) {
            return;
        }
        try {
            execute("CREATE TABLE IF NOT EXISTS android_metadata (locale TEXT)", null, null);
            String executeForString = executeForString("SELECT locale FROM android_metadata UNION SELECT NULL ORDER BY locale DESC LIMIT 1", null, null);
            if (executeForString != null && executeForString.equals(locale)) {
                return;
            }
            execute("BEGIN", null, null);
            execute("DELETE FROM android_metadata", null, null);
            execute("INSERT INTO android_metadata (locale) VALUES(?)", new Object[]{locale}, null);
            execute("REINDEX LOCALIZED", null, null);
            execute("COMMIT", null, null);
        } catch (SQLiteException e) {
            throw e;
        } catch (RuntimeException e2) {
            throw new SQLiteException("Failed to change locale for db '" + this.mConfiguration.label + "' to '" + locale + "'.", e2);
        }
    }

    public final void executePerConnectionSqlFromConfiguration(int i) {
        while (i < this.mConfiguration.perConnectionSql.size()) {
            Pair<String, Object[]> pair = this.mConfiguration.perConnectionSql.get(i);
            int sqlStatementType = DatabaseUtils.getSqlStatementType((String) pair.first);
            if (sqlStatementType == 1) {
                executeForString((String) pair.first, (Object[]) pair.second, null);
            } else if (sqlStatementType == 7) {
                execute((String) pair.first, (Object[]) pair.second, null);
            } else {
                throw new IllegalArgumentException("Unsupported configuration statement: " + pair);
            }
            i++;
        }
    }

    public void reconfigure(SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration) {
        boolean z = false;
        this.mOnlyAllowReadOnlyOperations = false;
        int size = sQLiteDatabaseConfiguration.customFunctions.size();
        for (int i = 0; i < size; i++) {
            SQLiteCustomFunction sQLiteCustomFunction = sQLiteDatabaseConfiguration.customFunctions.get(i);
            if (!this.mConfiguration.customFunctions.contains(sQLiteCustomFunction)) {
                nativeRegisterCustomFunction(this.mConnectionPtr, sQLiteCustomFunction);
            }
        }
        boolean z2 = sQLiteDatabaseConfiguration.foreignKeyConstraintsEnabled;
        SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration2 = this.mConfiguration;
        boolean z3 = z2 != sQLiteDatabaseConfiguration2.foreignKeyConstraintsEnabled;
        boolean z4 = ((sQLiteDatabaseConfiguration.openFlags ^ sQLiteDatabaseConfiguration2.openFlags) & 536870912) != 0;
        boolean z5 = !sQLiteDatabaseConfiguration.locale.equals(sQLiteDatabaseConfiguration2.locale);
        int size2 = this.mConfiguration.perConnectionSql.size();
        if (sQLiteDatabaseConfiguration.perConnectionSql.size() > size2) {
            z = true;
        }
        this.mConfiguration.updateParametersFrom(sQLiteDatabaseConfiguration);
        this.mPreparedStatementCache.resize(sQLiteDatabaseConfiguration.maxSqlCacheSize);
        if (z3) {
            setForeignKeyModeFromConfiguration();
        }
        if (z4) {
            setWalModeFromConfiguration();
        }
        if (z5) {
            setLocaleFromConfiguration();
        }
        if (z) {
            executePerConnectionSqlFromConfiguration(size2);
        }
    }

    public void setOnlyAllowReadOnlyOperations(boolean z) {
        this.mOnlyAllowReadOnlyOperations = z;
    }

    public boolean isPreparedStatementInCache(String str) {
        return this.mPreparedStatementCache.get(str) != null;
    }

    public int getConnectionId() {
        return this.mConnectionId;
    }

    public boolean isPrimaryConnection() {
        return this.mIsPrimaryConnection;
    }

    public void prepare(String str, SQLiteStatementInfo sQLiteStatementInfo) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        int beginOperation = this.mRecentOperations.beginOperation("prepare", str, null);
        try {
            try {
                PreparedStatement acquirePreparedStatement = acquirePreparedStatement(str);
                if (sQLiteStatementInfo != null) {
                    try {
                        sQLiteStatementInfo.numParameters = acquirePreparedStatement.mNumParameters;
                        sQLiteStatementInfo.readOnly = acquirePreparedStatement.mReadOnly;
                        int nativeGetColumnCount = nativeGetColumnCount(this.mConnectionPtr, acquirePreparedStatement.mStatementPtr);
                        if (nativeGetColumnCount == 0) {
                            sQLiteStatementInfo.columnNames = EMPTY_STRING_ARRAY;
                        } else {
                            sQLiteStatementInfo.columnNames = new String[nativeGetColumnCount];
                            for (int i = 0; i < nativeGetColumnCount; i++) {
                                sQLiteStatementInfo.columnNames[i] = nativeGetColumnName(this.mConnectionPtr, acquirePreparedStatement.mStatementPtr, i);
                            }
                        }
                    } finally {
                        releasePreparedStatement(acquirePreparedStatement);
                    }
                }
            } catch (RuntimeException e) {
                this.mRecentOperations.failOperation(beginOperation, e);
                throw e;
            }
        } finally {
            this.mRecentOperations.endOperation(beginOperation);
        }
    }

    public void execute(String str, Object[] objArr, CancellationSignal cancellationSignal) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        int beginOperation = this.mRecentOperations.beginOperation("execute", str, objArr);
        try {
            try {
                PreparedStatement acquirePreparedStatement = acquirePreparedStatement(str);
                try {
                    throwIfStatementForbidden(acquirePreparedStatement);
                    bindArguments(acquirePreparedStatement, objArr);
                    applyBlockGuardPolicy(acquirePreparedStatement);
                    attachCancellationSignal(cancellationSignal);
                    nativeExecute(this.mConnectionPtr, acquirePreparedStatement.mStatementPtr);
                    detachCancellationSignal(cancellationSignal);
                } finally {
                    releasePreparedStatement(acquirePreparedStatement);
                }
            } catch (RuntimeException e) {
                this.mRecentOperations.failOperation(beginOperation, e);
                throw e;
            }
        } finally {
            this.mRecentOperations.endOperation(beginOperation);
        }
    }

    public long executeForLong(String str, Object[] objArr, CancellationSignal cancellationSignal) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        int beginOperation = this.mRecentOperations.beginOperation("executeForLong", str, objArr);
        try {
            try {
                PreparedStatement acquirePreparedStatement = acquirePreparedStatement(str);
                try {
                    throwIfStatementForbidden(acquirePreparedStatement);
                    bindArguments(acquirePreparedStatement, objArr);
                    applyBlockGuardPolicy(acquirePreparedStatement);
                    attachCancellationSignal(cancellationSignal);
                    long nativeExecuteForLong = nativeExecuteForLong(this.mConnectionPtr, acquirePreparedStatement.mStatementPtr);
                    this.mRecentOperations.setResult(nativeExecuteForLong);
                    detachCancellationSignal(cancellationSignal);
                    return nativeExecuteForLong;
                } finally {
                    releasePreparedStatement(acquirePreparedStatement);
                }
            } catch (RuntimeException e) {
                this.mRecentOperations.failOperation(beginOperation, e);
                throw e;
            }
        } finally {
            this.mRecentOperations.endOperation(beginOperation);
        }
    }

    public String executeForString(String str, Object[] objArr, CancellationSignal cancellationSignal) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        int beginOperation = this.mRecentOperations.beginOperation("executeForString", str, objArr);
        try {
            try {
                PreparedStatement acquirePreparedStatement = acquirePreparedStatement(str);
                try {
                    throwIfStatementForbidden(acquirePreparedStatement);
                    bindArguments(acquirePreparedStatement, objArr);
                    applyBlockGuardPolicy(acquirePreparedStatement);
                    attachCancellationSignal(cancellationSignal);
                    String nativeExecuteForString = nativeExecuteForString(this.mConnectionPtr, acquirePreparedStatement.mStatementPtr);
                    this.mRecentOperations.setResult(nativeExecuteForString);
                    detachCancellationSignal(cancellationSignal);
                    return nativeExecuteForString;
                } finally {
                    releasePreparedStatement(acquirePreparedStatement);
                }
            } catch (RuntimeException e) {
                this.mRecentOperations.failOperation(beginOperation, e);
                throw e;
            }
        } finally {
            this.mRecentOperations.endOperation(beginOperation);
        }
    }

    public int executeForChangedRowCount(String str, Object[] objArr, CancellationSignal cancellationSignal) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        int beginOperation = this.mRecentOperations.beginOperation("executeForChangedRowCount", str, objArr);
        try {
            try {
                PreparedStatement acquirePreparedStatement = acquirePreparedStatement(str);
                try {
                    throwIfStatementForbidden(acquirePreparedStatement);
                    bindArguments(acquirePreparedStatement, objArr);
                    applyBlockGuardPolicy(acquirePreparedStatement);
                    attachCancellationSignal(cancellationSignal);
                    int nativeExecuteForChangedRowCount = nativeExecuteForChangedRowCount(this.mConnectionPtr, acquirePreparedStatement.mStatementPtr);
                    detachCancellationSignal(cancellationSignal);
                    if (this.mRecentOperations.endOperationDeferLog(beginOperation)) {
                        OperationLog operationLog = this.mRecentOperations;
                        operationLog.logOperation(beginOperation, "changedRows=" + nativeExecuteForChangedRowCount);
                    }
                    return nativeExecuteForChangedRowCount;
                } finally {
                    releasePreparedStatement(acquirePreparedStatement);
                }
            } catch (RuntimeException e) {
                this.mRecentOperations.failOperation(beginOperation, e);
                throw e;
            }
        } catch (Throwable th) {
            if (this.mRecentOperations.endOperationDeferLog(beginOperation)) {
                OperationLog operationLog2 = this.mRecentOperations;
                operationLog2.logOperation(beginOperation, "changedRows=0");
            }
            throw th;
        }
    }

    public long executeForLastInsertedRowId(String str, Object[] objArr, CancellationSignal cancellationSignal) {
        if (str == null) {
            throw new IllegalArgumentException("sql must not be null.");
        }
        int beginOperation = this.mRecentOperations.beginOperation("executeForLastInsertedRowId", str, objArr);
        try {
            try {
                PreparedStatement acquirePreparedStatement = acquirePreparedStatement(str);
                try {
                    throwIfStatementForbidden(acquirePreparedStatement);
                    bindArguments(acquirePreparedStatement, objArr);
                    applyBlockGuardPolicy(acquirePreparedStatement);
                    attachCancellationSignal(cancellationSignal);
                    long nativeExecuteForLastInsertedRowId = nativeExecuteForLastInsertedRowId(this.mConnectionPtr, acquirePreparedStatement.mStatementPtr);
                    detachCancellationSignal(cancellationSignal);
                    return nativeExecuteForLastInsertedRowId;
                } finally {
                    releasePreparedStatement(acquirePreparedStatement);
                }
            } catch (RuntimeException e) {
                this.mRecentOperations.failOperation(beginOperation, e);
                throw e;
            }
        } finally {
            this.mRecentOperations.endOperation(beginOperation);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x014a A[Catch: all -> 0x0177, TryCatch #9 {all -> 0x0177, blocks: (B:6:0x001d, B:18:0x0064, B:20:0x006c, B:57:0x0142, B:59:0x014a, B:60:0x0176), top: B:80:0x001d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int executeForCursorWindow(java.lang.String r22, java.lang.Object[] r23, android.database.CursorWindow r24, int r25, int r26, boolean r27, android.os.CancellationSignal r28) {
        /*
            Method dump skipped, instructions count: 396
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.sqlite.database.sqlite.SQLiteConnection.executeForCursorWindow(java.lang.String, java.lang.Object[], android.database.CursorWindow, int, int, boolean, android.os.CancellationSignal):int");
    }

    public PreparedStatement acquirePreparedStatement(String str) {
        boolean z;
        PreparedStatement preparedStatement = this.mPreparedStatementCache.get(str);
        if (preparedStatement == null) {
            z = false;
        } else if (!preparedStatement.mInUse) {
            return preparedStatement;
        } else {
            z = true;
        }
        long nativePrepareStatement = nativePrepareStatement(this.mConnectionPtr, str);
        try {
            int nativeGetParameterCount = nativeGetParameterCount(this.mConnectionPtr, nativePrepareStatement);
            int sqlStatementType = DatabaseUtils.getSqlStatementType(str);
            preparedStatement = obtainPreparedStatement(str, nativePrepareStatement, nativeGetParameterCount, sqlStatementType, nativeIsReadOnly(this.mConnectionPtr, nativePrepareStatement));
            if (!z && isCacheable(sqlStatementType)) {
                this.mPreparedStatementCache.put(str, preparedStatement);
                preparedStatement.mInCache = true;
            }
            preparedStatement.mInUse = true;
            return preparedStatement;
        } catch (RuntimeException e) {
            if (preparedStatement == null || !preparedStatement.mInCache) {
                nativeFinalizeStatement(this.mConnectionPtr, nativePrepareStatement);
            }
            throw e;
        }
    }

    public void releasePreparedStatement(PreparedStatement preparedStatement) {
        preparedStatement.mInUse = false;
        if (preparedStatement.mInCache) {
            try {
                nativeResetStatementAndClearBindings(this.mConnectionPtr, preparedStatement.mStatementPtr);
                return;
            } catch (SQLiteException unused) {
                this.mPreparedStatementCache.remove(preparedStatement.mSql);
                return;
            }
        }
        finalizePreparedStatement(preparedStatement);
    }

    public final void finalizePreparedStatement(PreparedStatement preparedStatement) {
        nativeFinalizeStatement(this.mConnectionPtr, preparedStatement.mStatementPtr);
        recyclePreparedStatement(preparedStatement);
    }

    public final void attachCancellationSignal(CancellationSignal cancellationSignal) {
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
            int i = this.mCancellationSignalAttachCount + 1;
            this.mCancellationSignalAttachCount = i;
            if (i != 1) {
                return;
            }
            nativeResetCancel(this.mConnectionPtr, true);
            cancellationSignal.setOnCancelListener(this);
        }
    }

    public final void detachCancellationSignal(CancellationSignal cancellationSignal) {
        if (cancellationSignal != null) {
            int i = this.mCancellationSignalAttachCount - 1;
            this.mCancellationSignalAttachCount = i;
            if (i != 0) {
                return;
            }
            cancellationSignal.setOnCancelListener(null);
            nativeResetCancel(this.mConnectionPtr, false);
        }
    }

    @Override // android.os.CancellationSignal.OnCancelListener
    public void onCancel() {
        nativeCancel(this.mConnectionPtr);
    }

    public final void bindArguments(PreparedStatement preparedStatement, Object[] objArr) {
        int length = objArr != null ? objArr.length : 0;
        if (length != preparedStatement.mNumParameters) {
            throw new SQLiteBindOrColumnIndexOutOfRangeException("Expected " + preparedStatement.mNumParameters + " bind arguments but " + length + " were provided.");
        } else if (length != 0) {
            long j = preparedStatement.mStatementPtr;
            for (int i = 0; i < length; i++) {
                Object obj = objArr[i];
                int typeOfObject = DatabaseUtils.getTypeOfObject(obj);
                if (typeOfObject == 0) {
                    nativeBindNull(this.mConnectionPtr, j, i + 1);
                } else if (typeOfObject == 1) {
                    nativeBindLong(this.mConnectionPtr, j, i + 1, ((Number) obj).longValue());
                } else if (typeOfObject == 2) {
                    nativeBindDouble(this.mConnectionPtr, j, i + 1, ((Number) obj).doubleValue());
                } else if (typeOfObject == 4) {
                    nativeBindBlob(this.mConnectionPtr, j, i + 1, (byte[]) obj);
                } else if (obj instanceof Boolean) {
                    nativeBindLong(this.mConnectionPtr, j, i + 1, ((Boolean) obj).booleanValue() ? 1L : 0L);
                } else {
                    nativeBindString(this.mConnectionPtr, j, i + 1, obj.toString());
                }
            }
        }
    }

    public final void resetStatement(PreparedStatement preparedStatement, boolean z) {
        nativeResetStatement(this.mConnectionPtr, preparedStatement.mStatementPtr, z);
    }

    public final void throwIfStatementForbidden(PreparedStatement preparedStatement) {
        if (!this.mOnlyAllowReadOnlyOperations || preparedStatement.mReadOnly) {
            return;
        }
        throw new SQLiteException("Cannot execute this statement because it might modify the database but the connection is read-only.");
    }

    public void dump(Printer printer, boolean z) {
        dumpUnsafe(printer, z);
    }

    public void dumpUnsafe(Printer printer, boolean z) {
        printer.println("Connection #" + this.mConnectionId + ":");
        if (z) {
            printer.println("  connectionPtr: 0x" + Long.toHexString(this.mConnectionPtr));
        }
        printer.println("  isPrimaryConnection: " + this.mIsPrimaryConnection);
        printer.println("  onlyAllowReadOnlyOperations: " + this.mOnlyAllowReadOnlyOperations);
        this.mRecentOperations.dump(printer, z);
        if (z) {
            this.mPreparedStatementCache.dump(printer);
        }
    }

    public String describeCurrentOperationUnsafe() {
        return this.mRecentOperations.describeCurrentOperation();
    }

    public String toString() {
        return "SQLiteConnection: " + this.mConfiguration.path + " (" + this.mConnectionId + ")";
    }

    public final PreparedStatement obtainPreparedStatement(String str, long j, int i, int i2, boolean z) {
        PreparedStatement preparedStatement = this.mPreparedStatementPool;
        if (preparedStatement != null) {
            this.mPreparedStatementPool = preparedStatement.mPoolNext;
            preparedStatement.mPoolNext = null;
            preparedStatement.mInCache = false;
        } else {
            preparedStatement = new PreparedStatement(this);
        }
        preparedStatement.mSql = str;
        preparedStatement.mStatementPtr = j;
        preparedStatement.mNumParameters = i;
        preparedStatement.mType = i2;
        preparedStatement.mReadOnly = z;
        return preparedStatement;
    }

    public final void recyclePreparedStatement(PreparedStatement preparedStatement) {
        preparedStatement.mSql = null;
        preparedStatement.mPoolNext = this.mPreparedStatementPool;
        this.mPreparedStatementPool = preparedStatement;
    }

    public static String trimSqlForDisplay(String str) {
        return str.replaceAll("[\\s]*\\n+[\\s]*", " ");
    }

    /* loaded from: classes3.dex */
    public static final class PreparedStatement {
        public final WeakReference<SQLiteConnection> mConnection;
        public boolean mInCache;
        public boolean mInUse;
        public int mNumParameters;
        public Operation mOperation;
        public PreparedStatement mPoolNext;
        public boolean mReadOnly;
        public String mSql;
        public long mStatementPtr;
        public int mType;

        public PreparedStatement(SQLiteConnection sQLiteConnection) {
            this.mConnection = new WeakReference<>(sQLiteConnection);
        }

        public void bindArguments(Object[] objArr) {
            SQLiteConnection sQLiteConnection = this.mConnection.get();
            if (sQLiteConnection == null) {
                return;
            }
            sQLiteConnection.bindArguments(this, objArr);
        }

        public void reset(boolean z) {
            SQLiteConnection sQLiteConnection = this.mConnection.get();
            if (sQLiteConnection == null) {
                return;
            }
            sQLiteConnection.resetStatement(this, z);
        }

        public long getPtr() {
            return this.mStatementPtr;
        }

        public void attachCancellationSignal(CancellationSignal cancellationSignal) {
            SQLiteConnection sQLiteConnection = this.mConnection.get();
            if (sQLiteConnection == null) {
                return;
            }
            sQLiteConnection.attachCancellationSignal(cancellationSignal);
        }

        public void detachCancellationSignal(CancellationSignal cancellationSignal) {
            SQLiteConnection sQLiteConnection = this.mConnection.get();
            if (sQLiteConnection == null) {
                return;
            }
            sQLiteConnection.detachCancellationSignal(cancellationSignal);
        }

        public void beginOperation(String str, Object[] objArr) {
            SQLiteConnection sQLiteConnection = this.mConnection.get();
            if (sQLiteConnection == null) {
                return;
            }
            Operation operationLocked = sQLiteConnection.mRecentOperations.getOperationLocked(sQLiteConnection.mRecentOperations.beginOperation(str, this.mSql, objArr));
            this.mOperation = operationLocked;
            operationLocked.mType = this.mType;
        }

        public void failOperation(Exception exc) {
            SQLiteConnection sQLiteConnection;
            if (this.mOperation == null || (sQLiteConnection = this.mConnection.get()) == null) {
                return;
            }
            sQLiteConnection.mRecentOperations.failOperation(this.mOperation.mCookie, exc);
        }

        public void endOperation(String str) {
            SQLiteConnection sQLiteConnection;
            if (this.mOperation == null || (sQLiteConnection = this.mConnection.get()) == null) {
                return;
            }
            if (sQLiteConnection.mRecentOperations.endOperationDeferLog(this.mOperation.mCookie)) {
                sQLiteConnection.mRecentOperations.logOperation(this.mOperation.mCookie, str);
            }
            this.mOperation = null;
        }
    }

    /* loaded from: classes3.dex */
    public final class PreparedStatementCache extends LruCache<String, PreparedStatement> {
        public PreparedStatementCache(int i) {
            super(i);
        }

        @Override // android.util.LruCache
        public void entryRemoved(boolean z, String str, PreparedStatement preparedStatement, PreparedStatement preparedStatement2) {
            preparedStatement.mInCache = false;
            if (!preparedStatement.mInUse) {
                SQLiteConnection.this.finalizePreparedStatement(preparedStatement);
            }
        }

        public void dump(Printer printer) {
            printer.println("  Prepared statement cache:");
            Map<String, PreparedStatement> snapshot = snapshot();
            if (!snapshot.isEmpty()) {
                int i = 0;
                for (Map.Entry<String, PreparedStatement> entry : snapshot.entrySet()) {
                    PreparedStatement value = entry.getValue();
                    if (value.mInCache) {
                        printer.println("    " + i + ": statementPtr=0x" + Long.toHexString(value.mStatementPtr) + ", numParameters=" + value.mNumParameters + ", type=" + value.mType + ", readOnly=" + value.mReadOnly + ", sql=\"" + SQLiteConnection.trimSqlForDisplay(entry.getKey()) + "\"");
                    }
                    i++;
                }
                return;
            }
            printer.println("    <none>");
        }
    }

    /* loaded from: classes3.dex */
    public static final class OperationLog {
        public int mGeneration;
        public int mIndex;
        public final SQLiteConnectionPool mPool;
        public String mResultString;
        public final Operation[] mOperations = new Operation[20];
        public long mResultLong = Long.MIN_VALUE;

        public OperationLog(SQLiteConnectionPool sQLiteConnectionPool) {
            this.mPool = sQLiteConnectionPool;
        }

        public int beginOperation(String str, String str2, Object[] objArr) {
            int newOperationCookieLocked;
            this.mResultLong = Long.MIN_VALUE;
            this.mResultString = null;
            synchronized (this.mOperations) {
                int i = (this.mIndex + 1) % 20;
                Operation operation = this.mOperations[i];
                if (operation == null) {
                    operation = new Operation();
                    this.mOperations[i] = operation;
                } else {
                    operation.mFinished = false;
                    operation.mException = null;
                    ArrayList<Object> arrayList = operation.mBindArgs;
                    if (arrayList != null) {
                        arrayList.clear();
                    }
                }
                operation.mStartWallTime = System.currentTimeMillis();
                operation.mStartTime = SystemClock.uptimeMillis();
                operation.mKind = str;
                operation.mSql = str2;
                operation.mPath = this.mPool.getPath();
                operation.mResultLong = Long.MIN_VALUE;
                operation.mResultString = null;
                if (objArr != null) {
                    ArrayList<Object> arrayList2 = operation.mBindArgs;
                    if (arrayList2 == null) {
                        operation.mBindArgs = new ArrayList<>();
                    } else {
                        arrayList2.clear();
                    }
                    for (Object obj : objArr) {
                        if (obj instanceof byte[]) {
                            operation.mBindArgs.add(SQLiteConnection.EMPTY_BYTE_ARRAY);
                        } else {
                            operation.mBindArgs.add(obj);
                        }
                    }
                }
                newOperationCookieLocked = newOperationCookieLocked(i);
                operation.mCookie = newOperationCookieLocked;
                this.mIndex = i;
            }
            return newOperationCookieLocked;
        }

        public void failOperation(int i, Exception exc) {
            synchronized (this.mOperations) {
                Operation operationLocked = getOperationLocked(i);
                if (operationLocked != null) {
                    operationLocked.mException = exc;
                }
            }
        }

        public void endOperation(int i) {
            synchronized (this.mOperations) {
                if (endOperationDeferLogLocked(i)) {
                    logOperationLocked(i, null);
                }
            }
        }

        public boolean endOperationDeferLog(int i) {
            boolean endOperationDeferLogLocked;
            synchronized (this.mOperations) {
                endOperationDeferLogLocked = endOperationDeferLogLocked(i);
            }
            return endOperationDeferLogLocked;
        }

        public void logOperation(int i, String str) {
            synchronized (this.mOperations) {
                logOperationLocked(i, str);
            }
        }

        public void setResult(long j) {
            this.mResultLong = j;
        }

        public void setResult(String str) {
            this.mResultString = str;
        }

        public final boolean endOperationDeferLogLocked(int i) {
            Operation operationLocked = getOperationLocked(i);
            if (operationLocked != null) {
                long uptimeMillis = SystemClock.uptimeMillis();
                operationLocked.mEndTime = uptimeMillis;
                operationLocked.mFinished = true;
                long j = uptimeMillis - operationLocked.mStartTime;
                this.mPool.onStatementExecuted(j);
                return SQLiteDebug.NoPreloadHolder.DEBUG_LOG_SLOW_QUERIES && SQLiteDebug.shouldLogSlowQuery(j);
            }
            return false;
        }

        public final void logOperationLocked(int i, String str) {
            Operation operationLocked = getOperationLocked(i);
            operationLocked.mResultLong = this.mResultLong;
            operationLocked.mResultString = this.mResultString;
            StringBuilder sb = new StringBuilder();
            operationLocked.describe(sb, true);
            if (str != null) {
                sb.append(", ");
                sb.append(str);
            }
            Log.d("SQLiteConnection", sb.toString());
        }

        public final int newOperationCookieLocked(int i) {
            int i2 = this.mGeneration;
            this.mGeneration = i2 + 1;
            return i | (i2 << 8);
        }

        public final Operation getOperationLocked(int i) {
            Operation operation = this.mOperations[i & 255];
            if (operation.mCookie == i) {
                return operation;
            }
            return null;
        }

        public String describeCurrentOperation() {
            synchronized (this.mOperations) {
                Operation operation = this.mOperations[this.mIndex];
                if (operation == null || operation.mFinished) {
                    return null;
                }
                StringBuilder sb = new StringBuilder();
                operation.describe(sb, false);
                return sb.toString();
            }
        }

        public void dump(Printer printer, boolean z) {
            synchronized (this.mOperations) {
                printer.println("  Most recently executed operations:");
                int i = this.mIndex;
                Operation operation = this.mOperations[i];
                if (operation != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    int i2 = 0;
                    do {
                        StringBuilder sb = new StringBuilder();
                        sb.append("    ");
                        sb.append(i2);
                        sb.append(": [");
                        sb.append(simpleDateFormat.format(new Date(operation.mStartWallTime)));
                        sb.append("] ");
                        operation.describe(sb, false);
                        printer.println(sb.toString());
                        i = i > 0 ? i - 1 : 19;
                        i2++;
                        operation = this.mOperations[i];
                        if (operation == null) {
                            break;
                        }
                    } while (i2 < 20);
                } else {
                    printer.println("    <none>");
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class Operation {
        public ArrayList<Object> mBindArgs;
        public int mCookie;
        public long mEndTime;
        public Exception mException;
        public boolean mFinished;
        public String mKind;
        public String mPath;
        public long mResultLong;
        public String mResultString;
        public String mSql;
        public long mStartTime;
        public long mStartWallTime;
        public int mType;

        public Operation() {
        }

        public void describe(StringBuilder sb, boolean z) {
            ArrayList<Object> arrayList;
            sb.append(this.mKind);
            if (this.mFinished) {
                sb.append(" took ");
                sb.append(this.mEndTime - this.mStartTime);
                sb.append(d.H);
            } else {
                sb.append(" started ");
                sb.append(System.currentTimeMillis() - this.mStartWallTime);
                sb.append("ms ago");
            }
            sb.append(" - ");
            sb.append(getStatus());
            if (this.mSql != null) {
                sb.append(", sql=\"");
                sb.append(SQLiteConnection.trimSqlForDisplay(this.mSql));
                sb.append("\"");
            }
            if (z && (arrayList = this.mBindArgs) != null && arrayList.size() != 0) {
                sb.append(", bindArgs=[");
                int size = this.mBindArgs.size();
                for (int i = 0; i < size; i++) {
                    Object obj = this.mBindArgs.get(i);
                    if (i != 0) {
                        sb.append(", ");
                    }
                    if (obj == null) {
                        sb.append("null");
                    } else if (obj instanceof byte[]) {
                        sb.append("<byte[]>");
                    } else if (obj instanceof String) {
                        sb.append("\"");
                        sb.append((String) obj);
                        sb.append("\"");
                    } else {
                        sb.append(obj);
                    }
                }
                sb.append("]");
            }
            sb.append(", path=");
            sb.append(this.mPath);
            if (this.mException != null) {
                sb.append(", exception=\"");
                sb.append(this.mException.getMessage());
                sb.append("\"");
            }
            if (this.mResultLong != Long.MIN_VALUE) {
                sb.append(", result=");
                sb.append(this.mResultLong);
            }
            if (this.mResultString != null) {
                sb.append(", result=\"");
                sb.append(this.mResultString);
                sb.append("\"");
            }
        }

        public final String getStatus() {
            return !this.mFinished ? "running" : this.mException != null ? "failed" : "succeeded";
        }
    }
}
