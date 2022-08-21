package com.miui.gallery.map.cluster;

import com.miui.gallery.map.cluster.ClusterItem;
import java.util.Collection;

/* loaded from: classes2.dex */
public interface Cluster<T extends ClusterItem> {
    /* renamed from: getItems */
    Collection<T> mo1081getItems();

    MapLatLng getPosition();

    int getSize();
}
