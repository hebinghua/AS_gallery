package com.miui.gallery.arch.internal;

import android.os.Handler;
import android.os.Looper;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: TaskExecutor.kt */
/* loaded from: classes.dex */
public final class TaskExecutor$mainHandler$2 extends Lambda implements Function0<Handler> {
    public static final TaskExecutor$mainHandler$2 INSTANCE = new TaskExecutor$mainHandler$2();

    public TaskExecutor$mainHandler$2() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final Handler mo1738invoke() {
        Handler createAsync;
        TaskExecutor taskExecutor = TaskExecutor.INSTANCE;
        Looper mainLooper = Looper.getMainLooper();
        Intrinsics.checkNotNullExpressionValue(mainLooper, "getMainLooper()");
        createAsync = taskExecutor.createAsync(mainLooper);
        return createAsync;
    }
}
