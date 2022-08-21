package com.miui.gallery.adapter.itemmodel;

import com.miui.gallery.widget.recyclerview.ProportionTagModel;

/* loaded from: classes.dex */
public class MediaTimeProportionTagModel implements ProportionTagModel<Integer> {
    public boolean mIsAscOrder;
    public float mProportion;
    public int mTag;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.widget.recyclerview.ProportionTagModel
    /* renamed from: getTag */
    public Integer mo532getTag() {
        return Integer.valueOf(this.mTag);
    }

    public void setTag(Integer num) {
        this.mTag = num.intValue();
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagModel
    public float getProportion() {
        return this.mProportion;
    }

    public void setProportion(float f) {
        this.mProportion = f;
    }

    public void setIsAscOrder(boolean z) {
        this.mIsAscOrder = z;
    }

    @Override // java.lang.Comparable
    public int compareTo(ProportionTagModel<Integer> proportionTagModel) {
        if (this.mIsAscOrder) {
            return this.mTag - proportionTagModel.mo532getTag().intValue();
        }
        return proportionTagModel.mo532getTag().intValue() - this.mTag;
    }
}
