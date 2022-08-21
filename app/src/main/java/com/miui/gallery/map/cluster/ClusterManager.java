package com.miui.gallery.map.cluster;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import com.miui.gallery.map.algorithm.ClusterAlgorithm;
import com.miui.gallery.map.algorithm.NonHierarchicalDistanceBasedClusterAlgorithm;
import com.miui.gallery.map.algorithm.PreCachingClusterAlgorithmDecorator;
import com.miui.gallery.map.cluster.ClusterItem;
import com.miui.gallery.map.utils.IMapStatus;
import com.miui.gallery.map.utils.IMarker;
import com.miui.gallery.map.utils.MapConfig;
import com.miui.gallery.map.utils.MarkerManager;
import com.miui.gallery.map.utils.OnMapStatusChangeListener;
import com.miui.gallery.map.utils.OnMarkerClickListener;
import com.miui.gallery.map.view.IMapContainer;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes2.dex */
public class ClusterManager<T extends ClusterItem> implements OnMapStatusChangeListener, OnMarkerClickListener {
    public final ClusterAlgorithm<T> mAlgorithm;
    public final ReadWriteLock mAlgorithmLock;
    public final MarkerManager.Collection mClusterMarkers;
    public ClusterManager<T>.ClusterTask mClusterTask;
    public final ReadWriteLock mClusterTaskLock;
    public final IMapContainer mMap;
    public final MarkerManager mMarkerManager;
    public final MarkerManager.Collection mMarkers;
    public OnClusterClickListener<T> mOnClusterClickListener;
    public OnClusterItemClickListener<T> mOnClusterItemClickListener;
    public volatile IMapStatus mPreviousCameraPosition;
    public ClusterRenderer<T> mRenderer;
    public OnMapStatusChangeFinish onMapStatusChangeFinishListener;

    /* loaded from: classes2.dex */
    public interface OnClusterClickListener<T extends ClusterItem> {
        boolean onClusterClick(Cluster<T> cluster);
    }

    /* loaded from: classes2.dex */
    public interface OnClusterItemClickListener<T extends ClusterItem> {
        boolean onClusterItemClick(T t);
    }

    /* loaded from: classes2.dex */
    public interface OnMapStatusChangeFinish {
        void onMapStatusChangeFinish(IMapStatus iMapStatus);
    }

    public ClusterManager(Context context, IMapContainer iMapContainer) {
        this(context, iMapContainer, new MarkerManager(iMapContainer));
    }

    public ClusterManager(Context context, IMapContainer iMapContainer, MarkerManager markerManager) {
        this.mAlgorithmLock = new ReentrantReadWriteLock();
        this.mClusterTaskLock = new ReentrantReadWriteLock();
        this.mMap = iMapContainer;
        this.mMarkerManager = markerManager;
        this.mClusterMarkers = markerManager.newCollection();
        this.mMarkers = markerManager.newCollection();
        this.mRenderer = new DefaultClusterRenderer(context, iMapContainer, this);
        this.mAlgorithm = new PreCachingClusterAlgorithmDecorator(new NonHierarchicalDistanceBasedClusterAlgorithm());
        this.mRenderer.onAdd();
    }

    public MarkerManager.Collection getMarkerCollection() {
        return this.mMarkers;
    }

    public MarkerManager.Collection getClusterMarkerCollection() {
        return this.mClusterMarkers;
    }

    public MarkerManager getMarkerManager() {
        return this.mMarkerManager;
    }

    public void clearItems() {
        this.mAlgorithmLock.writeLock().lock();
        try {
            this.mAlgorithm.clearItems();
        } finally {
            this.mAlgorithmLock.writeLock().unlock();
        }
    }

    public void addItems(Collection<T> collection) {
        this.mAlgorithmLock.writeLock().lock();
        try {
            this.mAlgorithm.addItems(collection);
        } finally {
            this.mAlgorithmLock.writeLock().unlock();
        }
    }

