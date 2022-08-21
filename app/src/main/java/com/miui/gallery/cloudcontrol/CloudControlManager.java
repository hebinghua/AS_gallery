package com.miui.gallery.cloudcontrol;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.cloudcontrol.observers.FeatureStatusObserver;
import com.miui.gallery.cloudcontrol.observers.FeatureStrategyObserver;
import com.miui.gallery.cloudcontrol.strategies.BaseStrategy;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes.dex */
public class CloudControlManager {
    public ProfileCache mCache;
    public CountDownLatch mInitDoneSignal;
    public AsyncTask<Context, Void, Void> mInitTask;
    public volatile boolean mInitialized;
    public volatile boolean mIsInitDone;
    public volatile boolean mIsInitStart;

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final CloudControlManager INSTANCE = new CloudControlManager();
    }

    public CloudControlManager() {
        this.mInitTask = new AsyncTask<Context, Void, Void>() { // from class: com.miui.gallery.cloudcontrol.CloudControlManager.1
            @Override // android.os.AsyncTask
            public Void doInBackground(Context... contextArr) {
                try {
                    try {
                        long currentTimeMillis = System.currentTimeMillis();
                        CloudControlManager.this.mCache.load(contextArr[0]);
                        CloudControlManager.this.mInitialized = true;
                        DefaultLogger.d("CloudControlManager", "Load cache costs %d ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    } catch (Exception e) {
                        CloudControlManager.this.mInitialized = false;
                        DefaultLogger.e("CloudControlManager", "Init failed, what should not happen: %s.", e);
                    }
                    CloudControlManager.this.mIsInitDone = true;
                    CloudControlManager.this.mInitDoneSignal.countDown();
                    CloudControlManager.this.mCache.notifyAfterLoadFinished();
                    return null;
                } catch (Throwable th) {
                    CloudControlManager.this.mIsInitDone = true;
                    CloudControlManager.this.mInitDoneSignal.countDown();
                    throw th;
                }
            }
        };
        this.mInitDoneSignal = new CountDownLatch(1);
        this.mCache = new ProfileCache();
    }

    public synchronized void init(Context context) {
        if (!this.mIsInitDone && !this.mIsInitStart) {
            DefaultLogger.d("CloudControlManager", "init");
            this.mIsInitStart = true;
            this.mInitTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
        }
    }

    public final boolean initialized() {
        if (this.mIsInitDone) {
            return this.mInitialized;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            DefaultLogger.d("CloudControlManager", "not initialized, but caller is main thread, doesn't block, stack trace:\n%s", TextUtils.join("\n\t", Thread.currentThread().getStackTrace()));
            return false;
        }
        if (!this.mIsInitStart) {
            DefaultLogger.d("CloudControlManager", "start init");
            init(GalleryApp.sGetAndroidContext());
        }
        DefaultLogger.d("CloudControlManager", "not initialized, waiting lock from:\n%s", TextUtils.join("\n\t", Thread.currentThread().getStackTrace()));
        try {
            long uptimeMillis = SystemClock.uptimeMillis();
            this.mInitDoneSignal.await();
            DefaultLogger.d("CloudControlManager", "wait init done costs %d ms", Long.valueOf(SystemClock.uptimeMillis() - uptimeMillis));
        } catch (InterruptedException e) {
            DefaultLogger.e("CloudControlManager", e);
        }
        DefaultLogger.d("CloudControlManager", "initialized: %b", Boolean.valueOf(this.mInitialized));
        return this.mInitialized;
    }

    public static CloudControlManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void insertToCache(FeatureProfile featureProfile) {
        if (featureProfile == null || !initialized()) {
            return;
        }
        this.mCache.insertToCloudCache(featureProfile);
    }

    public void clearCloudCache() {
        if (!this.mIsInitStart || !initialized()) {
            return;
        }
        this.mCache.clearCloudCache();
    }

    public FeatureProfile.Status queryFeatureStatus(String str) {
        if (initialized()) {
            return this.mCache.queryStatus(str);
        }
        return FeatureProfile.Status.UNAVAILABLE;
    }

    public FeatureProfile.Status registerStatusObserver(String str, FeatureStatusObserver featureStatusObserver) {
        return this.mCache.registerStatusObserver(str, featureStatusObserver);
    }

    public void unregisterStatusObserver(FeatureStatusObserver featureStatusObserver) {
        this.mCache.unregisterStatusObserver(featureStatusObserver);
    }

    public <T extends BaseStrategy> T registerStrategyObserver(String str, Merger<T> merger, FeatureStrategyObserver<T> featureStrategyObserver) {
        Class<T> query = StrategyRegistry.getInstance().query(str);
        if (query != null) {
            try {
                return (T) this.mCache.registerStrategyObserver(str, query, merger, featureStrategyObserver);
            } catch (Exception e) {
                DefaultLogger.e("CloudControlManager", e);
                return null;
            }
        }
        DefaultLogger.e("CloudControlManager", "No strategy class was registered with %s.", str);
        return null;
    }

    public void unregisterStrategyObserver(FeatureStrategyObserver featureStrategyObserver) {
        this.mCache.unregisterStrategyObserver(featureStrategyObserver);
    }

    public <T extends BaseStrategy> T queryFeatureStrategy(String str) {
        return (T) queryFeatureStrategy(str, null);
    }

    public <T extends BaseStrategy> T queryFeatureStrategy(String str, Merger<T> merger) {
        if (initialized()) {
            Class<T> query = StrategyRegistry.getInstance().query(str);
            if (query != null) {
                try {
                    return (T) this.mCache.queryStrategy(str, query, merger);
                } catch (Exception e) {
                    DefaultLogger.e("CloudControlManager", "Generic type doesn't match.");
                    e.printStackTrace();
                    return null;
                }
            }
            DefaultLogger.e("CloudControlManager", "No strategy class was registered with %s.", str);
            return null;
        }
        return null;
    }

    public boolean isInitDone() {
        return this.mIsInitDone;
    }
}
