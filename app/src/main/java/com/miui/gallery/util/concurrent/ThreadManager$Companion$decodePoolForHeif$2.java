package com.miui.gallery.util.concurrent;

import com.miui.gallery.concurrent.PriorityThreadFactory;
import com.miui.gallery.concurrent.ThreadPool;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ThreadManager.kt */
/* loaded from: classes2.dex */
public final class ThreadManager$Companion$decodePoolForHeif$2 extends Lambda implements Function0<ThreadPool> {
    public static final ThreadManager$Companion$decodePoolForHeif$2 INSTANCE = new ThreadManager$Companion$decodePoolForHeif$2();

    public ThreadManager$Companion$decodePoolForHeif$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final ThreadPool mo1738invoke() {
        return new ThreadPool(1, 1, new PriorityThreadFactory("heif-region-decode", 10));
    }
}
