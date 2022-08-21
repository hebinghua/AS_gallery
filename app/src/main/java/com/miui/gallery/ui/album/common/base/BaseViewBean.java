package com.miui.gallery.ui.album.common.base;

import com.google.gson.annotations.SerializedName;
import java.util.Comparator;

/* loaded from: classes2.dex */
public class BaseViewBean<SOURCE, R> implements Comparator<BaseViewBean> {
    @SerializedName("id")
    public long id;
    public boolean mNeedShowMoreStyle = false;
    public SOURCE mSource;

    public BaseViewBean(long j) {
        this.id = j;
    }

    public BaseViewBean() {
    }

    public BaseViewBean(R r) {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public boolean needToShowMoreStyle() {
        return this.mNeedShowMoreStyle;
    }

    public void setNeedShowMoreStyle(boolean z) {
        this.mNeedShowMoreStyle = z;
    }

    public SOURCE getSource() {
        return this.mSource;
    }

    public void setSource(SOURCE source) {
        this.mSource = source;
    }

    public int hashCode() {
        return Long.hashCode(this.id) + Boolean.hashCode(needToShowMoreStyle());
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof BaseViewBean)) {
            BaseViewBean baseViewBean = (BaseViewBean) obj;
            return baseViewBean.getId() == getId() && baseViewBean.needToShowMoreStyle() == needToShowMoreStyle();
        }
        return super.equals(obj);
    }

    @Override // java.util.Comparator
    public int compare(BaseViewBean baseViewBean, BaseViewBean baseViewBean2) {
        return Long.compare(baseViewBean.getId(), baseViewBean2.getId());
    }

    public void mapping(SOURCE source) {
        this.mSource = source;
    }
}
