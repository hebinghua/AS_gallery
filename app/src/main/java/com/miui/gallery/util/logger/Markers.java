package com.miui.gallery.util.logger;

import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.internal.Intrinsics;
import org.slf4j.Marker;

/* compiled from: Markers.kt */
/* loaded from: classes2.dex */
public final class Markers {
    public static final Markers INSTANCE = new Markers();
    public static final Lazy fileMarker$delegate = LazyKt__LazyJVMKt.lazy(Markers$fileMarker$2.INSTANCE);
    public static final Lazy replayMarker$delegate = LazyKt__LazyJVMKt.lazy(Markers$replayMarker$2.INSTANCE);
    public static final Lazy fileOnlyMarker$delegate = LazyKt__LazyJVMKt.lazy(Markers$fileOnlyMarker$2.INSTANCE);

    public static final Marker getFileMarker() {
        Object mo119getValue = fileMarker$delegate.mo119getValue();
        Intrinsics.checkNotNullExpressionValue(mo119getValue, "<get-fileMarker>(...)");
        return (Marker) mo119getValue;
    }

    public static final Marker getReplayMarker() {
        Object mo119getValue = replayMarker$delegate.mo119getValue();
        Intrinsics.checkNotNullExpressionValue(mo119getValue, "<get-replayMarker>(...)");
        return (Marker) mo119getValue;
    }

    public static final Marker getFileOnlyMarker() {
        Object mo119getValue = fileOnlyMarker$delegate.mo119getValue();
        Intrinsics.checkNotNullExpressionValue(mo119getValue, "<get-fileOnlyMarker>(...)");
        return (Marker) mo119getValue;
    }
}
