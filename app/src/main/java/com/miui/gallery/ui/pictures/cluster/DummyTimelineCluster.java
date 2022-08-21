package com.miui.gallery.ui.pictures.cluster;

import android.graphics.Rect;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/* loaded from: classes2.dex */
public class DummyTimelineCluster implements TimelineCluster {
    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int calculateMatchItemPosition(int i, int i2, float f, float f2, int i3, int i4, int i5, int i6, boolean z) {
        return 0;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int calculateScrollPosition(int i, int i2, int i3, boolean z) {
        return 0;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int computeDataPosition(int i, int i2, int i3, Function<Integer, Integer> function, int i4, int i5, int i6, float f, int i7, boolean z) {
        return 0;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int computeScrollOffset(int i, int i2, int i3, int i4, Function<Integer, Integer> function, int i5, boolean z) {
        return 0;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int computeScrollRange(int i, int i2, int i3, Function<Integer, Integer> function, int i4, boolean z) {
        return 0;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public Rect estimateItemRect(int i, int i2, long j, int i3, int i4, int i5, boolean z) {
        return null;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildCount(int i, int i2, int i3, boolean z) {
        return 0;
    }

    @Override // com.miui.gallery.data.Cluster
    public int getChildCount(int i, boolean z) {
        return 0;
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupCount(boolean z) {
        return 0;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public float getGroupHeight(int i, int i2, int i3, int i4, int i5, boolean z) {
        return 0.0f;
    }

    @Override // com.miui.gallery.data.Cluster
    public String getGroupLabel(int i, boolean z) {
        return null;
    }

    @Override // com.miui.gallery.data.Cluster
    public int[] getGroupPositions(int i, boolean z) {
        return new int[0];
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupStartPosition(int i, boolean z) {
        return 0;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster, com.miui.gallery.data.Cluster
    public int getItemCount() {
        return 0;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int packAdapterPosition(int i, int i2, boolean z) {
        return i2 + 1;
    }

    @Override // com.miui.gallery.data.Cluster
    public int packDataPosition(int i, int i2, boolean z) {
        return i2;
    }

    @Override // com.miui.gallery.data.Cluster
    public int[] unpackAdapterPosition(int i, boolean z) {
        return new int[]{0, i};
    }

    @Override // com.miui.gallery.data.Cluster
    public List<Integer> getGroupStartPositions(boolean z) {
        return Collections.emptyList();
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public List<TransitFrame> calculateTransitFrames(int i, long j, float f, int i2, int i3, int i4, Function<Integer, Integer> function, int i5, int i6, Rect rect, boolean z, boolean z2, boolean z3) {
        return Collections.emptyList();
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int[] computeScrollPositionAndOffset(int i, int i2, int i3, Function<Integer, Integer> function, int i4, int i5, float f, boolean z) {
        return new int[]{0, 0};
    }
}
