package org.sqlite.database.sqlite;

import android.util.Log;
import java.util.Objects;

/* loaded from: classes3.dex */
public final class CloseGuard {
    public Throwable allocationSite;
    public static final CloseGuard NOOP = new CloseGuard();
    public static volatile boolean ENABLED = true;
    public static volatile Reporter REPORTER = new DefaultReporter();

    /* loaded from: classes3.dex */
    public interface Reporter {
        void report(String str, Throwable th);
    }

    public static CloseGuard get() {
        if (!ENABLED) {
            return NOOP;
        }
        return new CloseGuard();
    }

    public void open(String str) {
        Objects.requireNonNull(str, "closer == null");
        if (this == NOOP || !ENABLED) {
            return;
        }
        this.allocationSite = new Throwable("Explicit termination method '" + str + "' not called");
    }

    public void close() {
        this.allocationSite = null;
    }

    public void warnIfOpen() {
        if (this.allocationSite == null || !ENABLED) {
            return;
        }
        REPORTER.report("A resource was acquired at attached stack trace but never released. See java.io.Closeable for information on avoiding resource leaks.", this.allocationSite);
    }

    /* loaded from: classes3.dex */
    public static final class DefaultReporter implements Reporter {
        public DefaultReporter() {
        }

        @Override // org.sqlite.database.sqlite.CloseGuard.Reporter
        public void report(String str, Throwable th) {
            Log.w(str, th);
        }
    }
}
