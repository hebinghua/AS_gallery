package com.miui.gallery.concurrent;

import android.os.Process;
import ch.qos.logback.core.CoreConstants;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class PriorityThreadFactory implements ThreadFactory {
    public final String mName;
    public final AtomicInteger mNumber = new AtomicInteger();
    public final int mPriority;

    public PriorityThreadFactory(String str, int i) {
        this.mName = str;
        this.mPriority = i;
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, this.mName + CoreConstants.DASH_CHAR + this.mNumber.getAndIncrement()) { // from class: com.miui.gallery.concurrent.PriorityThreadFactory.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Process.setThreadPriority(PriorityThreadFactory.this.mPriority);
                super.run();
            }
        };
    }
}
