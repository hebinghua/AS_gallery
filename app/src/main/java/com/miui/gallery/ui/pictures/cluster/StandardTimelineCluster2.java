package com.miui.gallery.ui.pictures.cluster;

import android.util.Log;
import com.miui.gallery.data.Cluster;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: StandardTimelineCluster2.kt */
/* loaded from: classes2.dex */
public class StandardTimelineCluster2 extends BaseTimelineCluster {
    public static final Companion Companion = new Companion(null);
    public static final boolean DBG = Log.isLoggable("StandardTimelineCluster", 3);
    public final Cluster cluster;

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public boolean isAggregated() {
        return false;
    }

    public final Cluster getCluster() {
        return this.cluster;
    }

    public StandardTimelineCluster2(Cluster cluster) {
        Intrinsics.checkNotNullParameter(cluster, "cluster");
        this.cluster = cluster;
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster, com.miui.gallery.data.Cluster
    public int getItemCount() {
        return this.cluster.getItemCount();
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupCount(boolean z) {
        return this.cluster.getGroupCount(z);
    }

    @Override // com.miui.gallery.data.Cluster
    public int packDataPosition(int i, int i2, boolean z) {
        return this.cluster.packDataPosition(i, i2, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int packAdapterPosition(int i, int i2, boolean z) {
        return this.cluster.packDataPosition(i, i2, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildCount(int i, int i2, int i3, boolean z) {
        return this.cluster.getChildCount(i, z);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.TimelineCluster
    public int getChildRows(int i, int i2, int i3, boolean z) {
        int childCount;
        if (!z && i == 0) {
            childCount = getItemCount();
        } else {
            childCount = this.cluster.getChildCount(i, z);
        }
        return (childCount / i2) + (childCount % i2 != 0 ? 1 : 0);
    }

    @Override // com.miui.gallery.ui.pictures.cluster.BaseTimelineCluster
    public int getRawChildCount(int i, boolean z) {
        return this.cluster.getChildCount(i, z);
    }

    @Override // com.miui.gallery.data.Cluster
    public int getGroupStartPosition(int i, boolean z) {
        return this.cluster.getGroupStartPosition(i, z);
    }

    @Override // com.miui.gallery.data.Cluster
    public int[] getGroupPositions(int i, boolean z) {
        return this.cluster.getGroupPositions(i, z);
    }

    @Override // com.miui.gallery.data.Cluster
    public String getGroupLabel(int i, boolean z) {
        return this.cluster.getGroupLabel(i, z);
    }

    @Override // com.miui.gallery.data.Cluster
    public int[] unpackAdapterPosition(int i, boolean z) {
        return this.cluster.unpackAdapterPosition(i, z);
    }

    @Override // com.miui.gallery.data.Cluster
    public List<Integer> getGroupStartPositions(boolean z) {
        return this.cluster.getGroupStartPositions(z);
    }

    /* compiled from: StandardTimelineCluster2.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
