package com.miui.gallery.widget.recyclerview.grouped;

import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.adapter.ItemIdComposer;

/* loaded from: classes3.dex */
public class GroupedItemManager {
    public GroupedWrapperAdapter mWrapperAdapter;

    public RecyclerView.Adapter createWrappedAdapter(RecyclerView.Adapter adapter) {
        if (!adapter.hasStableIds()) {
            throw new IllegalArgumentException("The passed adapter does not support stable IDs");
        }
        if (this.mWrapperAdapter != null) {
            throw new IllegalStateException("already have a wrapped adapter");
        }
        GroupedWrapperAdapter groupedWrapperAdapter = new GroupedWrapperAdapter(this, adapter);
        this.mWrapperAdapter = groupedWrapperAdapter;
        return groupedWrapperAdapter;
    }

    public long getExpandablePosition(int i) {
        GroupedWrapperAdapter groupedWrapperAdapter = this.mWrapperAdapter;
        if (groupedWrapperAdapter == null) {
            return -1L;
        }
        return groupedWrapperAdapter.getExpandablePosition(i);
    }

    public int getFlatPosition(long j) {
        GroupedWrapperAdapter groupedWrapperAdapter = this.mWrapperAdapter;
        if (groupedWrapperAdapter == null) {
            return -1;
        }
        return groupedWrapperAdapter.getFlatPosition(j);
    }

    public static int getPackedPositionChild(long j) {
        return GroupedAdapterHelper.getPackedPositionChild(j);
    }

    public static long getPackedPositionForChild(int i, int i2) {
        return GroupedAdapterHelper.getPackedPositionForChild(i, i2);
    }

    public static long getPackedPositionForGroup(int i) {
        return GroupedAdapterHelper.getPackedPositionForGroup(i);
    }

    public static int getPackedPositionGroup(long j) {
        return GroupedAdapterHelper.getPackedPositionGroup(j);
    }

    public static long getCombinedChildId(long j, long j2) {
        return ItemIdComposer.composeExpandableChildId(0L, j2);
    }

    public static long getCombinedGroupId(long j) {
        return ItemIdComposer.composeExpandableGroupId(j);
    }

    public boolean isGroupPosition(int i) {
        GroupedWrapperAdapter groupedWrapperAdapter = this.mWrapperAdapter;
        if (groupedWrapperAdapter == null) {
            return false;
        }
        return groupedWrapperAdapter.isGroupPosition(i);
    }

    public int getGroupCount() {
        return this.mWrapperAdapter.getGroupCount();
    }

    public int getChildCount(int i) {
        return this.mWrapperAdapter.getChildCount(i);
    }
}
