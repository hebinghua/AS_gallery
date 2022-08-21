package com.miui.gallery.ui.pictures.cluster;

import com.miui.gallery.data.Cluster;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;

/* compiled from: AggregatedTimelineCluster2.kt */
/* loaded from: classes2.dex */
public final class AggregatedTimelineCluster2 extends StandardTimelineCluster2 {
    @Override // com.miui.gallery.ui.pictures.cluster.StandardTimelineCluster2, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public boolean isAggregated() {
        return true;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AggregatedTimelineCluster2(Cluster cluster) {
        super(cluster);
        Intrinsics.checkNotNullParameter(cluster, "cluster");
    }

    @Override // com.miui.gallery.ui.pictures.cluster.StandardTimelineCluster2, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildCount(int i, int i2, int i3, boolean z) {
        int childCount;
        int i4 = i2 * i3;
        int i5 = 1;
        if (!z && i == 0) {
            childCount = getItemCount();
        } else {
            childCount = getCluster().getChildCount(i, true);
        }
        int i6 = childCount / i4;
        if (childCount % i4 == 0) {
            i5 = 0;
        }
        return i6 + i5;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.StandardTimelineCluster2, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildRows(int i, int i2, int i3, boolean z) {
        return getChildCount(i, i2, i3, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.BaseTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int calculateMatchItemPosition(int i, int i2, float f, float f2, int i3, int i4, int i5, int i6, boolean z) {
        float coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(0.0f, f - 0.1f);
        int i7 = i2 * i4 * i5;
        float f3 = ((int) (((i3 - ((i6 * 1.0f) * (i4 - 1))) / i4) + 0.5d)) + i6;
        int floor = ((int) Math.floor(RangesKt___RangesKt.coerceAtLeast(0.0f, f2 - 0.1f) / f3)) * i4;
        return (((getCluster().getGroupStartPosition(i, z) + i7) + floor) + RangesKt___RangesKt.coerceAtLeast(1, RangesKt___RangesKt.coerceAtMost((int) Math.ceil(coerceAtLeast / f3), RangesKt___RangesKt.coerceAtMost((getCluster().getChildCount(i, z) - i7) - floor, i4)))) - 1;
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
