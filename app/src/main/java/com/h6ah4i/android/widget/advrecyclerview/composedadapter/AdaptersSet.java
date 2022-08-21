package com.h6ah4i.android.widget.advrecyclerview.composedadapter;

import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.adapter.BridgeAdapterDataObserver;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AdaptersSet {
    public static long NO_SEGMENTED_POSITION = -1;
    public BridgeAdapterDataObserver.Subscriber mSubscriber;
    public List<ComposedChildAdapterTag> mAdapterTags = new ArrayList();
    public List<RecyclerView.Adapter> mAdapters = new ArrayList();
    public List<RecyclerView.Adapter> mUniqueAdapters = new ArrayList();
    public List<ComposedChildAdapterDataObserver> mObservers = new ArrayList();

    public static long composeSegmentedPosition(int i, int i2) {
        return (i2 & 4294967295L) | (i << 32);
    }

    public static int extractSegment(long j) {
        return (int) (j >>> 32);
    }

    public static int extractSegmentOffset(long j) {
        return (int) (j & 4294967295L);
    }

    public AdaptersSet(BridgeAdapterDataObserver.Subscriber subscriber) {
        this.mSubscriber = subscriber;
    }

    public ComposedChildAdapterTag addAdapter(RecyclerView.Adapter adapter, int i) {
        ComposedChildAdapterDataObserver composedChildAdapterDataObserver;
        ComposedChildAdapterTag composedChildAdapterTag = new ComposedChildAdapterTag();
        this.mAdapterTags.add(i, composedChildAdapterTag);
        this.mAdapters.add(i, adapter);
        int indexOf = this.mUniqueAdapters.indexOf(adapter);
        if (indexOf >= 0) {
            composedChildAdapterDataObserver = this.mObservers.get(indexOf);
        } else {
            ComposedChildAdapterDataObserver composedChildAdapterDataObserver2 = new ComposedChildAdapterDataObserver(this.mSubscriber, adapter);
            this.mObservers.add(composedChildAdapterDataObserver2);
            this.mUniqueAdapters.add(adapter);
            adapter.registerAdapterDataObserver(composedChildAdapterDataObserver2);
            composedChildAdapterDataObserver = composedChildAdapterDataObserver2;
        }
        composedChildAdapterDataObserver.registerChildAdapterTag(composedChildAdapterTag);
        return composedChildAdapterTag;
    }

    public int getAdapterSegment(ComposedChildAdapterTag composedChildAdapterTag) {
        return this.mAdapterTags.indexOf(composedChildAdapterTag);
    }

    public int getSegmentCount() {
        return this.mAdapters.size();
    }

    public RecyclerView.Adapter getAdapter(int i) {
        return this.mAdapters.get(i);
    }

    public List<RecyclerView.Adapter> getUniqueAdaptersList() {
        return this.mUniqueAdapters;
    }
}