    public void cluster(float f, LatLngBounds latLngBounds) {
        this.mClusterTaskLock.writeLock().lock();
        try {
            ClusterManager<T>.ClusterTask clusterTask = this.mClusterTask;
            if (clusterTask != null) {
                clusterTask.cancel(true);
            }
            this.mClusterTask = new ClusterTask(latLngBounds);
            if (f <= 0.0f || f > 23.0f) {
                f = this.mMap.getZoomLevel();
            }
            if (Build.VERSION.SDK_INT < 11) {
                this.mClusterTask.execute(Float.valueOf(f));
            } else {
                this.mClusterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Float.valueOf(f));
            }
        } finally {
            this.mClusterTaskLock.writeLock().unlock();
        }
    }

    public void setOnMapStatusChangeFinishListener(OnMapStatusChangeFinish onMapStatusChangeFinish) {
        this.onMapStatusChangeFinishListener = onMapStatusChangeFinish;
    }

    @Override // com.miui.gallery.map.utils.OnMapStatusChangeListener
    public void onMapStatusChangeFinish(IMapStatus iMapStatus) {
        IMapStatus mapStatus = this.mMap.getMapStatus();
        if (this.mMap.hasMapLoaded()) {
            if (this.mPreviousCameraPosition != null && Math.abs(this.mPreviousCameraPosition.getZoomLevel() - iMapStatus.getZoomLevel()) < MapConfig.MIN_CLUSTER_ZOOM_LEVEL.floatValue() && this.mPreviousCameraPosition.getTarget().latitude == iMapStatus.getTarget().latitude && this.mPreviousCameraPosition.getTarget().longitude == iMapStatus.getTarget().longitude) {
                return;
            }
            this.mPreviousCameraPosition = this.mMap.getMapStatus();
            cluster(mapStatus.getZoomLevel(), mapStatus.getBound());
            OnMapStatusChangeFinish onMapStatusChangeFinish = this.onMapStatusChangeFinishListener;
            if (onMapStatusChangeFinish == null) {
                return;
            }
            onMapStatusChangeFinish.onMapStatusChangeFinish(iMapStatus);
        }
    }

    public void setOnClusterClickListener(OnClusterClickListener<T> onClusterClickListener) {
        this.mOnClusterClickListener = onClusterClickListener;
        this.mRenderer.setOnClusterClickListener(onClusterClickListener);
    }

    public void setOnClusterItemClickListener(OnClusterItemClickListener<T> onClusterItemClickListener) {
        this.mOnClusterItemClickListener = onClusterItemClickListener;
        this.mRenderer.setOnClusterItemClickListener(onClusterItemClickListener);
    }

    @Override // com.miui.gallery.map.utils.OnMarkerClickListener
    public boolean onMarkerClick(IMarker iMarker) {
        return getMarkerManager().onMarkerClick(iMarker);
    }

    public void release() {
        ClusterManager<T>.ClusterTask clusterTask = this.mClusterTask;
        if (clusterTask != null) {
            clusterTask.cancel(true);
        }
        ClusterRenderer<T> clusterRenderer = this.mRenderer;
        if (clusterRenderer != null) {
            clusterRenderer.release();
            this.mClusterMarkers.clear();
            this.mMarkers.clear();
            this.mRenderer = null;
        }
    }

    /* loaded from: classes2.dex */
    public class ClusterTask extends AsyncTask<Float, Void, Set<Cluster<T>>> {
        public float curZoom = 0.0f;
        public LatLngBounds visibleBounds;

        @Override // android.os.AsyncTask
        public /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            onPostExecute((Set) ((Set) obj));
        }

        public ClusterTask(LatLngBounds latLngBounds) {
            this.visibleBounds = null;
            this.visibleBounds = latLngBounds;
        }

        @Override // android.os.AsyncTask
        public Set<Cluster<T>> doInBackground(Float... fArr) {
            ClusterManager.this.mAlgorithmLock.readLock().lock();
            try {
                this.curZoom = fArr[0].floatValue();
                ClusterAlgorithm clusterAlgorithm = ClusterManager.this.mAlgorithm;
                double floatValue = fArr[0].floatValue();
                LatLngBounds latLngBounds = this.visibleBounds;
                if (latLngBounds == null) {
                    latLngBounds = ClusterManager.this.mMap.getBound();
                }
                return clusterAlgorithm.getClusters(floatValue, latLngBounds);
            } finally {
                ClusterManager.this.mAlgorithmLock.readLock().unlock();
            }
        }

        public void onPostExecute(Set<Cluster<T>> set) {
            if (isCancelled() || ClusterManager.this.mRenderer == null) {
                return;
            }
            ClusterManager.this.mRenderer.onClustersChanged(set, this.curZoom);
        }
    }
}
