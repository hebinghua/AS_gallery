package com.miui.gallery.editor.photo.core.common.model;

import com.miui.gallery.editor.photo.core.Metadata;

/* loaded from: classes2.dex */
public abstract class Adjust2Data extends Metadata {
    public static int MAX = 100;
    public static int MIN;
    public final int icon;
    public final int iconJson;
    public float progress;

    public abstract String getEffectName();

    public abstract int getId();

    public abstract int getMax();

    public abstract int getMin();

    public abstract boolean isMid();

    public Adjust2Data(short s, String str, int i, int i2) {
        super(s, str);
        this.icon = i;
        this.iconJson = i2;
        this.progress = getMin();
    }
}
