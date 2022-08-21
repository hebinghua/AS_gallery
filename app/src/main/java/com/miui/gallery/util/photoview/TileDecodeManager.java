package com.miui.gallery.util.photoview;

import android.os.Handler;
import android.os.SystemClock;
import android.util.SparseArray;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.HeifUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class TileDecodeManager {
    public FutureContainer mDecodeFutureContainer;
    public WeakReference<Handler> mDecodeHandlerRef;
    public WeakReference<TileBitProvider> mDecodeProviderRef;
    public BlockingQueue<Tile> mDecodeQueue = new LinkedBlockingQueue();
    public volatile SparseArray<Tile> mCurrentDecodingTiles = new SparseArray<>();
    public final Object mLock = new Object();

    public TileDecodeManager(Handler handler, TileBitProvider tileBitProvider) {
        this.mDecodeHandlerRef = new WeakReference<>(handler);
        this.mDecodeProviderRef = new WeakReference<>(tileBitProvider);
    }

    public final void startDecodeEngine() {
        if (this.mDecodeFutureContainer == null) {
            this.mDecodeFutureContainer = new FutureContainer();
            TileBitProvider provider = getProvider();
            ThreadPool customDecodePool = provider.customDecodePool() != null ? provider.customDecodePool() : ThreadManager.getRegionDecodePool();
            if (provider.getParallelism() > 1) {
                for (int i = 0; i < provider.getParallelism(); i++) {
                    this.mDecodeFutureContainer.add(customDecodePool.submit(new TileDecodeJob()));
                }
            } else {
                this.mDecodeFutureContainer.add(customDecodePool.submit(new TileDecodeJob()));
            }
            DefaultLogger.d("TileDecodeManager", "start decode engine");
        }
    }

    public boolean put(Tile tile) {
        if (tile != null) {
            startDecodeEngine();
            return this.mDecodeQueue.offer(tile);
        }
        return false;
    }

    public void cancel() {
        FutureContainer futureContainer = this.mDecodeFutureContainer;
        if (futureContainer != null) {
            futureContainer.cancel();
        }
        this.mDecodeFutureContainer = null;
        this.mDecodeQueue.clear();
        synchronized (this.mLock) {
            this.mCurrentDecodingTiles.clear();
        }
    }

    public void clear() {
        this.mDecodeQueue.clear();
        DefaultLogger.d("TileDecodeManager", "clear queue");
    }

    public Tile getDecodingTile(int i) {
        Tile tile;
        synchronized (this.mLock) {
            tile = this.mCurrentDecodingTiles.get(i);
        }
        return tile;
    }

    public void removeDecodingTile(int i) {
        synchronized (this.mLock) {
            this.mCurrentDecodingTiles.remove(i);
        }
    }

    public final TileBitProvider getProvider() {
        WeakReference<TileBitProvider> weakReference = this.mDecodeProviderRef;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    public final Handler getHandler() {
        WeakReference<Handler> weakReference = this.mDecodeHandlerRef;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public class TileDecodeJob implements ThreadPool.Job<Void> {
        public TileDecodeJob() {
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run  reason: collision with other method in class */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            while (!jobContext.isCancelled()) {
                try {
                    try {
                        Tile tile = (Tile) TileDecodeManager.this.mDecodeQueue.poll(2000L, TimeUnit.MILLISECONDS);
                        if (tile != null && tile.isActive()) {
                            if (jobContext.isCancelled()) {
                                TileDecodeManager.this.mDecodeQueue.clear();
                                return null;
                            }
                            SystemClock.uptimeMillis();
                            int makeTileKey = TileView.makeTileKey(tile.getTileRect());
                            synchronized (TileDecodeManager.this.mLock) {
                                TileDecodeManager.this.mCurrentDecodingTiles.put(makeTileKey, tile);
                            }
                            boolean decode = tile.decode(TileDecodeManager.this.getProvider());
                            Handler handler = TileDecodeManager.this.getHandler();
                            if (handler != null) {
                                handler.obtainMessage(decode ? 1 : 2, tile).sendToTarget();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    if (TileDecodeManager.this.getProvider() != null && BaseFileMimeUtil.isHeifMimeType(TileDecodeManager.this.getProvider().getImageMimeType())) {
                        HeifUtil.releaseMemoryHeap();
                    }
                }
            }
            DefaultLogger.d("TileDecodeManager", "tile decode job is cancelled %s", Boolean.valueOf(jobContext.isCancelled()));
            if (TileDecodeManager.this.getProvider() != null && BaseFileMimeUtil.isHeifMimeType(TileDecodeManager.this.getProvider().getImageMimeType())) {
                HeifUtil.releaseMemoryHeap();
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class FutureContainer {
        public CancelTask mCancelTask;
        public List<Future> mFutures;

        public FutureContainer() {
            this.mFutures = new ArrayList();
        }

        public void add(Future future) {
            this.mFutures.add(future);
        }

        public void cancel() {
            if (this.mCancelTask != null) {
                ThreadManager.getWorkHandler().removeCallbacks(this.mCancelTask);
            }
            this.mCancelTask = new CancelTask(this.mFutures);
            ThreadManager.getWorkHandler().post(this.mCancelTask);
            this.mFutures = new ArrayList();
        }

        /* loaded from: classes2.dex */
        public static class CancelTask implements Runnable {
            public List<Future> mPendingCancelFutures;

            public CancelTask(List<Future> list) {
                this.mPendingCancelFutures = new ArrayList(list);
            }

            @Override // java.lang.Runnable
            public void run() {
                List<Future> list = this.mPendingCancelFutures;
                if (list == null || list.size() <= 0) {
                    return;
                }
                for (Future future : this.mPendingCancelFutures) {
                    if (future != null && !future.isCancelled() && !future.isDone()) {
                        future.cancel();
                    }
                }
                this.mPendingCancelFutures.clear();
            }
        }
    }
}
