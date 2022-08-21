package com.miui.gallery.ui.pictures.cluster;

import android.database.Cursor;
import android.graphics.Rect;
import java.util.List;
import java.util.function.Function;

/* loaded from: classes2.dex */
public class BunchTimelineCluster extends DummyTimelineCluster {
    public final int mCount;

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ int calculateMatchItemPosition(int i, int i2, float f, float f2, int i3, int i4, int i5, int i6, boolean z) {
        return super.calculateMatchItemPosition(i, i2, f, f2, i3, i4, i5, i6, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ int calculateScrollPosition(int i, int i2, int i3, boolean z) {
        return super.calculateScrollPosition(i, i2, i3, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ List calculateTransitFrames(int i, long j, float f, int i2, int i3, int i4, Function function, int i5, int i6, Rect rect, boolean z, boolean z2, boolean z3) {
        return super.calculateTransitFrames(i, j, f, i2, i3, i4, function, i5, i6, rect, z, z2, z3);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ int computeDataPosition(int i, int i2, int i3, Function function, int i4, int i5, int i6, float f, int i7, boolean z) {
        return super.computeDataPosition(i, i2, i3, function, i4, i5, i6, f, i7, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ int computeScrollOffset(int i, int i2, int i3, int i4, Function function, int i5, boolean z) {
        return super.computeScrollOffset(i, i2, i3, i4, function, i5, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ int[] computeScrollPositionAndOffset(int i, int i2, int i3, Function function, int i4, int i5, float f, boolean z) {
        return super.computeScrollPositionAndOffset(i, i2, i3, function, i4, i5, f, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ int computeScrollRange(int i, int i2, int i3, Function function, int i4, boolean z) {
        return super.computeScrollRange(i, i2, i3, function, i4, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ Rect estimateItemRect(int i, int i2, long j, int i3, int i4, int i5, boolean z) {
        return super.estimateItemRect(i, i2, j, i3, i4, i5, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.data.Cluster
    public /* bridge */ /* synthetic */ int getChildCount(int i, boolean z) {
        return super.getChildCount(i, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ float getGroupHeight(int i, int i2, int i3, int i4, int i5, boolean z) {
        return super.getGroupHeight(i, i2, i3, i4, i5, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.data.Cluster
    public /* bridge */ /* synthetic */ String getGroupLabel(int i, boolean z) {
        return super.getGroupLabel(i, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.data.Cluster
    public /* bridge */ /* synthetic */ int[] getGroupPositions(int i, boolean z) {
        return super.getGroupPositions(i, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.data.Cluster
    public /* bridge */ /* synthetic */ int getGroupStartPosition(int i, boolean z) {
        return super.getGroupStartPosition(i, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.data.Cluster
    public /* bridge */ /* synthetic */ List getGroupStartPositions(boolean z) {
        return super.getGroupStartPositions(z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public /* bridge */ /* synthetic */ int packAdapterPosition(int i, int i2, boolean z) {
        return super.packAdapterPosition(i, i2, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.data.Cluster
    public /* bridge */ /* synthetic */ int packDataPosition(int i, int i2, boolean z) {
        return super.packDataPosition(i, i2, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.data.Cluster
    public /* bridge */ /* synthetic */ int[] unpackAdapterPosition(int i, boolean z) {
        return super.unpackAdapterPosition(i, z);
    }

    public BunchTimelineCluster(Cursor cursor) {
        this.mCount = cursor.getCount();
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster, com.miui.gallery.data.Cluster
    public int getItemCount() {
        return this.mCount;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.data.Cluster
    public int getGroupCount(boolean z) {
        return this.mCount > 0 ? 1 : 0;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.DummyTimelineCluster, com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildCount(int i, int i2, int i3, boolean z) {
        return this.mCount;
    }
}
