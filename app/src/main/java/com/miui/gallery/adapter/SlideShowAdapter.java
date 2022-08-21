package com.miui.gallery.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.util.GlideLoadingUtils;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class SlideShowAdapter {
    public ImageLoadParams mCacheItem;
    public BaseDataSet mDataSet;
    public Future mGetFuture;
    public final boolean mIsLoop;
    public Future mLoadFuture;
    public int mLoadIndex;
    public final Object mLock = new Object();
    public final Object mLoadJobLock = new Object();
    public BlockingQueue<ShowItem> mCacheQueue = new LinkedBlockingQueue(3);
    public ShowItem mCurrentShowItem = null;

    public static /* synthetic */ int access$506(SlideShowAdapter slideShowAdapter) {
        int i = slideShowAdapter.mLoadIndex - 1;
        slideShowAdapter.mLoadIndex = i;
        return i;
    }

    public SlideShowAdapter(ImageLoadParams imageLoadParams, int i, boolean z) {
        this.mCacheItem = imageLoadParams;
        this.mLoadIndex = i;
        this.mIsLoop = z;
    }

    public void changeDataSet(BaseDataSet baseDataSet) {
        synchronized (this.mLock) {
            this.mDataSet = baseDataSet;
        }
    }

    public BaseDataItem getBaseDataItem(int i) {
        BaseDataSet baseDataSet = this.mDataSet;
        if (baseDataSet == null || i < 0 || i >= baseDataSet.getCount()) {
            return null;
        }
        return this.mDataSet.getItem(null, i);
    }

    public ImageLoadParams getCacheItem() {
        return this.mCacheItem;
    }

    public ShowItem getCurrentShowItem() {
        return this.mCurrentShowItem;
    }

    public int getCount() {
        BaseDataSet baseDataSet = this.mDataSet;
        if (baseDataSet != null) {
            return baseDataSet.getCount();
        }
        return 1;
    }

    public void nextBitmap(final FutureListener<ShowItem> futureListener) {
        cancelGet();
        this.mGetFuture = ThreadManager.getMiscPool().submit(new GetJob(), new FutureListener<ShowItem>() { // from class: com.miui.gallery.adapter.SlideShowAdapter.1
            @Override // com.miui.gallery.concurrent.FutureListener
            public void onFutureDone(Future<ShowItem> future) {
                futureListener.onFutureDone(future);
                SlideShowAdapter.this.onGetJobDone(future.get());
            }
        });
    }

    public final void startLoad() {
        cancelLoad();
        this.mLoadFuture = ThreadManager.getMiscPool().submit(new LoadJob());
    }

    public void resume() {
        startLoad();
    }

    public void pause() {
        cancelLoad();
        cancelGet();
    }

    public final void cancelGet() {
        Future future = this.mGetFuture;
        if (future != null) {
            future.cancel();
            this.mGetFuture = null;
        }
    }

    public final void cancelLoad() {
        Future future = this.mLoadFuture;
        if (future != null) {
            future.cancel();
            this.mLoadFuture = null;
        }
    }

    public final LoadItem getLoadItem() {
        synchronized (this.mLock) {
            if (this.mDataSet != null) {
                while (true) {
                    int i = this.mLoadIndex;
                    if (i < 0 || (!this.mIsLoop && i >= this.mDataSet.getCount())) {
                        break;
                    }
                    BaseDataSet baseDataSet = this.mDataSet;
                    BaseDataItem item = baseDataSet.getItem(null, this.mLoadIndex % baseDataSet.getCount());
                    if (item != null) {
                        String pathDisplayBetter = item.getPathDisplayBetter();
                        if (!TextUtils.isEmpty(pathDisplayBetter)) {
                            this.mLoadIndex++;
                            return new LoadItem(ensureFileScheme(pathDisplayBetter), this.mLoadIndex - 1, item.getSecretKey(), item.getSize());
                        }
                    }
                    this.mLoadIndex++;
                }
            } else {
                ImageLoadParams imageLoadParams = this.mCacheItem;
                if (imageLoadParams != null && imageLoadParams.match(null, this.mLoadIndex)) {
                    this.mLoadIndex++;
                    return new LoadItem(ensureFileScheme(this.mCacheItem.getPath()), this.mLoadIndex - 1, this.mCacheItem.getSecretKey(), this.mCacheItem.getFileLength());
                }
            }
            return null;
        }
    }

    public final String ensureFileScheme(String str) {
        return (TextUtils.isEmpty(str) || Scheme.ofUri(str) != Scheme.UNKNOWN) ? str : Scheme.FILE.wrap(str);
    }

    public final void onGetJobDone(ShowItem showItem) {
        this.mCurrentShowItem = showItem;
    }

    /* loaded from: classes.dex */
    public class GetJob implements ThreadPool.Job<ShowItem> {
        public GetJob() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public ShowItem mo1807run(ThreadPool.JobContext jobContext) {
            ShowItem showItem = null;
            while (!jobContext.isCancelled() && showItem == null) {
                try {
                    showItem = (ShowItem) SlideShowAdapter.this.mCacheQueue.poll(1000L, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    DefaultLogger.i("SlideShowAdapter", "poll interrupted, curSize %d", Integer.valueOf(SlideShowAdapter.this.mCacheQueue.size()));
                    e.printStackTrace();
                }
                if (showItem == null) {
                    synchronized (SlideShowAdapter.this.mLock) {
                        if (!SlideShowAdapter.this.mIsLoop && SlideShowAdapter.this.mLoadIndex >= SlideShowAdapter.this.getCount() && SlideShowAdapter.this.mDataSet != null) {
                            return null;
                        }
                    }
                }
            }
            DefaultLogger.d("SlideShowAdapter", "getJob cancelled, curSize %d", Integer.valueOf(SlideShowAdapter.this.mCacheQueue.size()));
            return showItem;
        }
    }

    /* loaded from: classes.dex */
    public class LoadJob implements ThreadPool.Job<Void> {
        public final int mTargetSize = Math.max(BaseConfig$ScreenConfig.getScreenWidth(), BaseConfig$ScreenConfig.getScreenHeight());

        public LoadJob() {
        }

        public final RequestOptions getRequestOptions(LoadItem loadItem) {
            return GlideOptions.bigPhotoOf(loadItem.mFileLength).mo970override(this.mTargetSize).secretKey(loadItem.getSecretKey()).mo974priority(Priority.HIGH);
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run  reason: collision with other method in class */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            synchronized (SlideShowAdapter.this.mLoadJobLock) {
                RequestManager with = Glide.with(GalleryApp.sGetAndroidContext());
                while (!jobContext.isCancelled()) {
                    LoadItem loadItem = SlideShowAdapter.this.getLoadItem();
                    if (loadItem == null) {
                        if (SlideShowAdapter.this.mDataSet != null && !SlideShowAdapter.this.mIsLoop && SlideShowAdapter.this.mLoadIndex > SlideShowAdapter.this.mDataSet.getCount()) {
                            break;
                        }
                    } else {
                        Bitmap blockingLoad = GlideLoadingUtils.blockingLoad(with, GalleryModel.of(loadItem.mUri), getRequestOptions(loadItem));
                        if (blockingLoad != null && !blockingLoad.isRecycled()) {
                            ShowItem showItem = new ShowItem(blockingLoad, loadItem.mIndex % (SlideShowAdapter.this.mDataSet == null ? Integer.MAX_VALUE : SlideShowAdapter.this.mDataSet.getCount()));
                            boolean z = false;
                            while (!jobContext.isCancelled() && !z) {
                                try {
                                    z = SlideShowAdapter.this.mCacheQueue.offer(showItem, 1000L, TimeUnit.MILLISECONDS);
                                } catch (InterruptedException e) {
                                    DefaultLogger.i("SlideShowAdapter", "offer interrupted, curSize %d", Integer.valueOf(SlideShowAdapter.this.mCacheQueue.size()));
                                    e.printStackTrace();
                                }
                            }
                            if (!z) {
                                DefaultLogger.i("SlideShowAdapter", "not offered, curSize %d", Integer.valueOf(SlideShowAdapter.this.mCacheQueue.size()));
                                synchronized (SlideShowAdapter.this.mLock) {
                                    SlideShowAdapter.access$506(SlideShowAdapter.this);
                                }
                            }
                        }
                    }
                }
                DefaultLogger.d("SlideShowAdapter", "loadJob cancelled, curSize %d", Integer.valueOf(SlideShowAdapter.this.mCacheQueue.size()));
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class ShowItem {
        public Bitmap mBit;
        public int mIndex;

        public ShowItem(Bitmap bitmap, int i) {
            this.mBit = bitmap;
            this.mIndex = i;
        }

        public Bitmap getBitmap() {
            return this.mBit;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public boolean isValid() {
            Bitmap bitmap = this.mBit;
            return bitmap != null && !bitmap.isRecycled();
        }
    }

    /* loaded from: classes.dex */
    public static class LoadItem {
        public long mFileLength;
        public int mIndex;
        public byte[] mSecretKey;
        public String mUri;

        public LoadItem(String str, int i, byte[] bArr, long j) {
            this.mUri = str;
            this.mIndex = i;
            this.mSecretKey = bArr;
            this.mFileLength = j;
        }

        public byte[] getSecretKey() {
            return this.mSecretKey;
        }
    }
}
