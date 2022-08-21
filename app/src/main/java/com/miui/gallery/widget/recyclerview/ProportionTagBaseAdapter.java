package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import java.util.List;

/* loaded from: classes3.dex */
public abstract class ProportionTagBaseAdapter<T> {
    public Context mContext;
    public List<? extends ProportionTagModel<T>> mProportionTagModels;
    public final ProportionTagRecycler<T> mRecycler;

    public abstract void onBindView(ProportionTagView<T> proportionTagView, int i);

    public abstract ProportionTagView<T> onCreatedView();

    public ProportionTagBaseAdapter(Context context) {
        this.mContext = context;
        ProportionTagRecycler<T> proportionTagRecycler = new ProportionTagRecycler<>();
        this.mRecycler = proportionTagRecycler;
        proportionTagRecycler.setAdapter(this);
    }

    public List<ProportionTagView<T>> setDataAndRefreshView(List<ProportionTagModel<T>> list, int i, int i2) {
        this.mProportionTagModels = list;
        return this.mRecycler.calculateProportionTagsPosition(i, i2);
    }

    public int getItemSize() {
        List<? extends ProportionTagModel<T>> list = this.mProportionTagModels;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public ProportionTagModel<T> getItem(int i) {
        return this.mProportionTagModels.get(i);
    }
}
