package kotlin.io;

import java.io.Closeable;
import kotlin.ExceptionsKt__ExceptionsKt;

/* compiled from: Closeable.kt */
/* loaded from: classes3.dex */
public final class CloseableKt {
    public static final void closeFinally(Closeable closeable, Throwable th) {
        if (closeable == null) {
            return;
        }
        if (th == null) {
            closeable.close();
            return;
        }
        try {
            closeable.close();
        } catch (Throwable th2) {
            ExceptionsKt__ExceptionsKt.addSuppressed(th, th2);
        }
    }
}
