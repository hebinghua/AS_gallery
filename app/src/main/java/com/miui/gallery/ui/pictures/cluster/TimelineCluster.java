package com.miui.gallery.ui.pictures.cluster;

import android.graphics.Rect;
import com.miui.gallery.data.Cluster;
import java.util.List;
import java.util.function.Function;

/* loaded from: classes2.dex */
public interface TimelineCluster extends Cluster {
    public static final TimelineCluster DUMMY = new DummyTimelineCluster();

    int calculateMatchItemPosition(int i, int i2, float f, float f2, int i3, int i4, int i5, int i6, boolean z);

    int calculateScrollPosition(int i, int i2, int i3, boolean z);

    List<TransitFrame> calculateTransitFrames(int i, long j, float f, int i2, int i3, int i4, Function<Integer, Integer> function, int i5, int i6, Rect rect, boolean z, boolean z2, boolean z3);

    int computeDataPosition(int i, int i2, int i3, Function<Integer, Integer> function, int i4, int i5, int i6, float f, int i7, boolean z);

    int computeScrollOffset(int i, int i2, int i3, int i4, Function<Integer, Integer> function, int i5, boolean z);

    int[] computeScrollPositionAndOffset(int i, int i2, int i3, Function<Integer, Integer> function, int i4, int i5, float f, boolean z);

    int computeScrollRange(int i, int i2, int i3, Function<Integer, Integer> function, int i4, boolean z);

    Rect estimateItemRect(int i, int i2, long j, int i3, int i4, int i5, boolean z);

    int getChildCount(int i, int i2, int i3, boolean z);

    int getChildRows(int i, int i2, int i3, boolean z);

    float getGroupHeight(int i, int i2, int i3, int i4, int i5, boolean z);

    @Override // com.miui.gallery.data.Cluster
    int getItemCount();

    boolean isAggregated();

    int packAdapterPosition(int i, int i2, boolean z);
}
