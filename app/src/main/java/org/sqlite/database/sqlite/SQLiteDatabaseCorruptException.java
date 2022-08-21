package org.sqlite.database.sqlite;

/* loaded from: classes3.dex */
public class SQLiteDatabaseCorruptException extends SQLiteException {
    public SQLiteDatabaseCorruptException() {
    }

    public SQLiteDatabaseCorruptException(String str) {
        super(str);
    }

    public static boolean isCorruptException(Throwable th) {
        while (th != null) {
            if (th instanceof SQLiteDatabaseCorruptException) {
                return true;
            }
            th = th.getCause();
        }
        return false;
    }
}
