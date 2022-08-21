package org.sqlite.database;

import android.util.Log;
import android.util.Pair;
import java.io.File;
import java.util.List;
import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteException;

/* loaded from: classes3.dex */
public final class DefaultDatabaseErrorHandler implements DatabaseErrorHandler {
    @Override // org.sqlite.database.DatabaseErrorHandler
    public void onCorruption(SQLiteDatabase sQLiteDatabase) {
        Log.e("DefaultDatabaseErrorHandler", "Corruption reported by sqlite on database: " + sQLiteDatabase.getPath());
        if (SQLiteDatabase.hasCodec()) {
            return;
        }
        if (!sQLiteDatabase.isOpen()) {
            deleteDatabaseFile(sQLiteDatabase.getPath());
            return;
        }
        List<Pair<String, String>> list = null;
        try {
            try {
                list = sQLiteDatabase.getAttachedDbs();
            } catch (SQLiteException unused) {
            }
            try {
                sQLiteDatabase.close();
            } catch (SQLiteException unused2) {
            }
        } finally {
            if (list != null) {
                for (Pair<String, String> next : list) {
                    deleteDatabaseFile((String) next.second);
                }
            } else {
                deleteDatabaseFile(sQLiteDatabase.getPath());
            }
        }
    }

    public final void deleteDatabaseFile(String str) {
        if (str.equalsIgnoreCase(":memory:") || str.trim().length() == 0) {
            return;
        }
        Log.e("DefaultDatabaseErrorHandler", "deleting the database file: " + str);
        try {
            SQLiteDatabase.deleteDatabase(new File(str));
        } catch (Exception e) {
            Log.w("DefaultDatabaseErrorHandler", "delete failed: " + e.getMessage());
        }
    }
}
