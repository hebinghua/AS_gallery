package com.miui.gallery.search.core.source.local;

import android.database.ContentObserver;
import android.os.Handler;
import com.android.internal.CompatHandler;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class ContentCache<T> {
    public final int mCacheHoldTime;
    public Runnable mCacheRecycleRunnable;
    public WeakReference<T> mCacheRef;
    public ContentObserver mContentObserver;
    public final ContentCacheProvider<T> mContentProvider;
    public final Object mLock;

    public ContentCache(ContentCacheProvider<T> contentCacheProvider) {
        this(contentCacheProvider, 300000);
    }

    public ContentCache(ContentCacheProvider<T> contentCacheProvider, int i) {
        this.mLock = new Object();
        this.mCacheRef = null;
        this.mContentObserver = null;
        this.mCacheRecycleRunnable = new Runnable() { // from class: com.miui.gallery.search.core.source.local.ContentCache.1
            @Override // java.lang.Runnable
            public void run() {
                ContentCache.this.releaseCache();
            }
        };
        if (contentCacheProvider == null) {
            throw new IllegalArgumentException("Cannot accept null content cache provider");
        }
        this.mContentProvider = contentCacheProvider;
        this.mCacheHoldTime = i;
    }

    public T getCache() {
        WeakReference<T> weakReference = this.mCacheRef;
        T t = weakReference == null ? null : weakReference.get();
        if (t == null) {
            try {
                try {
                    t = this.mContentProvider.mo1330loadContent();
                    if (t != null) {
                        this.mCacheRef = new WeakReference<>(t);
                        synchronized (this.mLock) {
                            if (this.mContentObserver == null) {
                                this.mContentObserver = new MyContentObserver(ThreadManager.getWorkHandler());
                                ContentCacheProvider<T> contentCacheProvider = this.mContentProvider;
                                contentCacheProvider.registerContentObserver(contentCacheProvider.getContentUri(), true, this.mContentObserver);
                            }
                        }
                    }
                } catch (Exception e) {
                    SearchLog.e("ContentCache", e);
                }
            } finally {
                hangOn();
            }
        }
        return t;
    }

    public void releaseCache() {
        CompatHandler workHandler = ThreadManager.getWorkHandler();
        synchronized (this.mLock) {
            ContentObserver contentObserver = this.mContentObserver;
            if (contentObserver != null) {
                this.mContentProvider.unregisterContentObserver(contentObserver);
                this.mContentObserver = null;
            }
        }
        WeakReference<T> weakReference = this.mCacheRef;
        if (weakReference != null) {
            weakReference.clear();
            this.mCacheRef = null;
        }
        workHandler.removeCallbacks(this.mCacheRecycleRunnable);
    }

    public void finalize() throws Throwable {
        super.finalize();
        releaseCache();
    }

    public final void hangOn() {
        CompatHandler workHandler = ThreadManager.getWorkHandler();
        workHandler.removeCallbacks(this.mCacheRecycleRunnable);
        workHandler.postDelayed(this.mCacheRecycleRunnable, this.mCacheHoldTime);
    }

    /* loaded from: classes2.dex */
    public class MyContentObserver extends ContentObserver {
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            Object mo1330loadContent;
            if (ContentCache.this.mContentObserver == null || (mo1330loadContent = ContentCache.this.mContentProvider.mo1330loadContent()) == null || ContentCache.this.mContentObserver == null) {
                return;
            }
            if (ContentCache.this.mCacheRef != null) {
                ContentCache.this.mCacheRef.clear();
            }
            ContentCache.this.mCacheRef = new WeakReference(mo1330loadContent);
        }
    }
}
