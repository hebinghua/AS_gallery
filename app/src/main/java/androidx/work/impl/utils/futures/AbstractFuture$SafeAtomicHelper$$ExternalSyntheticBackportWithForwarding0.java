package androidx.work.impl.utils.futures;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AbstractFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0 {
    public static /* synthetic */ boolean m(AtomicReferenceFieldUpdater atomicReferenceFieldUpdater, Object obj, Object obj2, Object obj3) {
        while (!atomicReferenceFieldUpdater.compareAndSet(obj, obj2, obj3)) {
            if (atomicReferenceFieldUpdater.get(obj) != obj2) {
                return false;
            }
        }
        return true;
    }
}
