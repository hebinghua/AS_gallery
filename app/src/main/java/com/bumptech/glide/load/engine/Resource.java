package com.bumptech.glide.load.engine;

/* loaded from: classes.dex */
public interface Resource<Z> {
    /* renamed from: get */
    Z mo237get();

    Class<Z> getResourceClass();

    int getSize();

    void recycle();
}
