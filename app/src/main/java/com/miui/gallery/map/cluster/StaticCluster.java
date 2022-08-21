package com.miui.gallery.map.cluster;

import com.miui.gallery.map.cluster.ClusterItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public class StaticCluster<T extends ClusterItem> implements Cluster<T> {
    public final MapLatLng mCenter;
    public final List<T> mItems = new ArrayList();

    public StaticCluster(MapLatLng mapLatLng) {
        this.mCenter = mapLatLng;
    }

    public boolean add(T t) {
        return this.mItems.add(t);
    }

    public boolean remove(T t) {
        return this.mItems.remove(t);
    }

    @Override // com.miui.gallery.map.cluster.Cluster
    public MapLatLng getPosition() {
        return this.mCenter;
    }

    @Override // com.miui.gallery.map.cluster.Cluster
    /* renamed from: getItems */
    public Collection<T> mo1081getItems() {
        return this.mItems;
    }

    @Override // com.miui.gallery.map.cluster.Cluster
    public int getSize() {
        return this.mItems.size();
    }

    public String toString() {
        return "StaticCluster{mCenter=" + this.mCenter + ", mItems.size=" + this.mItems.size() + '}';
    }

    public int hashCode() {
        MapLatLng mapLatLng = this.mCenter;
        if (mapLatLng != null) {
            int i = ((int) (mapLatLng.latitude * 37.0d)) + ((int) (mapLatLng.longitude * 37.0d * 37.0d));
            int i2 = 0;
            if (this.mItems.size() > 0) {
                i2 = this.mItems.get(0).hashCode() * 37 * 37 * 37;
            }
            return i + i2;
        }
        return super.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            StaticCluster staticCluster = (StaticCluster) obj;
            if (this.mItems.size() <= 0 || staticCluster.mItems.size() <= 0) {
                return this.mItems == staticCluster.mItems;
            }
            MapLatLng mapLatLng = staticCluster.mCenter;
            double d = mapLatLng.latitude;
            MapLatLng mapLatLng2 = this.mCenter;
            return d == mapLatLng2.latitude && mapLatLng.longitude == mapLatLng2.longitude && staticCluster.mItems.get(0).equals(this.mItems.get(0));
        }
        return false;
    }
}
