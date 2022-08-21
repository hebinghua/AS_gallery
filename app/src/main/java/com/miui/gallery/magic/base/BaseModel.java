package com.miui.gallery.magic.base;

import com.miui.gallery.magic.base.BasePresenter;

/* loaded from: classes2.dex */
public abstract class BaseModel<P extends BasePresenter, CONTRACT> extends SuperBase<CONTRACT> {
    public P mPresenter;

    public BaseModel(P p) {
        this.mPresenter = p;
    }
}
