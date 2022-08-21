package androidx.lifecycle;

import android.util.Log;
import java.io.Closeable;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ViewModelX.kt */
/* loaded from: classes.dex */
public final class CloseableContainer implements Closeable {
    public final Set<Closeable> closeableSet = Collections.newSetFromMap(new IdentityHashMap(2));

    public final void add(Closeable closeable) {
        Intrinsics.checkNotNullParameter(closeable, "closeable");
        this.closeableSet.add(closeable);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Object m2569constructorimpl;
        for (Closeable closeable : this.closeableSet) {
            try {
                Result.Companion companion = Result.Companion;
                closeable.close();
                m2569constructorimpl = Result.m2569constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                m2569constructorimpl = Result.m2569constructorimpl(ResultKt.createFailure(th));
            }
            Throwable m2571exceptionOrNullimpl = Result.m2571exceptionOrNullimpl(m2569constructorimpl);
            if (m2571exceptionOrNullimpl != null) {
                Log.e("CloseableContainer", Intrinsics.stringPlus("Failed to close: ", closeable), m2571exceptionOrNullimpl);
            }
        }
        this.closeableSet.clear();
    }
}
