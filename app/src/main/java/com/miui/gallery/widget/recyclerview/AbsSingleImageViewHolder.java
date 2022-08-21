package com.miui.gallery.widget.recyclerview;

import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.MicroThumbGridItem;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

/* loaded from: classes3.dex */
public abstract class AbsSingleImageViewHolder extends AbsViewHolder implements IDeferrableViewHolder {
    public final Map<BiConsumer<RecyclerView.ViewHolder, Object>, Object> mDeferredMap;
    public MicroThumbGridItem mView;

    public AbsSingleImageViewHolder(View view, Lifecycle lifecycle) {
        super(view, lifecycle);
        this.mDeferredMap = new WeakHashMap();
        this.mView = (MicroThumbGridItem) view;
    }

    @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
    public void recycle() {
        super.recycle();
        this.mView.recycle();
        this.mDeferredMap.clear();
    }

    public void defer(Object obj, BiConsumer<RecyclerView.ViewHolder, Object> biConsumer) {
        this.mDeferredMap.put(biConsumer, obj);
    }

    public void clearDeferred(BiConsumer<RecyclerView.ViewHolder, Object> biConsumer) {
        if (!this.mDeferredMap.isEmpty()) {
            this.mDeferredMap.remove(biConsumer);
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.IDeferrableViewHolder
    public void runDeferred() {
        for (Map.Entry<BiConsumer<RecyclerView.ViewHolder, Object>, Object> entry : this.mDeferredMap.entrySet()) {
            entry.getKey().accept(this, entry.getValue());
        }
        this.mDeferredMap.clear();
    }
}
