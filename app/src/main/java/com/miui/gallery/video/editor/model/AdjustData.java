package com.miui.gallery.video.editor.model;

/* loaded from: classes2.dex */
public abstract class AdjustData {
    public final int icon;
    public String name;
    public int priority;
    public int progress = getMin();

    public abstract int getMax();

    public abstract int getMin();

    public abstract boolean isMid();

    public AdjustData(short s, String str, int i) {
        this.icon = i;
        this.name = str;
        this.priority = s;
    }

    public void setProgress(int i) {
        this.progress = i;
    }

    public int getProgress() {
        return this.progress;
    }
}
