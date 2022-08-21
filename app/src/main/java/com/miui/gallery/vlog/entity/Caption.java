package com.miui.gallery.vlog.entity;

import java.util.Locale;

/* loaded from: classes2.dex */
public class Caption {
    public long inPoint;
    public long outPoint;
    public String text;

    public String toString() {
        return String.format(Locale.getDefault(), "inPoint:%d,outPoint:%d,sentence:%s", Long.valueOf(this.inPoint), Long.valueOf(this.outPoint), this.text);
    }
}
