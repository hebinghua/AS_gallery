package com.miui.gallery.util.thread;

/* loaded from: classes2.dex */
public class ThreadConfig {
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    public static int getSuggestThreadCoreSize() {
        return Math.min(CPU_COUNT - 1, 5);
    }

    public static int getSuggestThreadMaxSize() {
        return (CPU_COUNT * 2) + 1;
    }
}
