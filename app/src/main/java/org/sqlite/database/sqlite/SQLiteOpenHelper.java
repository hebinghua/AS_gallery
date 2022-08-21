package org.sqlite.database.sqlite;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.util.Objects;
import org.sqlite.database.DatabaseErrorHandler;
import org.sqlite.database.sqlite.SQLiteDatabase;

/* loaded from: classes3.dex */
public abstract class SQLiteOpenHelper implements AutoCloseable {
    public static final String TAG = SQLiteOpenHelper.class.getSimpleName();
    public final Context mContext;
    public SQLiteDatabase mDatabase;
    public boolean mIsInitializing;
    public final int mMinimumSupportedVersion;
    public final String mName;
    public final int mNewVersion;
    public SQLiteDatabase.OpenParams.Builder mOpenParamsBuilder;

    public void onBeforeDelete(SQLiteDatabase sQLiteDatabase) {
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
    }

    public abstract void onCreate(SQLiteDatabase sQLiteDatabase);

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
    }

    public abstract void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

    static {
        SQLiteGlobal.loadLib();
    }

    public SQLiteOpenHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i, DatabaseErrorHandler databaseErrorHandler) {
        this(context, str, cursorFactory, i, 0, databaseErrorHandler);
    }

    public SQLiteOpenHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i, int i2, DatabaseErrorHandler databaseErrorHandler) {
        this(context, str, i, i2, new SQLiteDatabase.OpenParams.Builder());
        this.mOpenParamsBuilder.setCursorFactory(cursorFactory);
        this.mOpenParamsBuilder.setErrorHandler(databaseErrorHandler);
    }

    public SQLiteOpenHelper(Context context, String str, int i, int i2, SQLiteDatabase.OpenParams.Builder builder) {
        Objects.requireNonNull(builder);
        if (i < 1) {
            throw new IllegalArgumentException("Version must be >= 1, was " + i);
        }
        this.mContext = context;
        this.mName = str;
        this.mNewVersion = i;
        this.mMinimumSupportedVersion = Math.max(0, i2);
        setOpenParamsBuilder(builder);
    }

    public String getDatabaseName() {
        return this.mName;
    }

    public void setWriteAheadLoggingEnabled(boolean z) {
        synchronized (this) {
            if (this.mOpenParamsBuilder.isWriteAheadLoggingEnabled() != z) {
                SQLiteDatabase sQLiteDatabase = this.mDatabase;
                if (sQLiteDatabase != null && sQLiteDatabase.isOpen() && !this.mDatabase.isReadOnly()) {
                    if (z) {
                        this.mDatabase.enableWriteAheadLogging();
                    } else {
                        this.mDatabase.disableWriteAheadLogging();
                    }
                }
                this.mOpenParamsBuilder.setWriteAheadLoggingEnabled(z);
            }
        }
    }

    public final void setOpenParamsBuilder(SQLiteDatabase.OpenParams.Builder builder) {
        this.mOpenParamsBuilder = builder;
        builder.addOpenFlags(268435456);
    }

    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(true);
        }
        return databaseLocked;
    }

    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(false);
        }
        return databaseLocked;
    }

    public final SQLiteDatabase getDatabaseLocked(boolean z) {
        SQLiteDatabase sQLiteDatabase = this.mDatabase;
        if (sQLiteDatabase != null) {
            if (!sQLiteDatabase.isOpen()) {
                this.mDatabase = null;
            } else if (!z || !this.mDatabase.isReadOnly()) {
                return this.mDatabase;
            }
        }
        if (this.mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }
        SQLiteDatabase sQLiteDatabase2 = this.mDatabase;
        try {
            this.mIsInitializing = true;
            if (sQLiteDatabase2 != null) {
                if (z && sQLiteDatabase2.isReadOnly()) {
                    sQLiteDatabase2.reopenReadWrite();
                }
            } else {
                String str = this.mName;
                if (str == null) {
                    sQLiteDatabase2 = SQLiteDatabase.createInMemory(this.mOpenParamsBuilder.build());
                } else {
                    File databasePath = this.mContext.getDatabasePath(str);
                    SQLiteDatabase.OpenParams build = this.mOpenParamsBuilder.build();
                    try {
                        sQLiteDatabase2 = SQLiteDatabase.openDatabase(databasePath, build);
                    } catch (SQLiteException e) {
                        if (z) {
                            throw e;
                        }
                        String str2 = TAG;
                        Log.e(str2, "Couldn't open " + this.mName + " for writing (will try read-only):", e);
                        sQLiteDatabase2 = SQLiteDatabase.openDatabase(databasePath, build.toBuilder().addOpenFlags(1).build());
                    }
                }
            }
            onConfigure(sQLiteDatabase2);
            int version = sQLiteDatabase2.getVersion();
            if (version != this.mNewVersion) {
                if (sQLiteDatabase2.isReadOnly()) {
                    throw new SQLiteException("Can't upgrade read-only database from version " + sQLiteDatabase2.getVersion() + " to " + this.mNewVersion + ": " + this.mName);
                } else if (version > 0 && version < this.mMinimumSupportedVersion) {
                    File file = new File(sQLiteDatabase2.getPath());
                    onBeforeDelete(sQLiteDatabase2);
                    sQLiteDatabase2.close();
                    if (SQLiteDatabase.deleteDatabase(file)) {
                        this.mIsInitializing = false;
                        SQLiteDatabase databaseLocked = getDatabaseLocked(z);
                        this.mIsInitializing = false;
                        if (sQLiteDatabase2 != this.mDatabase) {
                            sQLiteDatabase2.close();
                        }
                        return databaseLocked;
                    }
                    throw new IllegalStateException("Unable to delete obsolete database " + this.mName + " with version " + version);
                } else {
                    sQLiteDatabase2.beginTransaction();
                    if (version == 0) {
                        onCreate(sQLiteDatabase2);
                    } else {
                        int i = this.mNewVersion;
                        if (version > i) {
                            onDowngrade(sQLiteDatabase2, version, i);
                        } else {
                            onUpgrade(sQLiteDatabase2, version, i);
                        }
                    }
                    sQLiteDatabase2.setVersion(this.mNewVersion);
                    sQLiteDatabase2.setTransactionSuccessful();
                    sQLiteDatabase2.endTransaction();
                }
            }
            onOpen(sQLiteDatabase2);
            if (sQLiteDatabase2.isReadOnly()) {
                String str3 = TAG;
                Log.w(str3, "Opened " + this.mName + " in read-only mode");
            }
            this.mDatabase = sQLiteDatabase2;
            this.mIsInitializing = false;
            return sQLiteDatabase2;
        } catch (Throwable th) {
            this.mIsInitializing = false;
            if (sQLiteDatabase2 != null && sQLiteDatabase2 != this.mDatabase) {
                sQLiteDatabase2.close();
            }
            throw th;
        }
    }

    @Override // java.lang.AutoCloseable
    public synchronized void close() {
        if (this.mIsInitializing) {
            throw new IllegalStateException("Closed during initialization");
        }
        SQLiteDatabase sQLiteDatabase = this.mDatabase;
        if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
            this.mDatabase.close();
            this.mDatabase = null;
        }
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        throw new SQLiteException("Can't downgrade database from version " + i + " to " + i2);
    }
}
