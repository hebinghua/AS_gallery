package com.h6ah4i.android.widget.advrecyclerview.adapter;

/* loaded from: classes.dex */
public class ItemIdComposer {
    public static boolean isExpandableGroup(long j) {
        return j != -1 && (j & 134217727) == 134217727;
    }

    public static long composeExpandableChildId(long j, long j2) {
        if (j < -536870912 || j > 536870911) {
            throw new IllegalArgumentException("Group ID value is out of range. (groupId = " + j + ")");
        } else if (j2 >= -67108864 && j2 <= 67108863) {
            return ((j << 27) & 144115187941638144L) | ((j2 << 0) & 134217727);
        } else {
            throw new IllegalArgumentException("Child ID value is out of range. (childId = " + j2 + ")");
        }
    }

    public static long composeExpandableGroupId(long j) {
        if (j < -536870912 || j > 536870911) {
            throw new IllegalArgumentException("Group ID value is out of range. (groupId = " + j + ")");
        }
        return ((j << 27) & 144115187941638144L) | 134217727;
    }

    public static long extractExpandableChildIdPart(long j) {
        if (j == -1 || isExpandableGroup(j)) {
            return -1L;
        }
        return (j << 37) >> 37;
    }

    public static long composeSegment(int i, long j) {
        if (i >= 0 && i <= 63) {
            return (j & (-9079256848778919937L)) | (i << 57);
        }
        throw new IllegalArgumentException("Segment value is out of range. (segment = " + i + ")");
    }
}
