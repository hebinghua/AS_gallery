package com.miui.gallery.provider;

import android.database.Cursor;
import android.os.Bundle;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import kotlin.Pair;

/* loaded from: classes2.dex */
public class TimelineHeadersGroup {
    public int count;
    public List<String> itemLocations = new BoundedLinkedList(500);
    public int start;
    public String validLocation;

    public static void bindGroup(int i, List<TimelineHeadersGroup> list, Cursor cursor) {
        bindGroup(i, list, cursor, "extra_timeline_item_count_in_group", "extra_timeline_group_start_pos", "extra_timeline_group_labels");
    }

    public static void bindGroup(int i, List<TimelineHeadersGroup> list, Cursor cursor, String str, String str2, String str3) {
        if (cursor == null || !BaseMiscUtil.isValid(list)) {
            return;
        }
        ArrayList<Integer> arrayList = new ArrayList<>(list.size());
        ArrayList<Integer> arrayList2 = new ArrayList<>(list.size());
        ArrayList<String> arrayList3 = new ArrayList<>(list.size());
        for (TimelineHeadersGroup timelineHeadersGroup : list) {
            arrayList2.add(Integer.valueOf(timelineHeadersGroup.start));
            arrayList.add(Integer.valueOf(timelineHeadersGroup.count));
            if (i == 2 && "CN".equalsIgnoreCase(LocationManager.getRegion())) {
                arrayList3.add(LocationManager.getInstance().generateTitleLineForCnMonth(timelineHeadersGroup.itemLocations));
            } else {
                arrayList3.add(LocationManager.getInstance().generateTitleLine(timelineHeadersGroup.itemLocations));
            }
        }
        Bundle extras = cursor.getExtras();
        if (extras == null || extras == Bundle.EMPTY) {
            extras = new Bundle();
            cursor.setExtras(extras);
        }
        extras.putIntegerArrayList(str, arrayList);
        extras.putIntegerArrayList(str2, arrayList2);
        extras.putStringArrayList(str3, arrayList3);
    }

    public static void bindGroups(Pair<Integer, List<TimelineHeadersGroup>>[] pairArr, Cursor cursor) {
        if (cursor == null || pairArr == null || pairArr.length <= 0) {
            return;
        }
        for (Pair<Integer, List<TimelineHeadersGroup>> pair : pairArr) {
            int intValue = pair.getFirst().intValue();
            if (intValue == 1) {
                bindGroup(intValue, pair.getSecond(), cursor, "extra_timeline_item_count_in_group", "extra_timeline_group_start_pos", "extra_timeline_group_labels");
            } else if (intValue == 2) {
                bindGroup(intValue, pair.getSecond(), cursor, "extra_month_timeline_item_count_in_group", "extra_month_timeline_group_start_pos", "extra_month_timeline_group_start_locations");
            } else if (intValue == 4) {
                bindGroup(intValue, pair.getSecond(), cursor, "extra_year_timeline_item_count_in_group", "extra_year_timeline_group_start_pos", "extra_year_timeline_group_start_locations");
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class BoundedLinkedList<T> extends LinkedList<T> {
        private static final long serialVersionUID = 4425405307363195663L;
        private final int mBound;

        public BoundedLinkedList(int i) {
            this.mBound = i;
        }

        @Override // java.util.LinkedList, java.util.Deque
        public void addFirst(T t) {
            if (size() < this.mBound) {
                super.addFirst(t);
            }
        }

        @Override // java.util.LinkedList, java.util.Deque
        public void addLast(T t) {
            if (size() < this.mBound) {
                super.addLast(t);
            }
        }

        @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
        public boolean add(T t) {
            if (size() < this.mBound) {
                return super.add(t);
            }
            return false;
        }

        @Override // java.util.LinkedList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean addAll(Collection<? extends T> collection) {
            boolean z = false;
            for (T t : collection) {
                boolean add = add(t);
                z |= add;
                if (!add) {
                    break;
                }
            }
            return z;
        }

        @Override // java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public boolean addAll(int i, Collection<? extends T> collection) {
            if (collection.size() == 0) {
                return false;
            }
            for (T t : collection) {
                if (size() >= this.mBound) {
                    return true;
                }
                super.add(i, t);
                i++;
            }
            return true;
        }

        @Override // java.util.LinkedList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public void add(int i, T t) {
            if (size() < this.mBound) {
                super.add(i, t);
            }
        }
    }
}
