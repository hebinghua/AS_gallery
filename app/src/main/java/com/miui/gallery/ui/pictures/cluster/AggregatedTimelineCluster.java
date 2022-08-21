package com.miui.gallery.ui.pictures.cluster;

import java.util.List;

/* loaded from: classes2.dex */
public class AggregatedTimelineCluster extends StandardTimelineCluster {
    @Override // com.miui.gallery.ui.pictures.cluster.StandardTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public boolean isAggregated() {
        return true;
    }

    public AggregatedTimelineCluster(List<Integer> list, List<Integer> list2, List<String> list3) {
        super(list, list2, list3);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.StandardTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildCount(int i, int i2, int i3, boolean z) {
        int intValue;
        int i4 = i2 * i3;
        int i5 = 0;
        if (!z && i == 0) {
            intValue = getItemCount();
        } else {
            intValue = i < this.mGroupItemCount.size() ? this.mGroupItemCount.get(i).intValue() : 0;
        }
        int i6 = intValue / i4;
        if (intValue % i4 != 0) {
            i5 = 1;
        }
        return i6 + i5;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.StandardTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildRows(int i, int i2, int i3, boolean z) {
        return getChildCount(i, i2, i3, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.BaseTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int calculateMatchItemPosition(int i, int i2, float f, float f2, int i3, int i4, int i5, int i6, boolean z) {
        float max = Math.max(0.0f, f - 0.1f);
        float max2 = Math.max(0.0f, f2 - 0.1f);
        int i7 = i2 * i4 * i5;
        int intValue = (z ? this.mGroupStartPos.get(i).intValue() : 0) + i7;
        float f3 = ((int) (((i3 - ((i6 * 1.0f) * (i4 - 1))) / i4) + 0.5d)) + i6;
        int floor = ((int) Math.floor(max2 / f3)) * i4;
        return ((intValue + floor) + Math.max(1, Math.min((int) Math.ceil(max / f3), Math.min(((z ? this.mGroupItemCount.get(i).intValue() : getItemCount()) - i7) - floor, i4)))) - 1;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.BaseTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int calculateScrollPosition(int i, int i2, int i3, boolean z) {
        int[] unpackAdapterPosition = unpackAdapterPosition(i, z);
        int i4 = 0;
        int i5 = unpackAdapterPosition[0];
        int i6 = unpackAdapterPosition[1];
        int i7 = i5;
        for (int i8 = 0; i8 < i5; i8++) {
            i7 += getChildCount(i8, i2, i3, z);
        }
        int i9 = i2 * i3;
        int i10 = i6 + 1;
        int i11 = i10 / i9;
        if (i10 % i9 != 0) {
            i4 = 1;
        }
        int i12 = i7 + i11 + i4;
        return !z ? i12 - 1 : i12;
    }
}
