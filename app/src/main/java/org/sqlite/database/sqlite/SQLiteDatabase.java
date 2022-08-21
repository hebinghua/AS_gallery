package org.sqlite.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.CancellationSignal;
import android.os.Looper;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.util.Printer;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.core.CoreConstants;
import com.android.internal.SystemPropertiesCompat;
import com.google.common.base.Preconditions;
import com.xiaomi.stat.a;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import java.util.function.Supplier;
import org.sqlite.database.DatabaseErrorHandler;
import org.sqlite.database.DatabaseUtils;
import org.sqlite.database.DefaultDatabaseErrorHandler;
import org.sqlite.database.SQLException;

/* loaded from: classes3.dex */
public final class SQLiteDatabase extends SQLiteClosable {
    public final SQLiteDatabaseConfiguration mConfigurationLocked;
    public SQLiteConnectionPool mConnectionPoolLocked;
    public final CursorFactory mCursorFactory;
    public final DatabaseErrorHandler mErrorHandler;
    public boolean mHasAttachedDbsLocked;
    public static final WeakHashMap<SQLiteDatabase, Object> sActiveDatabases = new WeakHashMap<>();
    public static final String[] CONFLICT_VALUES = {"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
    public final ThreadLocal<SQLiteSession> mThreadSession = ThreadLocal.withInitial(new Supplier() { // from class: org.sqlite.database.sqlite.SQLiteDatabase$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return SQLiteDatabase.this.createSession();
        }
    });
    public final Object mLock = new Object();
    public final CloseGuard mCloseGuardLocked = CloseGuard.get();

    /* loaded from: classes3.dex */
    public interface CursorFactory {
        Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteProgram sQLiteProgram);

