package com.miui.gallery.util;

/* loaded from: classes2.dex */
public abstract class LazyValue<Param, Value> {
    public volatile boolean mResolved = false;
    public volatile Value mValue;

    /* renamed from: onInit */
    public abstract Value mo1272onInit(Param param);

    public final synchronized void init(Param param) {
        if (!this.mResolved) {
            this.mValue = mo1272onInit(param);
            this.mResolved = true;
        }
    }

    public final Value get(Param param) {
        if (!this.mResolved) {
            init(param);
        }
        return this.mValue;
    }

    public boolean hasResolved() {
        return this.mResolved;
    }

    public synchronized void reset() {
        this.mResolved = false;
    }
}
