package com.miui.gallery.map.cluster;

import com.miui.gallery.map.cluster.ClusterItem;
import com.miui.gallery.map.cluster.ClusterManager;
import java.util.Set;

/* loaded from: classes2.dex */
public interface ClusterRenderer<T extends ClusterItem> {
    void onAdd();

    void onClustersChanged(Set<Cluster<T>> set, float f);

    void release();

    void setOnClusterClickListener(ClusterManager.OnClusterClickListener<T> onClusterClickListener);

    void setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<T> onClusterItemClickListener);
}
