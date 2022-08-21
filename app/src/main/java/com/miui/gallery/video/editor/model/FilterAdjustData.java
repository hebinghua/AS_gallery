package com.miui.gallery.video.editor.model;

/* loaded from: classes2.dex */
public class FilterAdjustData extends AdjustData {
    public int MAX;
    public int MIN;
    public int mId;
    public boolean mIsMid;
    public String mLabel;

    public FilterAdjustData(int i, short s, String str, int i2, boolean z, String str2) {
        super(s, str, i2);
        this.MAX = 100;
        this.MIN = 0;
        this.mLabel = "";
        this.mId = i;
        this.mIsMid = z;
        this.mLabel = str2;
    }

    public String getLable() {
        return this.mLabel;
    }

    @Override // com.miui.gallery.video.editor.model.AdjustData
    public int getMax() {
        return this.MAX;
    }

    @Override // com.miui.gallery.video.editor.model.AdjustData
    public int getMin() {
        return this.mIsMid ? -this.MAX : this.MIN;
    }

    @Override // com.miui.gallery.video.editor.model.AdjustData
    public boolean isMid() {
        return this.mIsMid;
    }

    public int getId() {
        return this.mId;
    }
}
