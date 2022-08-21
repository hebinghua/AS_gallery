package com.miui.gallery.ui;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.asynclayoutinflater.view.GalleryAsyncLayoutInflater;
import androidx.asynclayoutinflater.view.OnInflateFinishedListener;
import com.google.common.base.Stopwatch;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public abstract class AsyncViewPrefetcher implements ViewProvider, OnInflateFinishedListener {
    public int mCurrentRemaining;
    public PrefetchSpec mCurrentSpec;
    public volatile GalleryAsyncLayoutInflater mInflater;
    public LayoutInflater.Factory2 mLayoutFactory2;
    public ViewGroup mParent;
    public List<PrefetchSpec> mPrefetchSpecs;
    public IPrefetchSpecProvider mPrefetchspecProvider;
    public volatile Boolean mStarted;
    public ViewPool mPool = new ViewPool();
    public Stopwatch mStopwatch = Stopwatch.createUnstarted();

    /* loaded from: classes2.dex */
    public interface IPrefetchSpecProvider {
        List<PrefetchSpec> provide();
    }

    public AsyncViewPrefetcher(Context context, ViewGroup viewGroup, List<PrefetchSpec> list) {
        this.mPrefetchSpecs = new ArrayList(list);
        this.mParent = viewGroup;
        if (viewGroup == null) {
            this.mParent = new FrameLayout(context);
        }
    }

    public AsyncViewPrefetcher(Context context, ViewGroup viewGroup, IPrefetchSpecProvider iPrefetchSpecProvider) {
        this.mPrefetchspecProvider = iPrefetchSpecProvider;
        this.mParent = viewGroup;
        if (viewGroup == null) {
            this.mParent = new FrameLayout(context);
        }
    }

    public void setLayoutFactory(LayoutInflater.Factory2 factory2) {
        this.mLayoutFactory2 = factory2;
    }

    public final void ensureInflater() {
        if (this.mInflater == null) {
            synchronized (this) {
                if (this.mInflater == null) {
                    this.mInflater = new GalleryAsyncLayoutInflater(this.mParent.getContext(), this.mLayoutFactory2);
                }
            }
        }
    }

    public void prefetch() {
        if (this.mStarted != null) {
            throw new IllegalStateException("has started");
        }
        DefaultLogger.d("AsyncViewPrefetcher", "starting prefetch with spec: %s", this.mPrefetchSpecs);
        ensureInflater();
        this.mStarted = Boolean.TRUE;
        this.mStopwatch.start();
        if (this.mPrefetchSpecs == null && this.mPrefetchspecProvider != null) {
            this.mPrefetchSpecs = new ArrayList(this.mPrefetchspecProvider.provide());
        }
        if (this.mPrefetchSpecs.size() <= 0) {
            return;
        }
        PrefetchSpec remove = this.mPrefetchSpecs.remove(0);
        this.mCurrentSpec = remove;
        this.mCurrentRemaining = remove.mCount;
        doFetch();
    }

    public void release() {
        this.mInflater = null;
        this.mParent = null;
        this.mStarted = Boolean.FALSE;
        this.mPool.clear();
    }

    public final void doFetch() {
        int viewResId = getViewResId(this.mCurrentSpec.mViewType);
        if (viewResId <= 0) {
            throw new IllegalArgumentException("invalid layout res id");
        }
        this.mInflater.inflate(viewResId, this.mParent, this);
    }

    @Override // androidx.asynclayoutinflater.view.OnInflateFinishedListener
    public final void onInflateFinished(View view, int i, ViewGroup viewGroup) {
        this.mCurrentRemaining--;
        DefaultLogger.d("AsyncViewPrefetcher", "onInflateFinished, viewType: %d, remaining: %d, elapsed: %d ms", Integer.valueOf(this.mCurrentSpec.mViewType), Integer.valueOf(this.mCurrentRemaining), Long.valueOf(this.mStopwatch.elapsed(TimeUnit.MILLISECONDS)));
        this.mPool.push(this.mCurrentSpec.mViewType, view);
        while (true) {
            if (this.mCurrentRemaining <= 0) {
                if (this.mPrefetchSpecs.size() > 0) {
                    PrefetchSpec remove = this.mPrefetchSpecs.remove(0);
                    this.mCurrentSpec = remove;
                    this.mCurrentRemaining = remove.mCount;
                } else {
                    this.mStarted = Boolean.FALSE;
                    break;
                }
            } else {
                break;
            }
        }
        if (this.mStarted.booleanValue()) {
            doFetch();
        } else {
            DefaultLogger.d("AsyncViewPrefetcher", "prefetch done, total costs: %s", this.mStopwatch.stop().toString());
        }
    }

    @Override // com.miui.gallery.ui.ViewProvider
    public View getViewByType(int i) {
        if (this.mStarted == null) {
            DefaultLogger.w("AsyncViewPrefetcher", "Can't access ViewProvider before prefetch start");
            return null;
        }
        boolean booleanValue = this.mStarted.booleanValue();
        View pop = this.mPool.pop(i);
        if (pop != null && booleanValue) {
            DefaultLogger.d("AsyncViewPrefetcher", "prefetch view %s, time %d, size %d", pop, Long.valueOf(this.mStopwatch.elapsed(TimeUnit.MILLISECONDS)), Integer.valueOf(this.mPool.size()));
        }
        return pop;
    }

    /* loaded from: classes2.dex */
    public static class ViewPool {
        public SparseArray<ScrapData> mCache;

        public ViewPool() {
            this.mCache = new SparseArray<>();
        }

        public synchronized void push(int i, View view) {
            ScrapData scrapData = this.mCache.get(i);
            if (scrapData == null) {
                scrapData = new ScrapData();
                this.mCache.put(i, scrapData);
            }
            scrapData.push(view);
        }

        public synchronized View pop(int i) {
            ScrapData scrapData = this.mCache.get(i);
            if (scrapData != null) {
                return scrapData.pop();
            }
            return null;
        }

        public synchronized int size() {
            int i;
            i = 0;
            for (int i2 = 0; i2 < this.mCache.size(); i2++) {
                i += this.mCache.valueAt(i2).size();
            }
            return i;
        }

        public synchronized void clear() {
            this.mCache.clear();
        }

        /* loaded from: classes2.dex */
        public static class ScrapData {
            public final LinkedList<View> mHeap;

            public ScrapData() {
                this.mHeap = new LinkedList<>();
            }

            public View pop() {
                if (this.mHeap.isEmpty()) {
                    return null;
                }
                return this.mHeap.pop();
            }

            public void push(View view) {
                this.mHeap.add(view);
            }

            public int size() {
                return this.mHeap.size();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class PrefetchSpec {
        public int mCount;
        public int mViewType;

        public PrefetchSpec(int i, int i2) {
            this.mViewType = i;
            this.mCount = i2;
        }

        public String toString() {
            return "PrefetchSpec{mViewType=" + this.mViewType + ", mCount=" + this.mCount + '}';
        }
    }
}
