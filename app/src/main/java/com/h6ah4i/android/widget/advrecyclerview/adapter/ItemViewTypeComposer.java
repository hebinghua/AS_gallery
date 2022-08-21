package com.h6ah4i.android.widget.advrecyclerview.adapter;

/* loaded from: classes.dex */
public class ItemViewTypeComposer {
    public static int extractSegmentPart(int i) {
        return (i & 2130706432) >>> 24;
    }

    public static int composeSegment(int i, int i2) {
        if (i < 0 || i > 127) {
            throw new IllegalArgumentException("Segment value is out of range. (segment = " + i + ")");
        }
        return (i << 24) | (i2 & (-2130706433));
    }
}
