package com.miui.gallery.map.algorithm;

import com.miui.gallery.map.cluster.Cluster;
import com.miui.gallery.map.cluster.ClusterItem;
import com.miui.gallery.map.cluster.LatLngBounds;
import java.util.Collection;
import java.util.Set;

/* loaded from: classes2.dex */
public interface ClusterAlgorithm<T extends ClusterItem> {
    void addItems(Collection<T> collection);

    void clearItems();

    Set<Cluster<T>> getClusters(double d, LatLngBounds latLngBounds);
}