        SQLiteProgram newQuery(SQLiteDatabase sQLiteDatabase, String str, Object[] objArr, CancellationSignal cancellationSignal);
    }

    /* loaded from: classes3.dex */
    public interface CustomFunction {
        void callback(String[] strArr);
    }

    static {
        SQLiteGlobal.loadLib();
    }

    public SQLiteDatabase(String str, int i, CursorFactory cursorFactory, DatabaseErrorHandler databaseErrorHandler, int i2, int i3, long j, String str2, String str3) {
        this.mCursorFactory = cursorFactory;
        this.mErrorHandler = databaseErrorHandler == null ? new DefaultDatabaseErrorHandler() : databaseErrorHandler;
        SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = new SQLiteDatabaseConfiguration(str, i);
        this.mConfigurationLocked = sQLiteDatabaseConfiguration;
        sQLiteDatabaseConfiguration.lookasideSlotSize = i2;
        sQLiteDatabaseConfiguration.lookasideSlotCount = i3;
        if (SystemPropertiesCompat.getBoolean("ro.config.low_ram", false)) {
            sQLiteDatabaseConfiguration.lookasideSlotCount = 0;
            sQLiteDatabaseConfiguration.lookasideSlotSize = 0;
        }
        sQLiteDatabaseConfiguration.idleConnectionTimeoutMs = (sQLiteDatabaseConfiguration.isInMemoryDb() || j < 0) ? Long.MAX_VALUE : j;
        sQLiteDatabaseConfiguration.journalMode = str2;
        sQLiteDatabaseConfiguration.syncMode = str3;
    }

    public void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    @Override // org.sqlite.database.sqlite.SQLiteClosable
    public void onAllReferencesReleased() {
        dispose(false);
    }

    public final void dispose(boolean z) {
        SQLiteConnectionPool sQLiteConnectionPool;
        synchronized (this.mLock) {
            CloseGuard closeGuard = this.mCloseGuardLocked;
            if (closeGuard != null) {
                if (z) {
                    closeGuard.warnIfOpen();
                }
                this.mCloseGuardLocked.close();
            }
            sQLiteConnectionPool = this.mConnectionPoolLocked;
            this.mConnectionPoolLocked = null;
        }
        if (!z) {
            WeakHashMap<SQLiteDatabase, Object> weakHashMap = sActiveDatabases;
            synchronized (weakHashMap) {
                weakHashMap.remove(this);
            }
            if (sQLiteConnectionPool == null) {
                return;
            }
            sQLiteConnectionPool.close();
        }
    }

    public String getLabel() {
        String str;
        synchronized (this.mLock) {
            str = this.mConfigurationLocked.label;
        }
        return str;
    }

    public void onCorruption() {
        EventLog.writeEvent(75004, getLabel());
        this.mErrorHandler.onCorruption(this);
    }

    public SQLiteSession getThreadSession() {
        return this.mThreadSession.get();
    }

    public SQLiteSession createSession() {
        SQLiteConnectionPool sQLiteConnectionPool;
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            sQLiteConnectionPool = this.mConnectionPoolLocked;
        }
        return new SQLiteSession(sQLiteConnectionPool);
    }

    public int getThreadDefaultConnectionFlags(boolean z) {
        int i = z ? 1 : 2;
        return isMainThread() ? i | 4 : i;
    }

    public static boolean isMainThread() {
        Looper myLooper = Looper.myLooper();
        return myLooper != null && myLooper == Looper.getMainLooper();
    }

    public void beginTransaction() {
        beginTransaction(null, true);
    }

    public void beginTransactionNonExclusive() {
        beginTransaction(null, false);
    }

    public final void beginTransaction(SQLiteTransactionListener sQLiteTransactionListener, boolean z) {
        acquireReference();
        try {
            getThreadSession().beginTransaction(z ? 2 : 1, sQLiteTransactionListener, getThreadDefaultConnectionFlags(false), null);
        } finally {
            releaseReference();
        }
    }

    public void endTransaction() {
        acquireReference();
        try {
            getThreadSession().endTransaction(null);
        } finally {
            releaseReference();
        }
    }

    public void setTransactionSuccessful() {
        acquireReference();
        try {
            getThreadSession().setTransactionSuccessful();
        } finally {
            releaseReference();
        }
    }

    public boolean inTransaction() {
        acquireReference();
        try {
            return getThreadSession().hasTransaction();
        } finally {
            releaseReference();
        }
    }

    public static SQLiteDatabase openDatabase(String str, CursorFactory cursorFactory, int i) {
        return openDatabase(str, cursorFactory, i, null);
    }

    public static SQLiteDatabase openDatabase(File file, OpenParams openParams) {
        return openDatabase(file.getPath(), openParams);
    }

    public static SQLiteDatabase openDatabase(String str, OpenParams openParams) {
        Preconditions.checkArgument(openParams != null, "OpenParams cannot be null");
        SQLiteDatabase sQLiteDatabase = new SQLiteDatabase(str, openParams.mOpenFlags, openParams.mCursorFactory, openParams.mErrorHandler, openParams.mLookasideSlotSize, openParams.mLookasideSlotCount, openParams.mIdleConnectionTimeout, openParams.mJournalMode, openParams.mSyncMode);
        sQLiteDatabase.open();
        return sQLiteDatabase;
    }

    public static SQLiteDatabase openDatabase(String str, CursorFactory cursorFactory, int i, DatabaseErrorHandler databaseErrorHandler) {
        SQLiteDatabase sQLiteDatabase = new SQLiteDatabase(str, i, cursorFactory, databaseErrorHandler, -1, -1, -1L, null, null);
        sQLiteDatabase.open();
        return sQLiteDatabase;
    }

    public static boolean deleteDatabase(File file) {
        return deleteDatabase(file, true);
    }

    public static boolean deleteDatabase(File file, boolean z) {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }
        boolean delete = file.delete() | false | new File(file.getPath() + "-journal").delete() | new File(file.getPath() + "-shm").delete() | new File(file.getPath() + "-wal").delete();
        StringBuilder sb = new StringBuilder();
        sb.append(file.getPath());
        sb.append("-wipecheck");
        new File(sb.toString()).delete();
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            final String str = file.getName() + "-mj";
            File[] listFiles = parentFile.listFiles(new FileFilter() { // from class: org.sqlite.database.sqlite.SQLiteDatabase.1
                @Override // java.io.FileFilter
                public boolean accept(File file2) {
                    return file2.getName().startsWith(str);
                }
            });
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    delete |= file2.delete();
                }
            }
        }
        return delete;
    }

    public void reopenReadWrite() {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            if (!isReadOnlyLocked()) {
                return;
            }
            SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = this.mConfigurationLocked;
            int i = sQLiteDatabaseConfiguration.openFlags;
            sQLiteDatabaseConfiguration.openFlags = (i & (-2)) | 0;
            try {
                this.mConnectionPoolLocked.reconfigure(sQLiteDatabaseConfiguration);
            } catch (RuntimeException e) {
                this.mConfigurationLocked.openFlags = i;
                throw e;
            }
        }
    }

    public final void open() {
        try {
            openInner();
        } catch (RuntimeException e) {
            try {
                if (SQLiteDatabaseCorruptException.isCorruptException(e)) {
                    Log.e("SQLiteDatabase", "Database corruption detected in open()", e);
                    onCorruption();
                    openInner();
                    return;
                }
                throw e;
            } catch (SQLiteException e2) {
                Log.e("SQLiteDatabase", "Failed to open database '" + getLabel() + "'.", e2);
                close();
                throw e2;
            }
        }
    }

    public final void openInner() {
        synchronized (this.mLock) {
            this.mConnectionPoolLocked = SQLiteConnectionPool.open(this.mConfigurationLocked);
            this.mCloseGuardLocked.open("close");
        }
        WeakHashMap<SQLiteDatabase, Object> weakHashMap = sActiveDatabases;
        synchronized (weakHashMap) {
            weakHashMap.put(this, null);
        }
    }

    public static SQLiteDatabase create(CursorFactory cursorFactory) {
        return openDatabase(":memory:", cursorFactory, 268435456);
    }

    public static SQLiteDatabase createInMemory(OpenParams openParams) {
        return openDatabase(":memory:", openParams.toBuilder().addOpenFlags(268435456).build());
    }

    public int getVersion() {
        return Long.valueOf(DatabaseUtils.longForQuery(this, "PRAGMA user_version;", null)).intValue();
    }

    public void setVersion(int i) {
        execSQL("PRAGMA user_version = " + i);
    }

    public SQLiteStatement compileStatement(String str) throws SQLException {
        acquireReference();
        try {
            return new SQLiteStatement(this, str, null);
        } finally {
            releaseReference();
        }
    }

    public Cursor rawQuery(String str, String[] strArr) {
        return rawQueryWithFactory(null, str, strArr, null, null);
    }

    public Cursor rawQueryWithFactory(CursorFactory cursorFactory, String str, String[] strArr, String str2, CancellationSignal cancellationSignal) {
        acquireReference();
        try {
            SQLiteDirectCursorDriver sQLiteDirectCursorDriver = new SQLiteDirectCursorDriver(this, str, str2, cancellationSignal);
            if (cursorFactory == null) {
                cursorFactory = this.mCursorFactory;
            }
            return sQLiteDirectCursorDriver.query(cursorFactory, strArr);
        } finally {
            releaseReference();
        }
    }

    public long insertWithOnConflict(String str, String str2, ContentValues contentValues, int i) {
        acquireReference();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT");
            sb.append(CONFLICT_VALUES[i]);
            sb.append(" INTO ");
            sb.append(str);
            sb.append(CoreConstants.LEFT_PARENTHESIS_CHAR);
            Object[] objArr = null;
            int i2 = 0;
            int size = (contentValues == null || contentValues.size() <= 0) ? 0 : contentValues.size();
            if (size > 0) {
                objArr = new Object[size];
                int i3 = 0;
                for (String str3 : contentValues.keySet()) {
                    sb.append(i3 > 0 ? "," : "");
                    sb.append(str3);
                    objArr[i3] = contentValues.get(str3);
                    i3++;
                }
                sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
                sb.append(" VALUES (");
                while (i2 < size) {
                    sb.append(i2 > 0 ? ",?" : CallerData.NA);
                    i2++;
                }
            } else {
                sb.append(str2);
                sb.append(") VALUES (NULL");
            }
            sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
            SQLiteStatement sQLiteStatement = new SQLiteStatement(this, sb.toString(), objArr);
            long executeInsert = sQLiteStatement.executeInsert();
            sQLiteStatement.close();
            return executeInsert;
        } finally {
            releaseReference();
        }
    }

    public void execSQL(String str) throws SQLException {
        executeSql(str, null);
    }

    public void execSQL(String str, Object[] objArr) throws SQLException {
        if (objArr == null) {
            throw new IllegalArgumentException("Empty bindArgs");
        }
        executeSql(str, objArr);
    }

    public final int executeSql(String str, Object[] objArr) throws SQLException {
        acquireReference();
        try {
            int sqlStatementType = DatabaseUtils.getSqlStatementType(str);
            if (sqlStatementType == 3) {
                boolean z = false;
                synchronized (this.mLock) {
                    if (!this.mHasAttachedDbsLocked) {
                        this.mHasAttachedDbsLocked = true;
                        this.mConnectionPoolLocked.disableIdleConnectionHandler();
                        z = true;
                    }
                }
                if (z) {
                    disableWriteAheadLogging();
                }
            }
            SQLiteStatement sQLiteStatement = new SQLiteStatement(this, str, objArr);
            try {
                int executeUpdateDelete = sQLiteStatement.executeUpdateDelete();
                sQLiteStatement.close();
                if (sqlStatementType == 8) {
                    this.mConnectionPoolLocked.closeAvailableNonPrimaryConnectionsAndLogExceptions();
                }
                return executeUpdateDelete;
            } catch (Throwable th) {
                try {
                    sQLiteStatement.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } finally {
            releaseReference();
        }
    }

    public boolean isReadOnly() {
        boolean isReadOnlyLocked;
        synchronized (this.mLock) {
            isReadOnlyLocked = isReadOnlyLocked();
        }
        return isReadOnlyLocked;
    }

    public final boolean isReadOnlyLocked() {
        return (this.mConfigurationLocked.openFlags & 1) == 1;
    }

    public boolean isOpen() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnectionPoolLocked != null;
        }
        return z;
    }

    public final String getPath() {
        String str;
        synchronized (this.mLock) {
            str = this.mConfigurationLocked.path;
        }
        return str;
    }

    public boolean enableWriteAheadLogging() {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            if ((this.mConfigurationLocked.openFlags & 536870912) != 0) {
                return true;
            }
            if (isReadOnlyLocked()) {
                return false;
            }
            if (this.mConfigurationLocked.isInMemoryDb()) {
                Log.i("SQLiteDatabase", "can't enable WAL for memory databases.");
                return false;
            } else if (this.mHasAttachedDbsLocked) {
                if (Log.isLoggable("SQLiteDatabase", 3)) {
                    Log.d("SQLiteDatabase", "this database: " + this.mConfigurationLocked.label + " has attached databases. can't  enable WAL.");
                }
                return false;
            } else {
                SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = this.mConfigurationLocked;
                sQLiteDatabaseConfiguration.openFlags = 536870912 | sQLiteDatabaseConfiguration.openFlags;
                try {
                    this.mConnectionPoolLocked.reconfigure(sQLiteDatabaseConfiguration);
                    return true;
                } catch (RuntimeException e) {
                    this.mConfigurationLocked.openFlags &= -536870913;
                    throw e;
                }
            }
        }
    }

    public void disableWriteAheadLogging() {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = this.mConfigurationLocked;
            int i = sQLiteDatabaseConfiguration.openFlags;
            boolean z = true;
            boolean z2 = (536870912 & i) != 0;
            if ((Integer.MIN_VALUE & i) == 0) {
                z = false;
            }
            if (z2 || z) {
                int i2 = (-536870913) & i;
                sQLiteDatabaseConfiguration.openFlags = i2;
                sQLiteDatabaseConfiguration.openFlags = i2 & Integer.MAX_VALUE;
                try {
                    this.mConnectionPoolLocked.reconfigure(sQLiteDatabaseConfiguration);
                } catch (RuntimeException e) {
                    this.mConfigurationLocked.openFlags = i;
                    throw e;
                }
            }
        }
    }

    public static ArrayList<SQLiteDatabase> getActiveDatabases() {
        ArrayList<SQLiteDatabase> arrayList = new ArrayList<>();
        WeakHashMap<SQLiteDatabase, Object> weakHashMap = sActiveDatabases;
        synchronized (weakHashMap) {
            arrayList.addAll(weakHashMap.keySet());
        }
        return arrayList;
    }

    public static void dumpAll(Printer printer, boolean z) {
        Iterator<SQLiteDatabase> it = getActiveDatabases().iterator();
        while (it.hasNext()) {
            it.next().dump(printer, z);
        }
    }

    public final void dump(Printer printer, boolean z) {
        synchronized (this.mLock) {
            if (this.mConnectionPoolLocked != null) {
                printer.println("");
                this.mConnectionPoolLocked.dump(printer, z);
            }
        }
    }

    public List<Pair<String, String>> getAttachedDbs() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            if (this.mConnectionPoolLocked == null) {
                return null;
            }
            if (!this.mHasAttachedDbsLocked) {
                arrayList.add(new Pair(a.d, this.mConfigurationLocked.path));
                return arrayList;
            }
            acquireReference();
            try {
                Cursor rawQuery = rawQuery("pragma database_list;", null);
                while (rawQuery.moveToNext()) {
                    arrayList.add(new Pair(rawQuery.getString(1), rawQuery.getString(2)));
                }
                rawQuery.close();
                return arrayList;
            } finally {
                releaseReference();
            }
        }
    }

    public String toString() {
        return "SQLiteDatabase: " + getPath();
    }

    public final void throwIfNotOpenLocked() {
        if (this.mConnectionPoolLocked != null) {
            return;
        }
        throw new IllegalStateException("The database '" + this.mConfigurationLocked.label + "' is not open.");
    }

    public static boolean hasCodec() {
        return SQLiteConnection.hasCodec();
    }

    /* loaded from: classes3.dex */
    public static final class OpenParams {
        public final CursorFactory mCursorFactory;
        public final DatabaseErrorHandler mErrorHandler;
        public final long mIdleConnectionTimeout;
        public final String mJournalMode;
        public final int mLookasideSlotCount;
        public final int mLookasideSlotSize;
        public final int mOpenFlags;
        public final String mSyncMode;

        public OpenParams(int i, CursorFactory cursorFactory, DatabaseErrorHandler databaseErrorHandler, int i2, int i3, long j, String str, String str2) {
            this.mOpenFlags = i;
            this.mCursorFactory = cursorFactory;
            this.mErrorHandler = databaseErrorHandler;
            this.mLookasideSlotSize = i2;
            this.mLookasideSlotCount = i3;
            this.mIdleConnectionTimeout = j;
            this.mJournalMode = str;
            this.mSyncMode = str2;
        }

        public Builder toBuilder() {
            return new Builder(this);
        }

        /* loaded from: classes3.dex */
        public static final class Builder {
            public CursorFactory mCursorFactory;
            public DatabaseErrorHandler mErrorHandler;
            public long mIdleConnectionTimeout;
            public String mJournalMode;
            public int mLookasideSlotCount;
            public int mLookasideSlotSize;
            public int mOpenFlags;
            public String mSyncMode;

            public Builder() {
                this.mLookasideSlotSize = -1;
                this.mLookasideSlotCount = -1;
                this.mIdleConnectionTimeout = -1L;
            }

            public Builder(OpenParams openParams) {
                this.mLookasideSlotSize = -1;
                this.mLookasideSlotCount = -1;
                this.mIdleConnectionTimeout = -1L;
                this.mLookasideSlotSize = openParams.mLookasideSlotSize;
                this.mLookasideSlotCount = openParams.mLookasideSlotCount;
                this.mOpenFlags = openParams.mOpenFlags;
                this.mCursorFactory = openParams.mCursorFactory;
                this.mErrorHandler = openParams.mErrorHandler;
                this.mJournalMode = openParams.mJournalMode;
                this.mSyncMode = openParams.mSyncMode;
            }

            public boolean isWriteAheadLoggingEnabled() {
                return (this.mOpenFlags & 536870912) != 0;
            }

            public Builder addOpenFlags(int i) {
                this.mOpenFlags = i | this.mOpenFlags;
                return this;
            }

            public Builder removeOpenFlags(int i) {
                this.mOpenFlags = (~i) & this.mOpenFlags;
                return this;
            }

            public void setWriteAheadLoggingEnabled(boolean z) {
                if (z) {
                    addOpenFlags(536870912);
                } else {
                    removeOpenFlags(536870912);
                }
            }

            public Builder setCursorFactory(CursorFactory cursorFactory) {
                this.mCursorFactory = cursorFactory;
                return this;
            }

            public Builder setErrorHandler(DatabaseErrorHandler databaseErrorHandler) {
                this.mErrorHandler = databaseErrorHandler;
                return this;
            }

            public OpenParams build() {
                return new OpenParams(this.mOpenFlags, this.mCursorFactory, this.mErrorHandler, this.mLookasideSlotSize, this.mLookasideSlotCount, this.mIdleConnectionTimeout, this.mJournalMode, this.mSyncMode);
            }
        }
    }
}
