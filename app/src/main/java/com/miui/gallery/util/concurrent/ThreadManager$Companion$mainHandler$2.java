package com.miui.gallery.util.concurrent;

import android.os.Looper;
import com.android.internal.CompatHandler;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ThreadManager.kt */
/* loaded from: classes2.dex */
public final class ThreadManager$Companion$mainHandler$2 extends Lambda implements Function0<CompatHandler> {
    public static final ThreadManager$Companion$mainHandler$2 INSTANCE = new ThreadManager$Companion$mainHandler$2();

    public ThreadManager$Companion$mainHandler$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final CompatHandler mo1738invoke() {
        return new CompatHandler(Looper.getMainLooper());
    }
}
