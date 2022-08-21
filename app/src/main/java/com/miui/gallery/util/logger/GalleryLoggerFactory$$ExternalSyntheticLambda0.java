package com.miui.gallery.util.logger;

import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusListener;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class GalleryLoggerFactory$$ExternalSyntheticLambda0 implements StatusListener {
    public static final /* synthetic */ GalleryLoggerFactory$$ExternalSyntheticLambda0 INSTANCE = new GalleryLoggerFactory$$ExternalSyntheticLambda0();

    @Override // ch.qos.logback.core.status.StatusListener
    public final void addStatusEvent(Status status) {
        GalleryLoggerFactory.lambda$configureLogger$0(status);
    }
}
