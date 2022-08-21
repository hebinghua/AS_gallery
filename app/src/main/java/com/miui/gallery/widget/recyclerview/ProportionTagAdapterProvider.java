package com.miui.gallery.widget.recyclerview;

import java.util.List;

/* loaded from: classes3.dex */
public interface ProportionTagAdapterProvider<T> {
    ProportionTagBaseAdapter<T> createTagAdapter();

    List<ProportionTagModel<T>> getProportionTagModel();

    boolean isProportionTagChanged();

    boolean isShowProportionTag();
}
