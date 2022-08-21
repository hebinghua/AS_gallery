package org.sqlite.database;

/* loaded from: classes3.dex */
public class SQLException extends RuntimeException {
    public SQLException() {
    }

    public SQLException(String str) {
        super(str);
    }

    public SQLException(String str, Throwable th) {
        super(str, th);
    }
}
