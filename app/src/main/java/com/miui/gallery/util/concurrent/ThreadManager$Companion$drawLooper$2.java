package com.miui.gallery.util.concurrent;

import android.os.HandlerThread;
import android.os.Looper;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ThreadManager.kt */
/* loaded from: classes2.dex */
public final class ThreadManager$Companion$drawLooper$2 extends Lambda implements Function0<Looper> {
    public static final ThreadManager$Companion$drawLooper$2 INSTANCE = new ThreadManager$Companion$drawLooper$2();

    public ThreadManager$Companion$drawLooper$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final Looper mo1738invoke() {
        HandlerThread handlerThread = new HandlerThread("background-render");
        handlerThread.start();
        return handlerThread.getLooper();
    }
}
