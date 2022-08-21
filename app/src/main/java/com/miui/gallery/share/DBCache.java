package com.miui.gallery.share;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.google.common.collect.Maps;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.share.AlbumShareUIManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes2.dex */
public abstract class DBCache<K, V> {
    public final ContentObserver mContentObserver;
    public final Handler mHandler;
    public final Map<K, V> mCache = Maps.newHashMap();
    public final List<OnDBCacheChangedListener<K, V>> mListeners = new CopyOnWriteArrayList();
    public boolean mDirty = true;
    public final Runnable mReloadRunnable = new Runnable() { // from class: com.miui.gallery.share.DBCache$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            DBCache.m1374$r8$lambda$sUIR6IkR7WISIvOuFOuD0Kn9AU(DBCache.this);
        }
    };

    /* loaded from: classes2.dex */
    public interface OnDBCacheChangedListener<K, V> {
        void onDBCacheChanged(DBCache<K, V> dBCache);
    }

    public static /* synthetic */ AsyncResult $r8$lambda$3T8jcwPYkeNnL2ck0Eh3b4q4CfE(DBCache dBCache, ThreadPool.JobContext jobContext) {
        return dBCache.lambda$new$0(jobContext);
    }

    /* renamed from: $r8$lambda$sUIR6IkR7-WISIvOuFOuD0Kn9AU */
    public static /* synthetic */ void m1374$r8$lambda$sUIR6IkR7WISIvOuFOuD0Kn9AU(DBCache dBCache) {
        dBCache.lambda$new$2();
    }

    public abstract Uri getUri();

    public abstract K newKey(Cursor cursor);

    /* renamed from: newValue */
    public abstract V mo1388newValue(Cursor cursor);

    public DBCache() {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.mContentObserver = new ContentObserver(handler) { // from class: com.miui.gallery.share.DBCache.1
            {
                DBCache.this = this;
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                DBCache.this.mDirty = true;
                DBCache.this.reloadIfNeeded(false);
            }
        };
    }

    public /* synthetic */ void lambda$new$2() {
        AlbumShareUIManager.submit(new ThreadPool.Job() { // from class: com.miui.gallery.share.DBCache$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public final Object mo1807run(ThreadPool.JobContext jobContext) {
                return DBCache.$r8$lambda$3T8jcwPYkeNnL2ck0Eh3b4q4CfE(DBCache.this, jobContext);
            }
        }, new AlbumShareUIManager.OnCompletionListener() { // from class: com.miui.gallery.share.DBCache$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
            public final void onCompletion(Object obj, Object obj2, int i, boolean z) {
                DBCache.this.lambda$new$1((Void) obj, (Map) obj2, i, z);
            }
        });
    }

    public /* synthetic */ AsyncResult lambda$new$0(ThreadPool.JobContext jobContext) {
        return AsyncResult.create(0, loadInBackground());
    }

    public /* synthetic */ void lambda$new$1(Void r1, Map map, int i, boolean z) {
        if (i != 0 || map == null) {
            return;
        }
        onReloadSuccess(map);
        reloadIfNeeded(false);
    }

    public Map<K, V> getCache() {
        return Collections.unmodifiableMap(this.mCache);
    }

    public V getValue(K k) {
        return this.mCache.get(k);
    }

    public void addListener(OnDBCacheChangedListener<K, V> onDBCacheChangedListener) {
        if (onDBCacheChangedListener != null) {
            this.mListeners.add(onDBCacheChangedListener);
            if (this.mListeners.size() != 1) {
                return;
            }
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            this.mDirty = true;
            sGetAndroidContext.getContentResolver().registerContentObserver(getUri(), true, this.mContentObserver);
            reloadIfNeeded(true);
        }
    }

    public void removeListener(OnDBCacheChangedListener<K, V> onDBCacheChangedListener) {
        if (onDBCacheChangedListener != null) {
            this.mListeners.remove(onDBCacheChangedListener);
            if (!this.mListeners.isEmpty()) {
                return;
            }
            GalleryApp.sGetAndroidContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
        }
    }

    public Cursor queryInBackground() {
        return GalleryApp.sGetAndroidContext().getContentResolver().query(getUri(), CloudUtils.getProjectionAll(), null, null, null);
    }

    public Map<K, V> loadInBackground() {
        HashMap newHashMap = Maps.newHashMap();
        Cursor queryInBackground = queryInBackground();
        if (queryInBackground != null) {
            while (queryInBackground.moveToNext()) {
                try {
                    K newKey = newKey(queryInBackground);
                    if (newKey != null) {
                        newHashMap.put(newKey, mo1388newValue(queryInBackground));
                    }
                } finally {
                    queryInBackground.close();
                }
            }
        }
        return newHashMap;
    }

    public final boolean reloadIfNeeded(boolean z) {
        if (!this.mListeners.isEmpty() && this.mDirty) {
            this.mDirty = false;
            this.mHandler.removeCallbacks(this.mReloadRunnable);
            if (z) {
                this.mHandler.post(this.mReloadRunnable);
                return true;
            }
            this.mHandler.postDelayed(this.mReloadRunnable, 1000L);
            return true;
        }
        return false;
    }

    public final void onReloadSuccess(Map<K, V> map) {
        this.mCache.clear();
        this.mCache.putAll(map);
        notifyShareUserChanged();
    }

    public final void notifyShareUserChanged() {
        for (OnDBCacheChangedListener<K, V> onDBCacheChangedListener : this.mListeners) {
            onDBCacheChangedListener.onDBCacheChanged(this);
        }
    }
}
