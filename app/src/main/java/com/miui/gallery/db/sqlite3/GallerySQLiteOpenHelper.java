package com.miui.gallery.db.sqlite3;

import android.content.Context;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.miui.gallery.db.sqlite3.GallerySQLiteOpenHelper;
import com.miui.gallery.util.ProcessLock;
import com.miui.gallery.util.SneakyThrow;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.UUID;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.sqlite.database.DatabaseErrorHandler;
import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteException;
import org.sqlite.database.sqlite.SQLiteOpenHelper;

/* compiled from: GallerySQLiteOpenHelper.kt */
/* loaded from: classes.dex */
public final class GallerySQLiteOpenHelper implements SupportSQLiteOpenHelper {
    public static final Companion Companion = new Companion(null);
    public final boolean allowDataLossOnRecovery;
    public final SupportSQLiteOpenHelper.Callback callback;
    public final Context context;
    public OpenHelper delegate;
    public final Object lock;
    public final String name;
    public final boolean useNoBackupDirectory;
    public boolean writeAheadLoggingEnabled;

    public GallerySQLiteOpenHelper(Context context, String str, SupportSQLiteOpenHelper.Callback callback, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.context = context;
        this.name = str;
        this.callback = callback;
        this.useNoBackupDirectory = z;
        this.allowDataLossOnRecovery = z2;
        this.lock = new Object();
    }

