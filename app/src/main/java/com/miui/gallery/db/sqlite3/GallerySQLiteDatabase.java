package com.miui.gallery.db.sqlite3;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.CancellationSignal;
import android.util.Pair;
import androidx.sqlite.db.CursorSpec;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.sqlite.database.sqlite.SQLiteCursor;
import org.sqlite.database.sqlite.SQLiteCursorDriver;
import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteDirectCursor;
import org.sqlite.database.sqlite.SQLiteProgram;
import org.sqlite.database.sqlite.SQLiteQuery;
import org.sqlite.database.sqlite.SQLiteStatement;

/* compiled from: GallerySQLiteDatabase.kt */
/* loaded from: classes.dex */
public final class GallerySQLiteDatabase implements SupportSQLiteDatabase {
    public final SQLiteDatabase delegate;
    public static final Companion Companion = new Companion(null);
    public static final String[] CONFLICT_VALUES = {"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public GallerySQLiteDatabase(SQLiteDatabase delegate) {
        Intrinsics.checkNotNullParameter(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.delegate.close();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public SupportSQLiteStatement compileStatement(String str) {
        SQLiteStatement compileStatement = this.delegate.compileStatement(str);
        Intrinsics.checkNotNullExpressionValue(compileStatement, "delegate.compileStatement(sql)");
        return new GallerySQLiteStatement(compileStatement);
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public void beginTransaction() {
        this.delegate.beginTransaction();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public void beginTransactionNonExclusive() {
        this.delegate.beginTransactionNonExclusive();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public void endTransaction() {
        this.delegate.endTransaction();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public void setTransactionSuccessful() {
        this.delegate.setTransactionSuccessful();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public boolean inTransaction() {
        return this.delegate.inTransaction();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public int getVersion() {
        return this.delegate.getVersion();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public Cursor query(String str) {
        return query(new SimpleSQLiteQuery(str));
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public Cursor query(String str, Object[] objArr) {
        return query(new SimpleSQLiteQuery(str, objArr));
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public Cursor query(SupportSQLiteQuery supportQuery) {
        Intrinsics.checkNotNullParameter(supportQuery, "supportQuery");
        return query(supportQuery, (CancellationSignal) null);
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public Cursor query(final SupportSQLiteQuery supportQuery, CancellationSignal cancellationSignal) {
        Intrinsics.checkNotNullParameter(supportQuery, "supportQuery");
        Cursor rawQueryWithFactory = this.delegate.rawQueryWithFactory(new SQLiteDatabase.CursorFactory() { // from class: com.miui.gallery.db.sqlite3.GallerySQLiteDatabase$query$1
            @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteProgram query) {
                Intrinsics.checkNotNullParameter(db, "db");
                Intrinsics.checkNotNullParameter(query, "query");
                SupportSQLiteQuery.this.bindTo(new GallerySQLiteProgram(query));
                Cursor newCursor = SQLiteCursor.FACTORY.newCursor(db, sQLiteCursorDriver, str, query);
                Intrinsics.checkNotNullExpressionValue(newCursor, "FACTORY.newCursor(db, ma…rQuery, editTable, query)");
                return newCursor;
            }

            @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
            public SQLiteProgram newQuery(SQLiteDatabase db, String str, Object[] objArr, CancellationSignal cancellationSignal2) {
                Intrinsics.checkNotNullParameter(db, "db");
                SQLiteProgram newQuery = SQLiteCursor.FACTORY.newQuery(db, str, objArr, cancellationSignal2);
                Intrinsics.checkNotNullExpressionValue(newQuery, "FACTORY.newQuery(\n      …are\n                    )");
                return newQuery;
            }
        }, supportQuery.getSql(), EMPTY_STRING_ARRAY, null, cancellationSignal);
        Intrinsics.checkNotNullExpressionValue(rawQueryWithFactory, "supportQuery: SupportSQL…cellationSignal\n        )");
        return rawQueryWithFactory;
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public Cursor query(SupportSQLiteQuery supportQuery, CursorSpec cursorSpec) {
        Intrinsics.checkNotNullParameter(supportQuery, "supportQuery");
        Intrinsics.checkNotNullParameter(cursorSpec, "cursorSpec");
        return query(supportQuery, cursorSpec, null);
    }

    public Cursor query(final SupportSQLiteQuery supportQuery, CursorSpec cursorSpec, CancellationSignal cancellationSignal) {
        Intrinsics.checkNotNullParameter(supportQuery, "supportQuery");
        Intrinsics.checkNotNullParameter(cursorSpec, "cursorSpec");
        final SQLiteDatabase.CursorFactory createDelegateFactoryBy = Companion.createDelegateFactoryBy(cursorSpec);
        Cursor rawQueryWithFactory = this.delegate.rawQueryWithFactory(new SQLiteDatabase.CursorFactory() { // from class: com.miui.gallery.db.sqlite3.GallerySQLiteDatabase$query$2
            @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteProgram query) {
                Intrinsics.checkNotNullParameter(db, "db");
                Intrinsics.checkNotNullParameter(query, "query");
                SupportSQLiteQuery.this.bindTo(new GallerySQLiteProgram(query));
                Cursor newCursor = createDelegateFactoryBy.newCursor(db, sQLiteCursorDriver, str, query);
                Intrinsics.checkNotNullExpressionValue(newCursor, "delegateFactory.newCurso…rQuery, editTable, query)");
                return newCursor;
            }

            @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
            public SQLiteProgram newQuery(SQLiteDatabase db, String str, Object[] objArr, CancellationSignal cancellationSignal2) {
                Intrinsics.checkNotNullParameter(db, "db");
                SQLiteProgram newQuery = createDelegateFactoryBy.newQuery(db, str, objArr, cancellationSignal2);
                Intrinsics.checkNotNullExpressionValue(newQuery, "delegateFactory.newQuery…ellationSignalForPrepare)");
                return newQuery;
            }
        }, supportQuery.getSql(), EMPTY_STRING_ARRAY, null, cancellationSignal);
        Intrinsics.checkNotNullExpressionValue(rawQueryWithFactory, "supportQuery: SupportSQL…cellationSignal\n        )");
        return rawQueryWithFactory;
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public long insert(String str, int i, ContentValues contentValues) {
        return this.delegate.insertWithOnConflict(str, null, contentValues, i);
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public int delete(String table, String str, Object[] objArr) {
        Intrinsics.checkNotNullParameter(table, "table");
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(table);
        sb.append(str == null || str.length() == 0 ? "" : Intrinsics.stringPlus(" WHERE ", str));
        SupportSQLiteStatement compileStatement = compileStatement(sb.toString());
        SimpleSQLiteQuery.bind(compileStatement, objArr);
        return compileStatement.executeUpdateDelete();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public int update(String table, int i, ContentValues contentValues, String str, Object[] objArr) {
        Intrinsics.checkNotNullParameter(table, "table");
        boolean z = false;
        if (!((contentValues == null || contentValues.size() == 0) ? false : true)) {
            throw new IllegalArgumentException("Empty values".toString());
        }
        StringBuilder sb = new StringBuilder(120);
        sb.append("UPDATE ");
        sb.append(CONFLICT_VALUES[i]);
        sb.append(table);
        sb.append(" SET ");
        int size = contentValues.size();
        int length = objArr == null ? size : objArr.length + size;
        Object[] objArr2 = new Object[length];
        int i2 = 0;
        for (String str2 : contentValues.keySet()) {
            sb.append(i2 > 0 ? "," : "");
            sb.append(str2);
            objArr2[i2] = contentValues.get(str2);
            sb.append("=?");
            i2++;
        }
        if (objArr != null) {
            for (int i3 = size; i3 < length; i3++) {
                objArr2[i3] = objArr[i3 - size];
            }
        }
        if (str == null || str.length() == 0) {
            z = true;
        }
        if (!z) {
            sb.append(" WHERE ");
            sb.append(str);
        }
        SupportSQLiteStatement compileStatement = compileStatement(sb.toString());
        SimpleSQLiteQuery.bind(compileStatement, objArr2);
        return compileStatement.executeUpdateDelete();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public void execSQL(String str) {
        this.delegate.execSQL(str);
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public void execSQL(String str, Object[] objArr) {
        this.delegate.execSQL(str, objArr);
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public boolean isOpen() {
        return this.delegate.isOpen();
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public String getPath() {
        String path = this.delegate.getPath();
        Intrinsics.checkNotNullExpressionValue(path, "delegate.path");
        return path;
    }

    @Override // androidx.sqlite.db.SupportSQLiteDatabase
    public List<Pair<String, String>> getAttachedDbs() {
        List<Pair<String, String>> attachedDbs = this.delegate.getAttachedDbs();
        Intrinsics.checkNotNullExpressionValue(attachedDbs, "delegate.attachedDbs");
        return attachedDbs;
    }

    public final boolean isDelegate(SQLiteDatabase sQLiteDatabase) {
        return this.delegate == sQLiteDatabase;
    }

    /* compiled from: GallerySQLiteDatabase.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final SQLiteDatabase.CursorFactory createDelegateFactoryBy(final CursorSpec cursorSpec) {
            if (cursorSpec.isForwardOnly()) {
                SQLiteDatabase.CursorFactory cursorFactory = SQLiteDirectCursor.FACTORY;
                Intrinsics.checkNotNullExpressionValue(cursorFactory, "{\n                SQLite…sor.FACTORY\n            }");
                return cursorFactory;
            }
            return new SQLiteDatabase.CursorFactory() { // from class: com.miui.gallery.db.sqlite3.GallerySQLiteDatabase$Companion$createDelegateFactoryBy$1
                @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
                public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteProgram query) {
                    Intrinsics.checkNotNullParameter(db, "db");
                    Intrinsics.checkNotNullParameter(query, "query");
                    SQLiteCursor sQLiteCursor = new SQLiteCursor(sQLiteCursorDriver, str, (SQLiteQuery) query);
                    CursorSpec cursorSpec2 = CursorSpec.this;
                    sQLiteCursor.setFillWindowForwardOnly(true);
                    sQLiteCursor.setWindowSizeBytes(cursorSpec2.getWindowSizeBytes());
                    return sQLiteCursor;
                }

                @Override // org.sqlite.database.sqlite.SQLiteDatabase.CursorFactory
                public SQLiteProgram newQuery(SQLiteDatabase db, String str, Object[] objArr, CancellationSignal cancellationSignal) {
                    Intrinsics.checkNotNullParameter(db, "db");
                    SQLiteProgram newQuery = SQLiteCursor.FACTORY.newQuery(db, str, objArr, cancellationSignal);
                    Intrinsics.checkNotNullExpressionValue(newQuery, "FACTORY.newQuery(\n      …                        )");
                    return newQuery;
                }
            };
        }
    }
}
