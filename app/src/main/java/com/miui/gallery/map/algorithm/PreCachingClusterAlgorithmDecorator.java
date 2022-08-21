package com.miui.gallery.map.algorithm;

import androidx.collection.LruCache;
import com.miui.gallery.map.cluster.Cluster;
import com.miui.gallery.map.cluster.ClusterItem;
import com.miui.gallery.map.cluster.LatLngBounds;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes2.dex */
public class PreCachingClusterAlgorithmDecorator<T extends ClusterItem> implements ClusterAlgorithm<T> {
    public final ClusterAlgorithm<T> mAlgorithm;
    public final LruCache<Integer, Set<Cluster<T>>> mCache = new LruCache<>(8);
    public final ReadWriteLock mCacheLock = new ReentrantReadWriteLock();

    public PreCachingClusterAlgorithmDecorator(ClusterAlgorithm<T> clusterAlgorithm) {
        this.mAlgorithm = clusterAlgorithm;
    }

    @Override // com.miui.gallery.map.algorithm.ClusterAlgorithm
    public void addItems(Collection<T> collection) {
        this.mAlgorithm.addItems(collection);
        clearCache();
    }

    @Override // com.miui.gallery.map.algorithm.ClusterAlgorithm
    public void clearItems() {
        this.mAlgorithm.clearItems();
        clearCache();
    }

    @Override // com.miui.gallery.map.algorithm.ClusterAlgorithm
    public Set<Cluster<T>> getClusters(double d, LatLngBounds latLngBounds) {
        return getClustersInternal((int) d, latLngBounds);
    }

    public final Set<Cluster<T>> getClustersInternal(int i, LatLngBounds latLngBounds) {
        this.mCacheLock.readLock().lock();
        try {
            Set<Cluster<T>> set = this.mCache.get(Integer.valueOf(i));
            if (set == null) {
                this.mCacheLock.writeLock().lock();
                try {
                    set = this.mCache.get(Integer.valueOf(i));
                    if (set == null) {
                        Set<Cluster<T>> clusters = this.mAlgorithm.getClusters(i, latLngBounds);
                        this.mCache.put(Integer.valueOf(i), clusters);
                        set = clusters;
                    }
                } finally {
                    this.mCacheLock.writeLock().unlock();
                }
            }
            return set;
        } finally {
            this.mCacheLock.readLock().unlock();
        }
    }

    public final synchronized void clearCache() {
        this.mCache.evictAll();
    }
}