    public final OpenHelper getDelegate() {
        OpenHelper openHelper;
        OpenHelper openHelper2;
        synchronized (this.lock) {
            if (this.delegate == null) {
                GallerySQLiteDatabase[] gallerySQLiteDatabaseArr = new GallerySQLiteDatabase[1];
                if (this.name != null && this.useNoBackupDirectory) {
                    openHelper2 = new OpenHelper(this.context, new File(this.context.getNoBackupFilesDir(), this.name).getAbsolutePath(), gallerySQLiteDatabaseArr, this.callback, this.allowDataLossOnRecovery);
                } else {
                    openHelper2 = new OpenHelper(this.context, this.name, gallerySQLiteDatabaseArr, this.callback, this.allowDataLossOnRecovery);
                }
                openHelper2.setWriteAheadLoggingEnabled(this.writeAheadLoggingEnabled);
                this.delegate = openHelper2;
            }
            openHelper = this.delegate;
            Intrinsics.checkNotNull(openHelper);
        }
        return openHelper;
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        getDelegate().close();
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public String getDatabaseName() {
        return this.name;
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public void setWriteAheadLoggingEnabled(boolean z) {
        synchronized (this.lock) {
            OpenHelper openHelper = this.delegate;
            if (openHelper != null) {
                openHelper.setWriteAheadLoggingEnabled(z);
            }
            this.writeAheadLoggingEnabled = z;
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public SupportSQLiteDatabase getWritableDatabase() {
        return getDelegate().getSupportDatabase(true);
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public SupportSQLiteDatabase getReadableDatabase() {
        return getDelegate().getSupportDatabase(false);
    }

    /* compiled from: GallerySQLiteOpenHelper.kt */
    /* loaded from: classes.dex */
    public static final class OpenHelper extends SQLiteOpenHelper {
        public static final Companion Companion = new Companion(null);
        public final boolean allowDataLossOnRecovery;
        public final Context context;
        public final GallerySQLiteDatabase[] dbRef;
        public final SupportSQLiteOpenHelper.Callback innerCallback;
        public final ProcessLock lock;
        public boolean migrated;
        public boolean opened;

        /* compiled from: GallerySQLiteOpenHelper.kt */
        /* loaded from: classes.dex */
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[Companion.CallbackName.values().length];
                iArr[Companion.CallbackName.ON_CONFIGURE.ordinal()] = 1;
                iArr[Companion.CallbackName.ON_CREATE.ordinal()] = 2;
                iArr[Companion.CallbackName.ON_UPGRADE.ordinal()] = 3;
                iArr[Companion.CallbackName.ON_DOWNGRADE.ordinal()] = 4;
                iArr[Companion.CallbackName.ON_OPEN.ordinal()] = 5;
                $EnumSwitchMapping$0 = iArr;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OpenHelper(Context context, String str, final GallerySQLiteDatabase[] dbRef, final SupportSQLiteOpenHelper.Callback innerCallback, boolean z) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, innerCallback.version, new DatabaseErrorHandler() { // from class: com.miui.gallery.db.sqlite3.GallerySQLiteOpenHelper$OpenHelper$$ExternalSyntheticLambda0
                @Override // org.sqlite.database.DatabaseErrorHandler
                public final void onCorruption(SQLiteDatabase sQLiteDatabase) {
                    GallerySQLiteOpenHelper.OpenHelper.m733_init_$lambda0(SupportSQLiteOpenHelper.Callback.this, dbRef, sQLiteDatabase);
                }
            });
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(dbRef, "dbRef");
            Intrinsics.checkNotNullParameter(innerCallback, "innerCallback");
            this.context = context;
            this.dbRef = dbRef;
            this.innerCallback = innerCallback;
            this.allowDataLossOnRecovery = z;
            if (str == null) {
                str = UUID.randomUUID().toString();
                Intrinsics.checkNotNullExpressionValue(str, "randomUUID().toString()");
            }
            this.lock = new ProcessLock(str, context.getCacheDir(), false);
        }

        /* renamed from: _init_$lambda-0  reason: not valid java name */
        public static final void m733_init_$lambda0(SupportSQLiteOpenHelper.Callback innerCallback, GallerySQLiteDatabase[] dbRef, SQLiteDatabase dbObj) {
            Intrinsics.checkNotNullParameter(innerCallback, "$innerCallback");
            Intrinsics.checkNotNullParameter(dbRef, "$dbRef");
            Companion companion = Companion;
            Intrinsics.checkNotNullExpressionValue(dbObj, "dbObj");
            innerCallback.onCorruption(companion.getWrappedDb(dbRef, dbObj));
        }

        public final SupportSQLiteDatabase getSupportDatabase(boolean z) {
            try {
                this.lock.lock(!this.opened && getDatabaseName() != null);
                this.migrated = false;
                SQLiteDatabase innerGetDatabase = innerGetDatabase(z);
                if (this.migrated) {
                    close();
                    return getSupportDatabase(z);
                }
                Intrinsics.checkNotNull(innerGetDatabase);
                return getWrappedDb(innerGetDatabase);
            } finally {
                this.lock.unlock();
            }
        }

        public final SQLiteDatabase innerGetDatabase(boolean z) {
            File parentFile;
            String databaseName = getDatabaseName();
            if (databaseName != null && (parentFile = this.context.getDatabasePath(databaseName).getParentFile()) != null) {
                parentFile.mkdirs();
                if (!parentFile.isDirectory()) {
                    DefaultLogger.w("GallerySQLiteOpenHelper", Intrinsics.stringPlus("Invalid database parent file, not a directory: ", parentFile));
                }
            }
            try {
                return getWritableOrReadableDatabase(z);
            } catch (Throwable unused) {
                super.close();
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException unused2) {
                }
                try {
                    return getWritableOrReadableDatabase(z);
                } catch (Throwable th) {
                    super.close();
                    if (th instanceof Companion.CallbackException) {
                        Companion.CallbackException callbackException = th;
                        Throwable cause = callbackException.getCause();
                        int i = WhenMappings.$EnumSwitchMapping$0[callbackException.getCallbackName().ordinal()];
                        if (i == 1 || i == 2 || i == 3 || i == 4) {
                            SneakyThrow.reThrow(cause);
                        }
                        if (!(cause instanceof SQLiteException)) {
                            SneakyThrow.reThrow(cause);
                        }
                    } else if (th instanceof SQLiteException) {
                        if (databaseName == null || !this.allowDataLossOnRecovery) {
                            SneakyThrow.reThrow(th);
                        }
                    } else {
                        SneakyThrow.reThrow(th);
                    }
                    this.context.deleteDatabase(databaseName);
                    try {
                        return getWritableOrReadableDatabase(z);
                    } catch (Companion.CallbackException e) {
                        SneakyThrow.reThrow(e.getCause());
                        return null;
                    }
                }
            }
        }

        public final SQLiteDatabase getWritableOrReadableDatabase(boolean z) {
            if (z) {
                return super.getWritableDatabase();
            }
            return super.getReadableDatabase();
        }

        public final GallerySQLiteDatabase getWrappedDb(SQLiteDatabase sQLiteDatabase) {
            return Companion.getWrappedDb(this.dbRef, sQLiteDatabase);
        }

        @Override // org.sqlite.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Intrinsics.checkNotNullParameter(sqLiteDatabase, "sqLiteDatabase");
            try {
                this.innerCallback.onCreate(getWrappedDb(sqLiteDatabase));
            } catch (Throwable th) {
                throw new Companion.CallbackException(Companion.CallbackName.ON_CREATE, th);
            }
        }

        @Override // org.sqlite.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            Intrinsics.checkNotNullParameter(sqLiteDatabase, "sqLiteDatabase");
            this.migrated = true;
            try {
                this.innerCallback.onUpgrade(getWrappedDb(sqLiteDatabase), i, i2);
            } catch (Throwable th) {
                throw new Companion.CallbackException(Companion.CallbackName.ON_UPGRADE, th);
            }
        }

        @Override // org.sqlite.database.sqlite.SQLiteOpenHelper
        public void onConfigure(SQLiteDatabase db) {
            Intrinsics.checkNotNullParameter(db, "db");
            try {
                this.innerCallback.onConfigure(getWrappedDb(db));
            } catch (Throwable th) {
                throw new Companion.CallbackException(Companion.CallbackName.ON_CONFIGURE, th);
            }
        }

        @Override // org.sqlite.database.sqlite.SQLiteOpenHelper
        public void onDowngrade(SQLiteDatabase db, int i, int i2) {
            Intrinsics.checkNotNullParameter(db, "db");
            this.migrated = true;
            try {
                this.innerCallback.onDowngrade(getWrappedDb(db), i, i2);
            } catch (Throwable th) {
                throw new Companion.CallbackException(Companion.CallbackName.ON_DOWNGRADE, th);
            }
        }

        @Override // org.sqlite.database.sqlite.SQLiteOpenHelper
        public void onOpen(SQLiteDatabase db) {
            Intrinsics.checkNotNullParameter(db, "db");
            if (!this.migrated) {
                try {
                    this.innerCallback.onOpen(getWrappedDb(db));
                } catch (Throwable th) {
                    throw new Companion.CallbackException(Companion.CallbackName.ON_OPEN, th);
                }
            }
            this.opened = true;
        }

        @Override // org.sqlite.database.sqlite.SQLiteOpenHelper, java.lang.AutoCloseable
        public void close() {
            try {
                this.lock.lock();
                super.close();
                this.dbRef[0] = null;
                this.opened = false;
            } finally {
                this.lock.unlock();
            }
        }

        /* compiled from: GallerySQLiteOpenHelper.kt */
        /* loaded from: classes.dex */
        public static final class Companion {

            /* compiled from: GallerySQLiteOpenHelper.kt */
            /* loaded from: classes.dex */
            public enum CallbackName {
                ON_CONFIGURE,
                ON_CREATE,
                ON_UPGRADE,
                ON_DOWNGRADE,
                ON_OPEN
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }

            public final GallerySQLiteDatabase getWrappedDb(GallerySQLiteDatabase[] refHolder, SQLiteDatabase sqLiteDatabase) {
                Intrinsics.checkNotNullParameter(refHolder, "refHolder");
                Intrinsics.checkNotNullParameter(sqLiteDatabase, "sqLiteDatabase");
                GallerySQLiteDatabase gallerySQLiteDatabase = refHolder[0];
                if (gallerySQLiteDatabase == null || !gallerySQLiteDatabase.isDelegate(sqLiteDatabase)) {
                    refHolder[0] = new GallerySQLiteDatabase(sqLiteDatabase);
                }
                GallerySQLiteDatabase gallerySQLiteDatabase2 = refHolder[0];
                Intrinsics.checkNotNull(gallerySQLiteDatabase2);
                return gallerySQLiteDatabase2;
            }

            /* compiled from: GallerySQLiteOpenHelper.kt */
            /* loaded from: classes.dex */
            public static final class CallbackException extends RuntimeException {
                private final CallbackName callbackName;
                private final Throwable cause;

                public final CallbackName getCallbackName() {
                    return this.callbackName;
                }

                @Override // java.lang.Throwable
                public Throwable getCause() {
                    return this.cause;
                }

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                public CallbackException(CallbackName callbackName, Throwable cause) {
                    super(cause);
                    Intrinsics.checkNotNullParameter(callbackName, "callbackName");
                    Intrinsics.checkNotNullParameter(cause, "cause");
                    this.callbackName = callbackName;
                    this.cause = cause;
                }
            }
        }
    }

    /* compiled from: GallerySQLiteOpenHelper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
