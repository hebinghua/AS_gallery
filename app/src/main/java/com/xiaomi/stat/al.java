package com.xiaomi.stat;

import java.lang.Thread;

/* loaded from: classes3.dex */
public class al implements Thread.UncaughtExceptionHandler {
    private e a;
    private Thread.UncaughtExceptionHandler b;
    private boolean c = true;

    public al(e eVar) {
        this.a = eVar;
    }

    public void a() {
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler instanceof al) {
            return;
        }
        this.b = defaultUncaughtExceptionHandler;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void a(boolean z) {
        this.c = z;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        if (this.c) {
            this.a.a(th, (String) null, false);
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.b;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        }
    }
}
