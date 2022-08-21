package com.h6ah4i.android.widget.advrecyclerview.composedadapter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.adapter.AdapterPathSegment;
import com.h6ah4i.android.widget.advrecyclerview.adapter.BridgeAdapterDataObserver;
import com.h6ah4i.android.widget.advrecyclerview.adapter.ItemIdComposer;
import com.h6ah4i.android.widget.advrecyclerview.adapter.ItemViewTypeComposer;
import com.h6ah4i.android.widget.advrecyclerview.adapter.WrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrappedAdapterUtils;
import java.util.List;

/* loaded from: classes.dex */
public class ComposedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements WrapperAdapter<RecyclerView.ViewHolder>, BridgeAdapterDataObserver.Subscriber {
    public static long NO_SEGMENTED_POSITION = AdaptersSet.NO_SEGMENTED_POSITION;
    public AdaptersSet mAdaptersSet;
    public SegmentedPositionTranslator mSegmentedPositionTranslator;
    public SegmentedViewTypeTranslator mViewTypeTranslator;

    public ComposedAdapter() {
        AdaptersSet adaptersSet = new AdaptersSet(this);
        this.mAdaptersSet = adaptersSet;
        this.mSegmentedPositionTranslator = new SegmentedPositionTranslator(adaptersSet);
        this.mViewTypeTranslator = new SegmentedViewTypeTranslator();
        setHasStableIds(true);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.WrapperAdapter
    public void getWrappedAdapters(List<RecyclerView.Adapter> list) {
        AdaptersSet adaptersSet = this.mAdaptersSet;
        if (adaptersSet != null) {
            list.addAll(adaptersSet.getUniqueAdaptersList());
        }
    }

    public int getChildAdapterCount() {
        return this.mAdaptersSet.getSegmentCount();
    }

    public ComposedChildAdapterTag addAdapter(RecyclerView.Adapter adapter) {
        return addAdapter(adapter, getChildAdapterCount());
    }

    public ComposedChildAdapterTag addAdapter(RecyclerView.Adapter adapter, int i) {
        if (hasObservers() && hasStableIds() && !adapter.hasStableIds()) {
            throw new IllegalStateException("Wrapped child adapter must has stable IDs");
        }
        ComposedChildAdapterTag addAdapter = this.mAdaptersSet.addAdapter(adapter, i);
        this.mSegmentedPositionTranslator.invalidateSegment(this.mAdaptersSet.getAdapterSegment(addAdapter));
        notifyDataSetChanged();
        return addAdapter;
    }

    public long getSegmentedPosition(int i) {
        return this.mSegmentedPositionTranslator.getSegmentedPosition(i);
    }

    public static int extractSegmentPart(long j) {
        return AdaptersSet.extractSegment(j);
    }

    public static int extractSegmentOffsetPart(long j) {
        return AdaptersSet.extractSegmentOffset(j);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void setHasStableIds(boolean z) {
        if (z && !hasStableIds()) {
            int segmentCount = this.mAdaptersSet.getSegmentCount();
            for (int i = 0; i < segmentCount; i++) {
                if (!this.mAdaptersSet.getAdapter(i).hasStableIds()) {
                    throw new IllegalStateException("All child adapters must support stable IDs");
                }
            }
        }
        super.setHasStableIds(z);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        long segmentedPosition = getSegmentedPosition(i);
        int extractSegment = AdaptersSet.extractSegment(segmentedPosition);
        int extractSegmentOffset = AdaptersSet.extractSegmentOffset(segmentedPosition);
        RecyclerView.Adapter adapter = this.mAdaptersSet.getAdapter(extractSegment);
        int itemViewType = adapter.getItemViewType(extractSegmentOffset);
        return ItemIdComposer.composeSegment(ItemViewTypeComposer.extractSegmentPart(this.mViewTypeTranslator.wrapItemViewType(extractSegment, itemViewType)), adapter.getItemId(extractSegmentOffset));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        long segmentedPosition = getSegmentedPosition(i);
        int extractSegment = AdaptersSet.extractSegment(segmentedPosition);
        return this.mViewTypeTranslator.wrapItemViewType(extractSegment, this.mAdaptersSet.getAdapter(extractSegment).getItemViewType(AdaptersSet.extractSegmentOffset(segmentedPosition)));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        long unwrapViewType = this.mViewTypeTranslator.unwrapViewType(i);
        int extractWrapperSegment = SegmentedViewTypeTranslator.extractWrapperSegment(unwrapViewType);
        return this.mAdaptersSet.getAdapter(extractWrapperSegment).mo1843onCreateViewHolder(viewGroup, SegmentedViewTypeTranslator.extractWrappedViewType(unwrapViewType));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        long segmentedPosition = getSegmentedPosition(i);
        int extractSegment = AdaptersSet.extractSegment(segmentedPosition);
        this.mAdaptersSet.getAdapter(extractSegment).onBindViewHolder(viewHolder, AdaptersSet.extractSegmentOffset(segmentedPosition));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List<Object> list) {
        long segmentedPosition = getSegmentedPosition(i);
        int extractSegment = AdaptersSet.extractSegment(segmentedPosition);
        this.mAdaptersSet.getAdapter(extractSegment).onBindViewHolder(viewHolder, AdaptersSet.extractSegmentOffset(segmentedPosition), list);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        List<RecyclerView.Adapter> uniqueAdaptersList = this.mAdaptersSet.getUniqueAdaptersList();
        for (int i = 0; i < uniqueAdaptersList.size(); i++) {
            uniqueAdaptersList.get(i).onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        List<RecyclerView.Adapter> uniqueAdaptersList = this.mAdaptersSet.getUniqueAdaptersList();
        for (int i = 0; i < uniqueAdaptersList.size(); i++) {
            uniqueAdaptersList.get(i).onDetachedFromRecyclerView(recyclerView);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        onViewAttachedToWindow(viewHolder, viewHolder.getItemViewType());
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.WrappedAdapter
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder, int i) {
        long unwrapViewType = this.mViewTypeTranslator.unwrapViewType(i);
        int extractWrapperSegment = SegmentedViewTypeTranslator.extractWrapperSegment(unwrapViewType);
        WrappedAdapterUtils.invokeOnViewAttachedToWindow(this.mAdaptersSet.getAdapter(extractWrapperSegment), viewHolder, SegmentedViewTypeTranslator.extractWrappedViewType(unwrapViewType));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
        onViewDetachedFromWindow(viewHolder, viewHolder.getItemViewType());
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.WrappedAdapter
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder, int i) {
        long unwrapViewType = this.mViewTypeTranslator.unwrapViewType(i);
        int extractWrapperSegment = SegmentedViewTypeTranslator.extractWrapperSegment(unwrapViewType);
        WrappedAdapterUtils.invokeOnViewDetachedFromWindow(this.mAdaptersSet.getAdapter(extractWrapperSegment), viewHolder, SegmentedViewTypeTranslator.extractWrappedViewType(unwrapViewType));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        onViewRecycled(viewHolder, viewHolder.getItemViewType());
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.WrappedAdapter
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder, int i) {
        long unwrapViewType = this.mViewTypeTranslator.unwrapViewType(i);
        int extractWrapperSegment = SegmentedViewTypeTranslator.extractWrapperSegment(unwrapViewType);
        WrappedAdapterUtils.invokeOnViewRecycled(this.mAdaptersSet.getAdapter(extractWrapperSegment), viewHolder, SegmentedViewTypeTranslator.extractWrappedViewType(unwrapViewType));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder viewHolder) {
        return onFailedToRecycleView(viewHolder, viewHolder.getItemViewType());
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.WrappedAdapter
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder viewHolder, int i) {
        long unwrapViewType = this.mViewTypeTranslator.unwrapViewType(i);
        int extractWrapperSegment = SegmentedViewTypeTranslator.extractWrapperSegment(unwrapViewType);
        return WrappedAdapterUtils.invokeOnFailedToRecycleView(this.mAdaptersSet.getAdapter(extractWrapperSegment), viewHolder, SegmentedViewTypeTranslator.extractWrappedViewType(unwrapViewType));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mSegmentedPositionTranslator.getTotalItemCount();
    }

    public int wrapPosition(AdapterPathSegment adapterPathSegment, int i) {
        Object obj = adapterPathSegment.tag;
        if (obj != null) {
            return this.mSegmentedPositionTranslator.getFlatPosition(this.mAdaptersSet.getAdapterSegment((ComposedChildAdapterTag) obj), i);
        }
        return -1;
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.BridgeAdapterDataObserver.Subscriber
    public void onBridgedAdapterChanged(RecyclerView.Adapter adapter, Object obj) {
        onHandleWrappedAdapterChanged(adapter, (List) obj);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.BridgeAdapterDataObserver.Subscriber
    public void onBridgedAdapterItemRangeChanged(RecyclerView.Adapter adapter, Object obj, int i, int i2) {
        onHandleWrappedAdapterItemRangeChanged(adapter, (List) obj, i, i2);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.BridgeAdapterDataObserver.Subscriber
    public void onBridgedAdapterItemRangeChanged(RecyclerView.Adapter adapter, Object obj, int i, int i2, Object obj2) {
        onHandleWrappedAdapterItemRangeChanged(adapter, (List) obj, i, i2, obj2);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.BridgeAdapterDataObserver.Subscriber
    public void onBridgedAdapterItemRangeInserted(RecyclerView.Adapter adapter, Object obj, int i, int i2) {
        onHandleWrappedAdapterItemRangeInserted(adapter, (List) obj, i, i2);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.BridgeAdapterDataObserver.Subscriber
    public void onBridgedAdapterItemRangeRemoved(RecyclerView.Adapter adapter, Object obj, int i, int i2) {
        onHandleWrappedAdapterItemRangeRemoved(adapter, (List) obj, i, i2);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.BridgeAdapterDataObserver.Subscriber
    public void onBridgedAdapterRangeMoved(RecyclerView.Adapter adapter, Object obj, int i, int i2, int i3) {
        onHandleWrappedAdapterRangeMoved(adapter, (List) obj, i, i2, i3);
    }

    public void onHandleWrappedAdapterChanged(RecyclerView.Adapter adapter, List<ComposedChildAdapterTag> list) {
        this.mSegmentedPositionTranslator.invalidateAll();
        notifyDataSetChanged();
    }

    public void onHandleWrappedAdapterItemRangeChanged(RecyclerView.Adapter adapter, List<ComposedChildAdapterTag> list, int i, int i2) {
        int size = list.size();
        for (int i3 = 0; i3 < size; i3++) {
            notifyItemRangeChanged(this.mSegmentedPositionTranslator.getFlatPosition(this.mAdaptersSet.getAdapterSegment(list.get(i3)), i), i2);
        }
    }

    public void onHandleWrappedAdapterItemRangeChanged(RecyclerView.Adapter adapter, List<ComposedChildAdapterTag> list, int i, int i2, Object obj) {
        int size = list.size();
        for (int i3 = 0; i3 < size; i3++) {
            notifyItemRangeChanged(this.mSegmentedPositionTranslator.getFlatPosition(this.mAdaptersSet.getAdapterSegment(list.get(i3)), i), i2, obj);
        }
    }

    public void onHandleWrappedAdapterItemRangeInserted(RecyclerView.Adapter adapter, List<ComposedChildAdapterTag> list, int i, int i2) {
        if (i2 <= 0) {
            return;
        }
        int size = list.size();
        if (size == 1) {
            int adapterSegment = this.mAdaptersSet.getAdapterSegment(list.get(0));
            this.mSegmentedPositionTranslator.invalidateSegment(adapterSegment);
            notifyItemRangeInserted(this.mSegmentedPositionTranslator.getFlatPosition(adapterSegment, i), i2);
            return;
        }
        for (int i3 = 0; i3 < size; i3++) {
            this.mSegmentedPositionTranslator.invalidateSegment(this.mAdaptersSet.getAdapterSegment(list.get(i3)));
        }
        notifyDataSetChanged();
    }

    public void onHandleWrappedAdapterItemRangeRemoved(RecyclerView.Adapter adapter, List<ComposedChildAdapterTag> list, int i, int i2) {
        if (i2 <= 0) {
            return;
        }
        int size = list.size();
        if (size == 1) {
            int adapterSegment = this.mAdaptersSet.getAdapterSegment(list.get(0));
            this.mSegmentedPositionTranslator.invalidateSegment(adapterSegment);
            notifyItemRangeRemoved(this.mSegmentedPositionTranslator.getFlatPosition(adapterSegment, i), i2);
            return;
        }
        for (int i3 = 0; i3 < size; i3++) {
            this.mSegmentedPositionTranslator.invalidateSegment(this.mAdaptersSet.getAdapterSegment(list.get(i3)));
        }
        notifyDataSetChanged();
    }

    public void onHandleWrappedAdapterRangeMoved(RecyclerView.Adapter adapter, List<ComposedChildAdapterTag> list, int i, int i2, int i3) {
        if (i3 != 1) {
            throw new IllegalStateException("itemCount should be always 1  (actual: " + i3 + ")");
        } else if (list.size() == 1) {
            int adapterSegment = this.mAdaptersSet.getAdapterSegment(list.get(0));
            notifyItemMoved(this.mSegmentedPositionTranslator.getFlatPosition(adapterSegment, i), this.mSegmentedPositionTranslator.getFlatPosition(adapterSegment, i2));
        } else {
            notifyDataSetChanged();
        }
    }
}
