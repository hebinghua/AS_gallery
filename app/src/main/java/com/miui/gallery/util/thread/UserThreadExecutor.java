package com.miui.gallery.util.thread;

import com.miui.gallery.base_optimization.clean.thread.SubScribeThreadExecutor;

/* loaded from: classes2.dex */
public class UserThreadExecutor implements SubScribeThreadExecutor {
    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        ThreadManager.execute(31, runnable);
    }
}
