package com.miui.gallery.cloud;

/* loaded from: classes.dex */
public class InterruptedExceptionWrapper extends InterruptedException {
    private final Exception mWrapped;

    public InterruptedExceptionWrapper(Exception exc) {
        this.mWrapped = exc;
    }

    public Exception get() {
        return this.mWrapped;
    }
}
