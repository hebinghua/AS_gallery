package com.miui.gallery.arch.internal;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.lang.reflect.InvocationTargetException;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: TaskExecutor.kt */
/* loaded from: classes.dex */
public final class TaskExecutor {
    public static final TaskExecutor INSTANCE = new TaskExecutor();
    public static final Lazy mainHandler$delegate = LazyKt__LazyJVMKt.lazy(TaskExecutor$mainHandler$2.INSTANCE);

    public final Handler getMainHandler() {
        return (Handler) mainHandler$delegate.mo119getValue();
    }

    public final void postToMainThread(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "runnable");
        getMainHandler().post(runnable);
    }

    public final Handler createAsync(Looper looper) {
        if (Build.VERSION.SDK_INT >= 28) {
            Handler createAsync = Handler.createAsync(looper);
            Intrinsics.checkNotNullExpressionValue(createAsync, "createAsync(looper)");
            return createAsync;
        }
        try {
            Object newInstance = Handler.class.getDeclaredConstructor(Looper.class, Handler.Callback.class, Boolean.TYPE).newInstance(looper, null, Boolean.TRUE);
            Intrinsics.checkNotNullExpressionValue(newInstance, "Handler::class.java.getD…tance(looper, null, true)");
            return (Handler) newInstance;
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException unused) {
            return new Handler(looper);
        } catch (InvocationTargetException unused2) {
            return new Handler(looper);
        }
    }
}
