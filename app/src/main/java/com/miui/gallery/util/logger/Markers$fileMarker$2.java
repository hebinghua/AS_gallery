package com.miui.gallery.util.logger;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/* compiled from: Markers.kt */
/* loaded from: classes2.dex */
public final class Markers$fileMarker$2 extends Lambda implements Function0<Marker> {
    public static final Markers$fileMarker$2 INSTANCE = new Markers$fileMarker$2();

    public Markers$fileMarker$2() {
        super(0);
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final Marker mo1738invoke() {
        return MarkerFactory.getMarker("GalleryFileMarker");
    }
}
