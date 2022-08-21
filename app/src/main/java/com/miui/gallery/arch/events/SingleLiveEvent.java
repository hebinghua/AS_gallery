package com.miui.gallery.arch.events;

/* compiled from: SingleLiveEvent.kt */
/* loaded from: classes.dex */
public class SingleLiveEvent<T> {
    public final T content;
    public boolean hasBeenHandled;

    public SingleLiveEvent(T t) {
        this.content = t;
    }

    public final T getContentIfNotHandled() {
        if (this.hasBeenHandled) {
            return null;
        }
        this.hasBeenHandled = true;
        return this.content;
    }

    public final T peekContent() {
        return this.content;
    }
}
