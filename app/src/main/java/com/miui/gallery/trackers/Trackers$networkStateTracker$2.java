package com.miui.gallery.trackers;

import android.content.Context;
import com.miui.gallery.arch.internal.TaskExecutor;
import com.miui.gallery.util.StaticContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: Trackers.kt */
/* loaded from: classes2.dex */
public final class Trackers$networkStateTracker$2 extends Lambda implements Function0<NetworkStateTracker> {
    public static final Trackers$networkStateTracker$2 INSTANCE = new Trackers$networkStateTracker$2();

    public Trackers$networkStateTracker$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final NetworkStateTracker mo1738invoke() {
        Context sGetAndroidContext = StaticContext.sGetAndroidContext();
        Intrinsics.checkNotNullExpressionValue(sGetAndroidContext, "sGetAndroidContext()");
        return new NetworkStateTracker(sGetAndroidContext, TaskExecutor.INSTANCE);
    }
}
