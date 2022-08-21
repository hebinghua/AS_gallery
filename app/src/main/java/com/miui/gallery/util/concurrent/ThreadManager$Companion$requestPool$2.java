package com.miui.gallery.util.concurrent;

import com.miui.gallery.concurrent.ThreadPool;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ThreadManager.kt */
/* loaded from: classes2.dex */
public final class ThreadManager$Companion$requestPool$2 extends Lambda implements Function0<ThreadPool> {
    public static final ThreadManager$Companion$requestPool$2 INSTANCE = new ThreadManager$Companion$requestPool$2();

    public ThreadManager$Companion$requestPool$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final ThreadPool mo1738invoke() {
        return new ThreadPool(2, 2, "request-pool");
    }
}
