package com.miui.gallery.map.cluster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.map.cluster.ClusterItem;
import com.miui.gallery.map.cluster.ClusterManager;
import com.miui.gallery.map.cluster.DefaultClusterRenderer;
import com.miui.gallery.map.data.MapItem;
import com.miui.gallery.map.utils.BitmapDescriptorWrapperFactory;
import com.miui.gallery.map.utils.IMapStatus;
import com.miui.gallery.map.utils.IMarker;
import com.miui.gallery.map.utils.IMarkerOptions;
import com.miui.gallery.map.utils.IconGenerator;
import com.miui.gallery.map.utils.MapBitmapTool;
import com.miui.gallery.map.utils.OnMarkerClickListener;
import com.miui.gallery.map.view.IMapContainer;
import com.miui.gallery.map.view.MarkerOptionsWrapper;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public class DefaultClusterRenderer<T extends ClusterItem> implements ClusterRenderer<T> {
    public ClusterManager.OnClusterClickListener<T> mClickListener;
    public final ClusterManager<T> mClusterManager;
    public final Context mContext;
    public ClusterManager.OnClusterItemClickListener<T> mItemClickListener;
    public final IMapContainer mMapContainer;
    public final MarkerCache<T> mMarkerCache = new MarkerCache<>();
    public Set<Cluster<T>> mClusters = new HashSet();
    public final Map<IMarker, Cluster<T>> mMarkerToCluster = new ConcurrentHashMap();
    public final Map<Cluster<T>, IMarker> mClusterToMarker = new ConcurrentHashMap();
    public final Map<Cluster<T>, Integer> onReadyAddCluster = new ConcurrentHashMap();
    public DefaultClusterRenderer<T>.ViewModifier mViewModifier = new ViewModifier();
    public final ReentrantLock markRemoveAndAddLock = new ReentrantLock();
    public final ExecutorService mExecutor = Executors.newSingleThreadExecutor(DefaultClusterRenderer$$ExternalSyntheticLambda4.INSTANCE);

    /* renamed from: $r8$lambda$J-BVUE7LArseJkaG_Xe9Ru0KoUE */
    public static /* synthetic */ void m1082$r8$lambda$JBVUE7LArseJkaG_Xe9Ru0KoUE(DefaultClusterRenderer defaultClusterRenderer, Cluster cluster, MapItem mapItem, IMarkerOptions iMarkerOptions, Future future) {
        defaultClusterRenderer.lambda$onBeforeClusterRendered$4(cluster, mapItem, iMarkerOptions, future);
    }

    /* renamed from: $r8$lambda$M8-l0pt6ce4gJB6I1WY2BVQ4thI */
    public static /* synthetic */ boolean m1083$r8$lambda$M8l0pt6ce4gJB6I1WY2BVQ4thI(DefaultClusterRenderer defaultClusterRenderer, IMarker iMarker) {
        return defaultClusterRenderer.lambda$onAdd$2(iMarker);
    }

    /* renamed from: $r8$lambda$iw-xlkuhbGptiMGSYkuvj2dsRdI */
    public static /* synthetic */ Bitmap m1084$r8$lambda$iwxlkuhbGptiMGSYkuvj2dsRdI(DefaultClusterRenderer defaultClusterRenderer, MapItem mapItem, ThreadPool.JobContext jobContext) {
        return defaultClusterRenderer.lambda$onBeforeClusterRendered$3(mapItem, jobContext);
    }

    public static /* synthetic */ boolean $r8$lambda$sCFs5kpbhZnen54QzitQdARLH2M(DefaultClusterRenderer defaultClusterRenderer, IMarker iMarker) {
        return defaultClusterRenderer.lambda$onAdd$1(iMarker);
    }

    public void onRemoveCluster(Cluster<T> cluster) {
    }

    public DefaultClusterRenderer(Context context, IMapContainer iMapContainer, ClusterManager<T> clusterManager) {
        this.mContext = context;
        this.mMapContainer = iMapContainer;
        this.mClusterManager = clusterManager;
    }

    public static /* synthetic */ Thread lambda$new$0(Runnable runnable) {
        Thread newThread = Executors.defaultThreadFactory().newThread(runnable);
        newThread.setName("DefaultClusterRenderer");
        return newThread;
    }

    public /* synthetic */ boolean lambda$onAdd$1(IMarker iMarker) {
        ClusterManager.OnClusterItemClickListener<T> onClusterItemClickListener = this.mItemClickListener;
        return onClusterItemClickListener != null && onClusterItemClickListener.onClusterItemClick(this.mMarkerCache.get(iMarker));
    }

    @Override // com.miui.gallery.map.cluster.ClusterRenderer
    public void onAdd() {
        this.mClusterManager.getMarkerCollection().setOnMarkerClickListener(new OnMarkerClickListener() { // from class: com.miui.gallery.map.cluster.DefaultClusterRenderer$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.map.utils.OnMarkerClickListener
            public final boolean onMarkerClick(IMarker iMarker) {
                return DefaultClusterRenderer.$r8$lambda$sCFs5kpbhZnen54QzitQdARLH2M(DefaultClusterRenderer.this, iMarker);
            }
        });
        this.mClusterManager.getClusterMarkerCollection().setOnMarkerClickListener(new OnMarkerClickListener() { // from class: com.miui.gallery.map.cluster.DefaultClusterRenderer$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.map.utils.OnMarkerClickListener
            public final boolean onMarkerClick(IMarker iMarker) {
                return DefaultClusterRenderer.m1083$r8$lambda$M8l0pt6ce4gJB6I1WY2BVQ4thI(DefaultClusterRenderer.this, iMarker);
            }
        });
    }

    public /* synthetic */ boolean lambda$onAdd$2(IMarker iMarker) {
        ClusterManager.OnClusterClickListener<T> onClusterClickListener = this.mClickListener;
        return onClusterClickListener != null && onClusterClickListener.onClusterClick(this.mMarkerToCluster.get(iMarker));
    }

    public void onRemove() {
        this.mClusterManager.getMarkerCollection().setOnMarkerClickListener(null);
        this.mClusterManager.getClusterMarkerCollection().setOnMarkerClickListener(null);
    }

    @Override // com.miui.gallery.map.cluster.ClusterRenderer
    public void setOnClusterClickListener(ClusterManager.OnClusterClickListener<T> onClusterClickListener) {
        this.mClickListener = onClusterClickListener;
    }

    @Override // com.miui.gallery.map.cluster.ClusterRenderer
    public void setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<T> onClusterItemClickListener) {
        this.mItemClickListener = onClusterItemClickListener;
    }

    @Override // com.miui.gallery.map.cluster.ClusterRenderer
    public void onClustersChanged(Set<Cluster<T>> set, float f) {
        this.mViewModifier.queue(set, f);
    }

    public IMapStatus getMapStatus() {
        IMapContainer iMapContainer = this.mMapContainer;
        if (iMapContainer == null || !iMapContainer.hasMapLoaded()) {
            return null;
        }
        return this.mMapContainer.getMapStatus();
    }

    @Override // com.miui.gallery.map.cluster.ClusterRenderer
    public void release() {
        DefaultClusterRenderer<T>.ViewModifier viewModifier = this.mViewModifier;
        if (viewModifier != null) {
            viewModifier.removeCallbacksAndMessages(null);
            this.mViewModifier = null;
        }
        this.mItemClickListener = null;
        this.mClickListener = null;
        onRemove();
    }

    @SuppressLint({"HandlerLeak"})
    /* loaded from: classes2.dex */
    public class ViewModifier extends Handler {
        public float curZoom;
        public DefaultClusterRenderer<T>.RenderTask mNextClusters;
        public boolean mViewModificationInProgress;

        /* renamed from: $r8$lambda$pLxy-OXEvinV80NgTvQEZoMNcxU */
        public static /* synthetic */ void m1085$r8$lambda$pLxyOXEvinV80NgTvQEZoMNcxU(ViewModifier viewModifier) {
            viewModifier.lambda$handleMessage$0();
        }

        public ViewModifier() {
            DefaultClusterRenderer.this = r1;
            this.mViewModificationInProgress = false;
            this.mNextClusters = null;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            DefaultClusterRenderer<T>.RenderTask renderTask;
            if (message.what == 1) {
                this.mViewModificationInProgress = false;
                if (this.mNextClusters == null) {
                    return;
                }
                sendEmptyMessage(0);
                return;
            }
            removeMessages(0);
            if (this.mViewModificationInProgress || this.mNextClusters == null) {
                return;
            }
            synchronized (this) {
                renderTask = this.mNextClusters;
                this.mNextClusters = null;
                this.mViewModificationInProgress = true;
            }
            renderTask.setCallback(new Runnable() { // from class: com.miui.gallery.map.cluster.DefaultClusterRenderer$ViewModifier$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DefaultClusterRenderer.ViewModifier.m1085$r8$lambda$pLxyOXEvinV80NgTvQEZoMNcxU(DefaultClusterRenderer.ViewModifier.this);
                }
            });
            renderTask.setMapZoom(this.curZoom);
            DefaultClusterRenderer.this.mExecutor.execute(renderTask);
        }

        public /* synthetic */ void lambda$handleMessage$0() {
            sendEmptyMessage(1);
        }

        public void queue(Set<Cluster<T>> set, float f) {
            synchronized (this) {
                this.mNextClusters = new RenderTask(set);
                this.curZoom = f;
            }
            sendEmptyMessage(0);
        }
    }

    /* loaded from: classes2.dex */
    public class RenderTask implements Runnable {
        public final Set<Cluster<T>> clusters;
        public Runnable mCallback;
        public float mMapZoom;

        public RenderTask(Set<Cluster<T>> set) {
            DefaultClusterRenderer.this = r1;
            this.clusters = set;
        }

        public void setCallback(Runnable runnable) {
            this.mCallback = runnable;
        }

        public void setMapZoom(float f) {
            this.mMapZoom = f;
        }

        @Override // java.lang.Runnable
        @SuppressLint({"NewApi"})
        public void run() {
            if (Objects.equals(this.clusters, DefaultClusterRenderer.this.mClusters)) {
                this.mCallback.run();
                return;
            }
            MarkerModifier markerModifier = new MarkerModifier();
            float f = this.mMapZoom;
            Set<Cluster<T>> set = DefaultClusterRenderer.this.mClusters;
            if (DefaultClusterRenderer.this.getMapStatus() == null) {
                return;
            }
            LatLngBounds bound = DefaultClusterRenderer.this.getMapStatus().getBound();
            Set newSetFromMap = Collections.newSetFromMap(new ConcurrentHashMap());
            for (Cluster<T> cluster : this.clusters) {
                if (f != DefaultClusterRenderer.this.mMapContainer.getZoomLevel()) {
                    break;
                }
                markerModifier.add(bound.contains(cluster.getPosition()), new CreateMarkerTask(cluster, newSetFromMap, null));
            }
            markerModifier.waitUntilFree();
            set.removeAll(newSetFromMap);
            Set newSetFromMap2 = Collections.newSetFromMap(new ConcurrentHashMap());
            for (Cluster<T> cluster2 : set) {
                if (!bound.contains(cluster2.getPosition()) && f == DefaultClusterRenderer.this.mMapContainer.getZoomLevel()) {
                    newSetFromMap2.add(cluster2);
                } else {
                    markerModifier.remove(bound.contains(cluster2.getPosition()), cluster2);
                }
            }
            markerModifier.waitUntilFree();
            newSetFromMap.addAll(newSetFromMap2);
            DefaultClusterRenderer.this.mClusters = newSetFromMap;
            this.mMapZoom = DefaultClusterRenderer.this.mMapContainer.getZoomLevel();
            this.mCallback.run();
        }
    }

    public void onBeforeClusterRendered(final Cluster<T> cluster, final IMarkerOptions iMarkerOptions) {
        final MapItem mapItem = (MapItem) cluster.mo1081getItems().iterator().next();
        ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.map.cluster.DefaultClusterRenderer$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public final Object mo1807run(ThreadPool.JobContext jobContext) {
                return DefaultClusterRenderer.m1084$r8$lambda$iwxlkuhbGptiMGSYkuvj2dsRdI(DefaultClusterRenderer.this, mapItem, jobContext);
            }
        }, new FutureListener() { // from class: com.miui.gallery.map.cluster.DefaultClusterRenderer$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.concurrent.FutureListener
            public final void onFutureDone(Future future) {
                DefaultClusterRenderer.m1082$r8$lambda$JBVUE7LArseJkaG_Xe9Ru0KoUE(DefaultClusterRenderer.this, cluster, mapItem, iMarkerOptions, future);
            }
        });
    }

    public /* synthetic */ Bitmap lambda$onBeforeClusterRendered$3(MapItem mapItem, ThreadPool.JobContext jobContext) {
        return MapBitmapTool.getSourceBitmap(mapItem.getPath(), this.mContext.getResources().getDimensionPixelSize(R.dimen.custom_marker_image_width));
    }

    public /* synthetic */ void lambda$onBeforeClusterRendered$4(Cluster cluster, MapItem mapItem, IMarkerOptions iMarkerOptions, Future future) {
        if (!future.isDone() || future.get() == null || this.mMapContainer == null) {
            return;
        }
        IconGenerator iconGenerator = new IconGenerator(this.mContext.getApplicationContext());
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.map_marker_item, (ViewGroup) null);
        iconGenerator.setContentView(inflate);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.custom_marker_image);
        TextView textView = (TextView) inflate.findViewById(R.id.custom_marker_num);
        if (cluster.getSize() > 1) {
            textView.setText(formatSizeText(cluster.getSize()));
        } else {
            textView.setVisibility(8);
        }
        imageView.setImageBitmap((Bitmap) future.get());
        setIconByCluster(mapItem.getPath(), cluster, iMarkerOptions.icon(BitmapDescriptorWrapperFactory.fromBitmap(iconGenerator.makeIcon())));
    }

    public final String formatSizeText(int i) {
        return NumberFormat.getIntegerInstance().format(i);
    }

    public void setIconByCluster(String str, Cluster<T> cluster, IMarkerOptions iMarkerOptions) {
        this.markRemoveAndAddLock.lock();
        try {
            Integer num = this.onReadyAddCluster.get(cluster);
            if (num != null && cluster.getSize() == num.intValue()) {
                IMarker iMarker = this.mClusterToMarker.get(cluster);
                if (iMarker != null) {
                    iMarker.setIcon(iMarkerOptions.getIcon());
                } else {
                    iMarker = this.mClusterManager.getClusterMarkerCollection().addMarker(iMarkerOptions);
                }
                this.mMarkerToCluster.put(iMarker, cluster);
                this.mClusterToMarker.put(cluster, iMarker);
            }
        } finally {
            this.markRemoveAndAddLock.unlock();
        }
    }

    /* loaded from: classes2.dex */
    public class CreateMarkerTask {
        public final Cluster<T> cluster;
        public final Set<Cluster<T>> newClusters;

        public CreateMarkerTask(Cluster<T> cluster, Set<Cluster<T>> set, MapLatLng mapLatLng) {
            DefaultClusterRenderer.this = r1;
            this.cluster = cluster;
            this.newClusters = set;
        }

        public final void perform() {
            Integer num;
            DefaultClusterRenderer.this.markRemoveAndAddLock.lock();
            try {
                IMarker iMarker = (IMarker) DefaultClusterRenderer.this.mClusterToMarker.get(this.cluster);
                if ((iMarker == null || (DefaultClusterRenderer.this.mMarkerToCluster.get(iMarker) != null && ((Cluster) DefaultClusterRenderer.this.mMarkerToCluster.get(iMarker)).getSize() != this.cluster.getSize())) && ((num = (Integer) DefaultClusterRenderer.this.onReadyAddCluster.get(this.cluster)) == null || num.intValue() != this.cluster.getSize())) {
                    Map map = DefaultClusterRenderer.this.onReadyAddCluster;
                    Cluster<T> cluster = this.cluster;
                    map.put(cluster, Integer.valueOf(cluster.getSize()));
                    DefaultClusterRenderer.this.onBeforeClusterRendered(this.cluster, new MarkerOptionsWrapper().position(this.cluster.getPosition()));
                }
                DefaultClusterRenderer.this.markRemoveAndAddLock.unlock();
                this.newClusters.add(this.cluster);
            } catch (Throwable th) {
                DefaultClusterRenderer.this.markRemoveAndAddLock.unlock();
                throw th;
            }
        }
    }

    @SuppressLint({"HandlerLeak"})
    /* loaded from: classes2.dex */
    public class MarkerModifier extends Handler implements MessageQueue.IdleHandler {
        public final Condition busyCondition;
        public final Lock lock;
        public final Queue<Cluster<T>> mClustersToRemove;
        public final Queue<DefaultClusterRenderer<T>.CreateMarkerTask> mCreateMarkerTasks;
        public boolean mListenerAdded;
        public final Queue<DefaultClusterRenderer<T>.CreateMarkerTask> mOnScreenCreateMarkerTasks;
        public final Queue<Cluster<T>> mOnScreenRemoveClusterTasks;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MarkerModifier() {
            super(Looper.getMainLooper());
            DefaultClusterRenderer.this = r1;
            ReentrantLock reentrantLock = new ReentrantLock();
            this.lock = reentrantLock;
            this.busyCondition = reentrantLock.newCondition();
            this.mCreateMarkerTasks = new LinkedList();
            this.mOnScreenCreateMarkerTasks = new LinkedList();
            this.mClustersToRemove = new LinkedList();
            this.mOnScreenRemoveClusterTasks = new LinkedList();
        }

        public void add(boolean z, DefaultClusterRenderer<T>.CreateMarkerTask createMarkerTask) {
            this.lock.lock();
            try {
                sendEmptyMessage(0);
                if (z) {
                    this.mOnScreenCreateMarkerTasks.add(createMarkerTask);
                }
            } finally {
                this.lock.unlock();
            }
        }

        public void remove(boolean z, Cluster<T> cluster) {
            this.lock.lock();
            try {
                sendEmptyMessage(0);
                if (z) {
                    this.mOnScreenRemoveClusterTasks.add(cluster);
                } else {
                    this.mClustersToRemove.add(cluster);
                }
            } finally {
                this.lock.unlock();
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (!this.mListenerAdded) {
                Looper.myQueue().addIdleHandler(this);
                this.mListenerAdded = true;
            }
            removeMessages(0);
            this.lock.lock();
            for (int i = 0; i < 10; i++) {
                try {
                    performNextTask();
                } finally {
                    this.lock.unlock();
                }
            }
            if (!isBusy()) {
                this.mListenerAdded = false;
                Looper.myQueue().removeIdleHandler(this);
                this.busyCondition.signalAll();
            } else {
                sendEmptyMessageDelayed(0, 10L);
            }
        }

        public final void performNextTask() {
            if (!this.mOnScreenCreateMarkerTasks.isEmpty()) {
                this.mOnScreenCreateMarkerTasks.poll().perform();
            } else if (!this.mOnScreenRemoveClusterTasks.isEmpty()) {
                removeCluster(this.mOnScreenRemoveClusterTasks.poll());
            } else if (!this.mCreateMarkerTasks.isEmpty()) {
                this.mCreateMarkerTasks.poll().perform();
            } else if (this.mClustersToRemove.isEmpty()) {
            } else {
                removeCluster(this.mClustersToRemove.poll());
            }
        }

        public final void removeCluster(Cluster<T> cluster) {
            DefaultClusterRenderer.this.markRemoveAndAddLock.lock();
            try {
                DefaultClusterRenderer.this.onReadyAddCluster.remove(cluster);
                IMarker iMarker = (IMarker) DefaultClusterRenderer.this.mClusterToMarker.get(cluster);
                if (iMarker != null) {
                    DefaultClusterRenderer.this.mClusterToMarker.remove(cluster);
                    DefaultClusterRenderer.this.mMarkerCache.remove(iMarker);
                    DefaultClusterRenderer.this.mMarkerToCluster.remove(iMarker);
                    DefaultClusterRenderer.this.mClusterManager.getMarkerManager().remove(iMarker);
                } else {
                    DefaultClusterRenderer.this.onRemoveCluster(cluster);
                }
            } finally {
                DefaultClusterRenderer.this.markRemoveAndAddLock.unlock();
            }
        }

        public boolean isBusy() {
            boolean z;
            this.lock.lock();
            try {
                if (this.mCreateMarkerTasks.isEmpty() && this.mOnScreenCreateMarkerTasks.isEmpty() && this.mOnScreenRemoveClusterTasks.isEmpty()) {
                    if (this.mClustersToRemove.isEmpty()) {
                        z = false;
                        return z;
                    }
                }
                z = true;
                return z;
            } finally {
                this.lock.unlock();
            }
        }

        public void waitUntilFree() {
            while (isBusy()) {
                sendEmptyMessage(0);
                this.lock.lock();
                try {
                    try {
                        if (isBusy()) {
                            this.busyCondition.await();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    this.lock.unlock();
                }
            }
        }

        @Override // android.os.MessageQueue.IdleHandler
        public boolean queueIdle() {
            sendEmptyMessage(0);
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public static class MarkerCache<T> {
        public final BiMap<T, IMarker> mCache;

        public MarkerCache() {
            this.mCache = HashBiMap.create();
        }

        public T get(IMarker iMarker) {
            return this.mCache.inverse().get(iMarker);
        }

        public void remove(IMarker iMarker) {
            this.mCache.inverse().remove(iMarker);
        }
    }
}
