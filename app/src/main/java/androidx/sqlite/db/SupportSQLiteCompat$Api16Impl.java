package androidx.sqlite.db;

import android.database.sqlite.SQLiteDatabase;
import java.io.File;

/* loaded from: classes.dex */
public final class SupportSQLiteCompat$Api16Impl {
    public static boolean deleteDatabase(File file) {
        return SQLiteDatabase.deleteDatabase(file);
    }
}
