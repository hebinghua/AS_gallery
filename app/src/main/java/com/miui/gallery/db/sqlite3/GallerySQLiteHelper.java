package com.miui.gallery.db.sqlite3;

import android.util.Printer;
import androidx.sqlite.db.SupportSQLiteDatabase;
import kotlin.jvm.internal.Intrinsics;
import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteDebug;

/* compiled from: GallerySQLiteHelper.kt */
/* loaded from: classes.dex */
public final class GallerySQLiteHelper {
    public static final GallerySQLiteHelper INSTANCE = new GallerySQLiteHelper();

    public static final SupportSQLiteDatabase createInMemory() {
        SQLiteDatabase create = SQLiteDatabase.create(null);
        Intrinsics.checkNotNullExpressionValue(create, "create(null)");
        return new GallerySQLiteDatabase(create);
    }

    public static final void dump(String[] args, Printer printer) {
        Intrinsics.checkNotNullParameter(args, "args");
        Intrinsics.checkNotNullParameter(printer, "printer");
        SQLiteDebug.dump(printer, args);
    }
}
