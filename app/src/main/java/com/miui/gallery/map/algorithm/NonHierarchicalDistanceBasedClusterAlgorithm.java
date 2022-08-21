package com.miui.gallery.map.algorithm;

import com.miui.gallery.map.cluster.Cluster;
import com.miui.gallery.map.cluster.ClusterItem;
import com.miui.gallery.map.cluster.LatLngBounds;
import com.miui.gallery.map.cluster.MapLatLng;
import com.miui.gallery.map.cluster.StaticCluster;
import com.miui.gallery.map.projection.Bounds;
import com.miui.gallery.map.projection.Point;
import com.miui.gallery.map.projection.SphericalMercatorProjection;
import com.miui.gallery.map.quadtree.PointQuadTree;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class NonHierarchicalDistanceBasedClusterAlgorithm<T extends ClusterItem> implements ClusterAlgorithm<T> {
    public static final SphericalMercatorProjection PROJECTION = new SphericalMercatorProjection(1.0d);
    public final Collection<QuadItem<T>> mItems = new ArrayList();
    public final List<QuadItem<T>> mPreviousItemsOnScreen = new ArrayList();
    public final PointQuadTree<QuadItem<T>> mQuadTree = new PointQuadTree<>(SearchStatUtils.POW, 1.0d, SearchStatUtils.POW, 1.0d);

    public void addItem(T t) {
        QuadItem<T> quadItem = new QuadItem<>(t);
        this.mItems.add(quadItem);
        this.mQuadTree.add(quadItem);
    }

    @Override // com.miui.gallery.map.algorithm.ClusterAlgorithm
    public void addItems(Collection<T> collection) {
        synchronized (this.mQuadTree) {
            for (T t : collection) {
                addItem(t);
            }
        }
    }

    @Override // com.miui.gallery.map.algorithm.ClusterAlgorithm
    public void clearItems() {
        synchronized (this.mQuadTree) {
            this.mItems.clear();
            this.mQuadTree.clear();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.map.algorithm.ClusterAlgorithm
    public Set<Cluster<T>> getClusters(double d, LatLngBounds latLngBounds) {
        double pow = (450.0d / Math.pow(2.0d, (int) d)) / 256.0d;
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet();
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        synchronized (this.mQuadTree) {
            for (QuadItem<T> quadItem : this.mItems) {
                if (!hashSet.contains(quadItem)) {
                    Collection<QuadItem<T>> search = this.mQuadTree.search(createBoundsFromSpan(quadItem.getPoint(), pow));
                    if (search.size() == 1) {
                        hashSet2.add(quadItem);
                        hashSet.add(quadItem);
                        hashMap.put(quadItem, Double.valueOf((double) SearchStatUtils.POW));
                    } else {
                        StaticCluster staticCluster = new StaticCluster(quadItem.mClusterItem.getPosition());
                        hashSet2.add(staticCluster);
                        for (QuadItem<T> quadItem2 : search) {
                            Double d2 = (Double) hashMap.get(quadItem2);
                            double d3 = pow;
                            double distanceSquared = distanceSquared(quadItem2.getPoint(), quadItem.getPoint());
                            if (d2 != null) {
                                if (d2.doubleValue() < distanceSquared) {
                                    pow = d3;
                                } else {
                                    ((StaticCluster) hashMap2.get(quadItem2)).remove(quadItem2.mClusterItem);
                                }
                            }
                            hashMap.put(quadItem2, Double.valueOf(distanceSquared));
                            staticCluster.add(quadItem2.mClusterItem);
                            hashMap2.put(quadItem2, staticCluster);
                            pow = d3;
                        }
                        hashSet.addAll(search);
                        pow = pow;
                    }
                }
            }
        }
        return hashSet2;
    }

    public final double distanceSquared(Point point, Point point2) {
        double d = point.x;
        double d2 = point2.x;
        double d3 = (d - d2) * (d - d2);
        double d4 = point.y;
        double d5 = point2.y;
        return d3 + ((d4 - d5) * (d4 - d5));
    }

    public final Bounds createBoundsFromSpan(Point point, double d) {
        double d2 = d / 2.0d;
        double d3 = point.x;
        double d4 = d3 - d2;
        double d5 = d3 + d2;
        double d6 = point.y;
        return new Bounds(d4, d5, d6 - d2, d6 + d2);
    }

    /* loaded from: classes2.dex */
    public static class QuadItem<T extends ClusterItem> implements PointQuadTree.Item, Cluster<T> {
        public final T mClusterItem;
        public final Point mPoint;
        public final MapLatLng mPosition;
        public final Set<T> singletonSet;

        @Override // com.miui.gallery.map.cluster.Cluster
        public int getSize() {
            return 1;
        }

        public QuadItem(T t) {
            this.mClusterItem = t;
            MapLatLng position = t.getPosition();
            this.mPosition = position;
            this.mPoint = NonHierarchicalDistanceBasedClusterAlgorithm.PROJECTION.toPoint(position);
            this.singletonSet = Collections.singleton(t);
        }

        @Override // com.miui.gallery.map.quadtree.PointQuadTree.Item
        public Point getPoint() {
            return this.mPoint;
        }

        @Override // com.miui.gallery.map.cluster.Cluster
        public MapLatLng getPosition() {
            return this.mPosition;
        }

        @Override // com.miui.gallery.map.cluster.Cluster
        /* renamed from: getItems  reason: collision with other method in class */
        public Set<T> mo1081getItems() {
            return this.singletonSet;
        }

        public int hashCode() {
            T t = this.mClusterItem;
            if (t != null) {
                return t.hashCode();
            }
            return super.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj != null && getClass() == obj.getClass()) {
                T t = this.mClusterItem;
                if (t != null) {
                    return t.equals(((QuadItem) obj).mClusterItem);
                }
                return super.equals(obj);
            }
            return false;
        }
    }
}
