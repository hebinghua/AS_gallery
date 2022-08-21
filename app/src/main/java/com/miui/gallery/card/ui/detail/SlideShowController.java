package com.miui.gallery.card.ui.detail;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.card.model.BaseMedia;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.util.GlideLoadingUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.SlideShowView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class SlideShowController {
    public final int mDuration;
    public Future<ShowItem> mGetFuture;
    public Future<Void> mLoadFuture;
    public int mLoadIndex;
    public final List<BaseMedia> mMediaInfos;
    public int mShowIndex;
    public SlideShowNextListener mSlideShowNextListener;
    public final SlideShowView mSlideShowView;
    public final Object mLock = new Object();
    public final BlockingQueue<ShowItem> mCacheQueue = new LinkedBlockingQueue(2);
    public final Handler mHandler = new Handler(new Handler.Callback() { // from class: com.miui.gallery.card.ui.detail.SlideShowController.1
        {
            SlideShowController.this = this;
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                Object obj = message.obj;
                ShowItem showItem = obj == null ? null : (ShowItem) obj;
                if (showItem != null) {
                    if (SlideShowController.this.mSlideShowNextListener != null) {
                        SlideShowController.this.mSlideShowNextListener.onNext(showItem.getUri());
                    }
                    SlideShowController.this.mSlideShowView.checkAndNext(showItem.getBitmap(), 0, showItem.getIndex());
                    if (!SlideShowController.this.mHandler.hasMessages(2)) {
                        SlideShowController.this.mHandler.sendEmptyMessageDelayed(2, SlideShowController.this.mDuration);
                    } else {
                        DefaultLogger.w("SlideShowController", "MSG_WHAT_LOAD already posted");
                    }
                }
            } else if (i == 2) {
                SlideShowController.this.loadNextBitmap();
            }
            return true;
        }
    });

    /* loaded from: classes.dex */
    public interface SlideShowNextListener {
        void onNext(String str);
    }

    public static /* synthetic */ void $r8$lambda$JS7sYG4WALAJUpKkhGnQBlIssJs(SlideShowController slideShowController, Future future) {
        slideShowController.lambda$loadNextBitmap$0(future);
    }

    public void setSlideShowNextListener(SlideShowNextListener slideShowNextListener) {
        this.mSlideShowNextListener = slideShowNextListener;
    }

    public SlideShowController(SlideShowView slideShowView, int i) {
        this.mSlideShowView = slideShowView;
        slideShowView.setScaleOnlyMode(true);
        this.mMediaInfos = new ArrayList();
        this.mDuration = i;
    }

    public void updateMedias(List<? extends BaseMedia> list, boolean z) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        boolean z2 = true;
        if (!this.mMediaInfos.isEmpty()) {
            if (this.mMediaInfos.size() == list.size() && z) {
                return;
            }
            pause();
            synchronized (this.mLock) {
                this.mMediaInfos.clear();
                this.mMediaInfos.addAll(list);
                this.mLoadIndex = this.mLoadIndex < this.mMediaInfos.size() ? this.mLoadIndex : 0;
            }
            SlideShowView slideShowView = this.mSlideShowView;
            if (list.size() <= 1) {
                z2 = false;
            }
            slideShowView.setSlideAnimEnable(z2);
            this.mCacheQueue.clear();
            resume();
            return;
        }
        synchronized (this.mLock) {
            this.mMediaInfos.addAll(list);
            this.mLoadIndex = this.mLoadIndex < this.mMediaInfos.size() ? this.mLoadIndex : 0;
        }
        cancelLoad();
        SlideShowView slideShowView2 = this.mSlideShowView;
        if (list.size() <= 1) {
            z2 = false;
        }
        slideShowView2.setSlideAnimEnable(z2);
        startLoad();
    }

    public void resume() {
        startLoad();
        loadNextBitmap();
    }

    public void pause() {
        cancelLoad();
        cancelGet();
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(1);
    }

    public void stop() {
        pause();
        this.mSlideShowView.stop();
    }

    public void destroy() {
        pause();
        this.mSlideShowView.release();
    }

    public final void loadNextBitmap() {
        nextBitmap(new FutureListener() { // from class: com.miui.gallery.card.ui.detail.SlideShowController$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.concurrent.FutureListener
            public final void onFutureDone(Future future) {
                SlideShowController.$r8$lambda$JS7sYG4WALAJUpKkhGnQBlIssJs(SlideShowController.this, future);
            }
        });
    }

    public /* synthetic */ void lambda$loadNextBitmap$0(Future future) {
        ShowItem showItem;
        if (future.isCancelled() || (showItem = (ShowItem) future.get()) == null) {
            return;
        }
        this.mHandler.obtainMessage(1, showItem).sendToTarget();
    }

    public int getShowIndex() {
        return this.mShowIndex;
    }

    public void setLoadIndex(int i) {
        this.mLoadIndex = i;
    }

    public int getCount() {
        return this.mMediaInfos.size();
    }

    public void nextBitmap(FutureListener<ShowItem> futureListener) {
        cancelGet();
        this.mGetFuture = ThreadManager.getMiscPool().submit(new GetJob(), futureListener);
    }

    public final void startLoad() {
        cancelLoad();
        this.mLoadFuture = ThreadManager.getMiscPool().submit(new LoadJob());
    }

    public final void cancelGet() {
        Future<ShowItem> future = this.mGetFuture;
        if (future != null) {
            future.cancel();
            this.mGetFuture = null;
        }
    }

    public final void cancelLoad() {
        Future<Void> future = this.mLoadFuture;
        if (future != null) {
            future.cancel();
            this.mLoadFuture = null;
        }
    }

    /* loaded from: classes.dex */
    public class GetJob implements ThreadPool.Job<ShowItem> {
        public GetJob() {
            SlideShowController.this = r1;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public ShowItem mo1807run(ThreadPool.JobContext jobContext) {
            ShowItem showItem = null;
            while (!jobContext.isCancelled() && showItem == null) {
                try {
                    showItem = (ShowItem) SlideShowController.this.mCacheQueue.poll(1000L, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    DefaultLogger.i("SlideShowController", "poll interrupted, curSize %d", Integer.valueOf(SlideShowController.this.mCacheQueue.size()));
                    e.printStackTrace();
                }
                if (showItem == null) {
                    synchronized (SlideShowController.this.mLock) {
                        if (SlideShowController.this.mLoadIndex >= SlideShowController.this.getCount()) {
                            return null;
                        }
                    }
                }
            }
            if (showItem != null) {
                SlideShowController.this.mShowIndex = showItem.getIndex();
            }
            return showItem;
        }
    }

    /* loaded from: classes.dex */
    public class LoadJob implements ThreadPool.Job<Void> {
        public final RequestOptions mRequestOptions = GlideOptions.bigPhotoOf().mo970override(Math.min(BaseConfig$ScreenConfig.getScreenWidth(), BaseConfig$ScreenConfig.getScreenHeight())).mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo974priority(Priority.HIGH);

        public LoadJob() {
            SlideShowController.this = r3;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            LoadItem loadItem;
            RequestManager with = Glide.with(GalleryApp.sGetAndroidContext());
            while (!jobContext.isCancelled() && (loadItem = getLoadItem(jobContext)) != null) {
                Bitmap blockingLoad = GlideLoadingUtils.blockingLoad(with, GalleryModel.of(loadItem.mUri), this.mRequestOptions);
                if (blockingLoad != null && !blockingLoad.isRecycled()) {
                    ShowItem showItem = new ShowItem(blockingLoad, loadItem.mIndex, loadItem.mUri);
                    boolean z = false;
                    while (!jobContext.isCancelled() && !z) {
                        try {
                            z = SlideShowController.this.mCacheQueue.offer(showItem, 1000L, TimeUnit.MILLISECONDS);
                            if (jobContext.isCancelled() && z) {
                                DefaultLogger.d("SlideShowController", "Load cancel, remove from mCacheQueue ");
                                SlideShowController.this.mCacheQueue.remove(showItem);
                            }
                        } catch (InterruptedException e) {
                            DefaultLogger.i("SlideShowController", "offer interrupted, curSize %d", Integer.valueOf(SlideShowController.this.mCacheQueue.size()));
                            e.printStackTrace();
                        }
                    }
                    if (!z) {
                        DefaultLogger.i("SlideShowController", "not offered, curSize %d", Integer.valueOf(SlideShowController.this.mCacheQueue.size()));
                        SlideShowController.this.decreaseLoadIndex();
                    }
                }
            }
            DefaultLogger.v("SlideShowController", "loadJob cancelled, curSize %d", Integer.valueOf(SlideShowController.this.mCacheQueue.size()));
            return null;
        }

        public final LoadItem getLoadItem(ThreadPool.JobContext jobContext) {
            if (jobContext.isCancelled()) {
                return null;
            }
            synchronized (SlideShowController.this.mLock) {
                if (SlideShowController.this.mLoadIndex < 0) {
                    SlideShowController.this.mLoadIndex = 0;
                }
                while (!jobContext.isCancelled() && SlideShowController.this.mLoadIndex >= 0 && SlideShowController.this.mLoadIndex < SlideShowController.this.mMediaInfos.size()) {
                    BaseMedia baseMedia = (BaseMedia) SlideShowController.this.mMediaInfos.get(SlideShowController.this.mLoadIndex);
                    if (baseMedia != null) {
                        String uri = baseMedia.getUri();
                        if (!TextUtils.isEmpty(uri) && !jobContext.isCancelled()) {
                            if (Scheme.ofUri(uri) == Scheme.UNKNOWN) {
                                uri = Scheme.FILE.wrap(uri);
                            }
                            LoadItem loadItem = new LoadItem(uri, SlideShowController.this.mLoadIndex);
                            SlideShowController.this.increaseLoadIndex();
                            return loadItem;
                        }
                    }
                    SlideShowController.this.increaseLoadIndex();
                }
                return null;
            }
        }
    }

    public final void increaseLoadIndex() {
        synchronized (this.mLock) {
            int size = this.mMediaInfos.size();
            if (size > 0) {
                int i = this.mLoadIndex + 1;
                this.mLoadIndex = i;
                if (i == size) {
                    this.mLoadIndex = i % size;
                }
            }
        }
    }

    public final void decreaseLoadIndex() {
        synchronized (this.mLock) {
            int size = this.mMediaInfos.size();
            if (size > 0) {
                int i = this.mLoadIndex - 1;
                this.mLoadIndex = i;
                if (i <= 0) {
                    this.mLoadIndex = size - 1;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ShowItem {
        public final Bitmap mBit;
        public final int mIndex;
        public final String mUri;

        public ShowItem(Bitmap bitmap, int i, String str) {
            this.mBit = bitmap;
            this.mIndex = i;
            this.mUri = str;
        }

        public Bitmap getBitmap() {
            return this.mBit;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public String getUri() {
            return this.mUri;
        }
    }

    /* loaded from: classes.dex */
    public static class LoadItem {
        public final int mIndex;
        public final String mUri;

        public LoadItem(String str, int i) {
            this.mUri = str;
            this.mIndex = i;
        }
    }
}
