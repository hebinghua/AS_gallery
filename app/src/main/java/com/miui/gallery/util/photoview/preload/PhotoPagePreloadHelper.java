package com.miui.gallery.util.photoview.preload;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import java.lang.ref.WeakReference;
import java.util.concurrent.FutureTask;

/* loaded from: classes2.dex */
public class PhotoPagePreloadHelper {
    public volatile boolean mIsDestroyed;
    public FutureTask mPreloadTask;
    public PhotoPageAdapter.ViewProvider mPreloadViewProvider;
    public WeakReference<View> mPreloadViewRef;

    public void preloadPhotoPageInfo(Context context) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            DefaultLogger.v("PhotoPagePreloadHelper", "please call by main thread");
            return;
        }
        final WeakReference weakReference = new WeakReference(context);
        FutureTask futureTask = new FutureTask(new Runnable() { // from class: com.miui.gallery.util.photoview.preload.PhotoPagePreloadHelper.1
            @Override // java.lang.Runnable
            public void run() {
                if (weakReference.get() == null || Thread.interrupted() || PhotoPagePreloadHelper.this.mIsDestroyed) {
                    return;
                }
                try {
                    LayoutInflater cloneInContext = LayoutInflater.from((Context) weakReference.get()).cloneInContext((Context) weakReference.get());
                    PhotoPagePreloadHelper.this.mPreloadViewRef = new WeakReference(cloneInContext.inflate(R.layout.photo_page, (ViewGroup) new FrameLayout((Context) weakReference.get()), false));
                    if (!Thread.interrupted() && !PhotoPagePreloadHelper.this.mIsDestroyed) {
                        PhotoPagePreloadHelper.this.mPreloadViewProvider = PhotoPageAdapter.generateDefaultPhotoPageViewProvider();
                        PhotoPagePreloadHelper.this.mPreloadViewProvider.initBy(cloneInContext);
                        return;
                    }
                    PhotoPagePreloadHelper.this.mPreloadViewRef.clear();
                } catch (Exception e) {
                    DefaultLogger.e("PhotoPagePreloadHelper", e);
                }
            }
        }, null);
        this.mPreloadTask = futureTask;
        ThreadManager.execute(79, futureTask);
    }

    public View getPageLayout() {
        try {
            WeakReference<View> weakReference = this.mPreloadViewRef;
            return weakReference != null ? weakReference.get() : null;
        } finally {
            WeakReference<View> weakReference2 = this.mPreloadViewRef;
            if (weakReference2 != null) {
                weakReference2.clear();
            }
        }
    }

    public PhotoPageAdapter.ViewProvider getPhotoPageViewProviderIfHave() {
        try {
            return this.mPreloadViewProvider;
        } finally {
            this.mPreloadViewProvider = null;
        }
    }

    public void release() {
        this.mIsDestroyed = true;
        WeakReference<View> weakReference = this.mPreloadViewRef;
        if (weakReference != null) {
            weakReference.clear();
            this.mPreloadViewRef = null;
        }
        FutureTask futureTask = this.mPreloadTask;
        if (futureTask != null) {
            futureTask.cancel(true);
        }
        this.mPreloadViewProvider = null;
    }
}
