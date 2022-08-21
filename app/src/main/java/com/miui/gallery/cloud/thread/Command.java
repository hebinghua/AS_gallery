package com.miui.gallery.cloud.thread;

/* loaded from: classes.dex */
public interface Command {
    boolean canMergeWith(Command command);

    long getDelay(long j);

    String getKey();

    int getPriority();

    boolean needProcess();
}
