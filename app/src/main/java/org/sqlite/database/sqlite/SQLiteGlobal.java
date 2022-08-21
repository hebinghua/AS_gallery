package org.sqlite.database.sqlite;

import android.os.StatFs;
import com.android.internal.SystemPropertiesCompat;
import com.nexstreaming.nexeditorsdk.nexEngine;

/* loaded from: classes3.dex */
public final class SQLiteGlobal {
    public static int sDefaultPageSize;
    public static final Object sLock = new Object();

    public static String getDefaultJournalMode() {
        return "delete";
    }

    public static String getDefaultSyncMode() {
        return "normal";
    }

    public static int getJournalSizeLimit() {
        return 10000;
    }

    public static String getWALSyncMode() {
        return "normal";
    }

    public static void loadLib() {
    }

    private static native int nativeReleaseMemory();

    static {
        if (!SqliteXInitializationProbe.libLoaded) {
            System.loadLibrary("sqliteX");
        }
    }

    public static int getDefaultPageSize() {
        synchronized (sLock) {
            if (sDefaultPageSize == 0) {
                sDefaultPageSize = new StatFs("/data").getBlockSize();
            }
        }
        return 1024;
    }

    public static int getWALAutoCheckpoint() {
        return Math.max(1, 1000);
    }

    public static int getWALConnectionPoolSize() {
        return Math.max(2, 4);
    }

    public static long getWALTruncateSize() {
        return SystemPropertiesCompat.getInt("debug.sqlite.wal.truncatesize", nexEngine.ExportHEVCMainTierLevel6);
    }
}
