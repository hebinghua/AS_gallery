package kotlin.jdk7;

import kotlin.ExceptionsKt__ExceptionsKt;

/* compiled from: AutoCloseable.kt */
/* loaded from: classes3.dex */
public final class AutoCloseableKt {
    public static final void closeFinally(AutoCloseable autoCloseable, Throwable th) {
        if (autoCloseable == null) {
            return;
        }
        if (th == null) {
            autoCloseable.close();
            return;
        }
        try {
            autoCloseable.close();
        } catch (Throwable th2) {
            ExceptionsKt__ExceptionsKt.addSuppressed(th, th2);
        }
    }
}
