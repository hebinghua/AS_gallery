package com.miui.gallery.adapter;

import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.HeaderAndFootersEpoxyAdapter;
import com.miui.gallery.adapter.itemmodel.trans.ItemModelTransManager;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class GallerySimpleEpoxyAdapter<T> extends HeaderAndFootersEpoxyAdapter<T> {
    public GallerySimpleEpoxyAdapter() {
    }

    public GallerySimpleEpoxyAdapter(Executor executor, Executor executor2) {
        super(executor, executor2);
    }

    @Override // com.miui.epoxy.AsyncDiffEpoxyAdapter
    public EpoxyModel<?> transData(T t) {
        return ItemModelTransManager.getInstance().transDataToModel(t);
    }

    public void notifyDataChanged(T t) {
        int indexOf = getDatas().indexOf(t);
        if (-1 != indexOf) {
            notifyModelChanged(getModel(indexOf));
        }
    }
}
