package com.h6ah4i.android.widget.advrecyclerview.composedadapter;

import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.adapter.BridgeAdapterDataObserver;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ComposedChildAdapterDataObserver extends BridgeAdapterDataObserver {
    public ComposedChildAdapterDataObserver(BridgeAdapterDataObserver.Subscriber subscriber, RecyclerView.Adapter adapter) {
        super(subscriber, adapter, new ArrayList());
    }

    public final List<ComposedChildAdapterTag> getChildAdapterTags() {
        return (List) getTag();
    }

    public void registerChildAdapterTag(ComposedChildAdapterTag composedChildAdapterTag) {
        getChildAdapterTags().add(composedChildAdapterTag);
    }
}
