package com.miui.gallery.vlog.entity;

/* loaded from: classes2.dex */
public class RatioData {
    public String name;
    public int ratio;
    public int resId;
    public String talkbackName;

    public RatioData(int i, String str, String str2, int i2) {
        this.ratio = i;
        this.name = str;
        this.resId = i2;
        this.talkbackName = str2;
    }

    public int getRatio() {
        return this.ratio;
    }

    public String getName() {
        return this.name;
    }

    public String getTalkbackName() {
        return this.talkbackName;
    }

    public int getResId() {
        return this.resId;
    }

    public boolean isOriginRatio() {
        return this.ratio == 5;
    }
}
