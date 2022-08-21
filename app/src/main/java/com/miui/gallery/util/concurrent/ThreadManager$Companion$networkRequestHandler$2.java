package com.miui.gallery.util.concurrent;

import android.os.HandlerThread;
import com.android.internal.CompatHandler;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ThreadManager.kt */
/* loaded from: classes2.dex */
public final class ThreadManager$Companion$networkRequestHandler$2 extends Lambda implements Function0<CompatHandler> {
    public static final ThreadManager$Companion$networkRequestHandler$2 INSTANCE = new ThreadManager$Companion$networkRequestHandler$2();

    public ThreadManager$Companion$networkRequestHandler$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final CompatHandler mo1738invoke() {
        HandlerThread handlerThread = new HandlerThread("network-resp-delivery");
        handlerThread.start();
        return new CompatHandler(handlerThread.getLooper());
    }
}
