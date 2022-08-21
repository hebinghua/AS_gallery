package com.miui.gallery.widget.recyclerview.grouped;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.adapter.ItemIdComposer;
import com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import java.util.List;

/* loaded from: classes3.dex */
public class GroupedWrapperAdapter extends SimpleWrapperAdapter<RecyclerView.ViewHolder> {
    public GroupedItemAdapter mGroupedItemAdapter;
    public final PositionTranslator mPositionTranslator;

    public GroupedWrapperAdapter(GroupedItemManager groupedItemManager, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        super(adapter);
        GroupedItemAdapter groupedItemAdapter = getGroupedItemAdapter(adapter);
        this.mGroupedItemAdapter = groupedItemAdapter;
        if (groupedItemAdapter != null) {
            if (groupedItemManager == null) {
                throw new IllegalArgumentException("manager cannot be null");
            }
            PositionTranslator positionTranslator = new PositionTranslator();
            this.mPositionTranslator = positionTranslator;
            positionTranslator.build(this.mGroupedItemAdapter);
            return;
        }
        throw new IllegalArgumentException("adapter does not implement GroupedItemAdapter");
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mPositionTranslator.getItemCount();
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        if (this.mGroupedItemAdapter == null) {
            return -1L;
        }
        long expandablePosition = this.mPositionTranslator.getExpandablePosition(i);
        int packedPositionGroup = GroupedAdapterHelper.getPackedPositionGroup(expandablePosition);
        int packedPositionChild = GroupedAdapterHelper.getPackedPositionChild(expandablePosition);
        long groupId = this.mGroupedItemAdapter.getGroupId(packedPositionGroup);
        if (packedPositionChild == -1) {
            return ItemIdComposer.composeExpandableGroupId(groupId);
        }
        return ItemIdComposer.composeExpandableChildId(0L, this.mGroupedItemAdapter.getChildId(packedPositionGroup, packedPositionChild));
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        int childItemViewType;
        if (this.mGroupedItemAdapter == null) {
            return 0;
        }
        long expandablePosition = this.mPositionTranslator.getExpandablePosition(i);
        int packedPositionGroup = GroupedAdapterHelper.getPackedPositionGroup(expandablePosition);
        int packedPositionChild = GroupedAdapterHelper.getPackedPositionChild(expandablePosition);
        if (packedPositionChild == -1) {
            childItemViewType = this.mGroupedItemAdapter.getGroupItemViewType(packedPositionGroup);
        } else {
            childItemViewType = this.mGroupedItemAdapter.getChildItemViewType(packedPositionGroup, packedPositionChild);
        }
        if ((childItemViewType & Integer.MIN_VALUE) == 0) {
            return packedPositionChild == -1 ? childItemViewType | Integer.MIN_VALUE : childItemViewType;
        }
        throw new IllegalStateException("Illegal view type (type = " + Integer.toHexString(childItemViewType) + ")");
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        GroupedItemAdapter groupedItemAdapter = this.mGroupedItemAdapter;
        if (groupedItemAdapter != null) {
            int i2 = Integer.MAX_VALUE & i;
            if ((i & Integer.MIN_VALUE) != 0) {
                return groupedItemAdapter.mo1338onCreateGroupViewHolder(viewGroup, i2);
            }
            return groupedItemAdapter.mo1337onCreateChildViewHolder(viewGroup, i2);
        }
        throw new IllegalStateException();
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List<Object> list) {
        if (this.mGroupedItemAdapter == null) {
            return;
        }
        long expandablePosition = this.mPositionTranslator.getExpandablePosition(i);
        int packedPositionGroup = GroupedAdapterHelper.getPackedPositionGroup(expandablePosition);
        int packedPositionChild = GroupedAdapterHelper.getPackedPositionChild(expandablePosition);
        int itemViewType = viewHolder.getItemViewType() & Integer.MAX_VALUE;
        if (packedPositionChild == -1) {
            this.mGroupedItemAdapter.onBindGroupViewHolder(viewHolder, packedPositionGroup, itemViewType, list);
        } else {
            this.mGroupedItemAdapter.onBindChildViewHolder(viewHolder, packedPositionGroup, packedPositionChild, itemViewType, list);
        }
    }

    public final void rebuildPositionTranslator() {
        PositionTranslator positionTranslator = this.mPositionTranslator;
        if (positionTranslator != null) {
            positionTranslator.build(this.mGroupedItemAdapter);
        }
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter
    public void onHandleWrappedAdapterChanged() {
        rebuildPositionTranslator();
        super.onHandleWrappedAdapterChanged();
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter
    public void onHandleWrappedAdapterItemRangeChanged(int i, int i2) {
        super.onHandleWrappedAdapterItemRangeChanged(i, i2);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter
    public void onHandleWrappedAdapterItemRangeInserted(int i, int i2) {
        rebuildPositionTranslator();
        super.onHandleWrappedAdapterItemRangeInserted(i, i2);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter
    public void onHandleWrappedAdapterItemRangeRemoved(int i, int i2) {
        rebuildPositionTranslator();
        super.onHandleWrappedAdapterItemRangeRemoved(i, i2);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter
    public void onHandleWrappedAdapterRangeMoved(int i, int i2, int i3) {
        rebuildPositionTranslator();
        super.onHandleWrappedAdapterRangeMoved(i, i2, i3);
    }

    public long getExpandablePosition(int i) {
        return this.mPositionTranslator.getExpandablePosition(i);
    }

    public int getFlatPosition(long j) {
        return this.mPositionTranslator.getFlatPosition(j);
    }

    public boolean isGroupPosition(int i) {
        return this.mPositionTranslator.isGroupPosition(i);
    }

    public int getGroupCount() {
        return this.mGroupedItemAdapter.getGroupCount();
    }

    public int getChildCount(int i) {
        return this.mGroupedItemAdapter.getChildCount(i);
    }

    public static GroupedItemAdapter getGroupedItemAdapter(RecyclerView.Adapter adapter) {
        return (GroupedItemAdapter) WrapperAdapterUtils.findWrappedAdapter(adapter, GroupedItemAdapter.class);
    }
}
