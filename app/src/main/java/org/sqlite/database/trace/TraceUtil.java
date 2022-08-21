package org.sqlite.database.trace;

/* loaded from: classes3.dex */
public final class TraceUtil {
    public static final TraceHelper sHelper = new TraceHelper("SQLiteTrace");

    public static void trackBegin(String str) {
        sHelper.traceBegin(str);
    }

    public static void trackEnd() {
        sHelper.traceEnd();
    }
}
