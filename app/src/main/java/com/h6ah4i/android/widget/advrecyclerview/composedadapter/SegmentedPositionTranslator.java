package com.h6ah4i.android.widget.advrecyclerview.composedadapter;

import java.util.Arrays;

/* loaded from: classes.dex */
public class SegmentedPositionTranslator {
    public AdaptersSet mAdaptersSet;
    public int[] mSegmentItemCountCache;
    public int mLastOffsetCachedSegment = 0;
    public int mCachedTotalItemCount = -1;
    public int[] mSegmentOffsetCache = new int[128];

    public SegmentedPositionTranslator(AdaptersSet adaptersSet) {
        this.mAdaptersSet = adaptersSet;
        int[] iArr = new int[128];
        this.mSegmentItemCountCache = iArr;
        Arrays.fill(iArr, -1);
    }

    public int getTotalItemCount() {
        if (this.mCachedTotalItemCount == -1) {
            this.mCachedTotalItemCount = countTotalItems();
        }
        return this.mCachedTotalItemCount;
    }

    public int getFlatPosition(int i, int i2) {
        return getSegmentOffset(i) + i2;
    }

    public long getSegmentedPosition(int i) {
        int i2 = -1;
        if (i == -1) {
            return -1L;
        }
        int i3 = 0;
        int binarySearch = Arrays.binarySearch(this.mSegmentOffsetCache, 0, this.mLastOffsetCachedSegment, i);
        if (binarySearch >= 0) {
            i2 = binarySearch;
        } else {
            binarySearch = Math.max(0, (~binarySearch) - 1);
            i3 = -1;
        }
        int segmentCount = this.mAdaptersSet.getSegmentCount();
        int i4 = this.mSegmentOffsetCache[binarySearch];
        while (true) {
            if (binarySearch >= segmentCount) {
                break;
            }
            int segmentItemCount = getSegmentItemCount(binarySearch) + i4;
            if (segmentItemCount > i) {
                i3 = i - i4;
                i2 = binarySearch;
                break;
            }
            binarySearch++;
            i4 = segmentItemCount;
        }
        if (i2 >= 0) {
            return AdaptersSet.composeSegmentedPosition(i2, i3);
        }
        return AdaptersSet.NO_SEGMENTED_POSITION;
    }

    public final int countTotalItems() {
        int segmentCount = this.mAdaptersSet.getSegmentCount();
        if (segmentCount == 0) {
            return 0;
        }
        int i = segmentCount - 1;
        return getSegmentOffset(i) + getSegmentItemCount(i);
    }

    public int getSegmentOffset(int i) {
        if (i <= this.mLastOffsetCachedSegment) {
            return this.mSegmentOffsetCache[i];
        }
        this.mAdaptersSet.getSegmentCount();
        int i2 = this.mLastOffsetCachedSegment;
        int i3 = this.mSegmentOffsetCache[i2];
        while (i2 < i) {
            i3 += getSegmentItemCount(i2);
            i2++;
        }
        return i3;
    }

    public int getSegmentItemCount(int i) {
        int[] iArr = this.mSegmentItemCountCache;
        if (iArr[i] != -1) {
            return iArr[i];
        }
        int itemCount = this.mAdaptersSet.getAdapter(i).getItemCount();
        this.mSegmentItemCountCache[i] = itemCount;
        if (i == this.mLastOffsetCachedSegment) {
            int[] iArr2 = this.mSegmentOffsetCache;
            int i2 = i + 1;
            iArr2[i2] = iArr2[i] + itemCount;
            this.mLastOffsetCachedSegment = i2;
        }
        return itemCount;
    }

    public void invalidateSegment(int i) {
        this.mCachedTotalItemCount = -1;
        this.mLastOffsetCachedSegment = Math.min(this.mLastOffsetCachedSegment, i);
        this.mSegmentItemCountCache[i] = -1;
    }

    public void invalidateAll() {
        this.mCachedTotalItemCount = -1;
        this.mLastOffsetCachedSegment = 0;
        Arrays.fill(this.mSegmentItemCountCache, -1);
    }
}
