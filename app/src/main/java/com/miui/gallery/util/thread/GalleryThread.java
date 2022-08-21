package com.miui.gallery.util.thread;

import android.os.Process;

/* loaded from: classes2.dex */
public class GalleryThread extends Thread {
    public final int mPriority;

    public GalleryThread(Runnable runnable, String str, int i) {
        super(runnable, str);
        this.mPriority = i;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Process.setThreadPriority(this.mPriority);
        super.run();
    }
}
