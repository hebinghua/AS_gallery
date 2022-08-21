package org.sqlite.database.sqlite;

import android.util.Log;
import android.util.Printer;
import ch.qos.logback.core.util.FileSize;
import com.android.internal.SystemPropertiesCompat;
import com.xiaomi.stat.b.h;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public final class SQLiteDebug {

    /* loaded from: classes3.dex */
    public static class PagerStats {
        public ArrayList<DbStats> dbStats;
        public int largestMemAlloc;
        public int memoryUsed;
        public int pageCacheOverflow;
    }

    private static native void nativeGetPagerStats(PagerStats pagerStats);

    static {
        SQLiteGlobal.loadLib();
    }

    public static boolean shouldLogSlowQuery(long j) {
        int i = SystemPropertiesCompat.getInt("db.log.slow_query_threshold", 10000);
        return i >= 0 && j >= ((long) i);
    }

    /* loaded from: classes3.dex */
    public static class DbStats {
        public String cache;
        public String dbName;
        public long dbSize;
        public int lookaside;
        public long pageSize;

        public DbStats(String str, long j, long j2, int i, int i2, int i3, int i4) {
            this.dbName = str;
            this.pageSize = j2 / FileSize.KB_COEFFICIENT;
            this.dbSize = (j * j2) / FileSize.KB_COEFFICIENT;
            this.lookaside = i;
            this.cache = i2 + h.g + i3 + h.g + i4;
        }
    }

    public static void dump(Printer printer, String[] strArr) {
        boolean z = false;
        for (String str : strArr) {
            if (str.equals("-v")) {
                z = true;
            }
        }
        SQLiteDatabase.dumpAll(printer, z);
    }

    /* loaded from: classes3.dex */
    public static final class NoPreloadHolder {
        public static final boolean DEBUG_LOG_DETAILED;
        public static final boolean DEBUG_LOG_SLOW_QUERIES;
        public static final boolean DEBUG_SQL_LOG = Log.isLoggable("SQLiteLog", 2);
        public static final boolean DEBUG_SQL_STATEMENTS = Log.isLoggable("SQLiteStatements", 2);
        public static final boolean DEBUG_SQL_TIME = Log.isLoggable("SQLiteTime", 2);
        private static final String SLOW_QUERY_THRESHOLD_PROP = "db.log.slow_query_threshold";

        static {
            boolean z = false;
            DEBUG_LOG_SLOW_QUERIES = SystemPropertiesCompat.getInt("ro.debuggable", 0) == 1;
            if (SystemPropertiesCompat.getInt("ro.debuggable", 0) == 1 && SystemPropertiesCompat.getBoolean("db.log.detailed", false)) {
                z = true;
            }
            DEBUG_LOG_DETAILED = z;
        }
    }
}
