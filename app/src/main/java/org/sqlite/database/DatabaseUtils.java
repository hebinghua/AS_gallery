package org.sqlite.database;

import android.util.Log;
import java.text.Collator;
import java.util.Locale;
import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteStatement;

/* loaded from: classes3.dex */
public class DatabaseUtils {
    public static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static Collator mColl = null;

    public static int getTypeOfObject(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof byte[]) {
            return 4;
        }
        if ((obj instanceof Float) || (obj instanceof Double)) {
            return 2;
        }
        return ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte)) ? 1 : 3;
    }

    public static int cursorPickFillWindowStartPosition(int i, int i2) {
        return Math.max(i - (i2 / 3), 0);
    }

    public static long longForQuery(SQLiteDatabase sQLiteDatabase, String str, String[] strArr) {
        SQLiteStatement compileStatement = sQLiteDatabase.compileStatement(str);
        try {
            return longForQuery(compileStatement, strArr);
        } finally {
            compileStatement.close();
        }
    }

    public static long longForQuery(SQLiteStatement sQLiteStatement, String[] strArr) {
        sQLiteStatement.bindAllArgsAsStrings(strArr);
        return sQLiteStatement.simpleQueryForLong();
    }

    public static int getSqlStatementType(String str) {
        String trim = str.trim();
        if (trim.length() < 3) {
            return 99;
        }
        String substring = trim.substring(0, 3);
        Locale locale = Locale.ROOT;
        String upperCase = substring.toUpperCase(locale);
        if (upperCase.equals("SEL")) {
            return 1;
        }
        if (upperCase.equals("INS") || upperCase.equals("UPD") || upperCase.equals("REP") || upperCase.equals("DEL")) {
            return 2;
        }
        if (upperCase.equals("ATT")) {
            return 3;
        }
        if (upperCase.equals("COM") || upperCase.equals("END")) {
            return 5;
        }
        if (upperCase.equals("ROL")) {
            if (!trim.toUpperCase(locale).contains(" TO ")) {
                return 6;
            }
            Log.w("DatabaseUtils", "Statement '" + trim + "' may not work on API levels 16-27, use ';" + trim + "' instead");
            return 99;
        } else if (upperCase.equals("BEG")) {
            return 4;
        } else {
            if (upperCase.equals("PRA")) {
                return 7;
            }
            if (upperCase.equals("CRE") || upperCase.equals("DRO") || upperCase.equals("ALT")) {
                return 8;
            }
            return (upperCase.equals("ANA") || upperCase.equals("DET")) ? 9 : 99;
        }
    }
}
