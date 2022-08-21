package com.miui.gallery.model;

import android.database.Cursor;
import android.util.Pair;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class MergedCursorDataSetWrapper extends CursorDataSet {
    public final List<CursorDataSet> mDataSets;
    public final List<Pair<Integer, Integer>> mPositions;

    @Override // com.miui.gallery.model.BaseDataSet
    public boolean foldBurst() {
        return true;
    }

    public MergedCursorDataSetWrapper(List<CursorDataSet> list, Pair<Integer, Integer> pair, Comparator<Cursor> comparator) {
        super(null, 0);
        Objects.requireNonNull(list);
        list.removeIf(MergedCursorDataSetWrapper$$ExternalSyntheticLambda0.INSTANCE);
        this.mDataSets = list;
        List<Pair<Integer, Integer>> sort = sort(list, comparator);
        this.mPositions = sort;
        if (pair == null || sort == null) {
            return;
        }
        setInitPosition(sort.indexOf(pair));
    }

    public final List<Pair<Integer, Integer>> sort(List<CursorDataSet> list, Comparator<Cursor> comparator) {
        int size = list.size();
        Cursor[] cursorArr = new Cursor[size];
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            cursorArr[i2] = list.get(i2).mCursor;
            cursorArr[i2].moveToFirst();
            i += cursorArr[i2].getCount();
        }
        ArrayList<Pair<Integer, Integer>> arrayList = new ArrayList<Pair<Integer, Integer>>(i) { // from class: com.miui.gallery.model.MergedCursorDataSetWrapper.1
            @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
            /* renamed from: get */
            public Pair<Integer, Integer> mo1113get(int i3) {
                if (i3 < 0 || i3 >= MergedCursorDataSetWrapper.this.getCount()) {
                    DefaultLogger.w("MergedCursorDataSetWrapper", "access bounds error");
                    DefaultLogger.verbosePrintStackMsg("MergedCursorDataSetWrapper");
                    return null;
                }
                return (Pair) super.get(i3);
            }
        };
        while (check(cursorArr)) {
            Cursor cursor = cursorArr[0];
            int i3 = 0;
            for (int i4 = 1; i4 < size; i4++) {
                if (comparator.compare(cursor, cursorArr[i4]) > 0) {
                    cursor = cursorArr[i4];
                    i3 = i4;
                }
            }
            arrayList.add(new Pair<>(Integer.valueOf(i3), Integer.valueOf(cursor.getPosition())));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public final boolean check(Cursor[] cursorArr) {
        for (Cursor cursor : cursorArr) {
            if (!cursor.isAfterLast()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.gallery.model.CursorDataSet, com.miui.gallery.model.BaseDataSet, com.miui.gallery.projection.IConnectController.DataSet
    public int getCount() {
        return this.mPositions.size();
    }

    @Override // com.miui.gallery.model.CursorDataSet, com.miui.gallery.model.BaseDataSet
    public void onRelease() {
        for (CursorDataSet cursorDataSet : this.mDataSets) {
            cursorDataSet.onRelease();
        }
    }

    @Override // com.miui.gallery.model.CursorDataSet, com.miui.gallery.model.BaseDataSet
    public BaseDataItem createItem(int i) {
        Pair<Integer, Integer> pair;
        if (moveToPosition(i) && (pair = this.mPositions.get(i)) != null) {
            return this.mDataSets.get(((Integer) pair.first).intValue()).createItem(((Integer) pair.second).intValue());
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000c  */
    @Override // com.miui.gallery.model.CursorDataSet
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isValidate() {
        /*
            r2 = this;
            java.util.List<com.miui.gallery.model.CursorDataSet> r0 = r2.mDataSets
            java.util.Iterator r0 = r0.iterator()
        L6:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L1e
            java.lang.Object r1 = r0.next()
            com.miui.gallery.model.CursorDataSet r1 = (com.miui.gallery.model.CursorDataSet) r1
            android.database.Cursor r1 = r1.mCursor
            if (r1 == 0) goto L1c
            boolean r1 = r1.isClosed()
            if (r1 == 0) goto L6
        L1c:
            r0 = 0
            return r0
        L1e:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.model.MergedCursorDataSetWrapper.isValidate():boolean");
    }

    @Override // com.miui.gallery.model.CursorDataSet
    public int burstKeyIndex() {
        return this.mDataSets.get(0).burstKeyIndex();
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000d  */
    @Override // com.miui.gallery.model.CursorDataSet
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isValidate(int r4) {
        /*
            r3 = this;
            java.util.List<com.miui.gallery.model.CursorDataSet> r0 = r3.mDataSets
            java.util.Iterator r0 = r0.iterator()
        L6:
            boolean r1 = r0.hasNext()
            r2 = 0
            if (r1 == 0) goto L1e
            java.lang.Object r1 = r0.next()
            com.miui.gallery.model.CursorDataSet r1 = (com.miui.gallery.model.CursorDataSet) r1
            android.database.Cursor r1 = r1.mCursor
            if (r1 == 0) goto L1d
            boolean r1 = r1.isClosed()
            if (r1 == 0) goto L6
        L1d:
            return r2
        L1e:
            if (r4 < 0) goto L27
            int r0 = r3.getCount()
            if (r4 >= r0) goto L27
            r2 = 1
        L27:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.model.MergedCursorDataSetWrapper.isValidate(int):boolean");
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public boolean deletingIncludeCloud() {
        return this.mDataSets.get(0).deletingIncludeCloud();
    }

    @Override // com.miui.gallery.model.CursorDataSet
    public boolean moveToPosition(int i) {
        Pair<Integer, Integer> pair = this.mPositions.get(i);
        if (pair == null) {
            return false;
        }
        CursorDataSet cursorDataSet = this.mDataSets.get(((Integer) pair.first).intValue());
        if (!cursorDataSet.moveToPosition(((Integer) pair.second).intValue())) {
            return false;
        }
        this.mCursor = cursorDataSet.mCursor;
        return true;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public void bindItem(BaseDataItem baseDataItem, int i) {
        Pair<Integer, Integer> pair;
        if (moveToPosition(i) && (pair = this.mPositions.get(i)) != null) {
            this.mDataSets.get(((Integer) pair.first).intValue()).bindItem(baseDataItem, ((Integer) pair.second).intValue());
        }
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public long getItemKey(int i) {
        Pair<Integer, Integer> pair;
        if (moveToPosition(i) && (pair = this.mPositions.get(i)) != null) {
            return this.mDataSets.get(((Integer) pair.first).intValue()).getItemKey(((Integer) pair.second).intValue());
        }
        return -1L;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public String getItemPath(int i) {
        Pair<Integer, Integer> pair;
        if (moveToPosition(i) && (pair = this.mPositions.get(i)) != null) {
            return this.mDataSets.get(((Integer) pair.first).intValue()).getItemPath(((Integer) pair.second).intValue());
        }
        return null;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public int doDelete(int i, BaseDataItem baseDataItem, boolean z) {
        Pair<Integer, Integer> pair;
        if (moveToPosition(i) && (pair = this.mPositions.get(i)) != null) {
            return this.mDataSets.get(((Integer) pair.first).intValue()).doDelete(((Integer) pair.second).intValue(), baseDataItem, z);
        }
        return 0;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public boolean addToAlbum(FragmentActivity fragmentActivity, int i, boolean z, boolean z2, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
        Pair<Integer, Integer> pair;
        if (moveToPosition(i) && (pair = this.mPositions.get(i)) != null) {
            return this.mDataSets.get(((Integer) pair.first).intValue()).addToAlbum(fragmentActivity, ((Integer) pair.second).intValue(), z, z2, onAddAlbumListener);
        }
        return false;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public boolean addToFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        Pair<Integer, Integer> pair;
        if (moveToPosition(i) && (pair = this.mPositions.get(i)) != null) {
            return this.mDataSets.get(((Integer) pair.first).intValue()).addToFavorites(fragmentActivity, ((Integer) pair.second).intValue(), onCompleteListener);
        }
        return false;
    }

    @Override // com.miui.gallery.model.BaseDataSet
    public boolean removeFromFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        Pair<Integer, Integer> pair;
        if (moveToPosition(i) && (pair = this.mPositions.get(i)) != null) {
            return this.mDataSets.get(((Integer) pair.first).intValue()).removeFromFavorites(fragmentActivity, ((Integer) pair.second).intValue(), onCompleteListener);
        }
        return false;
    }
}
