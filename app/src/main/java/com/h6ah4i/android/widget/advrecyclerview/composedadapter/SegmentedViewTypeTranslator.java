package com.h6ah4i.android.widget.advrecyclerview.composedadapter;

import android.util.SparseIntArray;
import com.h6ah4i.android.widget.advrecyclerview.adapter.ItemViewTypeComposer;
import com.meicam.sdk.NvsMediaFileConvertor;

/* loaded from: classes.dex */
public class SegmentedViewTypeTranslator {
    public SparseIntArray mWrapSegmentMap = new SparseIntArray();
    public SparseIntArray mUnwrapSegmentMap = new SparseIntArray();

    public static int extractWrapperSegment(long j) {
        return (int) ((j >>> 48) & 65535);
    }

    public int wrapItemViewType(int i, int i2) {
        int i3;
        int extractSegmentPart = (i << 16) | ItemViewTypeComposer.extractSegmentPart(i2);
        int indexOfKey = this.mWrapSegmentMap.indexOfKey(extractSegmentPart);
        if (indexOfKey >= 0) {
            i3 = this.mWrapSegmentMap.valueAt(indexOfKey);
        } else {
            int size = this.mWrapSegmentMap.size() + 1;
            if (size > 127) {
                throw new IllegalStateException("Failed to allocate a new wrapped view type.");
            }
            this.mWrapSegmentMap.put(extractSegmentPart, size);
            this.mUnwrapSegmentMap.put(size, extractSegmentPart);
            i3 = size;
        }
        return ItemViewTypeComposer.composeSegment(i3, i2);
    }

    public long unwrapViewType(int i) {
        int indexOfKey = this.mUnwrapSegmentMap.indexOfKey(ItemViewTypeComposer.extractSegmentPart(i));
        if (indexOfKey < 0) {
            throw new IllegalStateException("Corresponding wrapped view type is not found!");
        }
        return (this.mUnwrapSegmentMap.valueAt(indexOfKey) << 32) | (i & 4294967295L);
    }

    public static int extractWrappedViewType(long j) {
        return ItemViewTypeComposer.composeSegment(((int) (j >>> 32)) & NvsMediaFileConvertor.CONVERTOR_ERROR_UNKNOWN, (int) (j & 4294967295L));
    }
}
