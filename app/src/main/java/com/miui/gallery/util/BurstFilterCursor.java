package com.miui.gallery.util;

import android.database.Cursor;
import android.database.CursorWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/* loaded from: classes2.dex */
public class BurstFilterCursor extends CursorWrapper {
    public int mPos;
    public ArrayList<TreeSet<BurstPosition>> mResultPos;

    public BurstFilterCursor(Cursor cursor) {
        super(cursor);
        this.mPos = 0;
        this.mResultPos = new ArrayList<>();
        initResultList();
    }

    public ArrayList<Integer> getResultPos() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        Iterator<TreeSet<BurstPosition>> it = this.mResultPos.iterator();
        while (it.hasNext()) {
            int i = Integer.MAX_VALUE;
            Iterator<BurstPosition> it2 = it.next().iterator();
            while (it2.hasNext()) {
                int i2 = it2.next().mCursorPos;
                if (i > i2) {
                    i = i2;
                }
            }
            arrayList.add(Integer.valueOf(i));
        }
        return arrayList;
    }

    public void initResultList() {
        Cursor wrappedCursor = getWrappedCursor();
        if (getWrappedCursor() == null) {
            return;
        }
        HashMap hashMap = new HashMap();
        int columnIndex = wrappedCursor.getColumnIndex("burst_group_id");
        int columnIndex2 = wrappedCursor.getColumnIndex("burst_index");
        for (int i = 0; i < wrappedCursor.getCount(); i++) {
            if (wrappedCursor.moveToPosition(i)) {
                if (columnIndex > 0 && wrappedCursor.getLong(columnIndex) > 0) {
                    long j = wrappedCursor.getLong(columnIndex);
                    if (hashMap.containsKey(Long.valueOf(j))) {
                        ((TreeSet) hashMap.get(Long.valueOf(j))).add(new BurstPosition(wrappedCursor.getInt(columnIndex2), i));
                    } else {
                        TreeSet<BurstPosition> treeSet = new TreeSet<>();
                        treeSet.add(new BurstPosition(wrappedCursor.getInt(columnIndex2), i));
                        this.mResultPos.add(treeSet);
                        hashMap.put(Long.valueOf(j), treeSet);
                    }
                } else {
                    TreeSet<BurstPosition> treeSet2 = new TreeSet<>();
                    treeSet2.add(new BurstPosition(wrappedCursor.getInt(columnIndex2), i));
                    this.mResultPos.add(treeSet2);
                }
            }
        }
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public int getCount() {
        return this.mResultPos.size();
    }

    public boolean isBurstPosition(int i) {
        if (i < 0 || i > this.mResultPos.size() || getBurstCount(i) <= 1) {
            return false;
        }
        moveToPosition(i);
        int columnIndex = getColumnIndex("burst_group_id");
        return columnIndex > 0 && getLong(columnIndex) > 0;
    }

    public ArrayList<Integer> getBurstGroup(int i) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        if (i >= 0 && i <= this.mResultPos.size()) {
            Iterator<BurstPosition> it = this.mResultPos.get(i).iterator();
            while (it.hasNext()) {
                arrayList.add(Integer.valueOf(it.next().mCursorPos));
            }
        }
        return arrayList;
    }

    public boolean isTimeBurstPosition(int i) {
        int columnIndex;
        return isBurstPosition(i) && (columnIndex = getColumnIndex("is_time_burst")) > 0 && getLong(columnIndex) == 1;
    }

    public int getBurstCount(int i) {
        if (getWrappedCursor() == null || i < 0 || i >= this.mResultPos.size()) {
            return 0;
        }
        return this.mResultPos.get(i).size();
    }

    public ArrayList<Long> getBurstIdsInGroup(int i, int i2) {
        ArrayList<Long> arrayList = new ArrayList<>();
        if (i >= 0 && i < this.mResultPos.size()) {
            Iterator<BurstPosition> it = this.mResultPos.get(i).iterator();
            while (it.hasNext()) {
                Cursor contentCursorAtPosition = getContentCursorAtPosition(it.next().mCursorPos);
                if (contentCursorAtPosition != null) {
                    arrayList.add(Long.valueOf(contentCursorAtPosition.getLong(i2)));
                }
            }
        }
        return arrayList;
    }

    public Cursor getContentCursorAtPosition(int i) {
        if (getWrappedCursor().moveToPosition(i)) {
            return getWrappedCursor();
        }
        return null;
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public boolean moveToPosition(int i) {
        if (i >= 0 && i < this.mResultPos.size()) {
            this.mPos = i;
            return getWrappedCursor().moveToPosition(this.mResultPos.get(i).first().mCursorPos);
        } else if (i < 0) {
            this.mPos = -1;
            return getWrappedCursor().moveToPosition(-1);
        } else if (i < this.mResultPos.size()) {
            return false;
        } else {
            this.mPos = this.mResultPos.size();
            return getWrappedCursor().moveToPosition(getWrappedCursor().getCount());
        }
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public boolean moveToFirst() {
        return moveToPosition(0);
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public boolean moveToLast() {
        return moveToPosition(getCount() - 1);
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public boolean moveToNext() {
        return moveToPosition(this.mPos + 1);
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public boolean moveToPrevious() {
        return moveToPosition(this.mPos - 1);
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public boolean move(int i) {
        return moveToPosition(this.mPos + i);
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public int getPosition() {
        return this.mPos;
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public boolean isBeforeFirst() {
        return this.mResultPos.size() == 0 || this.mPos == -1;
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public boolean isAfterLast() {
        return this.mResultPos.size() == 0 || this.mPos == this.mResultPos.size();
    }

    public static void wrapGroupInfos(List<Integer> list, int i, List<Integer> list2, List<Integer> list3) {
        int size = list2.size();
        int i2 = 0;
        int i3 = 0;
        int i4 = 1;
        for (int i5 = 0; i5 < i && i2 < size; i5++) {
            if (i4 < size && list2.get(i4).intValue() == i5) {
                int i6 = i4;
                i4++;
                i2 = i6;
            }
            if (i3 >= list.size() || list.get(i3).intValue() != i5) {
                list3.set(i2, Integer.valueOf(list3.get(i2).intValue() - 1));
            } else {
                i3++;
            }
        }
        for (int i7 = 1; i7 < list2.size(); i7++) {
            int i8 = i7 - 1;
            list2.set(i7, Integer.valueOf(list2.get(i8).intValue() + list3.get(i8).intValue()));
        }
    }

    public static void wrapGroupInfos(List<Integer> list, List<List<Integer>> list2) {
        int size = list2.size();
        List<Integer> list3 = list2.get(0);
        LinkedList linkedList = new LinkedList();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < list.size() && i2 < size) {
            while (i < list.size() && list3.get(i3).intValue() > list.get(i).intValue()) {
                i++;
            }
            if (i >= list.size()) {
                break;
            } else if (list3.get(i3).intValue() == list.get(i).intValue()) {
                linkedList.add(Integer.valueOf(i));
                i++;
                if (i >= list.size()) {
                    break;
                }
            } else {
                do {
                    i3++;
                    if (i3 >= list3.size()) {
                        list2.set(i2, linkedList);
                        i2++;
                        if (i2 < size) {
                            list3 = list2.get(i2);
                            linkedList = new LinkedList();
                            i3 = 0;
                        }
                    }
                } while (list3.get(i3).intValue() < list.get(i).intValue());
            }
        }
        if (i2 < size) {
            list2.set(i2, linkedList);
        } else {
            i2--;
        }
        while (list2.size() > i2 + 1) {
            list2.remove(list2.size() - 1);
        }
        list2.removeIf(BurstFilterCursor$$ExternalSyntheticLambda0.INSTANCE);
    }

    public static /* synthetic */ boolean lambda$wrapGroupInfos$0(List list) {
        return list.size() <= 0;
    }

    /* loaded from: classes2.dex */
    public static class BurstPosition implements Comparable<BurstPosition> {
        public int mBurstIndex;
        public int mCursorPos;

        public BurstPosition(int i, int i2) {
            this.mCursorPos = i2;
            this.mBurstIndex = i;
        }

        @Override // java.lang.Comparable
        public int compareTo(BurstPosition burstPosition) {
            return -Long.compare(this.mBurstIndex, burstPosition.mBurstIndex);
        }
    }
}
