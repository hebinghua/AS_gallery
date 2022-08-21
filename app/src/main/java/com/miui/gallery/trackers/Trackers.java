package com.miui.gallery.trackers;

import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;

/* compiled from: Trackers.kt */
/* loaded from: classes2.dex */
public final class Trackers {
    public static final Trackers INSTANCE = new Trackers();
    public static final Lazy networkStateTracker$delegate = LazyKt__LazyJVMKt.lazy(Trackers$networkStateTracker$2.INSTANCE);

    public static final NetworkStateTracker getNetworkStateTracker() {
        return (NetworkStateTracker) networkStateTracker$delegate.mo119getValue();
    }
}
